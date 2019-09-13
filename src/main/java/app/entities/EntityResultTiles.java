package app.entities;

import javax.persistence.*;
import java.util.Objects;

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

    public EntityResultTiles(String name, String priceListName, String priceAfterDiscount, String purchasePrice, String profit) {
        this.name = name;
        this.priceListName = priceListName;
        this.priceAfterDiscount = priceAfterDiscount;
        this.purchasePrice = purchasePrice;
        this.profit = profit;
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

    @Override
    public String toString() {
        return "EntityResultTiles{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", priceListName='" + priceListName + '\'' +
                ", priceAfterDiscount='" + priceAfterDiscount + '\'' +
                ", purchasePrice='" + purchasePrice + '\'' +
                ", profit='" + profit + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityResultTiles that = (EntityResultTiles) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(priceListName, that.priceListName) &&
                Objects.equals(priceAfterDiscount, that.priceAfterDiscount) &&
                Objects.equals(purchasePrice, that.purchasePrice) &&
                Objects.equals(profit, that.profit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, priceListName, priceAfterDiscount, purchasePrice, profit);
    }
}
