package vistar.practice.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.repost.RepostDto;
import vistar.practice.demo.models.RepostEntity;
import vistar.practice.demo.models.photo.PhotoEntity;
import vistar.practice.demo.repositories.RepostRepository;
import vistar.practice.demo.repositories.UserRepository;
import vistar.practice.demo.repositories.photo.PhotoRepository;
import vistar.practice.demo.repositories.photo.PhotoViewRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
public class RepostService {

    private final RepostRepository repostRepository;
    private final PhotoRepository photoRepository;
    private final PhotoViewRepository photoViewRepository;
    private final UserRepository userRepository;

    public void repostPhoto(
            long photoId,
            RepostDto repostCreateEditDto,
            String username
    ) {
        var photoEntity = photoRepository.getReferenceById(photoId);
        var userEntity = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User (username: " + username + ") not found")
        );
        var repostedFrom = photoViewRepository.findById(photoId).orElseThrow(
                () -> new NoSuchElementException("Photo (id: " + photoId + ") not found")
        ).getUsername();
        var text = repostCreateEditDto.getText();
        repostRepository.save(RepostEntity.builder()
                        .photo(photoEntity)
                        .text(text)
                        .user(userEntity)
                .build()
        );
        photoRepository.save(PhotoEntity.builder()
                        .description("Reposted from: " + repostedFrom +
                                (text != null && !text.isEmpty() ? "\n\n" + repostCreateEditDto.getText() : "")
                        )
                        .user(userEntity)
                        .feedUrl(photoEntity.getFeedUrl())
                        .storageUrl(photoEntity.getStorageUrl())
                        .iconUrl(photoEntity.getIconUrl())
                        .isAvatar(false)
                .build()
        );
    }
}
