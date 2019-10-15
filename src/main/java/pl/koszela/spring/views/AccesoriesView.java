package pl.koszela.spring.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.EntityAccesories;
import pl.koszela.spring.entities.EntityInputDataTiles;
import pl.koszela.spring.entities.EntityResultAccesories;
import pl.koszela.spring.repositories.AccesoriesRepository;
import pl.koszela.spring.repositories.InputDataAccesoriesRespository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationError;
import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationSucces;


@Route(value = AccesoriesView.SELECT_ACCESORIES, layout = MainView.class)
public class AccesoriesView extends VerticalLayout implements BeforeLeaveObserver {

    static final String SELECT_ACCESORIES = "accesories";
    private AccesoriesRepository accesoriesRepository;
    private InputDataAccesoriesRespository inputDataAccesoriesRespository;

//    private ComboBox<String> comboBoxtasmaKalenicowa = new ComboBox<>(TASMA_KELNICOWA);
//    private ComboBox<String> comboBoxwspornikLatyKalenicowej = new ComboBox<>(WSPORNIK_LATY_KALENICOWEJ);
//    private ComboBox<String> comboBoxtasmaDoObrobkiKomina = new ComboBox<>(TASMA_DO_OBROBKI_KOMINA);
//    private ComboBox<String> comboBoxlistwaWykonczeniowaAluminiowa = new ComboBox<>(LISTWA_WYKONCZENIOWA_ALUMINIOWA);
//    private ComboBox<String> comboBoxkoszDachowyAluminiowy2mb = new ComboBox<>(KOSZ_DACHOWY_ALUMINIOWY_2MB);
//    private ComboBox<String> comboBoxklamraDoMocowaniaKosza = new ComboBox<>(KLAMRA_DO_MOCOWANIA_KOSZA);
//    private ComboBox<String> comboBoxklinUszczelniajacyKosz = new ComboBox<>(KLIN_USZCZELNIAJACY_KOSZ);
//    private ComboBox<String> comboBoxgrzebienOkapowy = new ComboBox<>(GRZEBIEN_OKAPOWY);
//    private ComboBox<String> comboBoxkratkaZabezpieczajacaPrzedPtactwem = new ComboBox<>(KRATKA_ZABEZPIECZAJACA_PRZED_PTACTWEM);
//    private ComboBox<String> comboBoxpasOkapowy = new ComboBox<>(PAS_OKAPOWY);
//    private ComboBox<String> comboBoxklamraDoGasiora = new ComboBox<>(KLAMRA_DO_GASIORA);
//    private ComboBox<String> comboBoxspinkaDoDachowki = new ComboBox<>(SPINKA_DO_DACHOWKI);
//    private ComboBox<String> comboBoxspinkaDoDachowkiCietej = new ComboBox<>(SPINKA_DO_DACHOWKI_CIETEJ);
//    private ComboBox<String> comboBoxlawaKominiarska = new ComboBox<>(LAWA_KOMINIARSKA);
//    private ComboBox<String> comboBoxstopienKominiarski = new ComboBox<>(STOPIEN_KOMINIARSKI);
//    private ComboBox<String> comboBoxplotekPrzeciwsniegowy155mmx2mb = new ComboBox<>(PLOTEK_PRZECIWSNIEGOWY_155MMX2MB);
//    private ComboBox<String> comboBoxplotekPrzeciwsniegowy155mmx3mb = new ComboBox<>(PLOTEK_PRZECIWSNIEGOWY_155MMX3MB);
//    private ComboBox<String> comboBoxmembranaDachowa = new ComboBox<>(MEMBRANA_DACHOWA);
//    private ComboBox<String> comboBoxtasmaDoLaczeniaMembarnIFolii = new ComboBox<>(TASMA_DO_LACZENIA_MEMBRAN_I_FOLII);
//    private ComboBox<String> comboBoxtasmaReparacyjna = new ComboBox<>(TASMA_REPARACYJNA);
//    private ComboBox<String> comboBoxblachaAluminiowa = new ComboBox<>(BLACHA_ALUMINIOWA);
//    private ComboBox<String> comboBoxceglaKlinkierowa = new ComboBox<>(CEGLA_KLINKIEROWA);

    private NumberField numberField1 = new NumberField("Ilość");
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
    private NumberField numberField20 = new NumberField("Okno połaciowe");
    private NumberField numberField21 = new NumberField("Okno połaciowe");
    private NumberField numberField22 = new NumberField("Okno połaciowe");

    //    private List<ComboBox<String>> boxList = new ArrayList<>();
    private List<NumberField> listOfNumberFields = new ArrayList<>();
    private List<Double> valuesFromRepo;
    //    private List<Double> valuePriceAccesories = new ArrayList<>();
    private EntityInputDataTiles dataTiles = (EntityInputDataTiles) VaadinSession.getCurrent().getSession().getAttribute("tilesInput");
    private EntityInputDataTiles dataTilesRepo = (EntityInputDataTiles) VaadinSession.getCurrent().getSession().getAttribute("tilesInputFromRepo");
    private Set<EntityResultAccesories> set = new HashSet<>();
    private Set<EntityResultAccesories> setFromRepo = (Set<EntityResultAccesories>) VaadinSession.getCurrent().getSession().getAttribute("accesoriesFromRepo");

//    private FormLayout board = new FormLayout();


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        /*setFromRepo = (Set<EntityResultAccesories>) VaadinSession.getCurrent().getSession().getAttribute("accesoriesFromRepo");*/
        if (dataTilesRepo != null || dataTiles == null) {
            dataTilesRepo = (EntityInputDataTiles) VaadinSession.getCurrent().getSession().getAttribute("tilesInputFromRepo");
            getNotificationSucces("WEJSCIE Akcesoria - wszystko ok (repo)");
        } else if (dataTilesRepo == null || dataTiles != null) {
            dataTiles = (EntityInputDataTiles) VaadinSession.getCurrent().getSession().getAttribute("tilesInput");
            getNotificationSucces("WEJSCIE Akcesoria - wszystko ok (bez repo)");
        } else {
            getNotificationError("WEJSCIE Dachówki - coś poszło nie tak");
        }
    }

    @Autowired
    public AccesoriesView(AccesoriesRepository accesoriesRepository,
                          InputDataAccesoriesRespository inputDataAccesoriesRespository) {
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);
        this.inputDataAccesoriesRespository = Objects.requireNonNull(inputDataAccesoriesRespository);

        getValuesTiles();
        /*add(formLayoutAccesories());*/
        add(addSubLayout("tasma kalenicowa", numberField2.getValue()));
        add(addSubLayout("wspornik", numberField2.getValue()));
        add(addSubLayout("tasma do obrobki", numberField2.getValue()));
        add(addSubLayout("listwa", numberField1.getValue()));
        add(addSubLayout("kosz", numberField1.getValue()));
        add(addSubLayout("klamra do mocowania kosza", numberField1.getValue()));
        add(addSubLayout("tasma samorozprezna", numberField1.getValue()));
        add(addSubLayout("grzebien", numberField1.getValue()));
        add(addSubLayout("kratka", numberField2.getValue()));
        add(addSubLayout("pas", numberField3.getValue()));
        add(addSubLayout("klamra do gasiora", numberField4.getValue()));
        add(addSubLayout("spinka", numberField5.getValue()));
        add(addSubLayout("spinka cieta", numberField6.getValue()));
        add(addSubLayout("lawa mniejsza", numberField7.getValue()));
        add(addSubLayout("lawa wieksza", numberField8.getValue()));
        add(addSubLayout("stopien", numberField9.getValue()));
        add(addSubLayout("plotek mniejszy", numberField10.getValue()));
        add(addSubLayout("plotek wiekszy", numberField11.getValue()));
        add(addSubLayout("membrana", numberField12.getValue()));
        add(addSubLayout("laczenie membran", numberField13.getValue()));
        add(addSubLayout("reparacyjna", numberField14.getValue()));
        add(addSubLayout("blacha", numberField15.getValue()));
        add(addSubLayout("cegla", numberField16.getValue()));
        add(addSubLayout("podbitka", numberField17.getValue()));
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        VaadinSession.getCurrent().getSession().setAttribute("accesories", set);
        if (dataTilesRepo != null || dataTiles == null) {
            VaadinSession.getCurrent().getSession().setAttribute("tilesInputFromRepo", saveInputData());
            getNotificationSucces("WYJSCIE Akcesoria - wszystko ok (repo)");
            action.proceed();
        } else if (dataTilesRepo == null || dataTiles != null) {
//            VaadinSession.getCurrent().getSession().setAttribute("accesoriesInput", saveInputDataAccesories());
            VaadinSession.getCurrent().getSession().setAttribute("tilesInput", saveInputData());
            getNotificationSucces("WYJSCIE Akcesoria - wszystko ok (bez repo)");
            action.proceed();
        } else {
            getNotificationError("WEJSCIE Dachówki - coś poszło nie tak");
            action.proceed();
        }
    }

    private Double value(String category) {
        if (dataTilesRepo != null) {
            dataTiles = dataTilesRepo;
        }
        switch (category) {
            case "wspornik":
                return BigDecimal.valueOf(dataTiles.getDlugoscKalenic() / 0.8).setScale(0, RoundingMode.UP).doubleValue();
            case "tasma kalenicowa":
                return BigDecimal.valueOf(dataTiles.getDlugoscKalenic()).setScale(0, RoundingMode.UP).doubleValue();
            case "tasma do obrobki":
                return BigDecimal.valueOf((dataTiles.getObwodKomina() + 1) * 2).setScale(0, RoundingMode.UP).doubleValue();
            case "listwa":
                return BigDecimal.valueOf((dataTiles.getObwodKomina() / 1.95) + 1).setScale(0, RoundingMode.UP).doubleValue();
            case "kosz":
                return BigDecimal.valueOf((dataTiles.getDlugoscKoszy() / 1.95) + 1).setScale(0, RoundingMode.UP).doubleValue();
            case "klamra do mocowania kosza":
                return BigDecimal.valueOf((dataTiles.getDlugoscKoszy() / 2) * 6).setScale(0, RoundingMode.UP).doubleValue();
            case "tasma samorozprezna":
                return BigDecimal.valueOf(dataTiles.getDlugoscKoszy() * 2).setScale(0, RoundingMode.UP).doubleValue();
            case "grzebien":
                return BigDecimal.valueOf(dataTiles.getDlugoscOkapu()).setScale(0, RoundingMode.UP).doubleValue();
            case "pas":
                return BigDecimal.valueOf(dataTiles.getDlugoscKalenic() / 1.95).setScale(0, RoundingMode.UP).doubleValue();
            case "klamra do gasiora":
                return BigDecimal.valueOf(dataTiles.getDlugoscKalenic() * 2.5).setScale(0, RoundingMode.UP).doubleValue();
            case "spinka":
                return BigDecimal.valueOf(dataTiles.getPowierzchniaPolaci() / 50).setScale(0, RoundingMode.UP).doubleValue();
            case "spinka cieta":
                return BigDecimal.valueOf(dataTiles.getDlugoscKoszy() * 0.6).setScale(0, RoundingMode.UP).doubleValue();
            case "membrana":
                return BigDecimal.valueOf(dataTiles.getPowierzchniaPolaci() * 1.1).setScale(0, RoundingMode.UP).doubleValue();
            default:
                return 1d;
        }
    }

    private FormLayout addSubLayout(String category, Double numberField) {
        FormLayout formLayout = new FormLayout();
        FormLayout.ResponsiveStep form = new FormLayout.ResponsiveStep("5px", 9);
        formLayout.setResponsiveSteps(form);
        TextArea name = new TextArea("Nazwa");
        name.setReadOnly(true);
        name.setPlaceholder(category);

        NumberField numberField23 = new NumberField("Ilość");
        numberField23.setValue(value(category));
        numberField23.setReadOnly(true);

        TextArea pricePurchase = new TextArea("Cena zakupu");
        pricePurchase.setReadOnly(true);
        pricePurchase.setPlaceholder(category);

        TextArea priceRetail = new TextArea("Cena detal");
        priceRetail.setPlaceholder(category);

        TextArea allPriceRetail = new TextArea("Cena razem netto");
        allPriceRetail.setReadOnly(true);
        allPriceRetail.setPlaceholder(category);

        TextArea allPricePurchase = new TextArea("Cena razem zakup");
        allPricePurchase.setReadOnly(true);
        allPricePurchase.setPlaceholder(category);

        TextArea profit = new TextArea("Zysk");
        profit.setReadOnly(true);
        profit.setPlaceholder(category);
        if (setFromRepo != null) {
            set.addAll(setFromRepo);
            Set<EntityResultAccesories> collect = set.stream().filter(e -> e.getCategory().equals(category)).collect(Collectors.toSet());
            for (EntityResultAccesories resultAccesories : collect) {
                name.setValue(resultAccesories.getName());
                pricePurchase.setValue(String.valueOf(resultAccesories.getPricePurchase()));
                priceRetail.setValue(String.valueOf(resultAccesories.getPriceRetail()));
                allPriceRetail.setValue(String.valueOf(resultAccesories.getAllPriceRetail()));
                allPricePurchase.setValue(String.valueOf(resultAccesories.getAllPricePurchase()));
                profit.setValue(String.valueOf(resultAccesories.getProfit()));
            }
        }
        priceRetail.addValueChangeListener(e -> {
            allPriceRetail.setValue(String.valueOf(numberField23.getValue() * Double.parseDouble(pricePurchase.getValue())));
            allPricePurchase.setValue(String.valueOf(numberField23.getValue() * Double.parseDouble(priceRetail.getValue())));
            profit.setValue(String.valueOf(Double.parseDouble(allPricePurchase.getValue()) - Double.parseDouble(allPriceRetail.getValue())));
        });
        Checkbox checkbox = new Checkbox("Dodać do oferty?");
        ComboBox<EntityAccesories> comboBox = new ComboBox<>("Wybierz");
        formLayout.add(comboBox, name, numberField23, pricePurchase, priceRetail, allPriceRetail, allPricePurchase, profit, checkbox);
        Iterable<EntityAccesories> accesoriesRepositoryAll = accesoriesRepository.findAllByCategoryEquals(category);
        List<EntityAccesories> all = new ArrayList<>();
        accesoriesRepositoryAll.forEach(all::add);

//        ComboBox.ItemFilter<EntityAccesories> filter = (song, filterString) ->
//                song.getName().toLowerCase()
//                        .contains(filterString.toLowerCase())
//                        || song.getCategory().toLowerCase()
//                        .contains(filterString.toLowerCase());

        comboBox.setItems(/*filter,*/ all);

        comboBox.setItemLabelGenerator(EntityAccesories::getName);

        /*comboBox.setRenderer(new ComponentRenderer<>(item -> {
            VerticalLayout container = new VerticalLayout();

            Label song = new Label(item.getName());
            container.add(song);

            Label artist = new Label(item.getCategory());
            artist.getStyle().set("fontSize", "smaller");
            container.add(artist);

            return container;
        }));*/

        comboBox.addValueChangeListener(event -> {
            if (event.getSource().isEmpty()) {
                comboBox.setValue(event.getValue());
            } else {
                EntityAccesories value = event.getValue();
                name.setValue(value.getName());
                pricePurchase.setValue(String.valueOf(value.getPurchasePrice()));
                double cenaDetal = (value.getPurchasePrice() * value.getMargin() / 100) + value.getPurchasePrice();
                BigDecimal bigDecimalPriceRetail = new BigDecimal(cenaDetal);
                priceRetail.setValue(String.valueOf(bigDecimalPriceRetail.setScale(2, RoundingMode.HALF_UP)));

                double val = numberField23.getValue() * Double.parseDouble(pricePurchase.getValue());
                BigDecimal bigDecimalallPriceRetail = new BigDecimal(val);
                allPriceRetail.setValue(String.valueOf(bigDecimalallPriceRetail.setScale(2, RoundingMode.HALF_UP)));

                double va = numberField23.getValue() * Double.parseDouble(priceRetail.getValue());
                BigDecimal bigDecimalAllPricePurchase = new BigDecimal(va);
                allPricePurchase.setValue(String.valueOf(bigDecimalAllPricePurchase.setScale(2, RoundingMode.HALF_UP)));

                double cena = Double.parseDouble(allPricePurchase.getValue()) - Double.parseDouble(allPriceRetail.getValue());
                BigDecimal bigDecimalProfit = new BigDecimal(cena);
                profit.setValue(String.valueOf(bigDecimalProfit.setScale(2, RoundingMode.HALF_UP)));

                EntityResultAccesories resultAccesories = new EntityResultAccesories();
                resultAccesories.setName(name.getValue());
                resultAccesories.setQuantity(numberField23.getValue());
                resultAccesories.setPricePurchase(Double.valueOf(pricePurchase.getValue()));
                resultAccesories.setPriceRetail(Double.valueOf(priceRetail.getValue()));
                resultAccesories.setAllPriceRetail(Double.valueOf(allPriceRetail.getValue()));
                resultAccesories.setAllPricePurchase(Double.valueOf(allPricePurchase.getValue()));
                resultAccesories.setProfit(Double.valueOf(profit.getValue()));
                resultAccesories.setCategory(value.getCategory());
                if (set.isEmpty()) {
                    set.add(resultAccesories);
                }
                for (EntityResultAccesories resultAccesories1 : set) {
                    if (resultAccesories1.getCategory().equals(resultAccesories.getCategory())) {
                        resultAccesories1.setName(name.getValue());
                        resultAccesories1.setQuantity(numberField23.getValue());
                        resultAccesories1.setPricePurchase(Double.valueOf(pricePurchase.getValue()));
                        resultAccesories1.setPriceRetail(Double.valueOf(priceRetail.getValue()));
                        resultAccesories1.setAllPriceRetail(Double.valueOf(allPriceRetail.getValue()));
                        resultAccesories1.setAllPricePurchase(Double.valueOf(allPricePurchase.getValue()));
                        resultAccesories1.setProfit(Double.valueOf(profit.getValue()));
                    } else if (!resultAccesories1.getCategory().equals(resultAccesories.getCategory()) && !set.contains(resultAccesories)) {
                        boolean exist = set.stream().anyMatch(c -> c.getCategory().equals(resultAccesories.getCategory()));
                        if (!exist) {
                            set.add(resultAccesories);
                        }
                    }
                }
            }
        });
        return formLayout;
    }

//    private FormLayout formLayoutAccesories() {
//        createValueComboBoxes();
//        getValuesTiles();
//        Iterator<ComboBox<String>> iter1 = boxList.iterator();
//        Iterator<NumberField> iter2 = listOfNumberFields.iterator();
//        FormLayout.ResponsiveStep responsiveStep = new FormLayout.ResponsiveStep("5px", 7);
//        board.setResponsiveSteps(responsiveStep);
//        board.add(cos());
//        while (iter1.hasNext() && iter2.hasNext()) {
//            board.add(iter1.next(), iter2.next(), new TextField("Cena"), new TextField("Cena zakupu"), new TextField("Cena suma po rabacie"), new TextField("Zysk"), new Checkbox());
//        }
//        return board;
//    }

    private void setValue(List<NumberField> list) {
        list.forEach(e -> {
            e.setAutoselect(true);
            e.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
            e.setMin(0);
            e.setSuffixComponent(new Span("szt"));
        });
    }

//    private void createValueComboBoxes() {
//        List<EntityAccesories> repositoryAll = getAllAccesories();
//        List<String> names = new ArrayList<>();
//        List<Double> value = new ArrayList<>();
//        repositoryAll.forEach(search -> names.add(search.getName()));
//        repositoryAll.forEach(e -> value.add(e.getFirstMultiplier() * e.getSecondMultiplier()));
//        if (names.size() > 0) {
//            setValues(comboBoxtasmaKalenicowa, names, value, 0, 9);
//            setValues(comboBoxwspornikLatyKalenicowej, names, value, 9, 14);
//            setValues(comboBoxtasmaDoObrobkiKomina, names, value, 14, 20);
//            setValues(comboBoxlistwaWykonczeniowaAluminiowa, names, value, 20, 21);
//            setValues(comboBoxkoszDachowyAluminiowy2mb, names, value, 21, 23);
//            setValues(comboBoxklamraDoMocowaniaKosza, names, value, 24, 25);
//            setValues(comboBoxklinUszczelniajacyKosz, names, value, 25, 27);
//            setValues(comboBoxgrzebienOkapowy, names, value, 27, 31);
//            setValues(comboBoxkratkaZabezpieczajacaPrzedPtactwem, names, value, 31, 33);
//            setValues(comboBoxpasOkapowy, names, value, 33, 35);
//            setValues(comboBoxklamraDoGasiora, names, value, 35, 39);
//            setValues(comboBoxspinkaDoDachowki, names, value, 39, 44);
//            setValues(comboBoxspinkaDoDachowkiCietej, names, value, 44, 46);
//            setValues(comboBoxlawaKominiarska, names, value, 46, 54);
//            setValues(comboBoxstopienKominiarski, names, value, 55, 56);
//            setValues(comboBoxplotekPrzeciwsniegowy155mmx2mb, names, value, 56, 57);
//            setValues(comboBoxplotekPrzeciwsniegowy155mmx3mb, names, value, 57, 59);
//            setValues(comboBoxmembranaDachowa, names, value, 59, 64);
//            setValues(comboBoxtasmaDoLaczeniaMembarnIFolii, names, value, 64, 67);
//            setValues(comboBoxtasmaReparacyjna, names, value, 67, 69);
//            setValues(comboBoxblachaAluminiowa, names, value, 69, 72);
//            setValues(comboBoxceglaKlinkierowa, names, value, 72, 73);
//        }
//    }

//    private void setValues(ComboBox<String> comboBox, List<String> listaNazw, List<Double> value, int poczatek, int koniec) {
//        comboBox.setItems(getSubList(listaNazw, poczatek, koniec));
//        comboBox.setValue(listaNazw.get(poczatek));
//        boxList.add(comboBox);
//        /*valuePriceAccesories.add(value.get(poczatek));*/
//    }

    private List<NumberField> getListNumberFields() {
        return listOfNumberFields = Arrays.asList(numberField1, numberField2, numberField3, numberField4, numberField5, numberField6, numberField7,
                numberField8, numberField9, numberField10, numberField11, numberField12, numberField13, numberField14, numberField15, numberField16,
                numberField17, numberField18, numberField19, numberField20, numberField21, numberField22);
    }

//    private List<EntityAccesories> getAllAccesories() {
//        Iterable<EntityAccesories> repositoryAll = accesoriesRepository.findAll();
//        List<EntityAccesories> allAccesories = new ArrayList<>();
//        repositoryAll.forEach(allAccesories::add);
//        return allAccesories;
//    }

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

    private void getValuesTiles() {
        setValue(getListNumberFields());
        if (dataTilesRepo != null && dataTiles == null) {
            valuesFromRepo = Arrays.asList(dataTilesRepo.getPowierzchniaPolaci(), dataTilesRepo.getDlugoscKalenic(),
                    dataTilesRepo.getDlugoscKalenicProstych(), dataTilesRepo.getDlugoscKalenicSkosnych(),
                    dataTilesRepo.getDlugoscKoszy(), dataTilesRepo.getDlugoscKrawedziLewych(), dataTilesRepo.getDlugoscKrawedziPrawych(),
                    dataTilesRepo.getObwodKomina(), dataTilesRepo.getDlugoscOkapu(), dataTilesRepo.getDachowkaWentylacyjna(),
                    dataTilesRepo.getKompletKominkaWentylacyjnego(), dataTilesRepo.getGasiarPoczatkowyKalenicaProsta(),
                    dataTilesRepo.getGasiarKoncowyKalenicaProsta(), dataTilesRepo.getGasiarZaokraglony(),
                    dataTilesRepo.getTrojnik(), dataTilesRepo.getCzwornik(), dataTilesRepo.getGasiarZPodwojnaMufa(),
                    dataTilesRepo.getDachowkaDwufalowa(), dataTilesRepo.getOknoPolaciowe());
            for (int i = 0; i < valuesFromRepo.size(); i++) {
                listOfNumberFields.get(i).setValue(valuesFromRepo.get(i));
            }
            getNotificationSucces("Repo - wczytano dane");
        } else {
            valuesFromRepo = Arrays.asList(dataTiles.getPowierzchniaPolaci(), dataTiles.getDlugoscKalenic(),
                    dataTiles.getDlugoscKalenicProstych(), dataTiles.getDlugoscKalenicSkosnych(),
                    dataTiles.getDlugoscKoszy(), dataTiles.getDlugoscKrawedziLewych(), dataTiles.getDlugoscKrawedziPrawych(),
                    dataTiles.getObwodKomina(), dataTiles.getDlugoscOkapu(), dataTiles.getDachowkaWentylacyjna(),
                    dataTiles.getKompletKominkaWentylacyjnego(), dataTiles.getGasiarPoczatkowyKalenicaProsta(),
                    dataTiles.getGasiarKoncowyKalenicaProsta(), dataTiles.getGasiarZaokraglony(),
                    dataTiles.getTrojnik(), dataTiles.getCzwornik(), dataTiles.getGasiarZPodwojnaMufa(),
                    dataTiles.getDachowkaDwufalowa(), dataTiles.getOknoPolaciowe());
            for (int i = 0; i < valuesFromRepo.size(); i++) {
                listOfNumberFields.get(i).setValue(valuesFromRepo.get(i));
            }
            getNotificationSucces("Bez repo - wczytano dane");
        }
    }

//    private EntityInputDataAccesories saveInputDataAccesories() {
//        return EntityInputDataAccesories.builder()
//                .tasmaKalenicowa(comboBoxtasmaKalenicowa.getValue())
//                .wspornikLatyKalenicowej(comboBoxwspornikLatyKalenicowej.getValue())
//                .tasmaDoObrobkiKomina(comboBoxtasmaDoObrobkiKomina.getValue())
//                .listwaWykonczeniowaAluminiowa(comboBoxlistwaWykonczeniowaAluminiowa.getValue())
//                .koszDachowyAluminiowy(comboBoxkoszDachowyAluminiowy2mb.getValue())
//                .klamraDoMocowaniaKosza(comboBoxklamraDoMocowaniaKosza.getValue())
//                .klinUszczelniajacyKosz(comboBoxklinUszczelniajacyKosz.getValue())
//                .grzebienOkapowy(comboBoxgrzebienOkapowy.getValue())
//                .kratkaZabezpieczajacaPrzedPtactwem(comboBoxkratkaZabezpieczajacaPrzedPtactwem.getValue())
//                .pasOkapowy(comboBoxpasOkapowy.getValue())
//                .klamraDoGasiora(comboBoxklamraDoGasiora.getValue())
//                .spinkaDoDachowki(comboBoxspinkaDoDachowki.getValue())
//                .spinkaDoDachowkiCietej(comboBoxspinkaDoDachowkiCietej.getValue())
//                .lawaKominiarska(comboBoxlawaKominiarska.getValue())
//                .stopienKominiarski(comboBoxstopienKominiarski.getValue())
//                .plotekPrzeciwsniegowy155mmx2mb(comboBoxplotekPrzeciwsniegowy155mmx2mb.getValue())
//                .plotekPrzeciwsniegowy155mmx3mb(comboBoxplotekPrzeciwsniegowy155mmx3mb.getValue())
//                .membranaDachowa(comboBoxmembranaDachowa.getValue())
//                .tasmaDoLaczeniaMembarnIFolii(comboBoxtasmaDoLaczeniaMembarnIFolii.getValue())
//                .tasmaReparacyjna(comboBoxtasmaReparacyjna.getValue())
//                .blachaAluminiowa(comboBoxblachaAluminiowa.getValue())
//                .ceglaKlinkierowa(comboBoxceglaKlinkierowa.getValue())
//                .build();
//    }

//    private List<EntityAccesories> resultAccesories() {
//        List<EntityAccesories> allNameAccesories = getAllAccesories();
//        List<EntityAccesories> selected = new ArrayList<>();
//
//        for (int i = 0; i < valuesFromRepo.size(); i++) {
//            for (EntityAccesories accesories : allNameAccesories) {
//                if (accesories.getName().equalsIgnoreCase(boxList.get(i).getValue())) {
//
//                    /*BigDecimal bigDecimal = accesories.getPurchasePrice().multiply(new BigDecimal(accesories.getMargin()))
//                            .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
//
//                    BigDecimal unitRetailPrice = bigDecimal.add(accesories.getPurchasePrice());
//                    accesories.setUnitRetailPrice(unitRetailPrice);
//
//                    BigDecimal totalRetail = accesories.getUnitRetailPrice().multiply(new BigDecimal(valuesFromRepo.get(i)));
//                    accesories.setTotalRetail(totalRetail);
//
//                    BigDecimal totalPurchase = accesories.getPurchasePrice().multiply(new BigDecimal(valuesFromRepo.get(i)));
//                    accesories.setTotalPurchase(totalPurchase);*/
//
//                    selected.add(accesories);
//                }
//            }
//        }
//        return selected;
//    }

//    private List<String> getSubList(List<String> listaNazw, int poczatek, int koniec) {
//        return listaNazw.subList(poczatek, koniec);
//    }
}

