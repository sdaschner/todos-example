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
package de.sebastian_daschner.todos.business.tasks.entity;

import java.util.HashSet;
import java.util.Set;

public class Filter {

    private String text;

    @Contexts
    private Set<String> contexts = new HashSet<>();

    private Integer priorityThreshold;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<String> getContexts() {
        return contexts;
    }

    public void setContexts(Set<String> contexts) {
        this.contexts = contexts;
    }

    public Integer getPriorityThreshold() {
        return priorityThreshold;
    }

    public void setPriorityThreshold(Integer priorityThreshold) {
        this.priorityThreshold = priorityThreshold;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "text='" + text + '\'' +
                ", contexts=" + contexts +
                ", priorityThreshold=" + priorityThreshold +
                '}';
    }

}
