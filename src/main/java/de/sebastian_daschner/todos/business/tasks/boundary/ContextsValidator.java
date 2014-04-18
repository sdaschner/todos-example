package de.sebastian_daschner.todos.business.tasks.boundary;

import de.sebastian_daschner.todos.business.tasks.entity.Contexts;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.Set;

public class ContextsValidator implements ConstraintValidator<Contexts, Set<String>> {

    @Override
    public void initialize(Contexts constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(Set<String> value, ConstraintValidatorContext context) {
        return value.stream().allMatch(Objects::nonNull) && value.stream().allMatch(c -> c.matches("[a-z]+"));
    }
}
