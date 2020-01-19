package pl.koszela.spring.entities.main;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ak_fireside")
public class Fireside extends BaseEntity{

    private String manufacturer;
    private String category;
    private boolean offer;

    public Fireside() {
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isOffer() {
        return offer;
    }

    public void setOffer(boolean offer) {
        this.offer = offer;
    }
}