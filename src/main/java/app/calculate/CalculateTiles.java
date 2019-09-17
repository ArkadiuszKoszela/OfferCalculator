package app.calculate;

import app.entities.EntityResultTiles;
import app.entities.EntityTiles;
import app.repositories.AccesoriesRepository;
import app.repositories.ResultTilesRepository;
import app.repositories.TilesRepository;
import com.vaadin.flow.component.textfield.NumberField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class CalculateTiles {

    private TilesRepository tilesRepository;
    private ResultTilesRepository resultTilesRepository;
    private AccesoriesRepository accesoriesRepository;

    @Autowired
    public CalculateTiles(TilesRepository tilesRepository, ResultTilesRepository resultTilesRepository,
                          AccesoriesRepository accesoriesRepository) {
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
        this.resultTilesRepository = Objects.requireNonNull(resultTilesRepository);
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);
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


    public void getProfit(List<EntityResultTiles> resultTiles) {
        resultTiles.forEach(e -> {
            BigDecimal retailPrice = new BigDecimal(e.getPriceAfterDiscount());
            BigDecimal purchasePrice = new BigDecimal(e.getPurchasePrice());
            e.setProfit(String.valueOf(retailPrice.subtract(purchasePrice)));
        });
    }

    public void getPurchase(List<EntityResultTiles> resultTiles, List<EntityTiles> tilesList, List<NumberField> listOfNumberFields) {
        /*enterTiles.setValuesNumberFields();*/
        for (int i = 0; i < tilesList.size(); i++) {
            for (NumberField numberField : listOfNumberFields) {
                if (numberField.getLabel().contains("Powierzchnia")) {
                    calculatePurchasePowierzchnia(resultTiles, tilesList, i, numberField);
                } else {
                    calculatePurchase(resultTiles, tilesList, i, numberField);
                }
            }
        }
    }

    private void calculatePurchase(List<EntityResultTiles> resultTiles, List<EntityTiles> tilesList, int i, NumberField numberField) {
        BigDecimal profit = new BigDecimal(tilesList.get(i).getProfit()).add(new BigDecimal(100));
        BigDecimal bigDecimal = new BigDecimal(numberField.getValue())
                .multiply(tilesList.get(i).getUnitRetailPrice())
                .multiply(BigDecimal.valueOf(100)).divide(profit, 2, RoundingMode.HALF_UP);
        resultTiles.get(i).setPurchasePrice(String.valueOf(bigDecimal));
    }

    private void calculatePurchasePowierzchnia(List<EntityResultTiles> resultTiles, List<EntityTiles> tilesList, int i, NumberField numberField) {
        BigDecimal profit = new BigDecimal(tilesList.get(i).getProfit()).add(new BigDecimal(100));
        BigDecimal bigDecimal = new BigDecimal(numberField.getValue())
                .multiply(BigDecimal.valueOf(12))
                .multiply(tilesList.get(i).getUnitRetailPrice())
                .multiply(BigDecimal.valueOf(100)).divide(profit, 2, RoundingMode.HALF_UP);
        resultTiles.get(i).setPurchasePrice(String.valueOf(bigDecimal));
    }

    public void getRetail(List<EntityResultTiles> resultTiles, List<EntityTiles> tilesList, NumberField customerDiscount, List<NumberField> listOfNumberFields) {
        /*enterTiles.setValuesNumberFields();*/
        for (int i = 0; i < tilesList.size(); i++) {
            for (NumberField numberField : listOfNumberFields) {
                if (numberField.getLabel().contains("Powierzchnia")) {
                    calculateRetailPowierzchnia(resultTiles, tilesList, i, numberField, customerDiscount);
                } else {
                    calculateRetail(resultTiles, tilesList, i, numberField, customerDiscount);
                }
            }
        }
    }

    private void calculateRetail(List<EntityResultTiles> resultTiles, List<EntityTiles> tilesList, int i, NumberField numberField, NumberField customerDiscount) {
        BigDecimal rabat = new BigDecimal(customerDiscount.getValue()).add(new BigDecimal(100));
        BigDecimal bigDecimal = new BigDecimal(numberField.getValue())
                .multiply(tilesList.get(i).getUnitRetailPrice())
                .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        resultTiles.get(i).setPriceAfterDiscount(String.valueOf(bigDecimal));
    }

    private void calculateRetailPowierzchnia(List<EntityResultTiles> resultTiles, List<EntityTiles> tilesList, int i, NumberField numberField, NumberField customerDiscount) {
        BigDecimal rabat = new BigDecimal(customerDiscount.getValue()).add(new BigDecimal(100));
        BigDecimal bigDecimal = new BigDecimal(numberField.getValue())
                .multiply(BigDecimal.valueOf(12))
                .multiply(tilesList.get(i).getUnitRetailPrice())
                .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        resultTiles.get(i).setPurchasePrice(String.valueOf(bigDecimal));
    }
}
