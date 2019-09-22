package pl.koszela.spring.views;

import com.google.common.base.Joiner;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.calculate.CalculateTiles;
import pl.koszela.spring.entities.*;
import pl.koszela.spring.inputFields.ServiceNotification;
import pl.koszela.spring.repositories.*;
import pl.koszela.spring.service.MenuBarInterface;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static pl.koszela.spring.service.Labels.getLabel;
import static pl.koszela.spring.views.SelectAccesories.SELECT_ACCESORIES;

@Route(value = EnterTiles.ENTER_TILES, layout = MainView.class)
public class EnterTiles extends VerticalLayout implements MenuBarInterface {

    public static final String ENTER_TILES = "tiles";

    private AccesoriesRepository accesoriesRepository;
    private InputDataTilesRepository inputDataTilesRepository;
    private CalculateTiles calculateTiles;
    private TilesRepository tilesRepository;

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

    private List<NumberField> listOfNumberFields = new ArrayList<>();
    private List<Double> listValueOfNumberFields = new ArrayList<>();

    private VerticalLayout dane = new VerticalLayout();
    private VerticalLayout cennik = new VerticalLayout();

    private Grid<EntityTiles> grid = new Grid<>(EntityTiles.class);

    @Autowired
    public EnterTiles(AccesoriesRepository accesoriesRepository, InputDataTilesRepository inputDataTilesRepository,
                      CalculateTiles calculateTiles, TilesRepository tilesRepository) {
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);
        this.inputDataTilesRepository = Objects.requireNonNull(inputDataTilesRepository);
        this.calculateTiles = Objects.requireNonNull(calculateTiles);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);

        add(menu());
        add(createInputFields());
        add(createGrid());
    }

    private void getAvailablePriceList(ComboBox<String> comboBox) {
        if (!calculateTiles.getAvailablePriceList().isEmpty()) {
            VaadinSession.getCurrent().setAttribute("availablePriceList", calculateTiles.getAvailablePriceList());
            List list = (List) VaadinSession.getCurrent().getAttribute("availablePriceList");
            comboBox.setItems(list);
        }
    }

    private VerticalLayout createInputFields() {
        setDefaultValues();
        FormLayout formLayout = new FormLayout();
        FormLayout.ResponsiveStep responsiveStep = new FormLayout.ResponsiveStep("5px", 6);
        formLayout.setResponsiveSteps(responsiveStep);
        formLayout.add(getCustomerDiscount(), comboBoxInput);
        listOfNumberFields.forEach(formLayout::add);
        getAvailablePriceList(comboBoxInput);
        dane.add(formLayout);
        return dane;
    }

    private List<EntityTiles> listResultTiles() {
        String[] spliString = comboBoxInput.getValue().split(" ");

        List<EntityTiles> priceListFromRepository = tilesRepository.findByPriceListNameAndType(spliString[0] + " " + spliString[1] + " " + spliString[2], spliString[3] + " " + spliString[4]);
        priceListFromRepository.forEach(e -> e.setPriceListName(comboBoxInput.getValue()));

        calculateTiles.getRetail(priceListFromRepository, customerDiscount, listValueOfNumberFields);
        calculateTiles.getPurchase(priceListFromRepository, listValueOfNumberFields);
        calculateTiles.getProfit(priceListFromRepository);

        tilesRepository.saveAll(priceListFromRepository);
        return priceListFromRepository;
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
        getListNumberFields();
        getListValueOfNumberFields();
    }

    private void getListNumberFields() {
        listOfNumberFields = Arrays.asList(numberField1, numberField2, numberField3, numberField4, numberField5, numberField6, numberField7,
                numberField8, numberField9, numberField10, numberField11, numberField12, numberField13, numberField14, numberField15, numberField16,
                numberField17, numberField18, numberField19);
    }

    private void getListValueOfNumberFields() {
        listOfNumberFields.forEach(e -> listValueOfNumberFields.add(e.getValue()));
    }

    private NumberField getCustomerDiscount() {
        customerDiscount.setValue(0d);
        customerDiscount.setMin(0);
        customerDiscount.setMax(30);
        customerDiscount.setHasControls(true);
        customerDiscount.setSuffixComponent(new Span("%"));
        return customerDiscount;
    }

    private void setValues(NumberField numberField, String unit, Double defaultValue) {
        numberField.setValue(defaultValue);
        numberField.setMin(0);
        numberField.setMax(500);
        numberField.setSuffixComponent(new Span(unit));
    }

    private void loadUser() {
        EntityUser entityUser = (EntityUser) VaadinSession.getCurrent().getAttribute("user");
        EntityInputDataTiles result = saveInputData();
        if (result != null) {
            entityUser.setEntityInputDataTiles(result);
            entityUser.setEntityTiles(listResultTiles());
            entityUser.setHasTiles(true);
            ServiceNotification.getNotificationSucces("Dachówki zapisane");
        } else {
            ServiceNotification.getNotificationError("Dachówki nizapisane");
        }
    }

    private VerticalLayout createGrid() {
        grid.getColumnByKey("priceListName").setHeader("Nazwa Cennika");
        grid.getColumnByKey("type").setHeader("Typ dachówki");
        grid.getColumnByKey("name").setHeader("Kategoria");
        grid.getColumnByKey("unitRetailPrice").setHeader("Cena detaliczna");
        grid.getColumnByKey("profit").setHeader("Marża");
        grid.getColumnByKey("basicDiscount").setHeader("Rabat podstawowy");
        grid.getColumnByKey("supplierDiscount").setHeader("Promocja");
        grid.getColumnByKey("additionalDiscount").setHeader("Rabat dodatkowy");
        grid.getColumnByKey("skontoDiscount").setHeader("Skonto");
        grid.removeColumnByKey("id");
        grid.setItems(allTilesFromRespository());
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        cennik.add(grid);
        return cennik;
    }

    @Override
    public MenuBar menu() {
        MenuBar menuBar = new MenuBar();
        menuBar.addItem("Wprowadź dane", e -> {
            dane.setVisible(true);
            cennik.setVisible(false);
        });
        menuBar.addItem("Cennik", e -> {
            dane.setVisible(false);
            cennik.setVisible(true);
        });
        Button button = new Button("Dalej");
        button.addClickListener(buttonClickEvent -> {
            loadUser();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getUI().ifPresent(ui -> ui.navigate(SELECT_ACCESORIES));
        });
        menuBar.addItem(button);
        return menuBar;
    }
}
