package app.controllers;

import app.GUIs.*;
import app.importFiles.ImportFiles;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static app.GUIs.Cennik.CENNIK;
import static app.GUIs.Checkboxes.CHECKBOXES;
import static app.GUIs.EnterTiles.ENTER_TILES;
import static app.GUIs.InputUser.INPUT_USER;
import static app.GUIs.LoadUser.ENTER_DATA;
import static app.GUIs.SelectAccesories.SELECT_ACCESORIES;
import static app.GUIs.ShowTableTiles.SHOW_TABLE_TILES;
import static app.GUIs.Users.USERS;

@Service
public class ControllerVaadin {

    private ImportFiles importFiles;

    @Autowired
    public void setImportFiles(ImportFiles importFiles) {
        this.importFiles = Objects.requireNonNull(importFiles);
    }

    public ControllerVaadin() {
    }

    /*@Autowired
    public ControllerVaadin(ImportFiles importFiles) {
        this.importFiles = Objects.requireNonNull(importFiles);
    }*/

    /*public Board getMenu() {

        Button wprowadzdanedachowek = new Button("Wprowadź dane", VaadinIcon.CALC_BOOK.create());
        wprowadzdanedachowek.setHeight("30px");
        wprowadzdanedachowek.setWidth("100px");
        wprowadzdanedachowek.getStyle().set("color", "black");
        wprowadzdanedachowek.addClickListener(e -> {
            wprowadzdanedachowek.getUI().ifPresent(ui -> ui.navigate(ENTER_DATA));
        });

        *//*Button obliczonaoferta = new Button("Druk", VaadinIcon.CALC_BOOK.create());
        obliczonaoferta.setHeight("30px");
        obliczonaoferta.setWidth("100px");
        obliczonaoferta.getStyle().set("color", "black");
        obliczonaoferta.addClickListener(e -> {
            obliczonaoferta.getUI().ifPresent(ui -> ui.navigate(DRUK));
        });*//*
        Button daneklienta = new Button("Cennik", VaadinIcon.DOLLAR.create());
        daneklienta.setHeight("30px");
        daneklienta.setWidth("100px");
        daneklienta.getStyle().set("color", "black");
        daneklienta.addClickListener(e -> {
            daneklienta.getUI().ifPresent(ui -> ui.navigate(CENNIK));
        });

        Button bazyKlientow = new Button("Baza klientów", VaadinIcon.DATABASE.create());
        bazyKlientow.setHeight("30px");
        bazyKlientow.setWidth("100px");
        bazyKlientow.getStyle().set("color", "black");
        bazyKlientow.addClickListener(e -> {
            bazyKlientow.getUI().ifPresent(ui -> ui.navigate(USERS));
        });

        Button zaimportujPliki = new Button("Zaimportuj pliki", VaadinIcon.CLOUD_DOWNLOAD.create());
        zaimportujPliki.setHeight("30px");
        zaimportujPliki.getStyle().set("color", "black");
        zaimportujPliki.addClickListener(buttonClickEvent -> importFiles.csv());

        Board tabelkaPrzyciskow = new Board();
        tabelkaPrzyciskow.addRow(bazyKlientow, daneklienta, wprowadzdanedachowek);
        tabelkaPrzyciskow.addRow(zaimportujPliki*//*, obliczonaoferta*//*);
        return tabelkaPrzyciskow;
    }*/

    public Board routerLink() {
        Tabs menu = new Tabs();
        Button button = new Button("Zaimportuj cenniki");
        button.addClickListener(e -> importFiles.csv());
        menu.add(new Tab(new RouterLink(CENNIK, Cennik.class)),
                new Tab(new RouterLink(ENTER_DATA, LoadUser.class)),
                new Tab(new RouterLink(USERS, Users.class)),
                new Tab(new RouterLink(INPUT_USER, InputUser.class)),
                new Tab(new RouterLink(CHECKBOXES, Checkboxes.class)),
                new Tab(new RouterLink(SELECT_ACCESORIES, SelectAccesories.class)),
                new Tab(new RouterLink(ENTER_TILES, EnterTiles.class)),
                new Tab(new RouterLink(SHOW_TABLE_TILES, ShowTableTiles.class)),
                new Tab(button));
        menu.setOrientation(Tabs.Orientation.HORIZONTAL);
        Board board = new Board();
        board.addRow(menu);
        return board;
    }

    public Board sideMenu(){
        MenuBar menuBar = new MenuBar();
        Tabs sideMenu = new Tabs();
        Board board = new Board();

        return board;
    }

}
