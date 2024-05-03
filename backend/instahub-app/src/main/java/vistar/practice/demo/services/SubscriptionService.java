package vistar.practice.demo.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public void subscribe(Long id, Long subId, String name) {
        if(id.equals(subId))
            throw new AccessDeniedException("can't do this");
        var subscriber = userRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(notFoundErrorText)
                );
        if(!subscriber.getUsername().equals(name))
            throw new AccessDeniedException("no permission");
        var user = userRepository.findById(subId)
                .filter(UserEntity::isEnabled)
                .orElseThrow(() -> new EntityNotFoundException(notFoundErrorText));
        subscriptionRepository.save(SubscriptionEntity.builder().user(user).subscriber(subscriber).build());
    }

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public List<UserResponseDto> getSubscribers(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(notFoundErrorText)
                );
        return subscriptionRepository.findAllByUser(user).stream()
                .filter(SubscriptionEntity::isActive)
                .map(SubscriptionEntity::getSubscriber)
                .filter(UserEntity::isEnabled)
                .map(userMapper::toDto)
                .toList();
    }

    public void unsubscribe(Long id, Long subId, String name) {
        if(id.equals(subId))
            throw new IllegalStateException("can't unsubscribe yourself");  // --release Illegal handle
        var subscriber = userRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(notFoundErrorText)
                );
        if(!subscriber.getUsername().equals(name))
            throw new AccessDeniedException("no permission");
        var user = userRepository.findById(subId).filter(
                        UserEntity::isEnabled
                )
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
