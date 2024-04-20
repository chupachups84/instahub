package vistar.practice.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import vistar.practice.demo.validation.impl.UniqueUsernameImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = UniqueUsernameImpl.class)
public @interface UniqueUsername {
    String message() default "this username already exist";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
