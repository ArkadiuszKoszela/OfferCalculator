package pl.koszela.spring.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "result_accesories")
public class EntityResultAccesories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private Double quantity;
    private Double pricePurchase;
    private Double priceRetail;
    private Double allPricePurchase;
    private Double allPriceRetail;
    private Double profit;
    private boolean offer;
    @ManyToMany(mappedBy = "resultAccesories")
    private Set<EntityUser> userResultAccesories = new HashSet<>();

    public EntityResultAccesories() {
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

    public Double getPricePurchase() {
        return pricePurchase;
    }

    public void setPricePurchase(Double pricePurchase) {
        this.pricePurchase = pricePurchase;
    }

    public Double getPriceRetail() {
        return priceRetail;
    }

    public void setPriceRetail(Double priceRetail) {
        this.priceRetail = priceRetail;
    }

    public Double getAllPricePurchase() {
        return allPricePurchase;
    }

    public void setAllPricePurchase(Double allPricePurchase) {
        this.allPricePurchase = allPricePurchase;
    }

    public Double getAllPriceRetail() {
        return allPriceRetail;
    }

    public void setAllPriceRetail(Double allPriceRetail) {
        this.allPriceRetail = allPriceRetail;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public boolean isOffer() {
        return offer;
    }

    public void setOffer(boolean offer) {
        this.offer = offer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<EntityUser> getUserResultAccesories() {
        return userResultAccesories;
    }

    public void setUserResultAccesories(Set<EntityUser> userResultAccesories) {
        this.userResultAccesories = userResultAccesories;
    }
}
