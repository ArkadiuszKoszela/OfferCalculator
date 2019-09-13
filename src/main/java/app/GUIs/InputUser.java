package app.GUIs;

import app.calculate.CalculateTiles;
import app.controllers.ControllerVaadin;
import app.inputFields.ServiceDataCustomer;
import app.repositories.ResultTiles;
import app.repositories.Tiles;
import app.service.NazwaCennika;
import app.service.SaveUsers;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Route(value = InputUser.INPUT_USER)
public class InputUser extends SplitLayout {

    private ServiceDataCustomer serviceDataCustomer;
    private ControllerVaadin controllerVaadin;

    private SaveUsers saveUser;
    private NazwaCennika nazwaCennika;

    public static final String INPUT_USER = "InputUser";
    @Autowired
    private Tiles tiles;
    private ResultTiles resultTiles;
    private CalculateTiles calculateTiles;

    public InputUser() {
    }

    @Autowired
    public InputUser(ServiceDataCustomer serviceDataCustomer, ControllerVaadin controllerVaadin, SaveUsers saveUser,
                     NazwaCennika nazwaCennika, Tiles tiles, ResultTiles resultTiles, CalculateTiles calculateTiles) {
        this.serviceDataCustomer = Objects.requireNonNull(serviceDataCustomer);
        this.controllerVaadin = Objects.requireNonNull(controllerVaadin);
        this.saveUser = Objects.requireNonNull(saveUser);
        this.nazwaCennika = Objects.requireNonNull(nazwaCennika);
        this.tiles = Objects.requireNonNull(tiles);
        this.resultTiles = Objects.requireNonNull(resultTiles);
        this.calculateTiles = Objects.requireNonNull(calculateTiles);

        setOrientation(Orientation.VERTICAL);

        Board board = new Board();

        serviceDataCustomer.createTextFieldsForUser();
        board.add(serviceDataCustomer.getName(), serviceDataCustomer.getSurname(), serviceDataCustomer.getAdress(), serviceDataCustomer.getTelephoneNumber());
        board.add(serviceDataCustomer.getDateOfMeeting());

        addToSecondary(board);
        addToPrimary(controllerVaadin.routerLink());

        ustawieniaStrony();
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
