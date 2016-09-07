package pt.pmendes.tanks.api.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.pmendes.tanks.api.GameDirector;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by pmendes.
 */
@Path("/map")
@Component
public class MapResource {

    @Autowired
    private GameDirector gameDirector;

    @POST
    @Path("/width/{width}/height/{height}")
    @Produces("application/json")
    public Response createMap(@PathParam("width") Integer width, @PathParam("height") Integer height) {
        gameDirector.init(width, height);

        return Response.status(Response.Status.OK)
                .entity(gameDirector.getFrame().getMap())
                .type(MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/reset")
    @Produces("application/json")
    public Response resetTanks() {
        gameDirector.reset();
        return Response.status(Response.Status.OK)
                .entity(gameDirector.getFrame())
                .type(MediaType.APPLICATION_JSON).build();
    }
}
