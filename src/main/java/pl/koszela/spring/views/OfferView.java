package pl.koszela.spring.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.*;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.calculate.CalculateTiles;
import pl.koszela.spring.entities.EntityAccesories;
import pl.koszela.spring.entities.EntityUser;
import pl.koszela.spring.entities.Tiles;
import pl.koszela.spring.repositories.InputDataTilesRepository;
import pl.koszela.spring.repositories.UsersRepo;
import pl.koszela.spring.service.MenuBarInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Route(value = OfferView.CREATE_OFFER, layout = MainView.class)
public class OfferView extends VerticalLayout implements MenuBarInterface {

    public static final String CREATE_OFFER = "createOffer";

    private UsersRepo usersRepo;
    private CalculateTiles calculateTiles;
    private InputDataTilesRepository inputDataTilesRepository;

    private ComboBox<String> selectUser = new ComboBox<>("Wybierz klienta: ");
    private ComboBox<String> priceList = new ComboBox<>("Dostępne cenniki: ");

    /*private Grid<Tiles> resultTiles = new Grid<>(Tiles.class);*/
    private Grid<EntityAccesories> resultAccesories = new Grid<>(EntityAccesories.class);
    private Grid<EntityUser> resultUser = new Grid<>(EntityUser.class);
    private NumberField customerDiscount = new NumberField("Rabat na dachówki");
    private VerticalLayout layout = new VerticalLayout();

    private Button calculateProfit = new Button("Oblicz zyski");
    private Button viewUSer = new Button("Pokaż klienta");

    @Autowired
    public OfferView(UsersRepo usersRepo, CalculateTiles calculateTiles,
                     InputDataTilesRepository inputDataTilesRepository) {
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.calculateTiles = Objects.requireNonNull(calculateTiles);
        this.inputDataTilesRepository = Objects.requireNonNull(inputDataTilesRepository);

        loadUserComboBox();
        add(menu());
        add(addLayout());
    }

    private VerticalLayout addLayout() {
        FormLayout formLayout = new FormLayout();
        calculateProfit.addClickListener(buttonClickEvent -> {
            resultAccesories.setItems(resultAccesories());
        });
        EntityUser entityUser = (EntityUser) VaadinSession.getCurrent().getAttribute("user");
        viewUSer.addClickListener(buttonClickEvent -> resultUser.setItems(entityUser));
        formLayout.add(selectUser, getAvailablePriceList());
        formLayout.add(customerDiscount, calculateProfit);
        formLayout.add(viewUSer);
        layout.add(formLayout);
        layout.add(createTiles());
        layout.add(createGridUsers());
        /*layout.add(createGridTiles());*/
        layout.add(createGridAccesories());
        return layout;
    }

    private Grid<EntityUser> createGridUsers() {
        resultUser.getColumns().forEach(column -> column.setAutoWidth(true));
        return resultUser;
    }

    private TreeGrid<Tiles> createTiles() {
        TreeGrid<Tiles> treeGrid = new TreeGrid<>();
        TreeData<Tiles> treeData1 = new TreeData<>();

        List<Tiles> list = (List<Tiles>) VaadinSession.getCurrent().getAttribute("bogen1");
        List<Tiles> list2 = (List<Tiles>) VaadinSession.getCurrent().getAttribute("bogen2");
        if(list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    treeData1.addItem(null, list.get(i));
                    treeData1.addItem(null, list2.get(i));
                } else {
                    treeData1.addItems(list.get(0), list.get(i));
                    treeData1.addItems(list2.get(0), list2.get(i));
                }
            }
        }


        treeGrid.addHierarchyColumn(Tiles::getPriceListName).setHeader("Nazwa cennika");
        treeGrid.addColumn(Tiles::getName).setHeader("Kategoria");
        treeGrid.addColumn(Tiles::getPrice).setHeader("Cena jednostkowa");
        treeGrid.addColumn(Tiles::getTotalPrice).setHeader("Total");
        treeGrid.addColumn(Tiles::getPricePurchase).setHeader("Cena zakupu");
        treeGrid.addColumn(Tiles::getPriceAfterDiscount).setHeader("Cena po rabacie");
        treeGrid.addColumn(Tiles::getProfit).setHeader("Zysk");

        treeGrid.setDataProvider(new TreeDataProvider<Tiles>(treeData1));
        treeGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        return treeGrid;
    }

    public TreeData<String> addItems(String parent, Stream<String> items) {
        TreeData<String> treeData = new TreeData<>();
        items.forEach(item -> treeData.addItem(parent, item));
        return treeData;
    }

    /*private Grid<Tiles> createGridTiles() {
      *//*  resultTiles.getColumnByKey("name").setHeader("Kategoria");
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
        resultTiles.getColumns().forEach(column -> column.setAutoWidth(true));*//*
        return resultTiles;
    }*/

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

    /*private List<EntityTiles> resultTiles() {
        EntityUser entityUser = (EntityUser) VaadinSession.getCurrent().getAttribute("user");
        return entityUser.getEntityTiles();
    }*/

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
        allUsersFromRepository.forEach(user -> nameAndSurname.add(user.getEntityPersonalData().getName().concat(" ").concat(user.getEntityPersonalData().getSurname())));
        /*nameAndSurname.add(entityUser.getEntityPersonalData().getName().concat(" ").concat(entityUser.getEntityPersonalData().getSurname()));*/
        nameAndSurname.add("Brak");
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
