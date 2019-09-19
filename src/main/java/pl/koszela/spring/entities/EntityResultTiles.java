package pl.koszela.spring.entities;

import javax.persistence.*;

@Entity
@Table(name = "result_tiles")
public class EntityResultTiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String priceListName;
    private String priceAfterDiscount;
    private String purchasePrice;
    private String profit;

    public EntityResultTiles() {
    }

    public static Builder builder() {
        return new Builder();
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

    public String getPriceListName() {
        return priceListName;
    }

    public void setPriceListName(String priceListName) {
        this.priceListName = priceListName;
    }

    public String getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setPriceAfterDiscount(String priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public static final class Builder {
        private String name;
        private String priceListName;
        private String priceAfterDiscount;
        private String purchasePrice;
        private String profit;

        public EntityResultTiles build() {
            EntityResultTiles entityResultTiles = new EntityResultTiles();
            entityResultTiles.name = this.name;
            entityResultTiles.priceListName = this.priceListName;
            entityResultTiles.priceAfterDiscount = this.priceAfterDiscount;
            entityResultTiles.purchasePrice = this.purchasePrice;
            entityResultTiles.profit = this.profit;
            return entityResultTiles;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder priceListName(String priceListName) {
            this.priceListName = priceListName;
            return this;
        }

        public Builder priceAfterDiscount(String priceAfterDiscount) {
            this.priceAfterDiscount = priceAfterDiscount;
            return this;
        }

        public Builder purchasePrice(String purchasePrice) {
            this.purchasePrice = purchasePrice;
            return this;
        }

        public Builder profit(String profit) {
            this.profit = profit;
            return this;
        }
    }
}
