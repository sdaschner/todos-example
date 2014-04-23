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

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class TaskStore {

    private Map<Long, Task> tasks = new ConcurrentHashMap<>();

    private AtomicLong next = new AtomicLong(0L);

    public List<Task> listAll() {
        final List<Task> allTasks = new LinkedList<>(tasks.values());

        Collections.sort(allTasks);
        return allTasks;
    }

    public List<Task> filterAll(final Filter filter) {
        final List<Task> filteredTasks = tasks.values().stream().filter(t -> matches(t, filter)).collect(Collectors.toList());

        Collections.sort(filteredTasks);
        return filteredTasks;
    }

    public Task get(long taskId) {
        return tasks.get(taskId);
    }

    public long save(Task task) {
        final long taskId = next.getAndIncrement();
        task.setId(taskId);
        task.setUpdated(new Date());

        tasks.put(taskId, task);

        return taskId;
    }

    public void update(long taskId, Task task) {
        task.setId(taskId);
        task.setUpdated(new Date());

        tasks.put(taskId, task);
    }

    public void delete(long taskId) {
        tasks.remove(taskId);
    }

    private static boolean matches(Task task, Filter filter) {
        if (filter.getText() != null && task.getName().toLowerCase().indexOf(filter.getText()) < 0) {
            return false;
        }
        if (!filter.getContexts().isEmpty() && task.getContexts().stream().noneMatch(c -> filter.getContexts().contains(c))) {
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