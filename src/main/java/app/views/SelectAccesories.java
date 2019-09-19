package app.views;

import app.entities.EntityAccesories;
import app.entities.EntityInputDataAccesories;
import app.entities.EntityInputDataTiles;
import app.entities.EntityUser;
import app.repositories.AccesoriesRepository;
import app.repositories.InputDataAccesoriesRespository;
import app.repositories.UsersRepo;
import app.service.MenuBarInterface;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static app.inputFields.ServiceNotification.getNotificationError;
import static app.inputFields.ServiceNotification.getNotificationSucces;
import static app.service.Labels.*;

@Route(value = SelectAccesories.SELECT_ACCESORIES, layout = MainView.class)
public class SelectAccesories extends VerticalLayout implements MenuBarInterface {

    public static final String SELECT_ACCESORIES = "accesories/select";
    private AccesoriesRepository accesoriesRepository;
    private InputDataAccesoriesRespository inputDataAccesoriesRespository;

    private List<ComboBox<String>> boxList = new ArrayList<>();
    private List<NumberField> listOfNumberFields = new ArrayList<>();

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

    private ComboBox<String> comboBoxUsers = new ComboBox<>("Wczytaj użytkownika: ");
    private Button selectUser = new Button("Zapisz dane");
    private Button wczytajDaneZDachowek = new Button("Wczytaj dane z dachówek");

    private FormLayout board = new FormLayout();

    private Button calculateAccesories = new Button("Oblicz AccesoriesRepository");
    private UsersRepo usersRepo;

    @Autowired
    public SelectAccesories(AccesoriesRepository accesoriesRepository,
                            InputDataAccesoriesRespository inputDataAccesoriesRespository, UsersRepo usersRepo) {
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);
        this.inputDataAccesoriesRespository = Objects.requireNonNull(inputDataAccesoriesRespository);
        this.usersRepo = Objects.requireNonNull(usersRepo);

        loadUserComboBox();
        add(menu());
        add(formLayoutAccesories());
    }

    private FormLayout formLayoutAccesories() {
        selectUser.addClickListener(buttonClickEvent -> saveInputDataAccesories());
        createValueComboBoxes();
        createListBox();
        getListNumberFields();
        wczytajDaneZDachowek.addClickListener(buttonClickEvent -> getValuesTiles());
        board.add(calculateAccesories, getLabel(" "));
        board.add(comboBoxUsers, selectUser);
        board.add(wczytajDaneZDachowek, getLabel(" "));
        Iterator<ComboBox<String>> iter1 = boxList.iterator();
        Iterator<NumberField> iter2 = listOfNumberFields.iterator();
        while (iter1.hasNext() && iter2.hasNext()) {
            board.add(iter1.next(), iter2.next());
        }
        return board;
    }

    private void getValuesTiles() {
        EntityUser entityUser = findUserByNameAndSurname();
        EntityInputDataTiles dataTiles = entityUser.getEntityInputDataTiles();
        List<Double> valuesFromRepo = Arrays.asList(dataTiles.getPowierzchniaPolaci(), dataTiles.getDlugoscKalenic(),
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
    }

    private void createListBox() {
        boxList = Arrays.asList(comboBoxtasmaKalenicowa, comboBoxwspornikLatyKalenicowej, comboBoxtasmaDoObrobkiKomina,
                comboBoxlistwaWykonczeniowaAluminiowa, comboBoxkoszDachowyAluminiowy2mb, comboBoxklamraDoMocowaniaKosza,
                comboBoxklinUszczelniajacyKosz, comboBoxgrzebienOkapowy, comboBoxkratkaZabezpieczajacaPrzedPtactwem,
                comboBoxpasOkapowy, comboBoxklamraDoGasiora, comboBoxspinkaDoDachowki, comboBoxspinkaDoDachowkiCietej,
                comboBoxlawaKominiarska, comboBoxstopienKominiarski, comboBoxplotekPrzeciwsniegowy155mmx2mb,
                comboBoxplotekPrzeciwsniegowy155mmx3mb, comboBoxmembranaDachowa, comboBoxtasmaDoLaczeniaMembarnIFolii,
                comboBoxtasmaReparacyjna, comboBoxblachaAluminiowa, comboBoxceglaKlinkierowa);
    }

    private void getListNumberFields() {
        listOfNumberFields = Arrays.asList(numberField1, numberField2, numberField3, numberField4, numberField5, numberField6, numberField7,
                numberField8, numberField9, numberField10, numberField11, numberField12, numberField13, numberField14, numberField15, numberField16,
                numberField17, numberField18, numberField19);
    }

    private void saveInputDataAccesories() {
        EntityUser entityUser = findUserByNameAndSurname();

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
        inputDataAccesoriesRespository.save(entityInputDataAccesories);
        if (entityInputDataAccesories != null) {
            entityUser.setHasAccesories(true);
            entityUser.setEntityInputDataAccesories(entityInputDataAccesories);
            usersRepo.save(entityUser);
            getNotificationSucces("Akcesoria zapisane");
        } else {
            getNotificationError("Akcesoria niezapisane");
        }
    }

    private EntityUser findUserByNameAndSurname() {
        String nameISurname = comboBoxUsers.getValue();
        String[] strings = nameISurname.split(" ");
        return usersRepo.findUsersEntityByNameAndSurnameEquals(strings[0], strings[1]);
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

    private void createValueComboBoxes() {
        Iterable<EntityAccesories> iterable = accesoriesRepository.findAll();
        List<String> names = new ArrayList<>();
        iterable.forEach(search -> names.add(search.getName()));
        if (names.size() > 0) {
            setValues(comboBoxtasmaKalenicowa, names, 0, 9);
            setValues(comboBoxwspornikLatyKalenicowej, names, 9, 14);
            setValues(comboBoxtasmaDoObrobkiKomina, names, 14, 20);
            setValues(comboBoxlistwaWykonczeniowaAluminiowa, names, 20, 21);
            setValues(comboBoxkoszDachowyAluminiowy2mb, names, 21, 23);
            setValues(comboBoxklamraDoMocowaniaKosza, names, 24, 25);
            setValues(comboBoxklinUszczelniajacyKosz, names, 25, 27);
            setValues(comboBoxgrzebienOkapowy, names, 27, 31);
            setValues(comboBoxkratkaZabezpieczajacaPrzedPtactwem, names, 31, 33);
            setValues(comboBoxpasOkapowy, names, 33, 35);
            setValues(comboBoxklamraDoGasiora, names, 35, 39);
            setValues(comboBoxspinkaDoDachowki, names, 39, 44);
            setValues(comboBoxspinkaDoDachowkiCietej, names, 44, 46);
            setValues(comboBoxlawaKominiarska, names, 46, 54);
            setValues(comboBoxstopienKominiarski, names, 55, 56);
            setValues(comboBoxplotekPrzeciwsniegowy155mmx2mb, names, 56, 57);
            setValues(comboBoxplotekPrzeciwsniegowy155mmx3mb, names, 57, 59);
            setValues(comboBoxmembranaDachowa, names, 59, 64);
            setValues(comboBoxtasmaDoLaczeniaMembarnIFolii, names, 64, 67);
            setValues(comboBoxtasmaReparacyjna, names, 67, 69);
            setValues(comboBoxblachaAluminiowa, names, 69, 72);
            setValues(comboBoxceglaKlinkierowa, names, 72, 73);
        }
    }

    private void setValues(ComboBox<String> comboBox, List<String> listaNazw, int poczatek, int koniec) {
        comboBox.setItems(getSubList(listaNazw, poczatek, koniec));
        comboBox.setValue(listaNazw.get(poczatek));
    }

    private List<String> getSubList(List<String> listaNazw, int poczatek, int koniec) {
        return listaNazw.subList(poczatek, koniec);
    }

    @Override
    public MenuBar menu() {
        MenuBar menuBar = new MenuBar();
        menuBar.addItem(new RouterLink("Kolejne dane", InputWindows.class));
        return menuBar;
    }
}

