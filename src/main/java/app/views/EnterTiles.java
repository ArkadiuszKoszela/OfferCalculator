package app.views;

import app.calculate.CalculateTiles;
import app.entities.*;
import app.repositories.*;
import app.service.MenuBarInterface;
import com.google.common.base.Joiner;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static app.inputFields.ServiceNotification.getNotificationError;
import static app.inputFields.ServiceNotification.getNotificationSucces;

@Route(value = EnterTiles.ENTER_TILES, layout = MainView.class)
public class EnterTiles extends VerticalLayout implements MenuBarInterface {

    public static final String ENTER_TILES = "tiles";

    private AccesoriesRepository accesoriesRepository;
    private UsersRepo usersRepo;
    private InputDataTilesRepository inputDataTilesRepository;
    private CalculateTiles calculateTiles;
    private TilesRepository tilesRepository;
    private ResultTilesRepository resultTilesRepository;

    private NumberField numberField1 = new NumberField("Powierzchnia połaci");
    private NumberField numberField2 = new NumberField("Długość kalenic");
    private NumberField numberField3 = new NumberField("Długość kalenic skośnych");
    private NumberField numberField4 = new NumberField("Długość kalenic prostych");
    private NumberField numberField5 = new NumberField("Długość koszy");
    private NumberField numberField6 = new NumberField("Długość krawędzi lewych");
    private NumberField numberField7 = new NumberField("Długość krawędzi prawych");
    private NumberField numberField8 = new NumberField("Obwód komina");
    private NumberField numberField9 = new NumberField("Długość okapu");
    private NumberField numberField10 = new NumberField("Dachówka wentylacyjna");
    private NumberField numberField11 = new NumberField("Komplet kominka wentylacyjnego");
    private NumberField numberField12 = new NumberField("Gąsior początkowy kalenica prosta");
    private NumberField numberField13 = new NumberField("Gąsior końcowy kalenica prosta");
    private NumberField numberField14 = new NumberField("Gąsior zaokrąglony");
    private NumberField numberField15 = new NumberField("Trójnik");
    private NumberField numberField16 = new NumberField("Czwórnik");
    private NumberField numberField17 = new NumberField("Gąsior z podwójną mufą");
    private NumberField numberField18 = new NumberField("Dachówka dwufalowa");
    private NumberField numberField19 = new NumberField("Okno połaciowe");
    private NumberField customerDiscount = new NumberField("Podaj rabat dla klienta:");

    private ComboBox<String> comboBoxViewTable = new ComboBox<>("Podaj nazwę cennika: ");
    private ComboBox<String> comboBoxInput = new ComboBox<>("Podaj nazwę cennika: ");
    private ComboBox<String> comboBoxUsers = new ComboBox<>("Wczytaj użytkownika: ");
    private List<NumberField> listOfNumberFields = new ArrayList<>();

    private VerticalLayout tabela = new VerticalLayout();
    private VerticalLayout dane = new VerticalLayout();
    private VerticalLayout cennik = new VerticalLayout();

    private Button save = new Button("Zapisz dane");
    private Button calculateProfit = new Button("Oblicz zysk");

    private Grid<EntityResultTiles> resultTilesGrid = new Grid<>(EntityResultTiles.class);
    private Grid<EntityTiles> grid = new Grid<>(EntityTiles.class);

    public EnterTiles() {
    }

    @Autowired
    public EnterTiles(AccesoriesRepository accesoriesRepository, UsersRepo usersRepo, InputDataTilesRepository inputDataTilesRepository,
                      CalculateTiles calculateTiles, TilesRepository tilesRepository,
                      ResultTilesRepository resultTilesRepository) {
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.inputDataTilesRepository = Objects.requireNonNull(inputDataTilesRepository);
        this.calculateTiles = Objects.requireNonNull(calculateTiles);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
        this.resultTilesRepository = Objects.requireNonNull(resultTilesRepository);

        loadUserComboBox();
        add(menu());
        add(createInputFields());
        add(addFormLayout());
        add(createGrid());
    }

    private VerticalLayout addFormLayout() {
        calculateProfit.addClickListener(buttonClickEvent -> loadResultTableTiles());
        FormLayout formLayout = new FormLayout();
        formLayout.add(comboBoxViewTable, calculateProfit);
        tabela.add(formLayout);
        tabela.add(createTable());
        getAvailablePriceList(comboBoxViewTable);
        return tabela;
    }

    private void getAvailablePriceList(ComboBox<String> comboBox) {
        if (!calculateTiles.getAvailablePriceList().isEmpty()) {
            comboBox.setItems(calculateTiles.getAvailablePriceList());
        }
    }

    private VerticalLayout createInputFields() {
        setDefaultValues();
        save.addClickListener(buttonClickEvent -> loadUser());
        FormLayout formLayout = new FormLayout();
        formLayout.add(getCustomerDiscount(), comboBoxInput);
        formLayout.add(comboBoxUsers, save);
        listOfNumberFields.forEach(formLayout::add);
        getAvailablePriceList(comboBoxInput);
        dane.add(formLayout);
        return dane;
    }

    private List<EntityResultTiles> listResultTiles() {
        String[] spliString = comboBoxViewTable.getValue().split(" ");

        List<EntityTiles> priceListFromRepository = tilesRepository.findByPriceListNameAndType(spliString[0] + " " + spliString[1] + " " + spliString[2], spliString[3] + " " + spliString[4]);
        List<EntityResultTiles> listResultTiles = getEntityResultTiles();
        listResultTiles.forEach(e -> e.setPriceListName(comboBoxViewTable.getValue()));

        calculateTiles.getRetail(listResultTiles, priceListFromRepository, customerDiscount, listOfNumberFields);
        calculateTiles.getPurchase(listResultTiles, priceListFromRepository, listOfNumberFields);
        calculateTiles.getProfit(listResultTiles);

        resultTilesRepository.saveAll(listResultTiles);
        return listResultTiles;
    }

    private List<EntityResultTiles> getEntityResultTiles() {
        Iterable<EntityResultTiles> resultTilesFromRepository = resultTilesRepository.findAll();
        List<EntityResultTiles> listResultTiles = new ArrayList<>();
        resultTilesFromRepository.forEach(listResultTiles::add);
        return listResultTiles;
    }

    private Grid<EntityResultTiles> createTable() {
        resultTilesGrid.getColumnByKey("name").setHeader("Kategoria");
        resultTilesGrid.getColumnByKey("priceListName").setHeader("Nazwa Cennika");
        resultTilesGrid.getColumnByKey("priceAfterDiscount").setHeader("Cena sprzedaży");
        resultTilesGrid.getColumnByKey("purchasePrice").setHeader("Cena Zakupu");
        resultTilesGrid.getColumnByKey("profit").setHeader("Zysk");
        resultTilesGrid.removeColumnByKey("id");
        return resultTilesGrid;
    }

    private void loadResultTableTiles() {
        if (allTilesFromRespository().size() > 0) {
            resultTilesGrid.setItems(listResultTiles());
            getNotificationSucces("Obliczono kalkulację");
        } else if (allTilesFromRespository().size() == 0) {
            getNotificationError("Zaimportuj cenniki");
        } else {
            getNotificationError("Wybierz cennik");
        }
    }

    private List<EntityTiles> allTilesFromRespository() {
        Iterable<EntityTiles> allTilesFromRepository = tilesRepository.findAll();
        List<EntityTiles> allTiles = new ArrayList<>();
        allTilesFromRepository.forEach(allTiles::add);
        return allTiles;
    }

    private EntityInputDataTiles saveInputData() {
        EntityInputDataTiles entityInputDataTiles = EntityInputDataTiles.builder()
                .powierzchniaPolaci(numberField1.getValue())
                .dlugoscKalenic(numberField2.getValue())
                .dlugoscKalenicProstych(numberField3.getValue())
                .dlugoscKalenicSkosnych(numberField4.getValue())
                .dlugoscKoszy(numberField5.getValue())
                .dlugoscKrawedziLewych(numberField6.getValue())
                .dlugoscKrawedziPrawych(numberField7.getValue())
                .obwodKomina(numberField8.getValue())
                .dlugoscOkapu(numberField9.getValue())
                .dachowkaWentylacyjna(numberField10.getValue())
                .kompletKominkaWentylacyjnego(numberField11.getValue())
                .gasiarPoczatkowyKalenicaProsta(numberField12.getValue())
                .gasiarKoncowyKalenicaProsta(numberField13.getValue())
                .gasiarZaokraglony(numberField14.getValue())
                .trojnik(numberField15.getValue())
                .czwornik(numberField16.getValue())
                .gasiarZPodwojnaMufa(numberField17.getValue())
                .dachowkaDwufalowa(numberField18.getValue())
                .oknoPolaciowe(numberField19.getValue())
                .build();
        inputDataTilesRepository.save(entityInputDataTiles);
        return entityInputDataTiles;
    }

    private void setDefaultValues() {
        setValues(numberField1, "m²", 300d);
        setValues(numberField2, "mb", 65d);
        setValues(numberField3, "mb", 65d);
        setValues(numberField4, "mb", 1d);
        setValues(numberField5, "mb", 8d);
        setValues(numberField6, "mb", 5d);
        setValues(numberField7, "mb", 5d);
        setValues(numberField8, "mb", 3d);
        setValues(numberField9, "mb", 38d);
        setValues(numberField10, "szt", 1d);
        setValues(numberField11, "szt", 1d);
        setValues(numberField12, "szt", 1d);
        setValues(numberField13, "mb", 1d);
        setValues(numberField14, "mb", 6d);
        setValues(numberField15, "szt", 1d);
        setValues(numberField16, "szt", 1d);
        setValues(numberField17, "mb", 1d);
        setValues(numberField18, "szt", 1d);
        setValues(numberField19, "szt", 1d);
        setTitle();
        getListNumberFields();
    }

    private void getListNumberFields() {
        listOfNumberFields = Arrays.asList(numberField1, numberField2, numberField3, numberField4, numberField5, numberField6, numberField7,
                numberField8, numberField9, numberField10, numberField11, numberField12, numberField13, numberField14, numberField15, numberField16,
                numberField17, numberField18, numberField19);
    }

    private String getString(List<String> listaNazw, int i, int i2) {
        return Joiner.on(" ").join(getSubList(listaNazw, i, i2));
    }

    private void setTitle() {
        if (accesoriesRepository != null) {
            Iterable<EntityAccesories> iterable = accesoriesRepository.findAll();
            List<String> names = new ArrayList<>();
            iterable.forEach(e -> names.add(e.getName()));
            if (names.size() != 0) {
                numberField1.setTitle(getString(names, 39, 44)
                        .concat(getString(names, 59, 64)));

                numberField2.setTitle(getString(names, 0, 9)
                        .concat(getString(names, 9, 14))
                        .concat(getString(names, 35, 39)));

                numberField5.setTitle(getString(names, 21, 23)
                        .concat(getString(names, 24, 25))
                        .concat(getString(names, 25, 27))
                        .concat(getString(names, 44, 46)));

                numberField8.setTitle(getString(names, 14, 20)
                        .concat(getString(names, 20, 21)));

                numberField9.setTitle(getString(names, 27, 31)
                        .concat(getString(names, 31, 33))
                        .concat(getString(names, 33, 35)));
            }
        }
    }

    private void loadUserComboBox() {
        if (nameAndSurname().size() > 0) {
            comboBoxUsers.setItems(nameAndSurname());
        }
    }

    private List<String> nameAndSurname() {
        Iterable<EntityUser> allUsersFromRepository = usersRepo.findAll();
        List<String> nameAndSurname = new ArrayList<>();
        allUsersFromRepository.forEach(user -> nameAndSurname.add(user.getName().concat(" ").concat(user.getSurname())));
        return nameAndSurname;
    }

    private NumberField getCustomerDiscount() {
        customerDiscount.setValue(0d);
        customerDiscount.setMin(0);
        customerDiscount.setMax(30);
        customerDiscount.setHasControls(true);
        customerDiscount.setSuffixComponent(new Span("%"));
        return customerDiscount;
    }

    private List<String> getSubList(List<String> nameList, int begin, int end) {
        return nameList.subList(begin, end);
    }

    private void setValues(NumberField numberField, String unit, Double defaultValue) {
        numberField.setValue(defaultValue);
        numberField.setMin(0);
        numberField.setMax(500);
        numberField.setHasControls(true);
        numberField.setSuffixComponent(new Span(unit));
    }

    private void loadUser() {
        String nameISurname = comboBoxUsers.getValue();
        String[] strings = nameISurname.split(" ");
        EntityUser entityUser = usersRepo.findUsersEntityByNameAndSurnameEquals(strings[0], strings[1]);

        if (saveInputData() != null) {
            entityUser.setEntityInputDataTiles(saveInputData());
            entityUser.setHasTiles(true);
            usersRepo.save(entityUser);
            getNotificationSucces("Dachówki zapisane");
        } else {
            getNotificationError("Dachówki nizapisane");
        }
    }

    private VerticalLayout createGrid() {
        grid.getColumnByKey("priceListName").setHeader("Nazwa Cennika");
        grid.getColumnByKey("type").setHeader("Typ dachówki");
        grid.getColumnByKey("name").setHeader("Kategoria");
        grid.getColumnByKey("unitRetailPrice").setHeader("Cena detaliczna").setWidth("30px");
        grid.getColumnByKey("profit").setHeader("Marża").setWidth("30px");
        grid.getColumnByKey("basicDiscount").setHeader("Rabat podstawowy").setWidth("30px");
        grid.getColumnByKey("supplierDiscount").setHeader("Promocja").setWidth("30px");
        grid.getColumnByKey("additionalDiscount").setHeader("Rabat dodatkowy").setWidth("30px");
        grid.getColumnByKey("skontoDiscount").setHeader("Skonto").setWidth("30px");
        grid.removeColumnByKey("id");
        grid.setItems(allTilesFromRespository());
        cennik.add(grid);
        return cennik;
    }

    @Override
    public MenuBar menu() {
        MenuBar menuBar = new MenuBar();
        menuBar.addItem("Pokaż tabelę", e -> {
            tabela.setVisible(true);
            resultTilesGrid.setVisible(true);
            dane.setVisible(false);
            cennik.setVisible(false);
        });
        menuBar.addItem("Wprowadź dane", e -> {
            tabela.setVisible(false);
            resultTilesGrid.setVisible(false);
            dane.setVisible(true);
            cennik.setVisible(false);
        });
        menuBar.addItem("Cennik", e -> {
            tabela.setVisible(false);
            resultTilesGrid.setVisible(false);
            dane.setVisible(false);
            cennik.setVisible(true);
        });
        menuBar.addItem(new RouterLink("Kolejne dane", SelectAccesories.class));
        return menuBar;
    }
}
