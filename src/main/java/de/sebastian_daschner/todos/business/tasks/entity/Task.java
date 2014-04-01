package de.sebastian_daschner.todos.business.tasks.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity(name = "tasks")
public class Task {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Min(1)
    @Max(255)
    @Basic(optional = false)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date dueDate;

}
