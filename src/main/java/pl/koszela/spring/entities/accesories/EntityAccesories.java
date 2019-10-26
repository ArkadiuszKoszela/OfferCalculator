package pl.koszela.spring.entities.accesories;

import pl.koszela.spring.entities.BaseEntity;
import pl.koszela.spring.entities.EntityUser;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "accesories")
public class EntityAccesories extends BaseEntity {

    private String category;
    private Integer margin;
    private String option;
    private boolean offer;
    private String date;
    @ManyToMany(mappedBy = "accesories")
    private Set<EntityUser> userResultAccesories = new HashSet<>();

    public EntityAccesories() {
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

    public static Builder builder(){
        return new Builder();
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public boolean isOffer() {
        return offer;
    }

    public void setOffer(boolean offer) {
        this.offer = offer;
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

    public static final class Builder extends BaseEntity {
        private String category;
        private Integer margin;
        private String option;
        private boolean offer;

        public EntityAccesories build() {
            EntityAccesories accesories = new EntityAccesories();
            accesories.category = this.category;
            accesories.margin = this.margin;
            accesories.option = this.option;
            accesories.offer = this.offer;
            return accesories;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

//        public Builder name(String name) {
//            this.name = name;
//            return this;
//        }
//
//        public Builder purchasePrice(Double purchasePrice) {
//            this.purchasePrice = purchasePrice;
//            return this;
//        }
//
//        public Builder detalPrice(Double detalPrice) {
//            this.detalPrice = detalPrice;
//            return this;
//        }

        public Builder margin(Integer margin) {
            this.margin = margin;
            return this;
        }

        public Builder pption(String option) {
            this.option = option;
            return this;
        }

//        public Builder allPricePurchase(Double allPricePurchase) {
//            this.allPricePurchase = allPricePurchase;
//            return this;
//        }
//
//        public Builder allPriceRetail(Double allPriceRetail) {
//            this.allPriceRetail = allPriceRetail;
//            return this;
//        }
//
//        public Builder profit(Double profit) {
//            this.profit = profit;
//            return this;
//        }

        public Builder offer(boolean offer) {
            this.offer = offer;
            return this;
        }

//        public Builder quantity(Double quantity) {
//            this.quantity = quantity;
//            return this;
//        }
    }
}