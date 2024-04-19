package vistar.practice.demo.configs.specification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Builder
@Getter
@Setter
public class Condition {

    private final String fieldName;

    private final Comparable value;
    private final Collection<Long> values;

    private final OperationType operation;

    private LogicalOperatorType logicalOperator;

    @Getter
    public enum OperationType {

        GREATER,
        LESS,
        EQUALS,
        IN,
        BEGINS_WITH
    }

    public enum LogicalOperatorType {

        AND, OR, END
    }
}
