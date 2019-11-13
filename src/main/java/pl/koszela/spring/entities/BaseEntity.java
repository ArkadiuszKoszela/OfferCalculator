package pl.koszela.spring.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BaseEntity extends Discounts {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String name;
    private String manufacturer;
    private Double quantity;
    private Double unitPurchasePrice;
    private Double unitDetalPrice;
    private Double allpriceAfterDiscount;
    private Double allpricePurchase;
    private Double allprofit;
    private String category;
    private String size;
    private String type;

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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPurchasePrice() {
        return unitPurchasePrice;
    }

    public void setUnitPurchasePrice(Double unitPurchasePrice) {
        this.unitPurchasePrice = unitPurchasePrice;
    }

    public Double getUnitDetalPrice() {
        return unitDetalPrice;
    }

    public void setUnitDetalPrice(Double unitDetalPrice) {
        this.unitDetalPrice = unitDetalPrice;
    }

    public Double getAllpriceAfterDiscount() {
        return allpriceAfterDiscount;
    }

    public void setAllpriceAfterDiscount(Double allpriceAfterDiscount) {
        this.allpriceAfterDiscount = allpriceAfterDiscount;
    }

    public Double getAllpricePurchase() {
        return allpricePurchase;
    }

    public void setAllpricePurchase(Double allpricePurchase) {
        this.allpricePurchase = allpricePurchase;
    }

    public Double getAllprofit() {
        return allprofit;
    }

    public void setAllprofit(Double allprofit) {
        this.allprofit = allprofit;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
