package pt.pmendes.tanks.api.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.pmendes.tanks.api.TankDirector;
import pt.pmendes.tanks.internal.entities.Tank;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by pmendes.
 */
@Path("/tanks")
@Component
public class TankResource {

    @Autowired
    private TankDirector tankDirector;

    @GET
    @Produces("application/json")
    public Response getTanks() {
        return Response.status(Response.Status.OK)
                .entity(tankDirector.getTanks())
                .type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getTank(@PathParam("id") String id) {
        Tank tank = tankDirector.getTank(id);

        return Response.status(Response.Status.OK)
                .entity(tank)
                .type(MediaType.APPLICATION_JSON).build();
    }


    @POST
    @Path("/{id}")
    @Produces("application/json")
    public Response createTank(@PathParam("id") String id) {
        Tank tank = tankDirector.addTank(id);

        return Response.status(Response.Status.OK)
                .entity(tank)
                .type(MediaType.APPLICATION_JSON).build();
    }
    
}
