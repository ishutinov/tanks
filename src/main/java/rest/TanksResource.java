package rest;

import model.GameManager;
import model.Tank;

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

    @PUT
    @Produces("application/json")
    @Path("/{id}/x/{posX}/y/{posY}")
    public Response moveTank(@PathParam("id") String id, @PathParam("posX") Integer posX, @PathParam("posY") Integer posY) {
        Tank tank = GameManager.getInstance().moveTank(id, posX, posY);

        return Response.status(Response.Status.OK)
                .entity(tank)
                .type(MediaType.APPLICATION_JSON).build();
    }
}