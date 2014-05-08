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

import de.sebastian_daschner.todos.business.tasks.control.TaskCache;
import de.sebastian_daschner.todos.business.tasks.entity.Filter;
import de.sebastian_daschner.todos.business.tasks.entity.Task;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class TaskStore {

    @Inject
    TaskCache cache;

    public List<Task> listAll() {
        return cache.getAll();
    }

    public List<Task> filterAll(final Filter filter) {
        return listAll().parallelStream().filter(filter::matches).collect(Collectors.toList());
    }

    public Task get(final long taskId) {
        return cache.get(taskId);
    }

    public long save(final Task task) {
        task.setUpdated(new Date());
        return cache.store(task);
    }

    public void delete(final long taskId) {
        cache.remove(taskId);
    }

}
