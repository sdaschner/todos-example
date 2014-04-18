package de.sebastian_daschner.todos.business.tasks.entity;

import de.sebastian_daschner.todos.business.tasks.boundary.ContextsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@NotNull
@Constraint(validatedBy = ContextsValidator.class)
@Documented
public @interface Contexts {

    String message() default "Context definitions are required to be [a-z]+";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
