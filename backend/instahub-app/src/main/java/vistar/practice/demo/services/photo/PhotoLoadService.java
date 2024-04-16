package vistar.practice.demo.services.photo;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.clients.StorageClient;
import vistar.practice.demo.configs.specification.Condition;
import vistar.practice.demo.configs.specification.SpecificationBuilder;
import vistar.practice.demo.mappers.PhotoMapper;
import vistar.practice.demo.models.photo.PhotoView;
import vistar.practice.demo.repositories.UserRepository;
import vistar.practice.demo.repositories.photo.PhotoViewRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, transactionManager = "transactionManager")
public class PhotoLoadService {

    private final PhotoViewRepository photoViewRepository;
    private final UserRepository userRepository;
    private final PhotoMapper photoMapper;
    private final StorageClient storageClient;

    public List<InputStreamSource> fetchLoad(
            String username,
            int page,
            int size
    ) {

        var ownerId = getOwnerIdOrElseThrow(username);

        var spec = new SpecificationBuilder<PhotoView>().with(
                Condition.builder()
                        .fieldName("userId")
                        .operation(Condition.OperationType.EQUALS)
                        .value(ownerId)
                        .logicalOperator(Condition.LogicalOperatorType.END)
                        .build()
        ).build();
        var pr = PageRequest.of(page, size);

        return photoViewRepository.findAll(spec, pr).stream().map(
                photoView -> photoMapper.toInputStreamSource(
                    storageClient.requestObject(photoView.getIconUrl())
                )
        ).toList();
    }

    public InputStreamSource loadPhotoByCreationOffset(String username, int creationOffset) {

        var ownerId = userRepository.findByUsername(username).orElseThrow(
                () -> new NoSuchElementException("User (username: " + username + ") does not exist")
        ).getId();

        var storageUrl = photoViewRepository.getByCreationOffset(creationOffset, ownerId).orElseThrow(
                () -> new NoSuchElementException("Photo (creationOffset: " + creationOffset + ") does not exist")
        ).getStorageUrl();

        return photoMapper.toInputStreamSource(storageClient.requestObject(storageUrl));
    }

    public InputStreamSource loadAvatar(String username) {
        var ownerId = getOwnerIdOrElseThrow(username);
        return photoMapper.toInputStreamSource(storageClient.requestAvatar(ownerId));
    }

    private long getOwnerIdOrElseThrow(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new NoSuchElementException("User (username: " + username + ") not found")
        ).getId();
    }
}
