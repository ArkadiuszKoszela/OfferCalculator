package app.service;

import app.calculate.CalculateTiles;
import app.entities.EntityResultTiles;
import app.repositories.ResultTiles;
import app.repositories.Tiles;
import com.vaadin.flow.component.grid.Grid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class NazwaCennika {

    /*public ComboBox<String> priceListCB;*/
    private CalculateTiles calculateTiles;
    private Tiles tiles;
    private ResultTiles resultTiles;

    @Autowired
    public NazwaCennika(CalculateTiles calculateTiles, Tiles tiles, ResultTiles resultTiles) {
        this.calculateTiles = Objects.requireNonNull(calculateTiles);
        this.tiles = Objects.requireNonNull(tiles);
        this.resultTiles = Objects.requireNonNull(resultTiles);
        /*priceListCB = new ComboBox<>();*/
    }

    public Grid<EntityResultTiles> wynikiGrid;

    public void createTable() {
        wynikiGrid = new Grid<>(EntityResultTiles.class);
        wynikiGrid.getColumnByKey("name").setHeader("Kategoria");
        wynikiGrid.getColumnByKey("priceListName").setHeader("Nazwa Cennika");
        wynikiGrid.getColumnByKey("priceAfterDiscount").setHeader("Cena Netto Po rabacie");
        wynikiGrid.getColumnByKey("purchasePrice").setHeader("Cena Zakupu");
        wynikiGrid.getColumnByKey("profit").setHeader("Zysk");
        wynikiGrid.setColumns("name", "priceListName", "priceAfterDiscount", "purchasePrice", "profit");

    }

    /*public void loadResultTableTiles() {
        priceListCB = new ComboBox<>();
        if (allTilesFromRespository(tiles).size() > 0 && priceListCB.getValue() != null) {
            wynikiGrid.setItems(listResultTiles());
            Notification notification = new Notification("Obliczono kalkulację", 4000, Notification.Position.BOTTOM_CENTER);
            notification.open();
        } else if (allTilesFromRespository(tiles).size() == 0) {
            Notification notification = new Notification("Zaimportuj cenniki", 4000, Notification.Position.BOTTOM_CENTER);
            notification.getElement().getStyle().set("color", "red");
            notification.open();
        } else {
            Notification notification = new Notification("Wybierz allTilesFromRespository", 4000, Notification.Position.BOTTOM_CENTER);
            notification.getElement().getStyle().set("color", "red");
            notification.open();
        }
    }
*/
    /*public List<EntityResultTiles> listResultTiles() {

        String[] spliString = priceListCB.getValue().split(" ");

        List<EntityTiles> priceListFromRepository = tiles.findByPriceListNameAndType(spliString[0] + " " + spliString[1] + " " + spliString[2], spliString[3] + " " + spliString[4]);

        Iterable<EntityResultTiles> resultTilesFromRepository = resultTiles.findAll();
        List<EntityResultTiles> listResultTiles = new ArrayList<>();
        resultTilesFromRepository.forEach(listResultTiles::add);

        for (EntityResultTiles entityResultTiles : listResultTiles) {
            entityResultTiles.setPriceListName(priceListCB.getValue());
        }

        calculateTiles.getRetail(listResultTiles, priceListFromRepository);
        *//*getPurchase(listResultTiles, priceListFromRepository);*//*
        calculateTiles.cos(listResultTiles, priceListFromRepository);
        calculateTiles.getProfit(listResultTiles);

        resultTiles.saveAll(listResultTiles);
        return listResultTiles;
    }*/

    /*private List<EntityTiles> allTilesFromRespository(Tiles tiles) {
        Iterable<EntityTiles> allTilesFromRepository = tiles.findAll();
        List<EntityTiles> allTiles = new ArrayList<>();
        allTilesFromRepository.forEach(allTiles::add);
        return allTiles;
    }*/

}
