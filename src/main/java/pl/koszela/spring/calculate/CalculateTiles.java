package pl.koszela.spring.calculate;

import com.vaadin.flow.component.textfield.NumberField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.EntityTiles;
import pl.koszela.spring.repositories.AccesoriesRepository;
import pl.koszela.spring.repositories.TilesRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class CalculateTiles {

    private TilesRepository tilesRepository;

    @Autowired
    public CalculateTiles(TilesRepository tilesRepository) {
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
    }

    public List<String> getAvailablePriceList() {
        Iterable<EntityTiles> allTilesFromRepository = tilesRepository.findAll();
        Set<String> allTiles = new TreeSet<>();
        allTilesFromRepository.forEach(e -> allTiles
                .add(e.getPriceListName()
                        .concat(" ")
                        .concat(e.getType())));

        List<String> listWithoutDuplicates = new ArrayList<>(allTiles);
        Collection<String> remove = new TreeSet<>();
        for (int i = 0; i < listWithoutDuplicates.size(); i++) {
            remove.add(listWithoutDuplicates.get(i));
            i++;
        }
        allTiles.removeAll(remove);

        return new ArrayList<>(allTiles);
    }


    public void getProfit(List<EntityTiles> resultTiles) {
        resultTiles.forEach(e -> {
            BigDecimal retailPrice = new BigDecimal(e.getPriceAfterDiscount());
            BigDecimal purchasePrice = new BigDecimal(e.getPurchasePrice());
            e.setProfitCalculate(String.valueOf(retailPrice.subtract(purchasePrice)));
        });
    }

    public void getPurchase(List<EntityTiles> tilesList, List<Double> listOfNumberFields) {
        for (int i = 0; i < tilesList.size(); i++) {
            for (Double value : listOfNumberFields) {
                calculatePurchase(tilesList, i, value);
            }
        }
    }

    private void calculatePurchase(List<EntityTiles> tilesList, int i, Double value) {
        BigDecimal profit = new BigDecimal(tilesList.get(i).getProfit()).add(new BigDecimal(100));
        BigDecimal bigDecimal = new BigDecimal(value)
                .multiply(tilesList.get(i).getUnitRetailPrice())
                .multiply(BigDecimal.valueOf(100)).divide(profit, 2, RoundingMode.HALF_UP);
        tilesList.get(i).setPurchasePrice(String.valueOf(bigDecimal));
    }

    private void calculatePurchasePowierzchnia(List<EntityTiles> tilesList, int i, NumberField numberField) {
        BigDecimal profit = new BigDecimal(tilesList.get(i).getProfit()).add(new BigDecimal(100));
        BigDecimal bigDecimal = new BigDecimal(numberField.getValue())
                .multiply(BigDecimal.valueOf(12))
                .multiply(tilesList.get(i).getUnitRetailPrice())
                .multiply(BigDecimal.valueOf(100)).divide(profit, 2, RoundingMode.HALF_UP);
        tilesList.get(i).setPurchasePrice(String.valueOf(bigDecimal));
    }

    public void getRetail(List<EntityTiles> tilesList, NumberField customerDiscount, List<Double> listOfNumberFields) {
        /*enterTiles.setValuesNumberFields();*/
        for (int i = 0; i < tilesList.size(); i++) {
            for (Double value : listOfNumberFields) {
                calculateRetail(tilesList, i, value, customerDiscount);

            }
        }
    }

    private void calculateRetail(List<EntityTiles> tilesList, int i, Double value, NumberField customerDiscount) {
        BigDecimal rabat = new BigDecimal(customerDiscount.getValue()).add(new BigDecimal(100));
        BigDecimal bigDecimal = new BigDecimal(value)
                .multiply(tilesList.get(i).getUnitRetailPrice())
                .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        tilesList.get(i).setPriceAfterDiscount(String.valueOf(bigDecimal));
    }

    private void calculateRetailPowierzchnia(List<EntityTiles> tilesList, int i, NumberField numberField, NumberField customerDiscount) {
        BigDecimal rabat = new BigDecimal(customerDiscount.getValue()).add(new BigDecimal(100));
        BigDecimal bigDecimal = new BigDecimal(numberField.getValue())
                .multiply(BigDecimal.valueOf(12))
                .multiply(tilesList.get(i).getUnitRetailPrice())
                .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        tilesList.get(i).setPurchasePrice(String.valueOf(bigDecimal));
    }
}
