package org.whatever.library.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NameConstraint {
    public String message() default "Name is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}