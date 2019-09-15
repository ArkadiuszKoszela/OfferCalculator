package app.calculate;

import app.entities.EntityResultTiles;
import app.entities.EntityTiles;
import app.inputFields.ServiceNumberFiled;
import app.repositories.ResultTiles;
import app.repositories.Tiles;
import com.vaadin.flow.component.textfield.NumberField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class CalculateTiles {

    private Tiles tiles;
    private ResultTiles resultTiles;
    private ServiceNumberFiled serviceNumberFiled;

    @Autowired
    public CalculateTiles(Tiles tiles, ResultTiles resultTiles, ServiceNumberFiled serviceNumberFiled) {
        this.tiles = Objects.requireNonNull(tiles);
        this.resultTiles = Objects.requireNonNull(resultTiles);
        this.serviceNumberFiled = Objects.requireNonNull(serviceNumberFiled);
    }

    public List<String> getAvailablePriceList() {
        Iterable<EntityTiles> allTilesFromRepository = tiles.findAll();
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

    public void getPurchase(List<EntityResultTiles> resultTiles, List<EntityTiles> tilesList) {
        serviceNumberFiled.setValuesNumberFields();
        for (int i = 0; i < tilesList.size(); i++) {
            for (NumberField numberField : serviceNumberFiled.getListOfNumberFields()) {
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

    public void getRetail(List<EntityResultTiles> resultTiles, List<EntityTiles> tilesList) {
        serviceNumberFiled.setValuesNumberFields();
        for (int i = 0; i < tilesList.size(); i++) {
            for (NumberField numberField : serviceNumberFiled.getListOfNumberFields()) {
                if (numberField.getLabel().contains("Powierzchnia")) {
                    calculateRetailPowierzchnia(resultTiles, tilesList, i, numberField);
                } else {
                    calculateRetail(resultTiles, tilesList, i, numberField);
                }
            }
        }
    }

    private void calculateRetail(List<EntityResultTiles> resultTiles, List<EntityTiles> tilesList, int i, NumberField numberField) {
        BigDecimal rabat = new BigDecimal(serviceNumberFiled.getCustomerDiscount().getValue()).add(new BigDecimal(100));
        BigDecimal bigDecimal = new BigDecimal(numberField.getValue())
                .multiply(tilesList.get(i).getUnitRetailPrice())
                .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        resultTiles.get(i).setPriceAfterDiscount(String.valueOf(bigDecimal));
    }

    private void calculateRetailPowierzchnia(List<EntityResultTiles> resultTiles, List<EntityTiles> tilesList, int i, NumberField numberField) {
        BigDecimal rabat = new BigDecimal(serviceNumberFiled.getCustomerDiscount().getValue()).add(new BigDecimal(100));
        BigDecimal bigDecimal = new BigDecimal(numberField.getValue())
                .multiply(BigDecimal.valueOf(12))
                .multiply(tilesList.get(i).getUnitRetailPrice())
                .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        resultTiles.get(i).setPurchasePrice(String.valueOf(bigDecimal));
    }
}
