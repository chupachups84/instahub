package vistar.practice.demo.services.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.configs.specification.Condition;
import vistar.practice.demo.configs.specification.SpecificationBuilder;
import vistar.practice.demo.dtos.comment.CommentDto;
import vistar.practice.demo.mappers.CommentMapper;
import vistar.practice.demo.models.comment.CommentView;
import vistar.practice.demo.repositories.comment.CommentViewRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, transactionManager = "transactionManager")
public class CommentLoadService {

    private static final String SORT_COMMENTS_BY_FIELD = "createdAt";

    private final CommentViewRepository commentViewRepository;
    private final CommentMapper commentMapper;

    public CommentDto getByOffset(
            LocalDateTime lastCommentCreationTime,
            long photoId,
            long commentOffset
    ) {
        var commentViewOptional = commentViewRepository.findByOffset(lastCommentCreationTime, photoId, commentOffset);
        return commentViewOptional.map(
                commentMapper::toDto
        ).orElse(null);
    }

    public List<CommentDto> fetchLoad(
            LocalDateTime lastCommentCreationTime,
            long photoId,
            int page,
            int size
    ) {
        var spec = new SpecificationBuilder<CommentView>().with(
                List.of(
                        Condition.builder()
                                .fieldName("photoId")
                                .operation(Condition.OperationType.EQUALS)
                                .value(photoId)
                                .logicalOperator(Condition.LogicalOperatorType.AND)
                                .build(),
                        Condition.builder()
                                .fieldName("isShown")
                                .operation(Condition.OperationType.EQUALS)
                                .value(true)
                                .logicalOperator(Condition.LogicalOperatorType.AND)
                                .build(),
                        Condition.builder()
                                .fieldName("createdAt")
                                .operation(Condition.OperationType.LESS)
                                .value(lastCommentCreationTime)
                                .logicalOperator(Condition.LogicalOperatorType.END)
                                .build()
                )
        ).build();
        var pr = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, SORT_COMMENTS_BY_FIELD));

        return commentViewRepository.findAll(spec, pr).stream().map(
                commentMapper::toDto
        ).toList();
    }
}
