package app.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "tiles")
public class EntityTiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String priceListName;
    private String type;
    private String name;
    private BigDecimal unitRetailPrice;

    private Integer profit;
    private Integer basicDiscount;
    private Integer supplierDiscount;
    private Integer additionalDiscount;
    private Integer skontoDiscount;

    public EntityTiles() {
    }

    public EntityTiles(String priceListName, String type, String name, BigDecimal unitRetailPrice, Integer profit, Integer basicDiscount, Integer supplierDiscount, Integer additionalDiscount, Integer skontoDiscount) {
        this.priceListName = priceListName;
        this.type = type;
        this.name = name;
        this.unitRetailPrice = unitRetailPrice;
        this.profit = profit;
        this.basicDiscount = basicDiscount;
        this.supplierDiscount = supplierDiscount;
        this.additionalDiscount = additionalDiscount;
        this.skontoDiscount = skontoDiscount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPriceListName() {
        return priceListName;
    }

    public void setPriceListName(String priceListName) {
        this.priceListName = priceListName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getUnitRetailPrice() {
        return unitRetailPrice;
    }

    public void setUnitRetailPrice(BigDecimal unitRetailPrice) {
        this.unitRetailPrice = unitRetailPrice;
    }

    public Integer getProfit() {
        return profit;
    }

    public void setProfit(Integer profit) {
        this.profit = profit;
    }

    public Integer getBasicDiscount() {
        return basicDiscount;
    }

    public void setBasicDiscount(Integer basicDiscount) {
        this.basicDiscount = basicDiscount;
    }

    public Integer getSupplierDiscount() {
        return supplierDiscount;
    }

    public void setSupplierDiscount(Integer supplierDiscount) {
        this.supplierDiscount = supplierDiscount;
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

    @Override
    public String toString() {
        return "EntityTiles{" +
                "id=" + id +
                ", priceListName='" + priceListName + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", unitRetailPrice=" + unitRetailPrice +
                ", profit=" + profit +
                ", basicDiscount=" + basicDiscount +
                ", supplierDiscount=" + supplierDiscount +
                ", additionalDiscount=" + additionalDiscount +
                ", skontoDiscount=" + skontoDiscount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityTiles that = (EntityTiles) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(priceListName, that.priceListName) &&
                Objects.equals(type, that.type) &&
                Objects.equals(name, that.name) &&
                Objects.equals(unitRetailPrice, that.unitRetailPrice) &&
                Objects.equals(profit, that.profit) &&
                Objects.equals(basicDiscount, that.basicDiscount) &&
                Objects.equals(supplierDiscount, that.supplierDiscount) &&
                Objects.equals(additionalDiscount, that.additionalDiscount) &&
                Objects.equals(skontoDiscount, that.skontoDiscount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, priceListName, type, name, unitRetailPrice, profit, basicDiscount, supplierDiscount, additionalDiscount, skontoDiscount);
    }
}
