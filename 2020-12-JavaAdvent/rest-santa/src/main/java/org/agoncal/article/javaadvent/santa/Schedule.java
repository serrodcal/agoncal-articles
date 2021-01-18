package org.agoncal.article.javaadvent.santa;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import org.agoncal.article.javaadvent.santa.proxy.Child;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Goncalves @agoncal
 * http://www.antoniogoncalves.org
 * --
 * Edited by @serrodcal
 */
@Entity
public class Schedule extends PanacheEntity {

    public int year;
    public String country;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<Delivery> deliveries = new ArrayList<>();

    public Schedule() {
    }

    public Schedule(int year, String country) {
        this.year = year;
        this.country = country;
    }

    public static Uni<Schedule> findByYearAndCountry(int year, String country) {
        return find("year = ?1 and country = ?2", year, country).firstResult();
    }

    public void addDelivery(Child child) {
        deliveries.add(new Delivery(child));
    }

    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
    }
}
