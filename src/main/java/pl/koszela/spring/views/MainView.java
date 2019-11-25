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
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.gernateFile.GenerateOffer;
import pl.koszela.spring.importFiles.ImportFiles;
import pl.koszela.spring.crud.CreateUser;
import pl.koszela.spring.crud.UpdateUser;
import pl.koszela.spring.service.NotificationInterface;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static pl.koszela.spring.views.priceLists.AccessoriesPriceListView.ACCESSORIES_PRICE;
import static pl.koszela.spring.views.priceLists.AccessoriesWindowsPriceListView.ACCESSORIES_WINDOWS_PRICE;
import static pl.koszela.spring.views.priceLists.CollarsPriceListView.COLLARS_PRICE;
import static pl.koszela.spring.views.priceLists.CustomerRecommendListView.CUSTOMER_RECOMMEND;
import static pl.koszela.spring.views.priceLists.GutterPriceListView.GUTTERS_PRICE;
import static pl.koszela.spring.views.priceLists.WindowsPriceListView.WINDOWS_PRICE;
import static staticField.Endpoint.FILE_TO_GENERATE_OFFER_URL;
import static pl.koszela.spring.views.AccesoriesView.SELECT_ACCESORIES;
import static pl.koszela.spring.views.AccesoriesWindowsView.ACCESORIES_WINDOWS;
import static pl.koszela.spring.views.CollarView.COLLAR;
import static pl.koszela.spring.views.FiresideView.FIRESIDE;
import static pl.koszela.spring.views.GutterView.GUTTER_VIEW;
import static pl.koszela.spring.views.LightningProtectionSystemView.PROTECTION_SYSTEM;
import static pl.koszela.spring.views.OfferView.CREATE_OFFER;
import static pl.koszela.spring.views.priceLists.PriceListOfSalesCompetition.PRICE_LIST_OF_SALES_COMPETITION;
import static pl.koszela.spring.views.IncludeDataView.INCLUDE_DATA;
import static pl.koszela.spring.views.UsersView.INPUT_USER;
import static pl.koszela.spring.views.WindowsView.WINDOWS;
import static pl.koszela.spring.views.priceLists.TilesPriceListView.TILES_PRICE_LIST;

//@Route("")
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
        addItemMenuBar(menuBar, "Klienci", INPUT_USER);
        addItemMenuBar(menuBar, "Wprowadź Dane", INCLUDE_DATA);
        addItemMenuBar(menuBar, "Akcesoria", SELECT_ACCESORIES);
        addItemMenuBar(menuBar, "Rynny", GUTTER_VIEW);
        addItemMenuBar(menuBar, "Okna", WINDOWS);
        addItemMenuBar(menuBar, "Kołnierze", COLLAR);
        addItemMenuBar(menuBar, "Akcesoria do okien", ACCESORIES_WINDOWS);
        addItemMenuBar(menuBar, "Kominki", FIRESIDE);
        addItemMenuBar(menuBar, "System Odgromowy", PROTECTION_SYSTEM);
        addItemMenuBar(menuBar, "Oferta", CREATE_OFFER);
        MenuItem priceLists = menuBar.addItem("Cenniki");
        priceLists.getSubMenu().addItem("Dachówki", event -> getUI().ifPresent(ui -> ui.navigate(TILES_PRICE_LIST)));
        priceLists.getSubMenu().addItem("Akcesoria", event -> getUI().ifPresent(ui -> ui.navigate(ACCESSORIES_PRICE)));
        priceLists.getSubMenu().addItem("Kołnierze", event -> getUI().ifPresent(ui -> ui.navigate(COLLARS_PRICE)));
        priceLists.getSubMenu().addItem("Akcesoria okienne", event -> getUI().ifPresent(ui -> ui.navigate(ACCESSORIES_WINDOWS_PRICE)));
        priceLists.getSubMenu().addItem("Rynny", event -> getUI().ifPresent(ui -> ui.navigate(GUTTERS_PRICE)));
        priceLists.getSubMenu().addItem("Okna", event -> getUI().ifPresent(ui -> ui.navigate(WINDOWS_PRICE)));
        priceLists.getSubMenu().addItem("Konkurencja", event -> getUI().ifPresent(ui -> ui.navigate(PRICE_LIST_OF_SALES_COMPETITION)));
        priceLists.getSubMenu().addItem("Rekomendacje", event -> getUI().ifPresent(ui -> ui.navigate(CUSTOMER_RECOMMEND)));

        Button importFilesButton = new Button("Zaimportuj pliki");
        importFilesButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        importFilesButton.addClickListener(e -> importFiles.csv());
        if (isDrawerOpened()) {
            setDrawerOpened(false);
        }

        File file = new File(FILE_TO_GENERATE_OFFER_URL.location());

        Anchor anchor = new Anchor(getStreamResource(file.getName(), file), file.getName());
        anchor.getElement().setAttribute("download", true);
        anchor.setHref(getStreamResource(file.getName(), file));
        anchor.setVisible(false);
        Button saveNewUser = new Button("Zapisz użytkownika", event -> createUser.saveUser());
        saveNewUser.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        Button update = new Button("Zaktualizuj użytkownika", event -> updateUser.updateUser());
        update.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        FormLayout formLayout = new FormLayout();
        FormLayout.ResponsiveStep responsiveStep = new FormLayout.ResponsiveStep("5px", 1);
        formLayout.setResponsiveSteps(responsiveStep);

        Button generateOffer = new Button("Generuj ofertę", buttonClickEvent -> {
            try {
                GenerateOffer.writeUsingIText(FILE_TO_GENERATE_OFFER_URL.location());
                formLayout.add(new Tab(anchor));
                anchor.setVisible(true);
                NotificationInterface.notificationOpen("Oferta została wygenerowana", NotificationVariant.LUMO_SUCCESS);
            } catch (NotFoundException ignored) {
                NotificationInterface.notificationOpen("Coś poszło nie tak. Proszę uzupełnić wszystkie pola", NotificationVariant.LUMO_ERROR);
            }
        });
        generateOffer.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        formLayout.add(importFilesButton, saveNewUser, update, generateOffer);
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