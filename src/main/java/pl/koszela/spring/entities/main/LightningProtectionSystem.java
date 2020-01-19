package pl.koszela.spring.entities.main;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ak_lightning_protection_system")
public class LightningProtectionSystem extends BaseEntity{

    private String category;
    private boolean offer;

    public LightningProtectionSystem() {
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