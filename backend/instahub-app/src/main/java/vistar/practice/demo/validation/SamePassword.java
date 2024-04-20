package vistar.practice.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import vistar.practice.demo.validation.impl.SamePasswordImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = SamePasswordImpl.class)
public @interface SamePassword {
    String message() default "two password must be the same";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
