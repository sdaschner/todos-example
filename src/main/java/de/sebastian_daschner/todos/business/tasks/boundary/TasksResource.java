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

import de.sebastian_daschner.todos.business.tasks.entity.Task;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("tasks")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TasksResource {

    @Inject
    TaskStore taskStore;

    @GET
    public List<Task> listAll() {
        return taskStore.listAll();
    }

    @GET
    @Path("{id}")
    public Task get(@PathParam("id") final long taskId) {
        return taskStore.get(taskId);
    }

    @POST
    public Response save(@Valid final Task task) {
        final long taskId = taskStore.save(task);

        URI taskUri = URI.create("/tasks/" + taskId);
        return Response.created(taskUri).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") final long taskId, @Valid final Task task) {
        taskStore.update(taskId, task);
        return Response.noContent().build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") final long taskId) {
        taskStore.delete(taskId);
        return Response.noContent().build();
    }

}
