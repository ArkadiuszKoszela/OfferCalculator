package pl.koszela.spring.service;

import pl.koszela.spring.entities.BaseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatePrices {

    public static Double calculatePurchasePrice(BaseEntity baseEntity) {
        BigDecimal priceDetal = BigDecimal.valueOf(baseEntity.getUnitDetalPrice());
        BigDecimal discounts = BigDecimal.valueOf(4 - discount(baseEntity.getBasicDiscount()) - discount(baseEntity.getAdditionalDiscount()) - discount(baseEntity.getPromotionDiscount()) - discount(baseEntity.getSkontoDiscount()));
        BigDecimal result = BigDecimal.valueOf(1).subtract(discounts).setScale(2, RoundingMode.HALF_UP);
        return priceDetal.multiply(result).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private static Double discount(Integer value) {
        return BigDecimal.valueOf(100 - value).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).doubleValue();
    }

    static Double calculateDetalPrice(BaseEntity baseEntity) {
        BigDecimal pricePurchase = BigDecimal.valueOf(baseEntity.getUnitPurchasePrice());
        BigDecimal discounts = BigDecimal.valueOf(4 - discount(baseEntity.getBasicDiscount()) - discount(baseEntity.getAdditionalDiscount()) - discount(baseEntity.getPromotionDiscount()) - discount(baseEntity.getSkontoDiscount()));
        BigDecimal result = BigDecimal.valueOf(1).subtract(discounts).setScale(2, RoundingMode.HALF_UP);
        return pricePurchase.divide(result, 2, RoundingMode.HALF_UP).doubleValue();
    }
}
