package org.agoncal.article.javaadvent.santa;

import org.agoncal.article.javaadvent.santa.proxy.Child;
import org.agoncal.article.javaadvent.santa.proxy.ChildProxy;
import org.agoncal.article.javaadvent.santa.proxy.PresentProxy;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @author Antonio Goncalves @agoncal
 * http://www.antoniogoncalves.org
 * --
 */
@ApplicationScoped
public class SantaService {

    private static final Logger LOGGER = Logger.getLogger(SantaService.class);

    @Inject
    EntityManager em;

    @Inject
    @RestClient
    ChildProxy childProxy;

    @Retry(maxRetries = 5, delay = 2, delayUnit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "getLastYearScheduleForASpecificCountry")
    public Schedule getAllTheChildrenForASpecificCountry(String country) {
        LOGGER.info("Getting the children from " + country);
        Schedule schedule = new Schedule(2020, country);

        List<Child> allChildrenPerCountry = childProxy.getAllChildrenPerCountry(country);
        for (Child child : allChildrenPerCountry) {
            schedule.addDelivery(child);
        }
        return schedule;
    }

    public Schedule getLastYearScheduleForASpecificCountry(String country) {
        LOGGER.info("Getting last year schedule for " + country);
        Schedule schedule = Schedule.findByYearAndCountry(2019, country).get();
        return deepCopy(schedule);
    }

    private Schedule deepCopy(Schedule schedule) {
        em.clear();

        Schedule scheduleCopy = new Schedule();
        scheduleCopy.year = 2020;
        scheduleCopy.country = schedule.country;

        for (Delivery delivery : schedule.deliveries) {
            Delivery deliveryCopy = new Delivery();
            deliveryCopy.kidName = delivery.kidName;
            deliveryCopy.kidAddress = delivery.kidAddress;
            deliveryCopy.kidChimney = delivery.kidChimney;
            scheduleCopy.addDelivery(deliveryCopy);
        }
        return scheduleCopy;
    }

    @Inject
    @RestClient
    PresentProxy presentProxy;

    @Fallback(fallbackMethod = "getEachChildASantaToy")
    public Schedule getEachChildAToy(Schedule schedule) {
        LOGGER.info("Getting a few toys");

        for (Delivery delivery : schedule.deliveries) {
            delivery.toyName = presentProxy.getAToy().name;
        }
        return schedule;
    }

    public Schedule getEachChildASantaToy(Schedule schedule) {
        LOGGER.info("Getting a Santa toy for each child");

        for (Delivery delivery : schedule.deliveries) {
            delivery.toyName = "Santa Toy";
        }
        return schedule;
    }
}
