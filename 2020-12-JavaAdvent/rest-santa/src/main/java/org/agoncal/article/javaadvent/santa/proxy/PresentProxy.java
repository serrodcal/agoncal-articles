package org.agoncal.article.javaadvent.santa.proxy;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Antonio Goncalves @agoncal
 * http://www.antoniogoncalves.org
 * --
 * Edited by @serrodcal
 */
// tag::adocSnippet[]
@Path("/api/pokemons/random")
@RegisterRestClient(configKey = "present-proxy")
public interface PresentProxy {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Present> getAPresent();
}
// end::adocSnippet[]
