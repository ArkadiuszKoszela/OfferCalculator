package pl.koszela.spring.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.EntityAccesories;
import pl.koszela.spring.entities.EntityInputDataAccesories;
import pl.koszela.spring.entities.EntityInputDataTiles;
import pl.koszela.spring.entities.EntityUser;
import pl.koszela.spring.inputFields.ServiceNotification;
import pl.koszela.spring.repositories.AccesoriesRepository;
import pl.koszela.spring.repositories.InputDataAccesoriesRespository;
import pl.koszela.spring.service.MenuBarInterface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationError;
import static pl.koszela.spring.service.Labels.*;
import static pl.koszela.spring.views.WindowsView.WINDOWS;

@Route(value = AccesoriesView.SELECT_ACCESORIES, layout = MainView.class)
public class AccesoriesView extends VerticalLayout implements MenuBarInterface {

    public static final String SELECT_ACCESORIES = "accesories/select";
    private AccesoriesRepository accesoriesRepository;
    private InputDataAccesoriesRespository inputDataAccesoriesRespository;

    private ComboBox<String> comboBoxtasmaKalenicowa = new ComboBox<>(TASMA_KELNICOWA);
    private ComboBox<String> comboBoxwspornikLatyKalenicowej = new ComboBox<>(WSPORNIK_LATY_KALENICOWEJ);
    private ComboBox<String> comboBoxtasmaDoObrobkiKomina = new ComboBox<>(TASMA_DO_OBROBKI_KOMINA);
    private ComboBox<String> comboBoxlistwaWykonczeniowaAluminiowa = new ComboBox<>(LISTWA_WYKONCZENIOWA_ALUMINIOWA);
    private ComboBox<String> comboBoxkoszDachowyAluminiowy2mb = new ComboBox<>(KOSZ_DACHOWY_ALUMINIOWY_2MB);
    private ComboBox<String> comboBoxklamraDoMocowaniaKosza = new ComboBox<>(KLAMRA_DO_MOCOWANIA_KOSZA);
    private ComboBox<String> comboBoxklinUszczelniajacyKosz = new ComboBox<>(KLIN_USZCZELNIAJACY_KOSZ);
    private ComboBox<String> comboBoxgrzebienOkapowy = new ComboBox<>(GRZEBIEN_OKAPOWY);
    private ComboBox<String> comboBoxkratkaZabezpieczajacaPrzedPtactwem = new ComboBox<>(KRATKA_ZABEZPIECZAJACA_PRZED_PTACTWEM);
    private ComboBox<String> comboBoxpasOkapowy = new ComboBox<>(PAS_OKAPOWY);
    private ComboBox<String> comboBoxklamraDoGasiora = new ComboBox<>(KLAMRA_DO_GASIORA);
    private ComboBox<String> comboBoxspinkaDoDachowki = new ComboBox<>(SPINKA_DO_DACHOWKI);
    private ComboBox<String> comboBoxspinkaDoDachowkiCietej = new ComboBox<>(SPINKA_DO_DACHOWKI_CIETEJ);
    private ComboBox<String> comboBoxlawaKominiarska = new ComboBox<>(LAWA_KOMINIARSKA);
    private ComboBox<String> comboBoxstopienKominiarski = new ComboBox<>(STOPIEN_KOMINIARSKI);
    private ComboBox<String> comboBoxplotekPrzeciwsniegowy155mmx2mb = new ComboBox<>(PLOTEK_PRZECIWSNIEGOWY_155MMX2MB);
    private ComboBox<String> comboBoxplotekPrzeciwsniegowy155mmx3mb = new ComboBox<>(PLOTEK_PRZECIWSNIEGOWY_155MMX3MB);
    private ComboBox<String> comboBoxmembranaDachowa = new ComboBox<>(MEMBRANA_DACHOWA);
    private ComboBox<String> comboBoxtasmaDoLaczeniaMembarnIFolii = new ComboBox<>(TASMA_DO_LACZENIA_MEMBRAN_I_FOLII);
    private ComboBox<String> comboBoxtasmaReparacyjna = new ComboBox<>(TASMA_REPARACYJNA);
    private ComboBox<String> comboBoxblachaAluminiowa = new ComboBox<>(BLACHA_ALUMINIOWA);
    private ComboBox<String> comboBoxceglaKlinkierowa = new ComboBox<>(CEGLA_KLINKIEROWA);

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
    private NumberField numberField20 = new NumberField("Okno połaciowe");
    private NumberField numberField21 = new NumberField("Okno połaciowe");
    private NumberField numberField22 = new NumberField("Okno połaciowe");

    private List<ComboBox<String>> boxList = new ArrayList<>();
    private List<NumberField> listOfNumberFields = new ArrayList<>();
    private List<Double> valuesFromRepo;
    private List<Double> valuePriceAccesories = new ArrayList<>();

    private FormLayout board = new FormLayout();


    @Autowired
    public AccesoriesView(AccesoriesRepository accesoriesRepository,
                          InputDataAccesoriesRespository inputDataAccesoriesRespository) {
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);
        this.inputDataAccesoriesRespository = Objects.requireNonNull(inputDataAccesoriesRespository);

        add(menu());
        add(formLayoutAccesories());
    }

    private FormLayout formLayoutAccesories() {
        createValueComboBoxes();
        getValuesTiles();
        Iterator<ComboBox<String>> iter1 = boxList.iterator();
        Iterator<NumberField> iter2 = listOfNumberFields.iterator();
        FormLayout.ResponsiveStep responsiveStep = new FormLayout.ResponsiveStep("5px", 6);
        board.setResponsiveSteps(responsiveStep);
        while (iter1.hasNext() && iter2.hasNext()) {
            board.add(iter1.next(), iter2.next());
        }
        return board;
    }

    private void setValue(List<NumberField> list) {
        list.forEach(e -> {
            e.setAutoselect(true);
            e.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
            e.setMin(0);
            e.setSuffixComponent(new Span("szt"));
        });
    }

    private void createValueComboBoxes() {
        List<EntityAccesories> repositoryAll = getAllAccesories();
        List<String> names = new ArrayList<>();
        List<Double> value = new ArrayList<>();
        repositoryAll.forEach(search -> names.add(search.getName()));
        repositoryAll.forEach(e -> value.add(e.getFirstMultiplier() * e.getSecondMultiplier()));
        if (names.size() > 0) {
            setValues(comboBoxtasmaKalenicowa, names, value, 0, 9);
            setValues(comboBoxwspornikLatyKalenicowej, names, value, 9, 14);
            setValues(comboBoxtasmaDoObrobkiKomina, names, value, 14, 20);
            setValues(comboBoxlistwaWykonczeniowaAluminiowa, names, value, 20, 21);
            setValues(comboBoxkoszDachowyAluminiowy2mb, names, value, 21, 23);
            setValues(comboBoxklamraDoMocowaniaKosza, names, value, 24, 25);
            setValues(comboBoxklinUszczelniajacyKosz, names, value, 25, 27);
            setValues(comboBoxgrzebienOkapowy, names, value, 27, 31);
            setValues(comboBoxkratkaZabezpieczajacaPrzedPtactwem, names, value, 31, 33);
            setValues(comboBoxpasOkapowy, names, value, 33, 35);
            setValues(comboBoxklamraDoGasiora, names, value, 35, 39);
            setValues(comboBoxspinkaDoDachowki, names, value, 39, 44);
            setValues(comboBoxspinkaDoDachowkiCietej, names, value, 44, 46);
            setValues(comboBoxlawaKominiarska, names, value, 46, 54);
            setValues(comboBoxstopienKominiarski, names, value, 55, 56);
            setValues(comboBoxplotekPrzeciwsniegowy155mmx2mb, names, value, 56, 57);
            setValues(comboBoxplotekPrzeciwsniegowy155mmx3mb, names, value, 57, 59);
            setValues(comboBoxmembranaDachowa, names, value, 59, 64);
            setValues(comboBoxtasmaDoLaczeniaMembarnIFolii, names, value, 64, 67);
            setValues(comboBoxtasmaReparacyjna, names, value, 67, 69);
            setValues(comboBoxblachaAluminiowa, names, value, 69, 72);
            setValues(comboBoxceglaKlinkierowa, names, value, 72, 73);
        }
    }

    private void setValues(ComboBox<String> comboBox, List<String> listaNazw, List<Double> value, int poczatek, int koniec) {
        comboBox.setItems(getSubList(listaNazw, poczatek, koniec));
        comboBox.setValue(listaNazw.get(poczatek));
        boxList.add(comboBox);
        valuePriceAccesories.add(value.get(poczatek));
    }

    private List<NumberField> getListNumberFields() {
        return listOfNumberFields = Arrays.asList(numberField1, numberField2, numberField3, numberField4, numberField5, numberField6, numberField7,
                numberField8, numberField9, numberField10, numberField11, numberField12, numberField13, numberField14, numberField15, numberField16,
                numberField17, numberField18, numberField19, numberField20, numberField21, numberField22);
    }

    private List<EntityAccesories> getAllAccesories() {
        Iterable<EntityAccesories> repositoryAll = accesoriesRepository.findAll();
        List<EntityAccesories> allAccesories = new ArrayList<>();
        repositoryAll.forEach(allAccesories::add);
        return allAccesories;
    }

    private void getValuesTiles() {
        setValue(getListNumberFields());
        EntityInputDataTiles dataTiles = (EntityInputDataTiles) VaadinSession.getCurrent().getAttribute("tilesInput");
        if (dataTiles != null) {
            valuesFromRepo = Arrays.asList(dataTiles.getPowierzchniaPolaci(), dataTiles.getDlugoscKalenic(),
                    dataTiles.getDlugoscKalenicProstych(), dataTiles.getDlugoscKalenicSkosnych(),
                    dataTiles.getDlugoscKoszy(), dataTiles.getDlugoscKrawedziLewych(), dataTiles.getDlugoscKrawedziPrawych(),
                    dataTiles.getObwodKomina(), dataTiles.getDlugoscOkapu(), dataTiles.getDachowkaWentylacyjna(),
                    dataTiles.getKompletKominkaWentylacyjnego(), dataTiles.getGasiarPoczatkowyKalenicaProsta(),
                    dataTiles.getGasiarKoncowyKalenicaProsta(), dataTiles.getGasiarZaokraglony(),
                    dataTiles.getTrojnik(), dataTiles.getCzwornik(), dataTiles.getGasiarZPodwojnaMufa(),
                    dataTiles.getDachowkaDwufalowa(), dataTiles.getOknoPolaciowe());
            for (int i = 0; i < valuesFromRepo.size(); i++) {
                listOfNumberFields.get(i).setValue(valuesFromRepo.get(i) * valuePriceAccesories.get(i));
            }
        } else {
            getNotificationError("Proszę kliknąc dalej aby móc wczytać dane");
        }
    }

    private void saveInputDataAccesories() {
        EntityInputDataAccesories entityInputDataAccesories = EntityInputDataAccesories.builder()
                .tasmaKalenicowa(comboBoxtasmaKalenicowa.getValue())
                .wspornikLatyKalenicowej(comboBoxwspornikLatyKalenicowej.getValue())
                .tasmaDoObrobkiKomina(comboBoxtasmaDoObrobkiKomina.getValue())
                .listwaWykonczeniowaAluminiowa(comboBoxlistwaWykonczeniowaAluminiowa.getValue())
                .koszDachowyAluminiowy(comboBoxkoszDachowyAluminiowy2mb.getValue())
                .klamraDoMocowaniaKosza(comboBoxklamraDoMocowaniaKosza.getValue())
                .klinUszczelniajacyKosz(comboBoxklinUszczelniajacyKosz.getValue())
                .grzebienOkapowy(comboBoxgrzebienOkapowy.getValue())
                .kratkaZabezpieczajacaPrzedPtactwem(comboBoxkratkaZabezpieczajacaPrzedPtactwem.getValue())
                .pasOkapowy(comboBoxpasOkapowy.getValue())
                .klamraDoGasiora(comboBoxklamraDoGasiora.getValue())
                .spinkaDoDachowki(comboBoxspinkaDoDachowki.getValue())
                .spinkaDoDachowkiCietej(comboBoxspinkaDoDachowkiCietej.getValue())
                .lawaKominiarska(comboBoxlawaKominiarska.getValue())
                .stopienKominiarski(comboBoxstopienKominiarski.getValue())
                .plotekPrzeciwsniegowy155mmx2mb(comboBoxplotekPrzeciwsniegowy155mmx2mb.getValue())
                .plotekPrzeciwsniegowy155mmx3mb(comboBoxplotekPrzeciwsniegowy155mmx3mb.getValue())
                .membranaDachowa(comboBoxmembranaDachowa.getValue())
                .tasmaDoLaczeniaMembarnIFolii(comboBoxtasmaDoLaczeniaMembarnIFolii.getValue())
                .tasmaReparacyjna(comboBoxtasmaReparacyjna.getValue())
                .blachaAluminiowa(comboBoxblachaAluminiowa.getValue())
                .ceglaKlinkierowa(comboBoxceglaKlinkierowa.getValue())
                .build();
        VaadinSession.getCurrent().setAttribute("accesoriesInput", entityInputDataAccesories);
        ServiceNotification.getNotificationSucces("Akcesoria zapisane");
    }

    private List<EntityAccesories> resultAccesories() {
        List<EntityAccesories> allNameAccesories = getAllAccesories();
        List<EntityAccesories> selected = new ArrayList<>();

        for (int i = 0; i < valuesFromRepo.size(); i++) {
            for (EntityAccesories accesories : allNameAccesories) {
                if (accesories.getName().equalsIgnoreCase(boxList.get(i).getValue())) {

                    BigDecimal bigDecimal = accesories.getPurchasePrice().multiply(new BigDecimal(accesories.getMargin()))
                            .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);

                    BigDecimal unitRetailPrice = bigDecimal.add(accesories.getPurchasePrice());
                    accesories.setUnitRetailPrice(unitRetailPrice);

                    BigDecimal totalRetail = accesories.getUnitRetailPrice().multiply(new BigDecimal(valuesFromRepo.get(i)));
                    accesories.setTotalRetail(totalRetail);

                    BigDecimal totalPurchase = accesories.getPurchasePrice().multiply(new BigDecimal(valuesFromRepo.get(i)));
                    accesories.setTotalPurchase(totalPurchase);

                    selected.add(accesories);
                }
            }
        }
        return selected;
    }

    private List<String> getSubList(List<String> listaNazw, int poczatek, int koniec) {
        return listaNazw.subList(poczatek, koniec);
    }

    @Override
    public MenuBar menu() {
        MenuBar menuBar = new MenuBar();
        Button button = new Button("Dalej");
        button.addClickListener(buttonClickEvent -> {
            saveInputDataAccesories();
            getUI().ifPresent(ui -> ui.navigate(WINDOWS));
        });
        menuBar.addItem(button);
        return menuBar;
    }
}

