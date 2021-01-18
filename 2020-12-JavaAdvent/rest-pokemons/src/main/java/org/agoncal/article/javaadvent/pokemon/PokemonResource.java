package org.agoncal.article.javaadvent.pokemon;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
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

/**
 * @author Antonio Goncalves @agoncal
 * http://www.antoniogoncalves.org
 * --
 * Edited by @serrodcal
 */
@OpenAPIDefinition(info = @Info(title = "API that returns Pokemons", version = "1.0"))
@Singleton
@RouteBase(path = "/api")
public class PokemonResource {

    private static final Logger LOGGER = Logger.getLogger(PokemonResource.class);

    private static final String APPLICATION_JSON = "application/json";
    private static final String TEXT_PLAIN = "text/plain";

    /**
     * curl "http://localhost:8703/api/pokemons" | jq
     * curl "http://localhost:8703/api/pokemons/random" | jq
     */
    @Route(path = "/pokemons/random", methods = HttpMethod.GET)
    @APIResponse(responseCode = "200", description = "Returns a random Pokemon")
    public void getARandomPokemon(RoutingContext rc) {
        LOGGER.info("Get a random Pokemon");
        Pokemon.findARandomPokemon().subscribe().with(result -> {
            rc.response().putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON).setStatusCode(HttpResponseStatus.OK.code()).end(Json.encode(result));
        }, failure -> {
            rc.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN).end(failure.getMessage());
        });
    }

    @Route(path = "/pokemons", methods = HttpMethod.GET)
    @APIResponse(responseCode = "200", description = "Returns all the Pokemons", content = @Content(schema = @Schema(implementation = Pokemon.class, type = SchemaType.ARRAY)))
    public void getAllPokemons(RoutingContext rc) {
        LOGGER.info("Returns all the Pokemons");
        Pokemon.findAll().list().subscribe().with(result -> {
            rc.response().putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON).setStatusCode(HttpResponseStatus.OK.code()).end(Json.encode(result));
        }, failure -> {
            rc.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN).end(failure.getMessage());
        });
    }
}
