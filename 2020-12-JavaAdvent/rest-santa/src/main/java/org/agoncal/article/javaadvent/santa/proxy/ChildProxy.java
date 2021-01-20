package org.agoncal.article.javaadvent.santa.proxy;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Antonio Goncalves @agoncal
 * http://www.antoniogoncalves.org
 * --
 * Edited by @serrodcal
 */
// tag::adocSnippet[]
@Path("/api/kids")
@RegisterRestClient(configKey = "child-proxy")
public interface ChildProxy {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Uni<List<Child>> getAllGoodChildren(@QueryParam("country") String country);
}
// end::adocSnippet[]
