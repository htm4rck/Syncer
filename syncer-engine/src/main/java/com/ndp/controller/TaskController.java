package com.ndp.controller;

import com.ndp.entity.Task;
import com.ndp.service.TaskService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskController {

    @Inject
    TaskService taskService;

    @POST
    @Path("/load")
    public Response loadTasks() {
        try {
            taskService.loadTasksFromExcel("ruta/al/archivo.xlsx");
            return Response.ok("Tareas cargadas o actualizadas exitosamente").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al cargar las tareas: " + e.getMessage()).build();
        }
    }

    @POST
    public Response createOrUpdateTask(Task task) {
        try {
            Task savedTask = taskService.saveOrUpdate(task);
            return Response.ok(savedTask).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear o actualizar la tarea: " + e.getMessage()).build();
        }
    }

    @GET
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GET
    @Path("/{id}")
    public Response getTaskById(@PathParam("id") Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        if (task.isPresent()) {
            return Response.ok(task.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Tarea no encontrada").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTask(@PathParam("id") Long id) {
        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            return Response.ok("Tarea eliminada exitosamente").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Tarea no encontrada").build();
        }
    }
}
