package de.sebastian_daschner.todos.business.tasks.entity;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class Filter {

    private String text;

    @Contexts
    private Set<String> contexts;

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
