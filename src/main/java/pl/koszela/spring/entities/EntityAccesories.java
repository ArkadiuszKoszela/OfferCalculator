package pl.koszela.spring.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accesories")
public class EntityAccesories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    private String category;
    private String name;
    private Double purchasePrice;
    private Integer margin;
    private Double firstMultiplier;
    private Double secondMultiplier;
    private BigDecimal unitRetailPrice;
    private BigDecimal totalRetail;
    private BigDecimal unitPurchasePrice;
    private BigDecimal totalPurchase;
    @ManyToOne(fetch = FetchType.LAZY)
    private EntityUser entityUserAccesories;

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

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
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

    public EntityUser getEntityUserAccesories() {
        return entityUserAccesories;
    }

    public void setEntityUserAccesories(EntityUser entityUserAccesories) {
        this.entityUserAccesories = entityUserAccesories;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static EntityAccesories.Builder builder(){
        return new EntityAccesories.Builder();
    }

    public static final class Builder {
        private String category;
        private String name;
        private Double purchasePrice;
        private Integer margin;
        private Double firstMultiplier;
        private Double secondMultiplier;
        private BigDecimal unitRetailPrice;
        private BigDecimal totalRetail;
        private BigDecimal unitPurchasePrice;
        private BigDecimal totalPurchase;

        public EntityAccesories build() {
            EntityAccesories accesories = new EntityAccesories();
            accesories.category = this.category;
            accesories.name = this.name;
            accesories.purchasePrice = this.purchasePrice;
            accesories.margin = this.margin;
            accesories.firstMultiplier = this.firstMultiplier;
            accesories.secondMultiplier = this.secondMultiplier;
            accesories.unitRetailPrice = this.unitRetailPrice;
            accesories.totalRetail = this.totalRetail;
            accesories.unitPurchasePrice = this.unitPurchasePrice;
            accesories.totalPurchase = this.totalPurchase;
            return accesories;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder purchasePrice(Double purchasePrice) {
            this.purchasePrice = purchasePrice;
            return this;
        }

        public Builder margin(Integer margin) {
            this.margin = margin;
            return this;
        }

        public Builder firstMultiplier(Double firstMultiplier) {
            this.firstMultiplier = firstMultiplier;
            return this;
        }

        public Builder secondMultiplier(Double secondMultiplier) {
            this.secondMultiplier = secondMultiplier;
            return this;
        }

        public Builder unitRetailPrice(BigDecimal unitRetailPrice) {
            this.unitRetailPrice = unitRetailPrice;
            return this;
        }

        public Builder totalRetail(BigDecimal totalRetail) {
            this.totalRetail = totalRetail;
            return this;
        }

        public Builder unitPurchasePrice(BigDecimal unitPurchasePrice) {
            this.unitPurchasePrice = unitPurchasePrice;
            return this;
        }

        public Builder totalPurchase(BigDecimal totalPurchase) {
            this.totalPurchase = totalPurchase;
            return this;
        }
    }
}
