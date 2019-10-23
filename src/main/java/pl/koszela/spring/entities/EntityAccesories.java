package pl.koszela.spring.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "accesories")
public class EntityAccesories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    private String category;
    private String name;
    private Double purchasePrice;
    private Double detalPrice;
    private Integer margin;
    private String option;
    private Double allPricePurchase;
    private Double allPriceRetail;
    private Double profit;
    private boolean offer;
    private Double quantity;
    private String date;
    @ManyToMany(mappedBy = "resultAccesories")
    private Set<EntityUser> userResultAccesories = new HashSet<>();

    public EntityAccesories() {
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

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getMargin() {
        return margin;
    }

    public void setMargin(Integer margin) {
        this.margin = margin;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static EntityAccesories.Builder builder(){
        return new EntityAccesories.Builder();
    }

    public Double getDetalPrice() {
        return detalPrice;
    }

    public void setDetalPrice(Double detalPrice) {
        this.detalPrice = detalPrice;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Set<EntityUser> getUserResultAccesories() {
        return userResultAccesories;
    }

    public void setUserResultAccesories(Set<EntityUser> userResultAccesories) {
        this.userResultAccesories = userResultAccesories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static final class Builder {
        private String category;
        private String name;
        private Double purchasePrice;
        private Double detalPrice;
        private Integer margin;
        private String option;
        private Double allPricePurchase;
        private Double allPriceRetail;
        private Double profit;
        private boolean offer;
        private Double quantity;

        public EntityAccesories build() {
            EntityAccesories accesories = new EntityAccesories();
            accesories.category = this.category;
            accesories.name = this.name;
            accesories.purchasePrice = this.purchasePrice;
            accesories.detalPrice = this.detalPrice;
            accesories.margin = this.margin;
            accesories.option = this.option;
            accesories.allPricePurchase = this.allPricePurchase;
            accesories.allPriceRetail = this.allPriceRetail;
            accesories.profit = this.profit;
            accesories.offer = this.offer;
            accesories.quantity = this.quantity;
            return accesories;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder purchasePrice(Double purchasePrice) {
            this.purchasePrice = purchasePrice;
            return this;
        }

        public Builder detalPrice(Double detalPrice) {
            this.detalPrice = detalPrice;
            return this;
        }

        public Builder margin(Integer margin) {
            this.margin = margin;
            return this;
        }

        public Builder pption(String option) {
            this.option = option;
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

        public Builder quantity(Double quantity) {
            this.quantity = quantity;
            return this;
        }
    }
}