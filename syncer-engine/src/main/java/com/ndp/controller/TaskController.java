package com.ndp.controller;

import com.ndp.service.TaskService;
import com.ndp.types.rest.Response;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskController {

    @Inject
    TaskService taskService;
    @Inject
    Logger logger;

    @POST
    @Path("/load")
    public Response loadTasks() {
        try {
            //taskService.loadTasksFromExcel("ruta/al/archivo.xlsx");
            //return Response.ok("Tareas cargadas o actualizadas exitosamente").build();
            return null;
        } catch (Exception e) {
            return null;
            /*return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al cargar las tareas: " + e.getMessage()).build();*/
        }
    }
/*
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
    public Response<Task> getAllTasks(@QueryParam("company") String company,
                                  @QueryParam("page") @DefaultValue("1") int page,
                                  @QueryParam("size") @DefaultValue("20") int size) {

        List<Task> tasks;
        if (company != null && !company.isEmpty()) {
            tasks = taskService.listTasksByCompany(company, page, size);
        } else {
            tasks = taskService.listAllTasks(page, size);
        }
        return Response<>(true, new Data<>(new Pagination<>(tasks.size(), page, tasks)));
    }


    public com.ndp.types.rest.Response<Queue> getAllQueues(@QueryParam("company") String company,
                                                           @QueryParam("page") @DefaultValue("1") int page,
                                                           @QueryParam("size") @DefaultValue("20") int size) {
        List<Queue> queues;
        if (company != null && !company.isEmpty()) {
            queues = queueService.listQueuesByCompany(company, page, size);
        } else {
            queues = queueService.listAllQueues(page, size);
        }
        Data<Queue> data = new Data<>(new Pagination<>(queues.size(), page, queues));
        return new com.ndp.types.rest.Response<>(true, data);
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

 */
}
