package pl.koszela.spring.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.calculate.CalculateTiles;
import pl.koszela.spring.entities.*;
import pl.koszela.spring.repositories.*;

import java.util.*;

import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationSucces;

@Route(value = TilesView.ENTER_TILES, layout = MainView.class)
public class TilesView extends VerticalLayout {

    static final String ENTER_TILES = "tiles";

    private TilesRepository tilesRepository;
    private CalculateTiles calculateTiles;

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

    private ComboBox<String> comboBoxInput = new ComboBox<>("Podaj nazwę cennika: ");

    private List<NumberField> listOfNumberFields = new ArrayList<>();

    private VerticalLayout dane = new VerticalLayout();
    private VerticalLayout cennik = new VerticalLayout();

    private Grid<Tiles> grid = new Grid<>(Tiles.class);

    @Autowired
    public TilesView(CalculateTiles calculateTiles, TilesRepository tilesRepository) {
        this.calculateTiles = Objects.requireNonNull(calculateTiles);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);

        add(createInputFields());
        add(createGrid());
        UI.getCurrent().addBeforeLeaveListener(e -> {
            VaadinSession.getCurrent().setAttribute("tilesInput", saveInputData());
            VaadinSession.getCurrent().setAttribute("resultTiles", listResultTiles());
            getNotificationSucces("Tiles save");
        });
    }

    private void getAvailablePriceList(ComboBox<String> comboBox) {
        Object object = VaadinSession.getCurrent().getAttribute("availablePriceList");
        if (!Objects.isNull(object)) {
            List<String> list = (List<String>) VaadinSession.getCurrent().getAttribute("availablePriceList");
            comboBox.setItems(list);
        }
        List<String> available = calculateTiles.getAvailablePriceList();
        if (Objects.isNull(object) && !available.isEmpty()) {
            comboBox.setItems(available);
            VaadinSession.getCurrent().setAttribute("availablePriceList", available);
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

    private List<List<Tiles>> listResultTiles() {
        Set<Tiles> set = (Set<Tiles>) VaadinSession.getCurrent().getAttribute("allTilesFromRepo");
        List<List<Tiles>> listToTreeGrid = new ArrayList<>();
        if (set != null) {
            List<Tiles> parents = getParents();
            List<Tiles> childrens = getChildrens();
            for (Tiles tileParent : parents) {
                List<Tiles> oneOf = findChildrens(tileParent, childrens);
                for (Tiles tileChildren : oneOf) {
                    for (NumberField numberField : listOfNumberFields) {
                        if (tileParent.getName().equals(numberField.getPattern())) {
                            tileParent.setQuantity(numberField.getValue());
                        } else if (tileChildren.getName().equals(numberField.getPattern())) {
                            tileChildren.setQuantity(numberField.getValue());
                        }
                    }
                }
                oneOf.add(tileParent);
                listToTreeGrid.add(oneOf);
            }
            return listToTreeGrid;
        } else {
            List<String> availablePriceList = calculateTiles.getAvailablePriceList();
            for (String priceListName : availablePriceList) {
                List<Tiles> priceListFromRepository = tilesRepository.findByPriceListNameEquals(priceListName);
                for (Tiles tile : priceListFromRepository) {
                    for (NumberField listOfNumberField : listOfNumberFields) {
                        if (tile.getName().equals(listOfNumberField.getPattern())) {
                            tile.setQuantity(listOfNumberField.getValue());
                        }
                    }
                }
                listToTreeGrid.add(priceListFromRepository);
            }
            return listToTreeGrid;
        }
    }

    private List<Tiles> findChildrens(Tiles parent, List<Tiles> childrens) {
        List<Tiles> oneOfchildrens = new ArrayList<>();
        for (Tiles children : childrens) {
            if (children.getPriceListName().equals(parent.getPriceListName())) {
                oneOfchildrens.add(children);
            }
        }
        return oneOfchildrens;
    }

    private List<Tiles> getChildrens() {
        Set<Tiles> set = (Set<Tiles>) VaadinSession.getCurrent().getAttribute("allTilesFromRepo");
        List<Tiles> list = new ArrayList<>(set);
        List<Tiles> childrens = new ArrayList<>();
        if (list.size() > 0) {
            List<Tiles> parents = getParents();
            list.forEach(e -> {
                for (Tiles tiles : parents) {
                    if (e.getPriceListName().equals(tiles.getPriceListName())) {
                        childrens.add(e);
                    }
                }
            });
        }
        return childrens;
    }

    private List<Tiles> getParents() {
        Set<Tiles> set = (Set<Tiles>) VaadinSession.getCurrent().getAttribute("allTilesFromRepo");
        List<Tiles> list = new ArrayList<>(set);
        List<Tiles> parents = new ArrayList<>();
        if (list.size() > 0) {
            list.forEach(e -> {
                if (e.getName().equals(Category.DACHOWKA_PODSTAWOWA.toString())) {
                    parents.add(e);
                }
            });
        }
        return parents;
    }

    private List<Tiles> allTilesFromRespository() {
        Iterable<Tiles> allTilesFromRepository = tilesRepository.findAll();
        List<Tiles> allTiles = new ArrayList<>();
        allTilesFromRepository.forEach(allTiles::add);
        return allTiles;
    }

    private EntityInputDataTiles saveInputData() {
        return EntityInputDataTiles.builder()
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
    }

    private void setDefaultValues() {
        EntityInputDataTiles entityInputDataTiles = (EntityInputDataTiles) VaadinSession.getCurrent().getAttribute("tilesInputFromRepo");
        if (entityInputDataTiles != null) {
            setValues(numberField1, "m²", entityInputDataTiles.getPowierzchniaPolaci(), Category.DACHOWKA_PODSTAWOWA.toString());
            setValues(numberField2, "mb", entityInputDataTiles.getDlugoscKalenic(), Category.DACHOWKA_SKRAJNA_LEWA.toString());
            setValues(numberField3, "mb", entityInputDataTiles.getDlugoscKalenicProstych(), Category.DACHOWKA_SKRAJNA_PRAWA.toString());
            setValues(numberField4, "mb", entityInputDataTiles.getDlugoscKalenicSkosnych(), Category.DACHOWKA_POLOWKOWA.toString());
            setValues(numberField5, "mb", entityInputDataTiles.getDlugoscKoszy(), Category.DACHOWKA_WENTYLACYJNA.toString());
            setValues(numberField6, "mb", entityInputDataTiles.getDlugoscKrawedziLewych(), Category.KOMPLET_KOMINKA_WENTYLACYJNEGO.toString());
            setValues(numberField7, "mb", entityInputDataTiles.getDlugoscKrawedziPrawych(), Category.GASIOR_PODSTAWOWY.toString());
            setValues(numberField8, "mb", entityInputDataTiles.getObwodKomina(), Category.GASIOR_POCZATKOWY_KALENICA_PROSTA.toString());
            setValues(numberField9, "mb", entityInputDataTiles.getDlugoscOkapu(), Category.GASIOR_KONCOWY_KALENICA_PROSTA.toString());
            setValues(numberField10, "szt", entityInputDataTiles.getDachowkaWentylacyjna(), Category.PLYTKA_POCZATKOWA.toString());
            setValues(numberField11, "szt", entityInputDataTiles.getKompletKominkaWentylacyjnego(), Category.PLYTKA_KONCOWA.toString());
            setValues(numberField12, "szt", entityInputDataTiles.getGasiarPoczatkowyKalenicaProsta(), Category.TROJNIK.toString());
            setValues(numberField13, "mb", entityInputDataTiles.getGasiarKoncowyKalenicaProsta(), Category.GASIAR_ZAOKRAGLONY.toString());
            setValues(numberField14, "mb", entityInputDataTiles.getGasiarZaokraglony(), "brak");
            setValues(numberField15, "mb", entityInputDataTiles.getTrojnik(), "brak");
            setValues(numberField16, "szt", entityInputDataTiles.getCzwornik(), "brak");
            setValues(numberField17, "szt", entityInputDataTiles.getGasiarZPodwojnaMufa(), "brak");
            setValues(numberField18, "mb", entityInputDataTiles.getDachowkaDwufalowa(), "brak");
            setValues(numberField19, "szt", entityInputDataTiles.getOknoPolaciowe(), "brak");
        } else {
            setValues(numberField1, "m²", 13d, Category.DACHOWKA_PODSTAWOWA.toString());
            setValues(numberField2, "mb", 65d, Category.DACHOWKA_SKRAJNA_LEWA.toString());
            setValues(numberField3, "mb", 65d, Category.DACHOWKA_SKRAJNA_PRAWA.toString());
            setValues(numberField4, "mb", 1d, Category.DACHOWKA_POLOWKOWA.toString());
            setValues(numberField5, "mb", 8d, Category.DACHOWKA_WENTYLACYJNA.toString());
            setValues(numberField6, "mb", 5d, Category.KOMPLET_KOMINKA_WENTYLACYJNEGO.toString());
            setValues(numberField7, "mb", 5d, Category.GASIOR_PODSTAWOWY.toString());
            setValues(numberField8, "mb", 3d, Category.GASIOR_POCZATKOWY_KALENICA_PROSTA.toString());
            setValues(numberField9, "mb", 38d, Category.GASIOR_KONCOWY_KALENICA_PROSTA.toString());
            setValues(numberField10, "szt", 1d, Category.PLYTKA_POCZATKOWA.toString());
            setValues(numberField11, "szt", 1d, Category.PLYTKA_KONCOWA.toString());
            setValues(numberField12, "szt", 1d, Category.TROJNIK.toString());
            setValues(numberField13, "mb", 1d, Category.GASIAR_ZAOKRAGLONY.toString());
            setValues(numberField14, "mb", 6d, "brak");
            setValues(numberField15, "szt", 1d, "brak");
            setValues(numberField16, "szt", 1d, "brak");
            setValues(numberField17, "mb", 1d, "brak");
            setValues(numberField18, "szt", 1d, "brak");
            setValues(numberField19, "szt", 1d, "brak");
        }
        getListNumberFields();
    }

    private void getListNumberFields() {
        listOfNumberFields = Arrays.asList(numberField1, numberField2, numberField3, numberField4, numberField5, numberField6, numberField7,
                numberField8, numberField9, numberField10, numberField11, numberField12, numberField13, numberField14, numberField15, numberField16,
                numberField17, numberField18, numberField19);
    }

    private NumberField getCustomerDiscount() {
        customerDiscount.setValue(0d);
        customerDiscount.setMin(0);
        customerDiscount.setMax(30);
        customerDiscount.setHasControls(true);
        customerDiscount.setSuffixComponent(new Span("%"));
        return customerDiscount;
    }

    private void setValues(NumberField numberField, String unit, Double defaultValue, String pattern) {
        numberField.setPattern(pattern);
        numberField.setValue(defaultValue);
        numberField.setMin(0);
        numberField.setMax(500);
        numberField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        numberField.setSuffixComponent(new Span(unit));
    }

    private VerticalLayout createGrid() {
        grid.getColumnByKey("priceListName").setHeader("Nazwa Cennika");
        grid.getColumnByKey("name").setHeader("Nazwa");
        grid.getColumnByKey("price").setHeader("Cena");
        grid.removeColumnByKey("id");
        grid.removeColumnByKey("userTiles");
        grid.removeColumnByKey("quantity");
        grid.removeColumnByKey("discount");
        grid.removeColumnByKey("priceAfterDiscount");
        grid.removeColumnByKey("pricePurchase");
        grid.removeColumnByKey("profit");
        grid.removeColumnByKey("totalPrice");
        grid.removeColumnByKey("totalProfit");
        grid.setItems(allTilesFromRespository());
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        cennik.add(grid);
        return cennik;
    }
}