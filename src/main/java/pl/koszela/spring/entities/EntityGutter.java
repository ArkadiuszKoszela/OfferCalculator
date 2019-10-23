package pl.koszela.spring.entities;

import javax.persistence.*;

@Entity
@Table(name = "gutter")
public class EntityGutter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double quantity;
    private Integer discount;
    private Double unitPriceDetal;
    private Double unitPricePurchase;
    private Double allPriceDetal;
    private Double allPricePurchase;
    private Double profit;
    private String category;

    public EntityGutter() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPriceDetal() {
        return unitPriceDetal;
    }

    public void setUnitPriceDetal(Double unitPriceDetal) {
        this.unitPriceDetal = unitPriceDetal;
    }

    public Double getUnitPricePurchase() {
        return unitPricePurchase;
    }

    public void setUnitPricePurchase(Double unitPricePurchase) {
        this.unitPricePurchase = unitPricePurchase;
    }

    public Double getAllPriceDetal() {
        return allPriceDetal;
    }

    public void setAllPriceDetal(Double allPriceDetal) {
        this.allPriceDetal = allPriceDetal;
    }

    public Double getAllPricePurchase() {
        return allPricePurchase;
    }

    public void setAllPricePurchase(Double allPricePurchase) {
        this.allPricePurchase = allPricePurchase;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}