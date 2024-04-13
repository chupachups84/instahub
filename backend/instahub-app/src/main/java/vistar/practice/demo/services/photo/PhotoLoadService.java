package vistar.practice.demo.services.photo;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
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

        var ownerId = userRepository.findByUsername(username).orElseThrow(
                () -> new NoSuchElementException("User (username: " + username + ") not found")
        ).getId();

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
}
