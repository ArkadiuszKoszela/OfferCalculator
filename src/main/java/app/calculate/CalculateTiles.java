package app.calculate;

import app.entities.EntityResultTiles;
import app.entities.EntityTiles;
import app.inputFields.ServiceNumberFiled;
import app.repositories.ResultTiles;
import app.repositories.Tiles;
import app.service.AllFields;
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
    private AllFields allFields;

    @Autowired
    public CalculateTiles(Tiles tiles, ResultTiles resultTiles, ServiceNumberFiled serviceNumberFiled, AllFields allFields) {
        this.tiles = Objects.requireNonNull(tiles);
        this.resultTiles = Objects.requireNonNull(resultTiles);
        this.serviceNumberFiled = Objects.requireNonNull(serviceNumberFiled);
        this.allFields = Objects.requireNonNull(allFields);
        /*priceListCB = new ComboBox<>();*/
    }

    public List<String> getAvailablePriceList() {
        Iterable<EntityTiles> allTilesFromRepository = tiles.findAll();
        Set<String> allTiles = new TreeSet<>();
        allTilesFromRepository.forEach(e -> allTiles.add(e.getPriceListName() + " " + e.getType()));

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

        for (EntityResultTiles entityResultTiles : resultTiles) {

            BigDecimal retailPrice = new BigDecimal(entityResultTiles.getPriceAfterDiscount());
            BigDecimal purchasePrice = new BigDecimal(entityResultTiles.getPurchasePrice());

            entityResultTiles.setProfit(String.valueOf(retailPrice.subtract(purchasePrice)));
        }

    }

    private void getPurchase(List<EntityResultTiles> resultTiles, List<EntityTiles> tilesList) {


        BigDecimal profit = new BigDecimal(tilesList.get(0).getProfit()).add(new BigDecimal(100));
        BigDecimal dachowkaPodstawowa =
                new BigDecimal(serviceNumberFiled.getNumberField1().getValue())
                        .multiply(BigDecimal.valueOf(12))
                        .multiply(tilesList.get(0).getUnitRetailPrice())
                        .multiply(BigDecimal.valueOf(100)).divide(profit, 2, RoundingMode.HALF_UP);
        resultTiles.get(0).setPurchasePrice(String.valueOf(dachowkaPodstawowa));

        BigDecimal marza1 = new BigDecimal(tilesList.get(1).getProfit()).add(new BigDecimal(100));
        BigDecimal dachowkaSkrajnaLewa =
                new BigDecimal(serviceNumberFiled.getNumberField2().getValue())
                        .multiply(tilesList.get(1).getUnitRetailPrice())
                        .multiply(BigDecimal.valueOf(100)).divide(marza1, 2, RoundingMode.HALF_UP);
        resultTiles.get(1).setPurchasePrice(String.valueOf(dachowkaSkrajnaLewa));

        BigDecimal marza2 = new BigDecimal(tilesList.get(2).getProfit()).add(new BigDecimal(100));
        BigDecimal dachowkaSkrajnaPrawa =
                new BigDecimal(serviceNumberFiled.getNumberField3().getValue())
                        .multiply(tilesList.get(2).getUnitRetailPrice())
                        .multiply(BigDecimal.valueOf(100)).divide(marza2, 2, RoundingMode.HALF_UP);
        resultTiles.get(2).setPurchasePrice(String.valueOf(dachowkaSkrajnaPrawa));

        BigDecimal marza3 = new BigDecimal(tilesList.get(3).getProfit()).add(new BigDecimal(100));
        BigDecimal dachowkaDwufalowa =
                new BigDecimal(serviceNumberFiled.getNumberField4().getValue())
                        .multiply(tilesList.get(3).getUnitRetailPrice())
                        .multiply(BigDecimal.valueOf(100)).divide(marza3, 2, RoundingMode.HALF_UP);
        resultTiles.get(3).setPurchasePrice(String.valueOf(dachowkaDwufalowa));

        BigDecimal marza4 = new BigDecimal(tilesList.get(4).getProfit()).add(new BigDecimal(100));
        BigDecimal dachowkaWentylacyjna =
                new BigDecimal(serviceNumberFiled.getNumberField5().getValue())
                        .multiply(tilesList.get(4).getUnitRetailPrice())
                        .multiply(BigDecimal.valueOf(100)).divide(marza4, 2, RoundingMode.HALF_UP);
        resultTiles.get(4).setPurchasePrice(String.valueOf(dachowkaWentylacyjna));

        BigDecimal marza5 = new BigDecimal(tilesList.get(5).getProfit()).add(new BigDecimal(100));
        BigDecimal kompletKominkaWentylacyjnego =
                new BigDecimal(serviceNumberFiled.getNumberField6().getValue())
                        .multiply(tilesList.get(5).getUnitRetailPrice())
                        .multiply(BigDecimal.valueOf(100)).divide(marza5, 2, RoundingMode.HALF_UP);
        resultTiles.get(5).setPurchasePrice(String.valueOf(kompletKominkaWentylacyjnego));

        BigDecimal marza6 = new BigDecimal(tilesList.get(6).getProfit()).add(new BigDecimal(100));
        BigDecimal gasiarPodstawowy =
                new BigDecimal(serviceNumberFiled.getNumberField7().getValue())
                        .multiply(tilesList.get(6).getUnitRetailPrice())
                        .multiply(BigDecimal.valueOf(100)).divide(marza6, 2, RoundingMode.HALF_UP);
        resultTiles.get(6).setPurchasePrice(String.valueOf(gasiarPodstawowy));

        BigDecimal marza7 = new BigDecimal(tilesList.get(7).getProfit()).add(new BigDecimal(100));
        BigDecimal gasiorPoczatkowyKalenicaProsta =
                new BigDecimal(serviceNumberFiled.getNumberField8().getValue())
                        .multiply(tilesList.get(7).getUnitRetailPrice())
                        .multiply(BigDecimal.valueOf(100)).divide(marza7, 2, RoundingMode.HALF_UP);
        resultTiles.get(7).setPurchasePrice(String.valueOf(gasiorPoczatkowyKalenicaProsta));

        BigDecimal marza8 = new BigDecimal(tilesList.get(8).getProfit()).add(new BigDecimal(100));
        BigDecimal gasiorKoncowyKalenicaProsta =
                new BigDecimal(serviceNumberFiled.getNumberField9().getValue())
                        .multiply(tilesList.get(8).getUnitRetailPrice())
                        .multiply(BigDecimal.valueOf(100)).divide(marza8, 2, RoundingMode.HALF_UP);
        resultTiles.get(8).setPurchasePrice(String.valueOf(gasiorKoncowyKalenicaProsta));

        BigDecimal marza9 = new BigDecimal(tilesList.get(9).getProfit()).add(new BigDecimal(100));
        BigDecimal plytkaPoczatkowa =
                new BigDecimal(serviceNumberFiled.getNumberField10().getValue())
                        .multiply(tilesList.get(9).getUnitRetailPrice())
                        .multiply(BigDecimal.valueOf(100)).divide(marza9, 2, RoundingMode.HALF_UP);
        resultTiles.get(9).setPurchasePrice(String.valueOf(plytkaPoczatkowa));

        BigDecimal marza10 = new BigDecimal(tilesList.get(10).getProfit()).add(new BigDecimal(100));
        BigDecimal plytkaKoncowa =
                new BigDecimal(serviceNumberFiled.getNumberField11().getValue())
                        .multiply(tilesList.get(10).getUnitRetailPrice())
                        .multiply(BigDecimal.valueOf(100)).divide(marza10, 2, RoundingMode.HALF_UP);
        resultTiles.get(10).setPurchasePrice(String.valueOf(plytkaKoncowa));

        BigDecimal marza11 = new BigDecimal(tilesList.get(11).getProfit()).add(new BigDecimal(100));
        BigDecimal trojnik =
                new BigDecimal(serviceNumberFiled.getNumberField12().getValue())
                        .multiply(tilesList.get(11).getUnitRetailPrice())
                        .multiply(BigDecimal.valueOf(100)).divide(marza11, 2, RoundingMode.HALF_UP);
        resultTiles.get(11).setPurchasePrice(String.valueOf(trojnik));

        BigDecimal marza12 = new BigDecimal(tilesList.get(12).getProfit()).add(new BigDecimal(100));
        BigDecimal gasiarZaokraglony =
                new BigDecimal(serviceNumberFiled.getNumberField13().getValue())
                        .multiply(tilesList.get(12).getUnitRetailPrice())
                        .multiply(BigDecimal.valueOf(100)).divide(marza12, 2, RoundingMode.HALF_UP);
        resultTiles.get(12).setPurchasePrice(String.valueOf(gasiarZaokraglony));
    }

    public void cos(List<EntityResultTiles> resultTiles, List<EntityTiles> tilesList) {
        serviceNumberFiled.createNumberFields();
        for (int i = 0; i < tilesList.size(); i++) {
            for (NumberField numberField : serviceNumberFiled.getListOfNumberFields()) {
                /*if(numberField.getTitle().contains("Taśma kalenicowa")) {
                    BigDecimal profit = new BigDecimal(tilesList.get(i).getProfit()).add(new BigDecimal(100));
                    BigDecimal bigDecimal = new BigDecimal(numberField.getValue())
                            .multiply(BigDecimal.valueOf(12))
                            .multiply(tilesList.get(i).getUnitRetailPrice())
                            .multiply(BigDecimal.valueOf(100)).divide(profit, 2, RoundingMode.HALF_UP);
                    resultTiles.get(i).setPurchasePrice(String.valueOf(bigDecimal));
                }else{*/
                BigDecimal profit = new BigDecimal(tilesList.get(i).getProfit()).add(new BigDecimal(100));
                BigDecimal bigDecimal = new BigDecimal(numberField.getValue())
                        .multiply(tilesList.get(i).getUnitRetailPrice())
                        .multiply(BigDecimal.valueOf(100)).divide(profit, 2, RoundingMode.HALF_UP);
                resultTiles.get(i).setPurchasePrice(String.valueOf(bigDecimal));
                /*}*/
            }
        }
    }

    public void getRetail(List<EntityResultTiles> resultTiles, List<EntityTiles> tilesList) {

        BigDecimal rabat = new BigDecimal(serviceNumberFiled.getCustomerDiscount().getValue()).add(new BigDecimal(100));

        BigDecimal dachowkaPodstawowa =
                new BigDecimal(serviceNumberFiled.getNumberField1().getValue())
                        .multiply(BigDecimal.valueOf(12))
                        .multiply(tilesList.get(0).getUnitRetailPrice())
                        .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        resultTiles.get(0).setPriceAfterDiscount(String.valueOf(dachowkaPodstawowa));

        BigDecimal dachowkaSkrajnaLewa =
                new BigDecimal(serviceNumberFiled.getNumberField2().getValue())
                        .multiply(tilesList.get(1).getUnitRetailPrice())
                        .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        resultTiles.get(1).setPriceAfterDiscount(String.valueOf(dachowkaSkrajnaLewa));

        BigDecimal dachowkaSkrajnaPrawa =
                new BigDecimal(serviceNumberFiled.getNumberField3().getValue())
                        .multiply(tilesList.get(2).getUnitRetailPrice())
                        .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        resultTiles.get(2).setPriceAfterDiscount(String.valueOf(dachowkaSkrajnaPrawa));

        BigDecimal dachowkaDwufalowa =
                new BigDecimal(serviceNumberFiled.getNumberField4().getValue())
                        .multiply(tilesList.get(3).getUnitRetailPrice())
                        .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        resultTiles.get(3).setPriceAfterDiscount(String.valueOf(dachowkaDwufalowa));

        BigDecimal dachowkaWentylacyjna =
                new BigDecimal(serviceNumberFiled.getNumberField5().getValue())
                        .multiply(tilesList.get(4).getUnitRetailPrice())
                        .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        resultTiles.get(4).setPriceAfterDiscount(String.valueOf(dachowkaWentylacyjna));

        BigDecimal kompletKominkaWentylacyjnego =
                new BigDecimal(serviceNumberFiled.getNumberField6().getValue())
                        .multiply(tilesList.get(5).getUnitRetailPrice())
                        .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        resultTiles.get(5).setPriceAfterDiscount(String.valueOf(kompletKominkaWentylacyjnego));
        BigDecimal gasiarPodstawowy =
                new BigDecimal(serviceNumberFiled.getNumberField7().getValue())
                        .multiply(tilesList.get(6).getUnitRetailPrice())
                        .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        resultTiles.get(6).setPriceAfterDiscount(String.valueOf(gasiarPodstawowy));

        BigDecimal gasiorPoczatkowyKalenicaProsta =
                new BigDecimal(serviceNumberFiled.getNumberField8().getValue())
                        .multiply(tilesList.get(7).getUnitRetailPrice())
                        .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        resultTiles.get(7).setPriceAfterDiscount(String.valueOf(gasiorPoczatkowyKalenicaProsta));

        BigDecimal gasiorKoncowyKalenicaProsta =
                new BigDecimal(serviceNumberFiled.getNumberField9().getValue())
                        .multiply(tilesList.get(8).getUnitRetailPrice())
                        .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        resultTiles.get(8).setPriceAfterDiscount(String.valueOf(gasiorKoncowyKalenicaProsta));

        BigDecimal plytkaPoczatkowa =
                new BigDecimal(serviceNumberFiled.getNumberField10().getValue())
                        .multiply(tilesList.get(9).getUnitRetailPrice())
                        .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        resultTiles.get(9).setPriceAfterDiscount(String.valueOf(plytkaPoczatkowa));

        BigDecimal plytkaKoncowa =
                new BigDecimal(serviceNumberFiled.getNumberField11().getValue())
                        .multiply(tilesList.get(10).getUnitRetailPrice())
                        .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        resultTiles.get(10).setPriceAfterDiscount(String.valueOf(plytkaKoncowa));

        BigDecimal trojnik =
                new BigDecimal(serviceNumberFiled.getNumberField12().getValue())
                        .multiply(tilesList.get(11).getUnitRetailPrice())
                        .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        resultTiles.get(11).setPriceAfterDiscount(String.valueOf(trojnik));

        BigDecimal gasiarZaokraglony =
                new BigDecimal(serviceNumberFiled.getNumberField13().getValue())
                        .multiply(tilesList.get(12).getUnitRetailPrice())
                        .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        resultTiles.get(12).setPriceAfterDiscount(String.valueOf(gasiarZaokraglony));

    }
}
