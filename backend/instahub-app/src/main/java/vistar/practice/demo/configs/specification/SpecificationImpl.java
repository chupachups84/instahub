package vistar.practice.demo.configs.specification;

import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class SpecificationImpl<T> implements Specification<T> {

    private final Condition condition;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        switch (condition.getOperation()) {

            case EQUALS:
                return criteriaBuilder.equal(root.get(condition.getFieldName()).as(condition.getValue().getClass()),
                        condition.getValue());

            case LESS:
                return criteriaBuilder.lessThan(root.get(condition.getFieldName()).as(condition.getValue().getClass()),
                        condition.getValue());

            case GREATER:
                return criteriaBuilder.greaterThan(root.get(condition.getFieldName()).as(condition.getValue().getClass()),
                        condition.getValue());

            case IN: {

                if (condition.getValues().isEmpty()) {
                    return criteriaBuilder.disjunction();
                }

                Predicate predicate = root.get(condition.getFieldName())
                        .as(condition.getValues().stream().findFirst().get().getClass()).in(condition.getValues());

                if (condition.getValues().contains(null)) {
                    return criteriaBuilder.or(predicate, root.get(condition.getFieldName()).isNull());
                    }

                return predicate;
            }

            case BEGINS_WITH:
                return criteriaBuilder.like(
                        root.get(condition.getFieldName()).as(String.class), condition.getValue().toString() + "%"
                );

            default:
                return null;
        }
    }
}
