package de.sebastian_daschner.todos.business.tasks.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Entity(name = "tasks")
public class Task implements Comparable<Task> {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Size(min = 1, max = 255)
    @Basic(optional = false)
    private String name;

    @Contexts
    private Set<String> contexts;

    private Integer priority;

    private boolean finished;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date dueDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getContexts() {
        return contexts;
    }

    public void setContexts(Set<String> contexts) {
        this.contexts = contexts;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contexts=" + contexts +
                ", priority=" + priority +
                ", finished=" + finished +
                ", updated=" + updated +
                ", dueDate=" + dueDate +
                '}';
    }

    @Override
    public int compareTo(Task task) {
        if (task == null) {
            return -1;
        }
        // sort by finished state
        if (this.finished || task.finished) {
            if (this.finished && !task.finished) {
                return 1;
            } else if (!this.finished && task.finished) {
                return -1;
            }
        }
        // either both or none are finished
        if (this.updated != null && task.updated != null) {
            return this.updated.compareTo(task.updated);
        }
        return this.name.compareToIgnoreCase(task.name);
    }
}
