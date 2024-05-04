package vistar.practice.demo.services.photo;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.clients.StorageClient;
import vistar.practice.demo.configs.specification.Condition;
import vistar.practice.demo.configs.specification.SpecificationBuilder;
import vistar.practice.demo.dtos.photo.load.FeedPhotoDto;
import vistar.practice.demo.mappers.CommentMapper;
import vistar.practice.demo.mappers.PhotoMapper;
import vistar.practice.demo.mappers.ReactionMapper;
import vistar.practice.demo.models.photo.PhotoView;
import vistar.practice.demo.repositories.ReactionsPhotosRepository;
import vistar.practice.demo.repositories.RepostRepository;
import vistar.practice.demo.repositories.SubscriptionRepository;
import vistar.practice.demo.repositories.UserRepository;
import vistar.practice.demo.repositories.comment.CommentViewRepository;
import vistar.practice.demo.repositories.photo.PhotoViewRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, transactionManager = "transactionManager")
public class PhotoLoadService {

    private final PhotoViewRepository photoViewRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final CommentViewRepository commentViewRepository;
    private final UserRepository userRepository;
    private final ReactionsPhotosRepository reactionsPhotosRepository;
    private final RepostRepository repostRepository;

    private final PhotoMapper photoMapper;
    private final CommentMapper commentMapper;
    private final ReactionMapper reactionMapper;

    private final StorageClient storageClient;

    private static final String SORT_PHOTOS_BY_FIELD = "createdAt";

    public List<InputStreamSource> fetchLoad(
            String username,
            int page,
            int size
    ) {

        var ownerId = getOwnerIdOrElseThrow(username);

        var spec = new SpecificationBuilder<PhotoView>().with(
                List.of(
                        Condition.builder()
                                .fieldName("userId")
                                .operation(Condition.OperationType.EQUALS)
                                .value(ownerId)
                                .logicalOperator(Condition.LogicalOperatorType.AND)
                                .build(),
                        Condition.builder()
                                .fieldName("isShown")
                                .operation(Condition.OperationType.EQUALS)
                                .value(true)
                                .logicalOperator(Condition.LogicalOperatorType.END)
                                .build()
                )
        ).build();
        var pr = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, SORT_PHOTOS_BY_FIELD));

        return photoViewRepository.findAll(spec, pr).stream().map(
                photoView -> photoMapper.toInputStreamSource(
                    storageClient.requestObject(photoView.getIconUrl())
                )
        ).toList();
    }

    /**
     * Загрузить фото DTO по его порядковому номеру (с 0) в профиле
     *
     * @param username Никнейм пользователя
     * @param creationOffset Номер иконки в профиле
     * @return DTO, содержащий фото и информацию о нём
     */
    public FeedPhotoDto loadPhotoByIconCreationOffset(String username, int creationOffset) {

        var ownerId = getOwnerIdOrElseThrow(username);

        var photoViewOptional = photoViewRepository.getByIconCreationOffset(creationOffset, ownerId);
        if (photoViewOptional.isEmpty()) {
            return null;
        }
        var photoView = photoViewOptional.get();
        var storageUrl = photoView.getStorageUrl();

        return new FeedPhotoDto(
                photoMapper.toInputStreamSource(storageClient.requestObject(storageUrl)),
                commentMapper.toDto(
                        commentViewRepository.getLastShownCommentByPhotoId(photoView.getId()).orElse(null)
                ),
                photoView.getUserFullName(),
                photoView.getCreatedAt(),
                reactionMapper.toHashMap(reactionsPhotosRepository.findAllByPhotoId(photoView.getId())),
                repostRepository.countByPhotoId(photoView.getId())
        );
    }

    /**
     * Загрузить фото по его порядковому номеру (с 0) в ленте
     *
     * @param username Никнейм пользователя
     * @param creationOffset Номер иконки в ленте
     * @return Байтовый поток, содержащий фото
     */
    public InputStreamSource loadPhotoByFeedCreationOffset(
            LocalDateTime lastPhotoCreationTime,
            String username,
            int creationOffset
    ) {
        var ownerId = getOwnerIdOrElseThrow(username);

        var storageUrl = photoViewRepository.getByFeedCreationOffset(creationOffset, ownerId, lastPhotoCreationTime).orElseThrow(
                () -> new NoSuchElementException("Photo (creationOffset: " + creationOffset + ") does not exist in feed")
        ).getStorageUrl();

        return photoMapper.toInputStreamSource(storageClient.requestObject(storageUrl));
    }

    public FeedPhotoDto loadAvatar(String username) {
        var ownerId = getOwnerIdOrElseThrow(username);
        var avatarOptional = photoViewRepository.getAvatar(ownerId);
        return avatarOptional.map(
                avatar -> new FeedPhotoDto(
                        photoMapper.toInputStreamSource(storageClient.requestAvatar(ownerId)),
                        commentMapper.toDto(
                                commentViewRepository.getLastShownCommentByPhotoId(avatar.getId()).orElse(null)
                        ),
                        avatar.getUserFullName(),
                        avatar.getCreatedAt(),
                        reactionMapper.toHashMap(reactionsPhotosRepository.findAllByPhotoId(avatar.getId())),
                        repostRepository.countByPhotoId(avatar.getId())
                )
        ).orElse(null);
    }

    public List<FeedPhotoDto> fetchFeed(
            LocalDateTime lastPhotoCreationTime,
            String username,
            int page,
            int size
    ) {
        var owner = userRepository.findByUsername(username).orElseThrow(
                () -> new NoSuchElementException("User (username: " + username + ") not found")
        );
        var subscriptorIds = subscriptionRepository
                .findAllActiveSubscriptions(owner)
                .stream()
                .map(subscriptionEntity -> subscriptionEntity.getUser().getId())
                .toList();

        var spec = new SpecificationBuilder<PhotoView>().with(
                List.of(
                        Condition.builder()
                                .fieldName("isShown")
                                .operation(Condition.OperationType.EQUALS)
                                .value(true)
                                .logicalOperator(Condition.LogicalOperatorType.AND)
                                .build(),
                        Condition.builder()
                                .fieldName("createdAt")
                                .operation(Condition.OperationType.LESS)
                                .value(lastPhotoCreationTime)
                                .logicalOperator(Condition.LogicalOperatorType.AND)
                                .build(),
                        Condition.builder()
                                .fieldName("userId")
                                .operation(Condition.OperationType.IN)
                                .values(subscriptorIds)
                                .logicalOperator(Condition.LogicalOperatorType.END)
                                .build()
                )
        ).build();
        var pr = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, SORT_PHOTOS_BY_FIELD));

        return photoViewRepository.findAll(spec, pr)
                .stream()
                .map(
                        photoView -> new FeedPhotoDto(
                                photoMapper.toInputStreamSource(storageClient.requestObject(photoView.getFeedUrl())),
                                commentMapper.toDto(
                                        commentViewRepository.getLastShownCommentByPhotoId(photoView.getId()).orElse(null)
                                ),
                                photoView.getUserFullName(),
                                photoView.getCreatedAt(),
                                reactionMapper.toHashMap(reactionsPhotosRepository.findAllByPhotoId(photoView.getId())),
                                repostRepository.countByPhotoId(photoView.getId())
                        )
                )
                .toList();
    }

    private long getOwnerIdOrElseThrow(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User (username: " + username + ") not found")
        ).getId();
    }
}
