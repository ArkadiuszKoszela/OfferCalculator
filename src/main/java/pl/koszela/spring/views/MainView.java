package pl.koszela.spring.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.crud.CreateUser;
import pl.koszela.spring.crud.UpdateUser;
import pl.koszela.spring.gernateFile.GenerateOffer;
import pl.koszela.spring.importFiles.ImportFiles;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static pl.koszela.spring.service.ServiceNotification.getNotificationError;
import static pl.koszela.spring.service.ServiceNotification.getNotificationSucces;
import static pl.koszela.spring.views.AccesoriesView.SELECT_ACCESORIES;
import static pl.koszela.spring.views.GutterView.GUTTER_VIEW;
import static pl.koszela.spring.views.IncludeDataView.INCLUDE_DATA;
import static pl.koszela.spring.views.OfferView.CREATE_OFFER;
import static pl.koszela.spring.views.UsersView.INPUT_USER;
import static pl.koszela.spring.views.WindowsView.WINDOWS;
import static pl.koszela.spring.views.priceLists.AccesoriesPriceListView.ACCESORIES_PRICE_LIST;
import static pl.koszela.spring.views.priceLists.PriceListOfSalesCompetition.PRICE_LIST_OF_SALES_COMPETITION;
import static pl.koszela.spring.views.priceLists.TilesPriceListView.TILES_PRICE_LIST;

@Route("")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
public class MainView extends AppLayout {

    private ImportFiles importFiles;
    private CreateUser createUser;
    private UpdateUser updateUser;

    @Autowired
    public MainView(ImportFiles importFiles, CreateUser createUser, UpdateUser updateUser) {
        this.importFiles = Objects.requireNonNull(importFiles);
        this.createUser = Objects.requireNonNull(createUser);
        this.updateUser = Objects.requireNonNull(updateUser);

        Image img = new Image("http://www.nowoczesnebudowanie.pl/wp-content/uploads/2016/10/logo-nowoczesne-budowanie-1200x857.png", "Vaadin Logo");
        img.setHeight("44px");
        addToNavbar(new DrawerToggle(), img);

        MenuBar menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_CONTRAST);
        addItemMenuBar(menuBar, "Strona Główna", "");
        addItemMenuBar(menuBar, "Klienci", INPUT_USER);
        addItemMenuBar(menuBar, "Wprowadź Dane", INCLUDE_DATA);
        addItemMenuBar(menuBar, "Akcesoria", SELECT_ACCESORIES);
        addItemMenuBar(menuBar, "Rynny", GUTTER_VIEW);
        addItemMenuBar(menuBar, "Okna", WINDOWS);
        addItemMenuBar(menuBar, "Oferta", CREATE_OFFER);
        MenuItem priceLists = menuBar.addItem("Cenniki");
        priceLists.getSubMenu().addItem("Dachówki", event -> getUI().ifPresent(ui -> ui.navigate(TILES_PRICE_LIST)));
        priceLists.getSubMenu().addItem("Akcesoria", event -> getUI().ifPresent(ui -> ui.navigate(ACCESORIES_PRICE_LIST)));
        priceLists.getSubMenu().addItem("Konkurencja", event -> getUI().ifPresent(ui -> ui.navigate(PRICE_LIST_OF_SALES_COMPETITION)));

        Button importFilesButton = new Button("Zaimportuj pliki");
        importFilesButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        importFilesButton.addClickListener(e -> importFiles.csv());
        if (isDrawerOpened()) {
            setDrawerOpened(false);
        }

        File file = new File("src/main/resources/templates/offer.pdf");

        Anchor anchor = new Anchor(getStreamResource(file.getName(), file), file.getName());
        anchor.getElement().setAttribute("download", true);
        anchor.setHref(getStreamResource(file.getName(), file));
        anchor.setVisible(false);
        Button saveNewUser = new Button("Zapisz użytkownika");
        saveNewUser.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        saveNewUser.addClickListener(event -> createUser.saveUser());
        Button update = new Button("Zaktualizuj użytkownika");
        update.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        update.addClickListener(event -> updateUser.updateUser());
        FormLayout formLayout = new FormLayout();
        FormLayout.ResponsiveStep responsiveStep = new FormLayout.ResponsiveStep("5px", 1);
        formLayout.setResponsiveSteps(responsiveStep);

        Button gereneteOffer = new Button("Generuj ofertę");
        gereneteOffer.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        gereneteOffer.addClickListener(buttonClickEvent -> {
            try {
                GenerateOffer.writeUsingIText();
                formLayout.add(anchor);
                anchor.setVisible(true);
                getNotificationSucces("Oferta została wygenerowana");
            } catch (NotFoundException ignored) {
                getNotificationError("Coś poszło nie tak. Proszę uzupełnić wszystkie pola");
            }
        });

        formLayout.add(importFilesButton, saveNewUser, update, gereneteOffer);
        addToNavbar(menuBar);
        addToDrawer(formLayout);
    }

    private void addItemMenuBar(MenuBar menuBar, String nameItem, String nameRoute) {
        menuBar.addItem(nameItem, event -> getUI().ifPresent(ui -> ui.navigate(nameRoute)));
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