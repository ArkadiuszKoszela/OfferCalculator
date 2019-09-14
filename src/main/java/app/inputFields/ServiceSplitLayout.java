package app.inputFields;

import app.controllers.ControllerVaadin;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;

public class ServiceSplitLayout {

    public static void getSideMenuSettings(SplitLayout splitLayout){
        splitLayout.getStyle().set("background", "#DCDCDC");
        splitLayout.setOrientation(SplitLayout.Orientation.HORIZONTAL);
        splitLayout.setPrimaryStyle("minWidth", "150px");
        splitLayout.setPrimaryStyle("maxWidth", "150px");
        splitLayout.setPrimaryStyle("minHeight", "500px");
        splitLayout.setPrimaryStyle("maxHeight", "500px");
        splitLayout.setSecondaryStyle("minWidth", "1100px");
        splitLayout.setSecondaryStyle("maxWidth", "1100px");
        splitLayout.setSecondaryStyle("minHeight", "500px");
        splitLayout.setSecondaryStyle("maxHeight", "500px");
    }

    public static SplitLayout ustawieniaStrony(ControllerVaadin controllerVaadin) {
        SplitLayout splitLayout = new SplitLayout();
        FormLayout board = new FormLayout();
        board.add(controllerVaadin.mainMenu());
        board.getStyle().set("background", "#DCDCDC");
        splitLayout.addToPrimary(board);
        splitLayout.setPrimaryStyle("minWidth", "1280px");
        splitLayout.setPrimaryStyle("maxWidth", "1280px");
        splitLayout.setPrimaryStyle("minHeight", "70px");
        splitLayout.setPrimaryStyle("maxHeight", "700px");
        splitLayout.setSecondaryStyle("minWidth", "1280px");
        splitLayout.setSecondaryStyle("maxWidth", "1280px");
        splitLayout.setSecondaryStyle("minHeight", "500px");
        splitLayout.setSecondaryStyle("maxHeight", "500px");
        return splitLayout;
    }
}
