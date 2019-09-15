package app.views;

import app.calculate.CalculateTiles;
import app.controllers.ControllerVaadin;
import app.entities.EntityResultTiles;
import app.entities.EntityTiles;
import app.repositories.ResultTiles;
import app.repositories.Tiles;
import app.service.Layout;
import app.service.SaveUsers;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static app.inputFields.ServiceNotification.getNotificationError;
import static app.inputFields.ServiceNotification.getNotificationSucces;
import static app.inputFields.ServiceSplitLayout.getSideMenuSettings;
import static app.inputFields.ServiceSplitLayout.ustawieniaStrony;

@Route(value = TableResultTiles.RESULT_TILES)
public class TableResultTiles extends SplitLayout implements Layout {

    public static final String RESULT_TILES = "tiles/allResultTiles";

    private ControllerVaadin controllerVaadin;
    private Tiles tiles;
    private CalculateTiles calculateTiles;
    private ResultTiles resultTiles;
    private SaveUsers saveUser;

    private FormLayout board;
    private Grid<EntityResultTiles> resultTilesGrid;
    private ComboBox<String> comboBox = new ComboBox<>();
    private VerticalLayout verticalLayout = new VerticalLayout();

    public TableResultTiles() {
    }

    @Autowired
    public TableResultTiles(ControllerVaadin controllerVaadin, Tiles tiles, CalculateTiles calculateTiles,
                            ResultTiles resultTiles, SaveUsers saveUser) {
        this.controllerVaadin = Objects.requireNonNull(controllerVaadin);
        this.tiles = Objects.requireNonNull(tiles);
        this.calculateTiles = Objects.requireNonNull(calculateTiles);
        this.resultTiles = Objects.requireNonNull(resultTiles);
        this.saveUser = Objects.requireNonNull(saveUser);
        setOrientation(SplitLayout.Orientation.VERTICAL);
        addToPrimary(ustawieniaStrony(controllerVaadin));
        createTable();

        board = new FormLayout();
        Label label = new Label("Podaj nazwę cennika:");
        Label label1 = new Label(" ");
        Label label2 = new Label(" ");
        Button calculateProfit = new Button("Oblicz zysk");
        Label label3 = new Label(" ");
        calculateProfit.addClickListener(buttonClickEvent -> loadResultTableTiles(tiles));
        board.add(label, label1);
        board.add(comboBox, calculateProfit);
        verticalLayout.add(board);
        verticalLayout.add(resultTilesGrid);
        /*board.add(resultTilesGrid);*/


        if (!calculateTiles.getAvailablePriceList().isEmpty()) {
            comboBox.setItems(calculateTiles.getAvailablePriceList());
        }

        addToSecondary(getSideMenu(controllerVaadin));
    }

    private List<EntityResultTiles> listResultTiles() {
        String[] spliString = comboBox.getValue().split(" ");

        List<EntityTiles> priceListFromRepository = tiles.findByPriceListNameAndType(spliString[0] + " " + spliString[1] + " " + spliString[2], spliString[3] + " " + spliString[4]);
        List<EntityResultTiles> listResultTiles = getEntityResultTiles();
        listResultTiles.forEach(e -> e.setPriceListName(comboBox.getValue()));

        calculateTiles.getRetail(listResultTiles, priceListFromRepository);
        calculateTiles.getPurchase(listResultTiles, priceListFromRepository);
        calculateTiles.getProfit(listResultTiles);

        resultTiles.saveAll(listResultTiles);
        return listResultTiles;
    }

    private List<EntityResultTiles> getEntityResultTiles() {
        Iterable<EntityResultTiles> resultTilesFromRepository = resultTiles.findAll();
        List<EntityResultTiles> listResultTiles = new ArrayList<>();
        resultTilesFromRepository.forEach(listResultTiles::add);
        return listResultTiles;
    }

    private void createTable() {
        resultTilesGrid = new Grid<>(EntityResultTiles.class);
        resultTilesGrid.getColumnByKey("name").setHeader("Kategoria");
        resultTilesGrid.getColumnByKey("priceListName").setHeader("Nazwa Cennika");
        resultTilesGrid.getColumnByKey("priceAfterDiscount").setHeader("Cena sprzedaży");
        resultTilesGrid.getColumnByKey("purchasePrice").setHeader("Cena Zakupu");
        resultTilesGrid.getColumnByKey("profit").setHeader("Zysk");
        resultTilesGrid.removeColumnByKey("id");
    }

    private void loadResultTableTiles(Tiles tiles) {
        if (allTilesFromRespository(tiles).size() > 0) {
            resultTilesGrid.setItems(listResultTiles());
            getNotificationSucces("Obliczono kalkulację");
        } else if (allTilesFromRespository(tiles).size() == 0) {
            getNotificationError("Zaimportuj cenniki");
        } else {
            getNotificationError("Wybierz cennik");
        }
    }


    private List<EntityTiles> allTilesFromRespository(Tiles tiles) {
        Iterable<EntityTiles> allTilesFromRepository = tiles.findAll();
        List<EntityTiles> allTiles = new ArrayList<>();
        allTilesFromRepository.forEach(allTiles::add);
        return allTiles;
    }

    @Override
    public SplitLayout getSideMenu(ControllerVaadin controllerVaadin) {
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.addToPrimary(controllerVaadin.sideMenuTiles());
        splitLayout.addToSecondary(verticalLayout);
        getSideMenuSettings(splitLayout);
        return splitLayout;
    }
}
