package pl.koszela.spring.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tiles_tiles")
public class Tiles {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String priceListName;
    private String name;
    private BigDecimal price;
    private BigDecimal priceAfterDiscount;
    private BigDecimal pricePurchase;
    private BigDecimal profit;
    private BigDecimal totalPrice;

    public Tiles() {
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

    public String getPriceListName() {
        return priceListName;
    }

    public void setPriceListName(String priceListName) {
        this.priceListName = priceListName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setPriceAfterDiscount(BigDecimal priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    public BigDecimal getPricePurchase() {
        return pricePurchase;
    }

    public void setPricePurchase(BigDecimal pricePurchase) {
        this.pricePurchase = pricePurchase;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Tiles{" +
                "id=" + id +
                ", priceListName='" + priceListName + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", priceAfterDiscount=" + priceAfterDiscount +
                ", pricePurchase=" + pricePurchase +
                ", profit=" + profit +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
