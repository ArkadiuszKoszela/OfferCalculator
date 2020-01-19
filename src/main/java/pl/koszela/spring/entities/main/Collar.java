package pl.koszela.spring.entities.main;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ak_collar")
public class Collar extends BaseEntity{

    private String manufacturer;
    private String size;
    private boolean offer;
    @ManyToMany(mappedBy = "userCollars")
    private Set<User> collarsUser = new HashSet<>();

    public Collar() {
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isOffer() {
        return offer;
    }

    public void setOffer(boolean offer) {
        this.offer = offer;
    }

    public Set<User> getCollarsUser() {
        return collarsUser;
    }

    public void setCollarsUser(Set<User> collarsUser) {
        this.collarsUser = collarsUser;
    }
}