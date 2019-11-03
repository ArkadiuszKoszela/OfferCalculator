package pl.koszela.spring.entities;

import pl.koszela.spring.entities.BaseEntity;
import pl.koszela.spring.entities.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tiles")
public class Tiles extends BaseEntity {

    private String priceListName;
    private Integer basicDiscount;
    private Integer promotionDiscount;
    private Integer additionalDiscount;
    private Integer skontoDiscount;
    private BigDecimal totalPrice;
    private BigDecimal totalProfit;
    private String date;
    @ManyToMany(mappedBy = "userTiles")
    private Set<User> tilesUser = new HashSet<>();
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean main;
    @Column(name = "option_tiles", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean option;

    public Tiles() {
    }

    public String getPriceListName() {
        return priceListName;
    }

    public void setPriceListName(String priceListName) {
        this.priceListName = priceListName;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public Set<User> getTilesUser() {
        return tilesUser;
    }

    public void setTilesUser(Set<User> tilesUser) {
        this.tilesUser = tilesUser;
    }

    public boolean isOption() {
        return option;
    }

    public void setOption(boolean option) {
        this.option = option;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public Integer getBasicDiscount() {
        return basicDiscount;
    }

    public void setBasicDiscount(Integer basicDiscount) {
        this.basicDiscount = basicDiscount;
    }

    public Integer getPromotionDiscount() {
        return promotionDiscount;
    }

    public void setPromotionDiscount(Integer promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }

    public Integer getAdditionalDiscount() {
        return additionalDiscount;
    }

    public void setAdditionalDiscount(Integer additionalDiscount) {
        this.additionalDiscount = additionalDiscount;
    }

    public Integer getSkontoDiscount() {
        return skontoDiscount;
    }

    public void setSkontoDiscount(Integer skontoDiscount) {
        this.skontoDiscount = skontoDiscount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}