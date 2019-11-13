package pl.koszela.spring.service;

import pl.koszela.spring.entities.BaseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatePrices {

    public static Double calculatePurchasePrice(BaseEntity baseEntity) {
        BigDecimal pricePurchase = BigDecimal.valueOf(baseEntity.getUnitDetalPrice());
        BigDecimal result = BigDecimal.valueOf(4 - cos(baseEntity.getBasicDiscount()) - cos(baseEntity.getAdditionalDiscount()) - cos(baseEntity.getPromotionDiscount()) - cos(baseEntity.getSkontoDiscount()));
        BigDecimal result1 = BigDecimal.valueOf(1).subtract(result).setScale(2, RoundingMode.HALF_UP);
        return pricePurchase.multiply(result1).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private static Double cos(Integer value) {
        return BigDecimal.valueOf(100 - value).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).doubleValue();
    }

    public static Double calculateDetalPrice(BaseEntity baseEntity) {
        BigDecimal pricePurchase = BigDecimal.valueOf(baseEntity.getUnitPurchasePrice());
        BigDecimal result = BigDecimal.valueOf(4 - cos(baseEntity.getBasicDiscount()) - cos(baseEntity.getAdditionalDiscount()) - cos(baseEntity.getPromotionDiscount()) - cos(baseEntity.getSkontoDiscount()));
        BigDecimal result1 = BigDecimal.valueOf(1).subtract(result).setScale(2, RoundingMode.HALF_UP);
        return pricePurchase.divide(result1, 2, RoundingMode.HALF_UP).doubleValue();
    }
}
