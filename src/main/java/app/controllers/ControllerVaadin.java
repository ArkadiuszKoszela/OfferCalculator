package app.controllers;

import app.GUIs.*;
import app.importFiles.ImportFiles;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ControllerVaadin {

    private ImportFiles importFiles;

    @Autowired
    public void setImportFiles(ImportFiles importFiles) {
        this.importFiles = Objects.requireNonNull(importFiles);
    }

    public ControllerVaadin() {
    }

    public Tabs mainMenu() {
        Button button = new Button("Zaimportuj cenniki");
        button.addClickListener(e -> importFiles.csv());
        Tabs menu = new Tabs();
        menu.add(new Tab(new RouterLink("Klienci", InputUser.class)),
                new Tab(new RouterLink("Akcesoria", SelectAccesories.class)),
                new Tab(new RouterLink("Dachówki", EnterTiles.class)),
                new Tab((button)));
        menu.setOrientation(Tabs.Orientation.HORIZONTAL);
        return menu;
    }

    public Tabs sideMenuUser() {
        Tabs sideMenu = new Tabs();
        sideMenu.add(new Tab(new RouterLink("Tabela", Users.class)),
                new Tab(new RouterLink("Wprowadź dane", InputUser.class))/*,
                new Tab(new RouterLink("Wczytaj", LoadUser.class))*/);

        sideMenu.setOrientation(Tabs.Orientation.VERTICAL);
        return sideMenu;
    }

    public Tabs sideMenuTiles() {
        Tabs sideMenu = new Tabs();
        sideMenu.add(new Tab(new RouterLink("Pokaż tabelę", ShowTableTiles.class)),
                new Tab(new RouterLink("Wprowadź dane", EnterTiles.class)),
                new Tab(new RouterLink("Cennik", Cennik.class)));

        sideMenu.setOrientation(Tabs.Orientation.VERTICAL);
        return sideMenu;
    }

    public Tabs sideMenuAccesories() {
        Tabs sideMenu = new Tabs();
        sideMenu.add(new Tab(new RouterLink("Wybierz akcesoria", SelectAccesories.class)),
                new Tab(new RouterLink("Checkboxy", Checkboxes.class)));

        sideMenu.setOrientation(Tabs.Orientation.VERTICAL);
        return sideMenu;
    }

}
