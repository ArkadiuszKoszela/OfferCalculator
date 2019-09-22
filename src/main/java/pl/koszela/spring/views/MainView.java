package pl.koszela.spring.views;

import com.vaadin.flow.component.applayout.AppLayout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.importFiles.ImportFiles;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static pl.koszela.spring.views.EnterTiles.ENTER_TILES;
import static pl.koszela.spring.views.InputUser.INPUT_USER;
import static pl.koszela.spring.views.SelectAccesories.SELECT_ACCESORIES;

public class MainView extends AppLayout {

    private Map<Tab, Component> tab2Workspace = new HashMap<>();
    private ImportFiles importFiles;

    @Autowired
    public MainView(ImportFiles importFiles) {
        this.importFiles = Objects.requireNonNull(importFiles);

        Image img = new Image("http://www.nowoczesnebudowanie.pl/wp-content/uploads/2016/10/logo-nowoczesne-budowanie-1200x857.png", "Vaadin Logo");
        img.setHeight("44px");
        addToNavbar(new DrawerToggle(), img);
        MenuBar menuBar = new MenuBar();
        Tabs tabs = new Tabs(false, new Tab("Klienci"), new Tab("Dachówki"), new Tab("Akcesoria"));
        tabs.setFlexGrowForEnclosedTabs(1);
        tabs.addSelectedChangeListener(e -> {
            if (e.getSelectedTab().getLabel().equalsIgnoreCase("Klienci")) {
                getUI().ifPresent(ui -> ui.navigate(INPUT_USER));
                tabs.setSelectedTab(e.getSelectedTab());
            } else if (e.getSelectedTab().getLabel().equalsIgnoreCase("Dachówki")) {
                getUI().ifPresent(ui -> ui.navigate(ENTER_TILES));
                tabs.setSelectedTab(e.getSelectedTab());
            } else if (e.getSelectedTab().getLabel().equalsIgnoreCase("Akcesoria")) {
                getUI().ifPresent(ui -> ui.navigate(SELECT_ACCESORIES));
                tabs.setSelectedTab(e.getSelectedTab());
            }
        });

        menuBar.addItem(tabs);
        Button button = new Button("Zaimportuj pliki");
        button.addClickListener(e -> importFiles.csv());
        if (isDrawerOpened()) {
            setDrawerOpened(false);
        }
        addToNavbar(menuBar);
        addToDrawer(button);
    }
}
