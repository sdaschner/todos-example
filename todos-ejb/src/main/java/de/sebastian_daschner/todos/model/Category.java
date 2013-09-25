package de.sebastian_daschner.todos.model;

import javax.persistence.Entity;

@Entity
public class Category extends PersistedEntity {

	private String name;
}
