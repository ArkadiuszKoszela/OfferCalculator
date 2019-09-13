package app.GUIs;

import app.controllers.ControllerVaadin;
import app.inputFields.ServiceNumberFiled;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Route(value = EnterTiles.ENTER_TILES)
public class EnterTiles extends SplitLayout {

    public static final String ENTER_TILES = "EnterTiles";

    private ControllerVaadin controllerVaadin;
    private ServiceNumberFiled serviceNumberFiled;

    @Autowired
    public EnterTiles(ControllerVaadin controllerVaadin, ServiceNumberFiled serviceNumberFiled) {
        this.controllerVaadin = Objects.requireNonNull(controllerVaadin);
        this.serviceNumberFiled = Objects.requireNonNull(serviceNumberFiled);

        setOrientation(Orientation.VERTICAL);
        addToPrimary(controllerVaadin.routerLink());

        addToSecondary(createInputFields());

        ustawieniaStrony();
    }


    private FormLayout createInputFields() {
        serviceNumberFiled.createNumberFields();
        FormLayout board = new FormLayout();
        board.add(serviceNumberFiled.createPoleRabat());
        board.add(serviceNumberFiled.getNumberField1(), serviceNumberFiled.getNumberField2(), serviceNumberFiled.getNumberField3(), serviceNumberFiled.getNumberField4());
        board.add(serviceNumberFiled.getNumberField5(), serviceNumberFiled.getNumberField6(), serviceNumberFiled.getNumberField7(), serviceNumberFiled.getNumberField8());
        board.add(serviceNumberFiled.getNumberField9(), serviceNumberFiled.getNumberField10(), serviceNumberFiled.getNumberField11(), serviceNumberFiled.getNumberField12());
        board.add(serviceNumberFiled.getNumberField13(), serviceNumberFiled.getNumberField14(), serviceNumberFiled.getNumberField15(), serviceNumberFiled.getNumberField16());
        board.add(serviceNumberFiled.getNumberField17(), serviceNumberFiled.getNumberField18(), serviceNumberFiled.getNumberField19());
        return board;
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
