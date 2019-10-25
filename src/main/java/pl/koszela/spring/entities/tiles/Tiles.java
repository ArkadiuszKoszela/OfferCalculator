package pl.koszela.spring.entities.tiles;

import pl.koszela.spring.entities.EntityUser;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tiles")
public class Tiles {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String priceListName;
    private String name;
    private Double quantity;
    private Integer discount;
    private Integer basicDiscount;
    private Integer promotionDiscount;
    private Integer additionalDiscount;
    private Integer skontoDiscount;
    private Double priceFromRepo;
    private BigDecimal priceDetalUnit;
    private BigDecimal allpriceAfterDiscount;
    private BigDecimal allpricePurchase;
    private BigDecimal allprofit;
    private BigDecimal totalPrice;
    private BigDecimal totalProfit;
    private String date;
    @ManyToMany(mappedBy = "entityUserTiles")
    private Set<EntityUser> userTiles = new HashSet<>();
    private boolean main;
    private boolean option;

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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public BigDecimal getPriceDetalUnit() {
        return priceDetalUnit;
    }

    public void setPriceDetalUnit(BigDecimal priceDetalUnit) {
        this.priceDetalUnit = priceDetalUnit;
    }

    public BigDecimal getAllpriceAfterDiscount() {
        return allpriceAfterDiscount;
    }

    public void setAllpriceAfterDiscount(BigDecimal allpriceAfterDiscount) {
        this.allpriceAfterDiscount = allpriceAfterDiscount;
    }

    public BigDecimal getAllpricePurchase() {
        return allpricePurchase;
    }

    public void setAllpricePurchase(BigDecimal allpricePurchase) {
        this.allpricePurchase = allpricePurchase;
    }

    public BigDecimal getAllprofit() {
        return allprofit;
    }

    public void setAllprofit(BigDecimal allprofit) {
        this.allprofit = allprofit;
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

    public Set<EntityUser> getUserTiles() {
        return userTiles;
    }

    public void setUserTiles(Set<EntityUser> userTiles) {
        this.userTiles = userTiles;
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

    public Double getPriceFromRepo() {
        return priceFromRepo;
    }

    public void setPriceFromRepo(Double priceFromRepo) {
        this.priceFromRepo = priceFromRepo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}