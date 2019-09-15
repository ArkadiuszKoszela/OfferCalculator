package app.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accesories")
public class EntityAccesories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    private String name;

    private BigDecimal purchasePrice;
    private Integer margin;
    private Double firstMultiplier;
    private Double secondMultiplier;

    public EntityAccesories() {
    }

    public EntityAccesories(String name, BigDecimal purchasePrice, Integer margin, Double firstMultiplier, Double secondMultiplier) {
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.margin = margin;
        this.firstMultiplier = firstMultiplier;
        this.secondMultiplier = secondMultiplier;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getMargin() {
        return margin;
    }

    public void setMargin(Integer margin) {
        this.margin = margin;
    }

    public Double getFirstMultiplier() {
        return firstMultiplier;
    }

    public void setFirstMultiplier(Double firstMultiplier) {
        this.firstMultiplier = firstMultiplier;
    }

    public Double getSecondMultiplier() {
        return secondMultiplier;
    }

    public void setSecondMultiplier(Double secondMultiplier) {
        this.secondMultiplier = secondMultiplier;
    }

    @Override
    public String toString() {
        return "EntityAccesories{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", margin=" + margin +
                ", firstMultiplier=" + firstMultiplier +
                ", secondMultiplier=" + secondMultiplier +
                '}';
    }
}