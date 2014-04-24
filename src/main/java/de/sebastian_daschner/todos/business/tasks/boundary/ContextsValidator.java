/*
 * Copyright (C) 2014 Sebastian Daschner, sebastian-daschner.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        if (value != null) {
            return value.stream().allMatch(Objects::nonNull) && value.stream().allMatch(c -> c.matches("[a-z]+"));
        }
        return true;
    }
}
