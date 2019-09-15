package app.views;

import app.controllers.ControllerVaadin;
import app.entities.EntityAccesories;
import app.repositories.Accesories;
import app.service.Layout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static app.inputFields.ServiceDataCustomer.*;
import static app.inputFields.ServiceSplitLayout.getSideMenuSettings;
import static app.inputFields.ServiceSplitLayout.ustawieniaStrony;

@Route(value = SelectAccesories.SELECT_ACCESORIES)
public class SelectAccesories extends SplitLayout implements Layout {

    public static final String SELECT_ACCESORIES = "accesories/select";
    private ControllerVaadin controllerVaadin;
    private Accesories accesories;

    List<ComboBox<String>> boxList = new ArrayList<>();

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

    private Button calculateAccesories = new Button("Oblicz Accesories");

    @Autowired
    public SelectAccesories(ControllerVaadin controllerVaadin, Accesories accesories) {
        this.controllerVaadin = Objects.requireNonNull(controllerVaadin);
        this.accesories = Objects.requireNonNull(accesories);
        setOrientation(SplitLayout.Orientation.VERTICAL);
        addToPrimary(ustawieniaStrony(controllerVaadin));
        addToSecondary(getSideMenu(controllerVaadin));
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

    private FormLayout formLayoutAccesories() {
        FormLayout board = new FormLayout();
        createValueComboBoxes();
        createListBox();
        Label label = new Label(" ");
        board.add(calculateAccesories, label);
        boxList.forEach(board::add);
        return board;
    }

    private void createValueComboBoxes() {
        Iterable<EntityAccesories> iterable = accesories.findAll();
        List<String> names = new ArrayList<>();
        iterable.forEach(search -> names.add(search.getName()));
        if (names.size() > 0) {
            comboBoxtasmaKalenicowa.setItems(getSubList(names, 0, 9));
            comboBoxwspornikLatyKalenicowej.setItems(getSubList(names, 9, 14));
            comboBoxtasmaDoObrobkiKomina.setItems(getSubList(names, 14, 20));
            comboBoxlistwaWykonczeniowaAluminiowa.setItems(getSubList(names, 20, 21));
            comboBoxkoszDachowyAluminiowy2mb.setItems(getSubList(names, 21, 23));
            comboBoxklamraDoMocowaniaKosza.setItems(getSubList(names, 24, 25));
            comboBoxklinUszczelniajacyKosz.setItems(getSubList(names, 25, 27));
            comboBoxgrzebienOkapowy.setItems(getSubList(names, 27, 31));
            comboBoxkratkaZabezpieczajacaPrzedPtactwem.setItems(getSubList(names, 31, 33));
            comboBoxpasOkapowy.setItems(getSubList(names, 33, 35));
            comboBoxklamraDoGasiora.setItems(getSubList(names, 35, 39));
            comboBoxspinkaDoDachowki.setItems(getSubList(names, 39, 44));
            comboBoxspinkaDoDachowkiCietej.setItems(getSubList(names, 44, 46));
            comboBoxlawaKominiarska.setItems(getSubList(names, 46, 54));
            comboBoxstopienKominiarski.setItems(getSubList(names, 55, 56));
            comboBoxplotekPrzeciwsniegowy155mmx2mb.setItems(getSubList(names, 56, 57));
            comboBoxplotekPrzeciwsniegowy155mmx3mb.setItems(getSubList(names, 57, 59));
            comboBoxmembranaDachowa.setItems(getSubList(names, 59, 64));
            comboBoxtasmaDoLaczeniaMembarnIFolii.setItems(getSubList(names, 64, 67));
            comboBoxtasmaReparacyjna.setItems(getSubList(names, 67, 69));
            comboBoxblachaAluminiowa.setItems(getSubList(names, 69, 72));
            comboBoxceglaKlinkierowa.setItems(getSubList(names, 72, 73));
        }
    }

    private List<String> getSubList(List<String> listaNazw, int poczatek, int koniec) {
        return listaNazw.subList(poczatek, koniec);
    }

    @Override
    public SplitLayout getSideMenu(ControllerVaadin controllerVaadin) {
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.addToPrimary(controllerVaadin.sideMenuAccesories());
        splitLayout.addToSecondary(formLayoutAccesories());
        getSideMenuSettings(splitLayout);
        return splitLayout;
    }
}

