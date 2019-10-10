package pl.koszela.spring.views;

import com.vaadin.flow.component.applayout.AppLayout;

import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.importFiles.ImportFiles;
import pl.koszela.spring.service.SaveUsers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static pl.koszela.spring.views.OfferView.CREATE_OFFER;
import static pl.koszela.spring.views.TilesView.ENTER_TILES;
import static pl.koszela.spring.views.UsersView.INPUT_USER;
import static pl.koszela.spring.views.AccesoriesView.SELECT_ACCESORIES;
import static pl.koszela.spring.views.WindowsView.WINDOWS;

@Route("")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class MainView extends AppLayout {

    private ImportFiles importFiles;
    private SaveUsers saveUsers;

    @Autowired
    public MainView(ImportFiles importFiles, SaveUsers saveUsers) {
        this.importFiles = Objects.requireNonNull(importFiles);
        this.saveUsers = Objects.requireNonNull(saveUsers);

        Image img = new Image("http://www.nowoczesnebudowanie.pl/wp-content/uploads/2016/10/logo-nowoczesne-budowanie-1200x857.png", "Vaadin Logo");
        img.setHeight("44px");
        addToNavbar(new DrawerToggle(), img);
        MenuBar menuBar = new MenuBar();
        Tabs tabs = new Tabs(false, new Tab("Klienci"), new Tab("Dachówki"), new Tab("Akcesoria"),
                new Tab("Okna"), new Tab("Oferta"));
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
            } else if (e.getSelectedTab().getLabel().equalsIgnoreCase("Okna")) {
                getUI().ifPresent(ui -> ui.navigate(WINDOWS));
                tabs.setSelectedTab(e.getSelectedTab());
            } else if (e.getSelectedTab().getLabel().equalsIgnoreCase("Oferta")) {
                getUI().ifPresent(ui -> ui.navigate(CREATE_OFFER));
                tabs.setSelectedTab(e.getSelectedTab());
            }
        });

        VaadinSession.getCurrent().setAttribute("tabs", tabs);
        menuBar.addItem(tabs);
        Button button = new Button("Zaimportuj pliki");
        button.addClickListener(e -> importFiles.csv());
        if (isDrawerOpened()) {
            setDrawerOpened(false);
        }

        File file = new File("src\\main\\resources\\templates\\itext.pdf");

        Anchor anchor = new Anchor(getStreamResource(file.getName(), file), file.getName());
        anchor.getElement().setAttribute("download", true);

        anchor.setHref(getStreamResource(file.getName(), file));
        anchor.setVisible(false);
        Button btn = new Button("Download");
        btn.addClickListener(buttonClickEvent -> {
            GenerateOffer.writeUsingIText();
            addToDrawer(anchor);
            anchor.setVisible(true);
            saveUsers.saveUser();
        });

        addToNavbar(menuBar);
        addToDrawer(button, btn);
    }

    private StreamResource getStreamResource(String filename, File content) {
        return new StreamResource(filename, () -> {
            try {
                return new ByteArrayInputStream(FileUtils.readFileToByteArray(content));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
