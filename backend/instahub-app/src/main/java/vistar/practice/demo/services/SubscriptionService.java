package vistar.practice.demo.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
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
                .map(SubscriptionEntity::getSubscriber)
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
        return CountDto.builder().count(subscriptionRepository.countAllByUserAndActiveIsTrue(user)).build();
    }

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public CountDto getFollowsCount(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new EntityNotFoundException(notFoundErrorText)
                );
        return CountDto.builder().count(subscriptionRepository.countAllBySubscriberAndActiveIsTrue(user)).build();
    }

    public void subscribe(String username, String subUsername, String name) {
        if(username.equals(subUsername))
            throw new AccessDeniedException("can't do this");

        if(!username.equals(name))
            throw new AccessDeniedException("no permission");

        var subscriber = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new EntityNotFoundException(notFoundErrorText)
                );
        var user = userRepository.findByUsername(subUsername)
                .filter(UserEntity::isEnabled)
                .orElseThrow(() -> new EntityNotFoundException(notFoundErrorText));

        subscriptionRepository.save(SubscriptionEntity.builder().user(user).subscriber(subscriber).build());
    }

    public void unsubscribe(String username, String subUsername, String name) {
        if(username.equals(subUsername))
            throw new IllegalStateException("can't unsubscribe yourself");  // --release Illegal handle

        if(!username.equals(name))
            throw new AccessDeniedException("no permission");

        var subscriber = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new EntityNotFoundException(notFoundErrorText)
                );

        var user = userRepository.findByUsername(subUsername).filter(UserEntity::isEnabled)
                .orElseThrow(
                        () -> new EntityNotFoundException(notFoundErrorText)
                );

        subscriptionRepository.findBySubscriberAndUser(subscriber, user)
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
}
