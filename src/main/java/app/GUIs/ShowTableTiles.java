package app.GUIs;

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

@Route(value = ShowTableTiles.SHOW_TABLE_TILES)
public class ShowTableTiles extends SplitLayout implements Layout {

    public static final String SHOW_TABLE_TILES = "ShowTableTiles";

    private ControllerVaadin controllerVaadin;
    private Tiles tiles;
    private CalculateTiles calculateTiles;
    private ResultTiles resultTiles;
    private SaveUsers saveUser;

    public ComboBox<String> priceListCB = new ComboBox<>();
    private FormLayout board;
    private Grid<EntityResultTiles> wynikiGrid;

    public ShowTableTiles() {
    }

    @Autowired
    public ShowTableTiles(ControllerVaadin controllerVaadin, Tiles tiles, CalculateTiles calculateTiles,
                          ResultTiles resultTiles, SaveUsers saveUser) {
        this.controllerVaadin = Objects.requireNonNull(controllerVaadin);
        this.tiles = Objects.requireNonNull(tiles);
        this.calculateTiles = Objects.requireNonNull(calculateTiles);
        this.resultTiles = Objects.requireNonNull(resultTiles);
        this.saveUser = Objects.requireNonNull(saveUser);

        Button saveUserWithCalculations = new Button("Zapisz użytkownika");
        saveUserWithCalculations.addClickListener(buttonClickEvent -> saveUser.saveUser());
        setOrientation(Orientation.VERTICAL);
        addToPrimary(ustawieniaStrony(controllerVaadin));
        createTable();

        board = new FormLayout();
        Label label = new Label("Podaj nazwę cennika:");
        Label label1 = new Label(" ");
        board.add(label, label1);
        board.add(priceListCB, saveUserWithCalculations);

        Button calculateProfit = new Button("Oblicz zysk");
        calculateProfit.addClickListener(buttonClickEvent -> loadResultTableTiles(tiles));
        board.add(calculateProfit);
        board.add(wynikiGrid);


        if (!calculateTiles.getAvailablePriceList().isEmpty()) {
            priceListCB.setItems(calculateTiles.getAvailablePriceList());
        }

        addToSecondary(getSideMenu(controllerVaadin));
    }

    private List<EntityResultTiles> listResultTiles() {

        String[] spliString = priceListCB.getValue().split(" ");

        List<EntityTiles> priceListFromRepository = tiles.findByPriceListNameAndType(spliString[0] + " " + spliString[1] + " " + spliString[2], spliString[3] + " " + spliString[4]);

        Iterable<EntityResultTiles> resultTilesFromRepository = resultTiles.findAll();
        List<EntityResultTiles> listResultTiles = new ArrayList<>();
        resultTilesFromRepository.forEach(listResultTiles::add);

        for (EntityResultTiles entityResultTiles : listResultTiles) {
            entityResultTiles.setPriceListName(priceListCB.getValue());
        }

        calculateTiles.getRetail(listResultTiles, priceListFromRepository);
        /*getPurchase(listResultTiles, priceListFromRepository);*/
        calculateTiles.cos(listResultTiles, priceListFromRepository);
        calculateTiles.getProfit(listResultTiles);

        resultTiles.saveAll(listResultTiles);
        return listResultTiles;
    }

    private void createTable() {
        wynikiGrid = new Grid<>(EntityResultTiles.class);
        wynikiGrid.getColumnByKey("name").setHeader("Kategoria");
        wynikiGrid.getColumnByKey("priceListName").setHeader("Nazwa Cennika");
        wynikiGrid.getColumnByKey("priceAfterDiscount").setHeader("Cena Netto Po rabacie");
        wynikiGrid.getColumnByKey("purchasePrice").setHeader("Cena Zakupu");
        wynikiGrid.getColumnByKey("profit").setHeader("Zysk");
        wynikiGrid.setColumns("name", "priceListName", "priceAfterDiscount", "purchasePrice", "profit");
    }

    private void loadResultTableTiles(Tiles tiles) {
        if (allTilesFromRespository(tiles).size() > 0) {
            wynikiGrid.setItems(listResultTiles());
            /*Notification notification = new Notification("Obliczono kalkulację", 4000, Notification.Position.BOTTOM_CENTER);
            notification.open();*/
            getNotificationSucces("Obliczono kalkulację");
        } else if (allTilesFromRespository(tiles).size() == 0) {
            /*Notification notification = new Notification("Zaimportuj cenniki", 4000, Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();*/
            getNotificationError("Zaimportuj cenniki");
        } else {
            /*Notification notification = new Notification("Wybierz cennik", 4000, Notification.Position.BOTTOM_CENTER);
            notification.getElement().getStyle().set("color", "red");
            notification.open();*/
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
        splitLayout.addToSecondary(board);
        getSideMenuSettings(splitLayout);
        return splitLayout;
    }
}
