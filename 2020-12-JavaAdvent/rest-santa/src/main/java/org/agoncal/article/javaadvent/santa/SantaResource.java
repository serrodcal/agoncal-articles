package org.agoncal.article.javaadvent.santa;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.vertx.web.Body;
import io.quarkus.vertx.web.Param;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.core.cli.annotations.DefaultValue;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import javax.ws.rs.QueryParam;
import java.util.Optional;

/**
 * @author Antonio Goncalves @agoncal
 * http://www.antoniogoncalves.org
 * --
 * Edited by @serrodcal
 */
// tag::adocSnippet[]
@OpenAPIDefinition(info = @Info(title = "API to manage Santa's schedule", version = "1.0"))
@Singleton
@RouteBase(path = "/api")
public class SantaResource {

    private static final String APPLICATION_JSON = "application/json";
    private static final String TEXT_PLAIN = "text/plain";

    @Inject
    SantaService service;

    // tag::adocSkip[]
    private static final Logger LOGGER = Logger.getLogger(SantaService.class);

    /**
     * curl -X POST -H "Content-Type: text/plain" -d "Portugal" http://localhost:8701/api/santa
     */
    @APIResponse(responseCode = "201", description = "Creates a new 2020 Santa's schedule for a given country")
    // end::adocSkip[]
    @Route(path = "/santa", methods = HttpMethod.POST)
    @Transactional
    public void createASchedule(RoutingContext rc, @Body String country) {
        // tag::adocSkip[]
        LOGGER.info("Creating a schedule for " + country);

        // end::adocSkip[]
        service.getAllGoodChildren(country).subscribe().with(schedule -> {
            service.getEachChildAPresent(schedule).subscribe().with(result -> {
                result.persist();
                rc.response().putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON).setStatusCode(HttpResponseStatus.OK.code()).end(Json.encode(result));
            }, failure -> {
                rc.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN).end(failure.getMessage());
            });
        }, failure -> {
            rc.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN).end(failure.getMessage());
        });
    }

    // tag::adocSkip[]

    /**
     * curl "http://localhost:8701/api/santa?country=Angola&year=2019" | jq
     * curl "http://localhost:8701/api/santa?country=Venezuela" | jq
     */
    @APIResponse(responseCode = "200", description = "Returns Santa's schedule for a given country and year")
    // end::adocSkip[]
    @Route(path = "/santa", methods = HttpMethod.GET)
    public void getASchedule(RoutingContext rc, @Param("country") String country, @Param(value = "year") String yearParam) {
        int year = yearParam != null ? Integer.valueOf(yearParam) : 2020;
        // tag::adocSkip[]
        LOGGER.info("Getting the schedule of " + country + " in " + year);

        // end::adocSkip[]
        Schedule.findByYearAndCountry(year, country).subscribe().with(result -> {
            rc.response().putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON).setStatusCode(HttpResponseStatus.OK.code()).end(Json.encode(result));
        }, failure -> {
            rc.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).putHeader(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN).end(failure.getMessage());
        });
        //return Schedule.findByYearAndCountry(year, country);
    }
}
// end::adocSnippet[]
