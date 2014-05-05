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

import de.sebastian_daschner.todos.business.tasks.entity.Filter;
import de.sebastian_daschner.todos.business.tasks.entity.Task;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class TaskStore {

    @PersistenceContext
    EntityManager entityManager;

    public List<Task> listAll() {
        return entityManager.createNamedQuery("Task.findAll").getResultList();
    }

    public List<Task> filterAll(final Filter filter) {
        return listAll().parallelStream().filter(t -> matches(t, filter)).collect(Collectors.toList());
    }

    public Task get(final long taskId) {
        return entityManager.find(Task.class, taskId);
    }

    public long save(final Task task) {
        final Task managedTask = entityManager.merge(task);
        entityManager.flush();

        return managedTask.getId();
    }

    public void update(final Task task) {
        entityManager.merge(task);
        entityManager.flush();
    }

    public void delete(final long taskId) {
        final Task managedTask = entityManager.find(Task.class, taskId);
        entityManager.remove(managedTask);
        entityManager.flush();
    }

    private boolean matches(Task task, Filter filter) {
        if (filter.getText() != null && !task.getName().toLowerCase().contains(filter.getText())) {
            return false;
        }
        if (!filter.getContexts().isEmpty() && task.getContexts().stream().noneMatch(filter.getContexts()::contains)) {
            return false;
        }
        if (filter.getPriorityThreshold() != null) {
            if (task.getPriority() == null || filter.getPriorityThreshold().compareTo(task.getPriority()) >= 0) {
                return false;
            }
        }
        return true;
    }


}
