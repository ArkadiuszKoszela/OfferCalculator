package app.GUIs;

import app.calculate.CalculateTiles;
import app.controllers.ControllerVaadin;
import app.entities.EntityResultTiles;
import app.entities.EntityTiles;
import app.repositories.ResultTiles;
import app.repositories.Tiles;
import app.service.NazwaCennika;
import app.service.SaveUsers;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Route(value = ShowTableTiles.SHOW_TABLE_TILES)
public class ShowTableTiles extends SplitLayout {

    public static final String SHOW_TABLE_TILES = "ShowTableTiles";

    private ControllerVaadin controllerVaadin;
    private Tiles tiles;
    private CalculateTiles calculateTiles;
    private ResultTiles resultTiles;
    private SaveUsers saveUser;

    private NazwaCennika nazwaCennika;
    public ComboBox<String> priceListCB = new ComboBox<>();

    public ShowTableTiles() {
    }

    @Autowired
    public ShowTableTiles(ControllerVaadin controllerVaadin, Tiles tiles, CalculateTiles calculateTiles,
                          ResultTiles resultTiles, NazwaCennika nazwaCennika, SaveUsers saveUser) {
        this.controllerVaadin = Objects.requireNonNull(controllerVaadin);
        this.tiles = Objects.requireNonNull(tiles);
        this.calculateTiles = Objects.requireNonNull(calculateTiles);
        this.resultTiles = Objects.requireNonNull(resultTiles);
        this.nazwaCennika = Objects.requireNonNull(nazwaCennika);
        this.saveUser = Objects.requireNonNull(saveUser);

        Button saveUserWithCalculations = new Button("Zapisz użytkownika");
        ConfirmDialog confirmDialog = dialogSaveNewUser();
        saveUserWithCalculations.addClickListener(buttonClickEvent -> confirmDialog.open());
        setOrientation(Orientation.VERTICAL);
        addToPrimary(controllerVaadin.routerLink());
        nazwaCennika.createTable();
        Board board = new Board();
        Label label = new Label("Podaj nazwę cennika:");
        Label label1 = new Label(" ");
        board.addRow(label, label1);
        board.addRow(priceListCB, saveUserWithCalculations);
        InputUser inputUser = new InputUser();

        Button calculateProfit = new Button("Oblicz zysk");
        calculateProfit.addClickListener(buttonClickEvent -> loadResultTableTiles(tiles));
        board.addRow(calculateProfit);
        board.addRow(nazwaCennika.wynikiGrid);


        if (!calculateTiles.getAvailablePriceList().isEmpty()) {
            priceListCB.setItems(calculateTiles.getAvailablePriceList());
        }

        addToSecondary(board);

        ustawieniaStrony();
    }

    private ConfirmDialog dialogSaveNewUser() {
        return new ConfirmDialog("Zapisywanie użytkownika"
                , "Czy chcesz zapisać użtkownika ?"
                , "Zapisz", event -> saveUser.saveUser()
                , "Nie zapisuj", event -> System.out.println("nic sie nie stalo"));
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

    void loadResultTableTiles(Tiles tiles) {
        if (allTilesFromRespository(tiles).size() > 0) {
            nazwaCennika.wynikiGrid.setItems(listResultTiles());
            Notification notification = new Notification("Obliczono kalkulację", 4000, Notification.Position.BOTTOM_CENTER);
            notification.open();
        } else if (allTilesFromRespository(tiles).size() == 0) {
            Notification notification = new Notification("Zaimportuj cenniki", 4000, Notification.Position.BOTTOM_CENTER);
            notification.getElement().getStyle().set("color", "red");
            notification.open();
        } else {
            Notification notification = new Notification("Wybierz cennik", 4000, Notification.Position.BOTTOM_CENTER);
            notification.getElement().getStyle().set("color", "red");
            notification.open();
        }
    }
    private List<EntityTiles> allTilesFromRespository(Tiles tiles) {
        Iterable<EntityTiles> allTilesFromRepository = tiles.findAll();
        List<EntityTiles> allTiles = new ArrayList<>();
        allTilesFromRepository.forEach(allTiles::add);
        return allTiles;
    }

    private void ustawieniaStrony() {
        Board board = new Board();
        board.addRow(controllerVaadin.routerLink());
        board.getStyle().set("background", "#DCDCDC");
        addToPrimary(board);
        setPrimaryStyle("minWidth", "1280px");
        setPrimaryStyle("maxWidth", "1280px");
        setPrimaryStyle("minHeight", "70px");
        setPrimaryStyle("maxHeight", "700px");
        setSecondaryStyle("minWidth", "1280px");
        setSecondaryStyle("maxWidth", "1280px");
        setSecondaryStyle("minHeight", "500px");
        setSecondaryStyle("maxHeight", "500px");
    }
}
