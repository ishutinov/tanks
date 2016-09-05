package pt.pmendes.tanks.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.pmendes.tanks.manager.GameManager;

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
    private GameManager gameManager;

    @POST
    @Path("/width/{width}/height/{height}")
    @Produces("application/json")
    public Response createMap(@PathParam("width") Integer width, @PathParam("height") Integer height) {
        gameManager.initMap(width, height);

        return Response.status(Response.Status.OK)
                .entity(gameManager.getGameFrame().getMap())
                .type(MediaType.APPLICATION_JSON).build();
    }
}
