package pl.koszela.spring.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accesories")
public class EntityAccesories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    private String name;
    private BigDecimal purchasePrice;
    private Integer margin;
    private Double firstMultiplier;
    private Double secondMultiplier;
    private BigDecimal unitRetailPrice;
    private BigDecimal totalRetail;
    private BigDecimal unitPurchasePrice;
    private BigDecimal totalPurchase;

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

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getMargin() {
        return margin;
    }

    public void setMargin(Integer margin) {
        this.margin = margin;
    }

    public Double getFirstMultiplier() {
        return firstMultiplier;
    }

    public void setFirstMultiplier(Double firstMultiplier) {
        this.firstMultiplier = firstMultiplier;
    }

    public Double getSecondMultiplier() {
        return secondMultiplier;
    }

    public void setSecondMultiplier(Double secondMultiplier) {
        this.secondMultiplier = secondMultiplier;
    }

    public BigDecimal getUnitRetailPrice() {
        return unitRetailPrice;
    }

    public void setUnitRetailPrice(BigDecimal unitRetailPrice) {
        this.unitRetailPrice = unitRetailPrice;
    }

    public BigDecimal getTotalRetail() {
        return totalRetail;
    }

    public void setTotalRetail(BigDecimal totalRetail) {
        this.totalRetail = totalRetail;
    }

    public BigDecimal getUnitPurchasePrice() {
        return unitPurchasePrice;
    }

    public void setUnitPurchasePrice(BigDecimal unitPurchasePrice) {
        this.unitPurchasePrice = unitPurchasePrice;
    }

    public BigDecimal getTotalPurchase() {
        return totalPurchase;
    }

    public void setTotalPurchase(BigDecimal totalPurchase) {
        this.totalPurchase = totalPurchase;
    }
}
