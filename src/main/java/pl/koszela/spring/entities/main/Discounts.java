package pl.koszela.spring.entities.main;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Discounts extends IDs{

    private Integer discount;
    private Integer basicDiscount;
    private Integer promotionDiscount;
    private Integer additionalDiscount;
    private Integer skontoDiscount;

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
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
}
