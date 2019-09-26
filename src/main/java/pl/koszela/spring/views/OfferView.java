package pl.koszela.spring.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Entity;
import pl.koszela.spring.calculate.CalculateTiles;
import pl.koszela.spring.entities.EntityAccesories;
import pl.koszela.spring.entities.EntityTiles;
import pl.koszela.spring.entities.EntityUser;
import pl.koszela.spring.repositories.InputDataTilesRepository;
import pl.koszela.spring.repositories.TilesRepository;
import pl.koszela.spring.repositories.UsersRepo;
import pl.koszela.spring.service.MenuBarInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Route(value = OfferView.CREATE_OFFER, layout = MainView.class)
public class OfferView extends VerticalLayout implements MenuBarInterface {

    public static final String CREATE_OFFER = "createOffer";

    private UsersRepo usersRepo;
    private CalculateTiles calculateTiles;
    private TilesRepository tilesRepository;
    private InputDataTilesRepository inputDataTilesRepository;

    private ComboBox<String> selectUser = new ComboBox<>("Wybierz klienta: ");
    private ComboBox<String> priceList = new ComboBox<>("Dostępne cenniki: ");

    private Grid<EntityTiles> resultTiles = new Grid<>(EntityTiles.class);
    private Grid<EntityAccesories> resultAccesories = new Grid<>(EntityAccesories.class);
    private Grid<EntityUser> resultUser = new Grid<>(EntityUser.class);
    private NumberField customerDiscount = new NumberField("Rabat na dachówki");
    private VerticalLayout layout = new VerticalLayout();

    private Button calculateProfit = new Button("Oblicz zyski");
    private Button viewUSer = new Button("Pokaż klienta");

    @Autowired
    public OfferView(UsersRepo usersRepo, CalculateTiles calculateTiles, TilesRepository tilesRepository,
                     InputDataTilesRepository inputDataTilesRepository) {
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.calculateTiles = Objects.requireNonNull(calculateTiles);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
        this.inputDataTilesRepository = Objects.requireNonNull(inputDataTilesRepository);

        loadUserComboBox();
        add(menu());
        add(addLayout());
    }

    private VerticalLayout addLayout() {
        FormLayout formLayout = new FormLayout();
        calculateProfit.addClickListener(buttonClickEvent -> {
            resultTiles.setItems(resultTiles());
            resultAccesories.setItems(resultAccesories());
        });
        EntityUser entityUser = (EntityUser) VaadinSession.getCurrent().getAttribute("user");
        viewUSer.addClickListener(buttonClickEvent -> resultUser.setItems(entityUser));
        formLayout.add(selectUser, getAvailablePriceList());
        formLayout.add(customerDiscount, calculateProfit);
        formLayout.add(viewUSer);
        layout.add(formLayout);
        layout.add(createGridUsers());
        layout.add(createGridTiles());
        layout.add(createGridAccesories());
        return layout;
    }

    private Grid<EntityUser> createGridUsers() {
        resultUser.getColumns().forEach(column -> column.setAutoWidth(true));
        return resultUser;
    }

    private Grid<EntityTiles> createGridTiles() {
        resultTiles.getColumnByKey("name").setHeader("Kategoria");
        resultTiles.getColumnByKey("priceListName").setHeader("Nazwa Cennika");
        resultTiles.getColumnByKey("priceAfterDiscount").setHeader("Cena sprzedaży");
        resultTiles.getColumnByKey("purchasePrice").setHeader("Cena Zakupu");
        resultTiles.getColumnByKey("profitCalculate").setHeader("Zysk");
        resultTiles.removeColumnByKey("id");
        resultTiles.removeColumnByKey("type");
        resultTiles.removeColumnByKey("unitRetailPrice");
        resultTiles.removeColumnByKey("profit");
        resultTiles.removeColumnByKey("basicDiscount");
        resultTiles.removeColumnByKey("supplierDiscount");
        resultTiles.removeColumnByKey("additionalDiscount");
        resultTiles.removeColumnByKey("skontoDiscount");
        resultTiles.getColumns().forEach(column -> column.setAutoWidth(true));
        return resultTiles;
    }

    private Grid<EntityAccesories> createGridAccesories() {
        resultAccesories.getColumnByKey("name").setHeader("Nazwa");
        resultAccesories.getColumnByKey("purchasePrice").setHeader("Cena zakupu");
        resultAccesories.getColumnByKey("margin").setHeader("Ilość");
        resultAccesories.getColumnByKey("unitRetailPrice").setHeader("Cena Detaliczna (szt.");
        resultAccesories.getColumnByKey("totalRetail").setHeader("Cena Detaliczna total");
        resultAccesories.getColumnByKey("unitPurchasePrice").setHeader("Cena zakupu (szt.)");
        resultAccesories.getColumnByKey("totalPurchase").setHeader("Cena zakupu total");
        resultAccesories.removeColumnByKey("id");
        resultAccesories.removeColumnByKey("firstMultiplier");
        resultAccesories.removeColumnByKey("secondMultiplier");
        resultAccesories.getColumns().forEach(column -> column.setAutoWidth(true));
        return resultAccesories;
    }

    private List<EntityTiles> resultTiles() {
        EntityUser entityUser = (EntityUser) VaadinSession.getCurrent().getAttribute("user");
        return entityUser.getEntityTiles();
    }

    private List<EntityAccesories> resultAccesories() {
        EntityUser entityUser = (EntityUser) VaadinSession.getCurrent().getAttribute("user");
        return entityUser.getEntityAccesories();
    }

    private void loadUserComboBox() {
        if (nameAndSurname().size() > 0) {
            selectUser.setItems(nameAndSurname());
        }
    }

    private List<String> nameAndSurname() {
        EntityUser entityUser = (EntityUser) VaadinSession.getCurrent().getAttribute("user");
        Iterable<EntityUser> allUsersFromRepository = usersRepo.findAll();
        List<String> nameAndSurname = new ArrayList<>();
/*
        allUsersFromRepository.forEach(user -> nameAndSurname.add(user.getName().concat(" ").concat(user.getSurname())));
*/
        return nameAndSurname;
    }

    private ComboBox<String> getAvailablePriceList() {
        if (!calculateTiles.getAvailablePriceList().isEmpty()) {
            priceList.setItems(calculateTiles.getAvailablePriceList());
            return priceList;
        } else {
            priceList.setItems("Brak zaimportowanych cenników");
            return priceList;
        }
    }

    @Override
    public MenuBar menu() {
        MenuBar menuBar = new MenuBar();
        menuBar.addItem("Utwórz ofertę");
        return menuBar;
    }
}
