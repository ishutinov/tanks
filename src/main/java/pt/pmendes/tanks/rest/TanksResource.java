package pt.pmendes.tanks.rest;

import pt.pmendes.tanks.manager.GameManager;
import pt.pmendes.tanks.model.Tank;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tanks")
public class TanksResource {

    @GET
    @Produces("application/json")
    public Response getWorld() {
        return Response.status(Response.Status.OK)
                .entity(GameManager.getInstance())
                .type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces("application/json")
    @Path("/{id}")
    public Response getTank(@PathParam("id") String id) {
        Tank tank = GameManager.getInstance().getTank(id);

        return Response.status(Response.Status.OK)
                .entity(tank)
                .type(MediaType.APPLICATION_JSON).build();
    }


    @POST
    @Produces("application/json")
    @Path("/{id}")
    public Response createTank(@PathParam("id") String id) {
        Tank tank = GameManager.getInstance().addTank(id);

        return Response.status(Response.Status.OK)
                .entity(tank)
                .type(MediaType.APPLICATION_JSON).build();
    }

}