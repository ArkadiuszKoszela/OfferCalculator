package pl.koszela.spring.views;

/*
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
*/

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.importFiles.ImportFiles;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.vaadin.flow.component.icon.VaadinIcon.TRENDING_UP;

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

        Button button = new Button("Zaimportuj pliki");
        button.addClickListener(e -> importFiles.csv());
        MenuItem users = menuBar.addItem("Klienci");
        MenuItem tiles = menuBar.addItem(new RouterLink("Dachówki", EnterTiles.class));
        MenuItem accesories = menuBar.addItem("Akcesoria");
        MenuItem importFile = menuBar.addItem(button);

        users.getSubMenu().addItem(new RouterLink("Tabela", Users.class));
        users.getSubMenu().addItem(new RouterLink("Wprowadź dane", InputUser.class));

        accesories.getSubMenu().addItem(new RouterLink("Wybierz akcesoria", SelectAccesories.class));
        accesories.getSubMenu().addItem(new RouterLink("Checkboxy", Checkboxes.class));

        addToNavbar(menuBar);
        final Tabs tabs = new Tabs(enterTiles(), checkboxes());
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addSelectedChangeListener(event -> {
            final Tab selectedTab = event.getSelectedTab();
            final Component component = tab2Workspace.get(selectedTab);
            setContent(component);
        });
        addToDrawer(tabs);

    }

    private Tab checkboxes() {
        final Span label = new Span(getTranslation(Checkboxes.CHECKBOXES));
        final Icon icon = TRENDING_UP.create();
        final Tab tab = new Tab(new RouterLink("checkboxes", Checkboxes.class));
        tab2Workspace.put(tab, new Checkboxes());
        return tab;
    }

    private Tab enterTiles() {
        final Span label = new Span(getTranslation(EnterTiles.ENTER_TILES));
        final Icon icon = TRENDING_UP.create();
        final Tab tab = new Tab(new RouterLink("enter", EnterTiles.class));
        tab2Workspace.put(tab, new EnterTiles());
        return tab;
    }
}
