package vistar.practice.demo.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.count.CountDto;
import vistar.practice.demo.dtos.user.UserResponseDto;
import vistar.practice.demo.mappers.UserMapper;
import vistar.practice.demo.models.SubscriptionEntity;
import vistar.practice.demo.models.UserEntity;
import vistar.practice.demo.repositories.SubscriptionRepository;
import vistar.practice.demo.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
public class SubscriptionService {
    @Value("${user.errors.not-found}")
    public String notFoundErrorText;

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public List<UserResponseDto> getFollowers(String username, Pageable pageable) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new EntityNotFoundException(notFoundErrorText)
                );
        return subscriptionRepository.findAllByUser(user,pageable).stream()
                .filter(SubscriptionEntity::isActive)
                .map(SubscriptionEntity::getSubscriber)
                .filter(UserEntity::isEnabled)
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public List<UserResponseDto> getFollows(String username, Pageable pageable) {
        var subs = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new EntityNotFoundException(notFoundErrorText)
                );
        return subscriptionRepository.findAllBySubscriber(subs,pageable).stream()
                .filter(SubscriptionEntity::isActive)
                .map(SubscriptionEntity::getUser)
                .filter(UserEntity::isEnabled)
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public CountDto getFollowersCount(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new EntityNotFoundException(notFoundErrorText)
                );
        return CountDto.builder().count(subscriptionRepository.countAllByUserAndIsActiveIsTrue(user)).build();
    }

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public CountDto getFollowsCount(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new EntityNotFoundException(notFoundErrorText)
                );
        return CountDto.builder().count(subscriptionRepository.countAllBySubscriberAndIsActiveIsTrue(user)).build();
    }

    public void subscribe(String usernameToSub, String username) {
        if (usernameToSub.equals(username)) {
            throw new AccessDeniedException("Can not subscribe to yourself");
        }

        if (relation(username, usernameToSub).equals("SUBSCRIBED")) {
            return;
        }


        var subscriber = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new EntityNotFoundException(notFoundErrorText)
                );
        var user = userRepository.findByUsername(usernameToSub)
                .filter(UserEntity::isEnabled)
                .orElseThrow(() -> new EntityNotFoundException(notFoundErrorText));

        var subscriptionEntityOptional = subscriptionRepository.getSubscription(subscriber.getId(), user.getId());
        if (subscriptionEntityOptional.isPresent()) {
            subscriptionEntityOptional.get().setActive(true);
        } else {
            subscriptionRepository.save(
                    SubscriptionEntity.builder()
                            .user(user)
                            .subscriber(subscriber)
                            .build()
            );
        }
    }

    public void unsubscribe(String usernameToUnsub, String username) {
        if(usernameToUnsub.equals(username))
            throw new IllegalStateException("Can not unsubscribe from yourself");  // --release Illegal handle

        if (relation(username, usernameToUnsub).equals("UNSUBSCRIBED")) {
            return;
        }
        var subscriber = userRepository.findByUsername(usernameToUnsub)
                .orElseThrow(
                        () -> new EntityNotFoundException(notFoundErrorText)
                );

        var user = userRepository.findByUsername(username).filter(UserEntity::isEnabled)
                .orElseThrow(
                        () -> new EntityNotFoundException(notFoundErrorText)
                );

        subscriptionRepository.findBySubscriberAndUser(user, subscriber)
                .filter(
                        SubscriptionEntity::isActive
                )
                .ifPresentOrElse(
                        s -> s.setActive(false),
                        () -> {
                            throw new EntityNotFoundException("Subscription is not active");
                        }
                );

    }

    public String relation(String user, String relatedUser) {
        if (user.equals(relatedUser)) {
            return "OWNS";
        }
        var userId = userRepository.findByUsername(user).orElseThrow(
                () -> new UsernameNotFoundException("User (username = " + user + ") not found")
        ).getId();
        var relatedUserId = userRepository.findByUsername(relatedUser).orElseThrow(
                () -> new UsernameNotFoundException("User (username = " + relatedUser + ") not found")
        ).getId();
        return subscriptionRepository.isSubscribed(userId, relatedUserId) ? "SUBSCRIBED" : "UNSUBSCRIBED";
    }
}
