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

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
public class RepostService {

    private final RepostRepository repostRepository;
    private final PhotoRepository photoRepository;
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
        repostRepository.save(RepostEntity.builder()
                        .photo(photoEntity)
                        .text(repostCreateEditDto.getText())
                        .user(userEntity)
                .build()
        );
        photoRepository.save(PhotoEntity.builder()
                        .user(userEntity)
                        .feedUrl(photoEntity.getFeedUrl())
                        .storageUrl(photoEntity.getStorageUrl())
                        .iconUrl(photoEntity.getIconUrl())
                        .isAvatar(false)
                .build()
        );
    }
}
