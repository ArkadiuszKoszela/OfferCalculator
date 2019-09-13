package app.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "result_accesories")
public class EntityResultAccesories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    private String name;
    private Double quantity;
    private BigDecimal unitRetailPrice;
    private BigDecimal totalRetail;
    private BigDecimal unitPurchasePrice;
    private BigDecimal totalPurchase;

    public EntityResultAccesories() {
    }

    public EntityResultAccesories(String name, Double quantity, BigDecimal unitRetailPrice, BigDecimal totalRetail, BigDecimal unitPurchasePrice, BigDecimal totalPurchase) {
        this.name = name;
        this.quantity = quantity;
        this.unitRetailPrice = unitRetailPrice;
        this.totalRetail = totalRetail;
        this.unitPurchasePrice = unitPurchasePrice;
        this.totalPurchase = totalPurchase;
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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
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

    @Override
    public String toString() {
        return "EntityResultAccesories{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", unitRetailPrice=" + unitRetailPrice +
                ", totalRetail=" + totalRetail +
                ", unitPurchasePrice=" + unitPurchasePrice +
                ", totalPurchase=" + totalPurchase +
                '}';
    }
}
