package org.agoncal.article.javaadvent.kid;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;

import javax.persistence.Entity;
import java.util.List;

/**
 * @author Antonio Goncalves @agoncal
 * http://www.antoniogoncalves.org
 * --
 * Edited by @serrodcal
 */
@Entity
public class Kid extends PanacheEntity {

    public String name;
    public String address;
    public boolean chimney;
    public boolean naughty;
    public String country;

    public static Uni<List<Kid>> findNiceKidsByCountry(String country) {
        return list("country = ?1 and naughty = false", country);
    }
}
