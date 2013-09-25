package de.sebastian_daschner.todos.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Task extends PersistedEntity {

	private String description;
	@Temporal(TemporalType.DATE)
	private Date dueDate;
	@Temporal(TemporalType.DATE)
	private Date creationDate;
	private Category category;

	public String getDescription() {
		return description;
	}
	public void setDescription(final String description) {
		this.description = description;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(final Date dueDate) {
		this.dueDate = dueDate;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(final Date creationDate) {
		this.creationDate = creationDate;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(final Category category) {
		this.category = category;
	}



}
