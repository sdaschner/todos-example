package de.sebastian_daschner.todos.business.tasks.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity(name = "tasks")
public class Task {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Size(min = 1, max = 255)
    @Basic(optional = false)
    private String name;

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
}
