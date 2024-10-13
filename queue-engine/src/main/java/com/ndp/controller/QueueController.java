package com.ndp.controller;

import com.ndp.types.rest.Data;
import com.ndp.types.rest.Pagination;
import com.ndp.types.rest.Response;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import com.ndp.service.QueueService;
import com.ndp.entity.queue.Queue;

@Path("/queue")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QueueController {

    @Inject
    QueueService queueService;

@GET
public Response<Queue> getAllQueues(@QueryParam("company") String company,
                                    @QueryParam("page") @DefaultValue("1") int page,
                                    @QueryParam("size") @DefaultValue("20") int size) {
    List<Queue> queues;
    if (company != null && !company.isEmpty()) {
        queues = queueService.listQueuesByCompany(company, page, size);
    } else {
        queues = queueService.listAllQueues(page, size);
    }
    Data<Queue> data = new Data<>(new Pagination<>(queues.size(), page, queues));
    return new Response<>(true, data);
}

    @GET
    @Path("/{id}")
    public Queue getQueue(@PathParam("id") Long id) {
        return queueService.findQueueById(id);
    }

    @POST
    public void createQueue(Queue queue) {
        queueService.createQueue(queue);
    }

    @POST
    @Path("/bulk")
    public void createQueues(List<Queue> queues) {
        queueService.createQueues(queues);
    }

    @PUT
    @Path("/{id}")
    public void updateQueue(@PathParam("id") Long id, Queue queue) {
        queue.setId_queue(id);
        queueService.updateQueue(queue);
    }

    @DELETE
    @Path("/{id}")
    public void deleteQueue(@PathParam("id") Long id) {
        queueService.deleteQueue(id);
    }
}
