package org.agoncal.article.javaadvent.pokemon;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;

import javax.persistence.Entity;
import java.util.Random;

/**
 * @author Antonio Goncalves @agoncal
 * http://www.antoniogoncalves.org
 * --
 * Edited by @serrodcal
 */
@Entity
public class Pokemon extends PanacheEntity {

    public String name;
    public String manufacturer;
    public int weight;

    public static Uni<Pokemon> findARandomPokemon() {
        return Pokemon.count().flatMap(count -> {
            int random = new Random().nextInt(count.intValue());
            return Pokemon.findAll().page(random, 1).firstResult();
        });
    }
}
