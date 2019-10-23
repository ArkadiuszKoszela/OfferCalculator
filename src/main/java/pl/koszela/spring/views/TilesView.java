package pl.koszela.spring.views;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.service.AvailablePriceList;
import pl.koszela.spring.entities.*;
import pl.koszela.spring.repositories.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Route(value = TilesView.ENTER_TILES, layout = MainView.class)
public class TilesView extends VerticalLayout implements BeforeLeaveObserver {

    static final String ENTER_TILES = "tiles";

    private TilesRepository tilesRepository;
    private AvailablePriceList availablePriceList;

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

    NumberField gutter1 = new NumberField("Rynna");
    NumberField gutter2 = new NumberField("Sztucer");
    NumberField gutter3 = new NumberField("Denko");
    NumberField gutter4 = new NumberField("Złączka rynny");
    NumberField gutter5 = new NumberField("Narożnik wew.");
    NumberField gutter6 = new NumberField("Narożnik zew.");

    List<NumberField> listInputGutter = new ArrayList<>();

    private ComboBox<String> comboBoxInput = new ComboBox<>("Podaj nazwę cennika: ");

    private Set<Tiles> set = (Set<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("allTilesFromRepo");
    private EntityInputDataTiles entityInputDataTilesFromRepo = (EntityInputDataTiles) VaadinSession.getCurrent().getSession().getAttribute("tilesInputFromRepo");

    private List<NumberField> listOfNumberFields = new ArrayList<>();

    private VerticalLayout dane = new VerticalLayout();
    private Set<EntityAccesories> setAccesories = (Set<EntityAccesories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");

    @Autowired
    public TilesView(AvailablePriceList availablePriceList, TilesRepository tilesRepository) {
        this.availablePriceList = Objects.requireNonNull(availablePriceList);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);

        add(createInputFields());
    }

    private ComboBox<String> getAvailablePriceList() {
        List<String> available = availablePriceList.getAvailablePriceList();
        comboBoxInput.setItems(available);
        return comboBoxInput;
    }

    private VerticalLayout createInputFields() {
        FormLayout formLayout = new FormLayout();
        Span title = new Span("Dachówki");
        Span title2 = new Span("Rynny");
        FormLayout.ResponsiveStep responsiveStep = new FormLayout.ResponsiveStep("5px", 6);
        formLayout.setResponsiveSteps(responsiveStep);
        FormLayout gutterInput = new FormLayout();
        FormLayout.ResponsiveStep responsiveStep1 = new FormLayout.ResponsiveStep("5px", 6);
        gutterInput.setResponsiveSteps(responsiveStep1);
        formLayout.add(getCustomerDiscount(), getAvailablePriceList());
        setDefaultValuesFromRepo();
        listOfNumberFields.forEach(formLayout::add);
        listInputGutter.forEach(gutterInput::add);
        dane.add(title);
        dane.add(formLayout);
        dane.add(title2);
        dane.add(gutterInput);
        return dane;
    }

    private Set<Tiles> listResultTilesFromRepo() {
        if (set != null) {
            Set<Tiles> parents = set.stream().filter(e -> e.getName().equals(CategoryTiles.DACHOWKA_PODSTAWOWA.toString())).collect(Collectors.toSet());
            for (Tiles parent : parents) {
                List<Tiles> childrens = set.stream().filter(e -> e.getPriceListName().equals(parent.getPriceListName())).collect(Collectors.toList());
                List<NumberField> collect = listOfNumberFields.stream().filter(e -> e.getPattern().equals(parent.getName())).collect(Collectors.toList());
                parent.setQuantity(collect.get(0).getValue());
                listOfNumberFields.forEach(e -> childrens.forEach(f -> {
                    if (e.getPattern().equals(f.getName())) {
                        f.setQuantity(e.getValue());
                    }
                }));
            }
        } else {
            List<Tiles> allFromRepo = tilesRepository.findAll();
            set = new HashSet<>(allFromRepo);
            Set<Tiles> parents = set.stream().filter(e -> e.getName().equals(CategoryTiles.DACHOWKA_PODSTAWOWA.toString())).collect(Collectors.toSet());
            for (Tiles parent : parents) {
                List<Tiles> childrens = set.stream().filter(e -> e.getPriceListName().equals(parent.getPriceListName())).collect(Collectors.toList());
                List<NumberField> fields = listOfNumberFields.stream().filter(e -> e.getPattern().equals(parent.getName())).collect(Collectors.toList());
                parent.setQuantity(fields.get(0).getValue());
                parent.setTotalProfit(new BigDecimal(0));
                parent.setDiscount(0);
                parent.setTotalPrice(new BigDecimal(0));
                parent.setAllpriceAfterDiscount(new BigDecimal(0));
                parent.setAllpricePurchase(new BigDecimal(0));
                parent.setAllprofit(new BigDecimal(0));
                parent.setOption(false);
                parent.setMain(false);

                listOfNumberFields.forEach(field -> childrens.forEach(children -> {
                    if (field.getPattern().equals(children.getName())) {
                        children.setQuantity(field.getValue());
                        children.setTotalProfit(new BigDecimal(0));
                        children.setDiscount(0);
                        children.setTotalPrice(new BigDecimal(0));
                        children.setAllpriceAfterDiscount(new BigDecimal(0));
                        children.setAllpricePurchase(new BigDecimal(0));
                        children.setAllprofit(new BigDecimal(0));
                        children.setOption(false);
                        children.setMain(false);
                    }
                }));
            }
        }
        return set;
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

    private void setDefaultValuesFromRepo() {
//        if(set != null) {
        setValues(numberField1, "m²", entityInputDataTilesFromRepo.getPowierzchniaPolaci(), CategoryTiles.DACHOWKA_PODSTAWOWA.toString());
        setValues(numberField2, "mb", entityInputDataTilesFromRepo.getDlugoscKalenic(), CategoryTiles.DACHOWKA_SKRAJNA_LEWA.toString());
        setValues(numberField3, "mb", entityInputDataTilesFromRepo.getDlugoscKalenicProstych(), CategoryTiles.DACHOWKA_SKRAJNA_PRAWA.toString());
        setValues(numberField4, "mb", entityInputDataTilesFromRepo.getDlugoscKalenicSkosnych(), CategoryTiles.DACHOWKA_POLOWKOWA.toString());
        setValues(numberField5, "mb", entityInputDataTilesFromRepo.getDlugoscKoszy(), CategoryTiles.DACHOWKA_WENTYLACYJNA.toString());
        setValues(numberField6, "mb", entityInputDataTilesFromRepo.getDlugoscKrawedziLewych(), CategoryTiles.KOMPLET_KOMINKA_WENTYLACYJNEGO.toString());
        setValues(numberField7, "mb", entityInputDataTilesFromRepo.getDlugoscKrawedziPrawych(), CategoryTiles.GASIOR_PODSTAWOWY.toString());
        setValues(numberField8, "mb", entityInputDataTilesFromRepo.getObwodKomina(), CategoryTiles.GASIOR_POCZATKOWY_KALENICA_PROSTA.toString());
        setValues(numberField9, "mb", entityInputDataTilesFromRepo.getDlugoscOkapu(), CategoryTiles.GASIOR_KONCOWY_KALENICA_PROSTA.toString());
        setValues(numberField10, "szt", entityInputDataTilesFromRepo.getDachowkaWentylacyjna(), CategoryTiles.PLYTKA_POCZATKOWA.toString());
        setValues(numberField11, "szt", entityInputDataTilesFromRepo.getKompletKominkaWentylacyjnego(), CategoryTiles.PLYTKA_KONCOWA.toString());
        setValues(numberField12, "szt", entityInputDataTilesFromRepo.getGasiarPoczatkowyKalenicaProsta(), CategoryTiles.TROJNIK.toString());
        setValues(numberField13, "mb", entityInputDataTilesFromRepo.getGasiarKoncowyKalenicaProsta(), CategoryTiles.GASIAR_ZAOKRAGLONY.toString());
        setValues(numberField14, "mb", entityInputDataTilesFromRepo.getGasiarZaokraglony(), "brak");
        setValues(numberField15, "mb", entityInputDataTilesFromRepo.getTrojnik(), "brak");
        setValues(numberField16, "szt", entityInputDataTilesFromRepo.getCzwornik(), "brak");
        setValues(numberField17, "szt", entityInputDataTilesFromRepo.getGasiarZPodwojnaMufa(), "brak");
        setValues(numberField18, "mb", entityInputDataTilesFromRepo.getDachowkaDwufalowa(), "brak");
        setValues(numberField19, "szt", entityInputDataTilesFromRepo.getOknoPolaciowe(), "brak");

        setValues(gutter1, "mb", 30d, "brak");
        setValues(gutter2, "szt", 3d, "brak");
        setValues(gutter3, "szt", 5d, "brak");
        setValues(gutter4, "szt", 2d, "brak");
        setValues(gutter5, "szt", 1d, "brak");
        setValues(gutter6, "szt", 23d, "brak");

        getListNumberFields();
        getListInputGutter();
//        }else{
//            getNotificationError("Przejdź do zakładki 'Klienci' aby załadować dane");
//        }
    }

    private void getListInputGutter() {
        listInputGutter = Arrays.asList(gutter1, gutter2, gutter3, gutter4, gutter5, gutter6);
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

    private void setValues(NumberField numberField, String unit, Double valueFromRepo, String pattern) {
        numberField.setValue(valueFromRepo);
        numberField.setPattern(pattern);
        numberField.setMin(0);
        numberField.setMax(500);
        numberField.setAutoselect(true);
        numberField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        numberField.setSuffixComponent(new Span(unit));
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        VaadinSession.getCurrent().getSession().setAttribute("allTilesFromRepo", listResultTilesFromRepo());
        VaadinSession.getCurrent().getSession().setAttribute("tilesInputFromRepo", saveInputData());
    }
}