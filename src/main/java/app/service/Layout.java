package app.service;

import app.controllers.ControllerVaadin;
import com.vaadin.flow.component.splitlayout.SplitLayout;

public interface Layout {

    SplitLayout getSideMenu(ControllerVaadin controllerVaadin);
}
