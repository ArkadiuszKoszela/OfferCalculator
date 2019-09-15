package app.views;

import app.controllers.ControllerVaadin;
import app.inputFields.ServiceNumberFiled;
import app.service.Layout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

import static app.inputFields.ServiceSplitLayout.getSideMenuSettings;
import static app.inputFields.ServiceSplitLayout.ustawieniaStrony;

@Route(value = EnterTiles.ENTER_TILES)
public class EnterTiles extends SplitLayout implements Layout {

    public static final String ENTER_TILES = "tiles/enterTiles";

    private ControllerVaadin controllerVaadin;
    private ServiceNumberFiled serviceNumberFiled;

    @Autowired
    public EnterTiles(ControllerVaadin controllerVaadin, ServiceNumberFiled serviceNumberFiled) {
        this.controllerVaadin = Objects.requireNonNull(controllerVaadin);
        this.serviceNumberFiled = Objects.requireNonNull(serviceNumberFiled);
        setOrientation(SplitLayout.Orientation.VERTICAL);
        addToPrimary(ustawieniaStrony(controllerVaadin));
        addToSecondary(getSideMenu(controllerVaadin));
    }


    private FormLayout createInputFields() {
        serviceNumberFiled.setValuesNumberFields();
        FormLayout board = new FormLayout();
        Label label = new Label(" ");
        board.add(serviceNumberFiled.setValuesCustomerDiscount(), label);
        board.add(serviceNumberFiled.getNumberField1(), serviceNumberFiled.getNumberField2(), serviceNumberFiled.getNumberField3(), serviceNumberFiled.getNumberField4());
        board.add(serviceNumberFiled.getNumberField5(), serviceNumberFiled.getNumberField6(), serviceNumberFiled.getNumberField7(), serviceNumberFiled.getNumberField8());
        board.add(serviceNumberFiled.getNumberField9(), serviceNumberFiled.getNumberField10(), serviceNumberFiled.getNumberField11(), serviceNumberFiled.getNumberField12());
        board.add(serviceNumberFiled.getNumberField13(), serviceNumberFiled.getNumberField14(), serviceNumberFiled.getNumberField15(), serviceNumberFiled.getNumberField16());
        board.add(serviceNumberFiled.getNumberField17(), serviceNumberFiled.getNumberField18(), serviceNumberFiled.getNumberField19());
        return board;
    }

    @Override
    public SplitLayout getSideMenu(ControllerVaadin controllerVaadin) {
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.addToPrimary(controllerVaadin.sideMenuTiles());
        splitLayout.addToSecondary(createInputFields());
        getSideMenuSettings(splitLayout);
        return splitLayout;
    }
}
