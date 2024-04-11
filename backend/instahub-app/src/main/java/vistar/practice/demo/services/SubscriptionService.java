package vistar.practice.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.user.UserResponseDto;
import vistar.practice.demo.mappers.UserMapper;
import vistar.practice.demo.models.SubscriptionEntity;
import vistar.practice.demo.models.UserEntity;
import vistar.practice.demo.repositories.SubscriptionRepository;
import vistar.practice.demo.repositories.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

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
            throw new IllegalStateException("can't do this");
        var subscriber = userRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException(notFoundErrorText)
                );
        if(!subscriber.getUsername().equals(name))
            throw new IllegalStateException("no permission");
        var user = userRepository.findById(subId)
                .filter(UserEntity::isEnabled)
                .orElseThrow(() -> new NoSuchElementException(notFoundErrorText));
        subscriptionRepository.save(SubscriptionEntity.builder().user(user).subscriber(subscriber).build());
    }

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public List<UserResponseDto> getSubscribers(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException(notFoundErrorText)
                );
        return subscriptionRepository.findAllByUser(user).stream()
                .filter(SubscriptionEntity::isActive)
                .map(SubscriptionEntity::getSubscriber)
                .filter(UserEntity::isEnabled)
                .map(userMapper::toInfoDto)
                .toList();
    }

    public void unsubscribe(Long id, Long subId, String name) {
        if(id.equals(subId))
            throw new IllegalStateException("can't do this");
        var subscriber = userRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException(notFoundErrorText)
                );
        if(!subscriber.getUsername().equals(name))
            throw new IllegalStateException("no permission");
        var user = userRepository.findById(subId).filter(
                        UserEntity::isEnabled
                )
                .orElseThrow(
                        () -> new NoSuchElementException(notFoundErrorText)
                );
        subscriptionRepository.findBySubscriberAndUser(subscriber, user)
                .filter(
                        SubscriptionEntity::isActive
                )
                .ifPresentOrElse(
                        s -> s.setActive(false),
                        () -> {
                            throw new IllegalStateException("some bad request exception about subscription not found idk");
                        }
                );

    }
}
