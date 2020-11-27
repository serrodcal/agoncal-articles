package org.agoncal.article.javaadvent.santa.toy;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api/toys/random")
@RegisterRestClient
public interface ToyProxy {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Toy getARandomToy();
}