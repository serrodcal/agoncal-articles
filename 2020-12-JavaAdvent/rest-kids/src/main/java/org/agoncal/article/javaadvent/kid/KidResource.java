package org.agoncal.article.javaadvent.kid;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.vertx.web.Param;
import io.quarkus.vertx.web.Route;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;

import javax.inject.Singleton;
import java.util.List;

/**
 * @author Antonio Goncalves @agoncal
 * http://www.antoniogoncalves.org
 * --
 * Edited by @serrodcal
 */
@OpenAPIDefinition(info = @Info(title = "API that returns the name and address of good kids who deserve a present", version = "1.0"))
@Singleton
public class KidResource {

    private static final Logger LOGGER = Logger.getLogger(KidResource.class);

    private static final String APPLICATION_JSON = "application/json";
    private static final String TEXT_PLAIN = "text/plain";

    /**
     * curl "http://localhost:8702/api/kids?country=Angola" | jq
     * curl "http://localhost:8702/api/kids?country=Venezuela" | jq
     */
    @Route(path = "/api/kids", methods = HttpMethod.GET)
    @APIResponse(responseCode = "200", description = "Returns the good kids per country", content = @Content(schema = @Schema(implementation = Kid.class, type = SchemaType.ARRAY)))
    public void getAllKidsPerCountry(RoutingContext rc, @Param("country") String country) {
        LOGGER.info("Get all the kids from " + country);
        Kid.findNiceKidsByCountry(country).subscribe().with(result -> {
            rc.response().putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON).setStatusCode(HttpResponseStatus.OK.code()).end(Json.encode(result));
        }, failure -> {
            rc.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN).end(failure.getMessage());
        });
    }
}
