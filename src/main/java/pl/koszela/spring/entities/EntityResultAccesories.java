package pl.koszela.spring.entities;

import javafx.util.Builder;

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

    private EntityResultAccesories() {
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

    public static EntityResultAccesories.Builder builder() {
        return new EntityResultAccesories.Builder();
    }

    public static final class Builder {
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

        public EntityResultAccesories build() {
            EntityResultAccesories resultAccesories = new EntityResultAccesories();

            resultAccesories.id = this.id;
            resultAccesories.name = this.name;
            resultAccesories.category = this.category;
            resultAccesories.quantity = this.quantity;
            resultAccesories.pricePurchase = this.pricePurchase;
            resultAccesories.priceRetail = this.priceRetail;
            resultAccesories.allPricePurchase = this.allPricePurchase;
            resultAccesories.allPriceRetail = this.allPriceRetail;
            resultAccesories.profit = this.profit;
            resultAccesories.offer = this.offer;

            return resultAccesories;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder quantity(Double quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder pricePurchase(Double pricePurchase) {
            this.pricePurchase = pricePurchase;
            return this;
        }

        public Builder priceRetail(Double priceRetail) {
            this.priceRetail = priceRetail;
            return this;
        }

        public Builder allPricePurchase(Double allPricePurchase) {
            this.allPricePurchase = allPricePurchase;
            return this;
        }

        public Builder allPriceRetail(Double allPriceRetail) {
            this.allPriceRetail = allPriceRetail;
            return this;
        }

        public Builder profit(Double profit) {
            this.profit = profit;
            return this;
        }

        public Builder offer(boolean offer) {
            this.offer = offer;
            return this;
        }
    }
}
