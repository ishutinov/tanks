package pt.pmendes.tanks.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.pmendes.tanks.manager.GameManager;
import pt.pmendes.tanks.model.Tank;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tanks")
@Component
public class TanksResource {

    @Autowired
    private GameManager gameManager;

    @GET
    @Produces("application/json")
    public Response getTanks() {
        return Response.status(Response.Status.OK)
                .entity(gameManager.getGameFrame().getTanks())
                .type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces("application/json")
    @Path("/{id}")
    public Response getTank(@PathParam("id") String id) {
        Tank tank = gameManager.getTank(id);

        return Response.status(Response.Status.OK)
                .entity(tank)
                .type(MediaType.APPLICATION_JSON).build();
    }


    @POST
    @Produces("application/json")
    @Path("/{id}")
    public Response createTank(@PathParam("id") String id) {
        Tank tank = gameManager.addTank(id);

        return Response.status(Response.Status.OK)
                .entity(tank)
                .type(MediaType.APPLICATION_JSON).build();
    }

}