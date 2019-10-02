package pl.koszela.spring.calculate;

import com.vaadin.flow.component.textfield.NumberField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.Enums;
import pl.koszela.spring.entities.Tiles;
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
        List<Tiles> allTilesFromRepository = tilesRepository.findByNameEquals(Enums.DACHOWKA_PODSTAWOWA.toString());
        List<String> allTiles = new ArrayList<>();
        allTilesFromRepository.forEach(e -> allTiles
                .add(e.getPriceListName()));
        return allTiles;
    }


    public void getProfit(List<Tiles> resultTiles) {
        resultTiles.forEach(e -> {
            BigDecimal retailPrice = e.getPriceAfterDiscount();
            BigDecimal purchasePrice = e.getPricePurchase();
            e.setProfit(retailPrice.subtract(purchasePrice));
        });
    }

    public void getAllPriceAfterDiscount(List<Tiles> resultTiles) {
        List<BigDecimal> lista = new ArrayList<BigDecimal>();
        resultTiles.forEach(e -> {
            BigDecimal cos = e.getPriceAfterDiscount();
            lista.add(cos);
        });
        BigDecimal wynik = BigDecimal.ZERO;
        for (BigDecimal bigDecimal : lista) {
            wynik = bigDecimal.add(wynik);
        }
        BigDecimal finalWynik = wynik;
        resultTiles.forEach(e -> {
            if (e.getName().equals(Enums.DACHOWKA_PODSTAWOWA.toString())) {
                e.setTotalPrice(finalWynik);
            }
        });
    }

    public void getPurchase(List<Tiles> tilesList, List<Double> listOfNumberFields) {
        for (int i = 0; i < tilesList.size(); i++) {
            for (Double value : listOfNumberFields) {
                calculatePurchase(tilesList, i, value);
            }
        }
    }

    private void calculatePurchase(List<Tiles> tilesList, int i, Double value) {
        BigDecimal profit = new BigDecimal(30).add(new BigDecimal(100));
        BigDecimal unitPrice = tilesList.get(i).getPrice();
        BigDecimal bigDecimal = new BigDecimal(value)
                .multiply(unitPrice)
                .multiply(BigDecimal.valueOf(100)).divide(profit, 2, RoundingMode.HALF_UP);
        tilesList.get(i).setPricePurchase(bigDecimal);
    }

    public void getRetail(List<Tiles> tilesList, NumberField customerDiscount, List<Double> listOfNumberFields) {
        for (int i = 0; i < tilesList.size(); i++) {
            for (Double value : listOfNumberFields) {
                calculateRetail(tilesList, i, value, customerDiscount);
            }
        }
    }

    private void calculateRetail(List<Tiles> tilesList, int i, Double value, NumberField customerDiscount) {
        BigDecimal rabat = new BigDecimal(customerDiscount.getValue()).add(new BigDecimal(100));
        BigDecimal unitPrice = tilesList.get(i).getPrice();
        BigDecimal bigDecimal = new BigDecimal(value)
                .multiply(unitPrice)
                .multiply(new BigDecimal(100)).divide(rabat, 2, RoundingMode.HALF_UP);
        tilesList.get(i).setPriceAfterDiscount(bigDecimal);
    }
}
