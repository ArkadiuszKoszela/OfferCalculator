package app.GUIs;

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
import java.util.List;
import java.util.Objects;

import static app.inputFields.ServiceDataCustomer.*;
import static app.inputFields.ServiceSplitLayout.getSideMenuSettings;
import static app.inputFields.ServiceSplitLayout.ustawieniaStrony;

@Route(value = SelectAccesories.SELECT_ACCESORIES)
public class SelectAccesories extends SplitLayout implements Layout {

    public static final String SELECT_ACCESORIES = "SelectAccesories";
    private ControllerVaadin controllerVaadin;
    private Accesories accesories;

    private ComboBox<String> comboBoxtasmaKalenicowa = new ComboBox<>();
    private ComboBox<String> comboBoxwspornikLatyKalenicowej = new ComboBox<>();
    private ComboBox<String> comboBoxtasmaDoObrobkiKomina = new ComboBox<>();
    private ComboBox<String> comboBoxlistwaWykonczeniowaAluminiowa = new ComboBox<>();
    private ComboBox<String> comboBoxkoszDachowyAluminiowy2mb = new ComboBox<>();
    private ComboBox<String> comboBoxklamraDoMocowaniaKosza = new ComboBox<>();
    private ComboBox<String> comboBoxklinUszczelniajacyKosz = new ComboBox<>();
    private ComboBox<String> comboBoxgrzebienOkapowy = new ComboBox<>();
    private ComboBox<String> comboBoxkratkaZabezpieczajacaPrzedPtactwem = new ComboBox<>();
    private ComboBox<String> comboBoxpasOkapowy = new ComboBox<>();
    private ComboBox<String> comboBoxklamraDoGasiora = new ComboBox<>();
    private ComboBox<String> comboBoxspinkaDoDachowki = new ComboBox<>();
    private ComboBox<String> comboBoxspinkaDoDachowkiCietej = new ComboBox<>();
    private ComboBox<String> comboBoxlawaKominiarska = new ComboBox<>();
    private ComboBox<String> comboBoxstopienKominiarski = new ComboBox<>();
    private ComboBox<String> comboBoxplotekPrzeciwsniegowy155mmx2mb = new ComboBox<>();
    private ComboBox<String> comboBoxplotekPrzeciwsniegowy155mmx3mb = new ComboBox<>();
    private ComboBox<String> comboBoxmembranaDachowa = new ComboBox<>();
    private ComboBox<String> comboBoxtasmaDoLaczeniaMembarnIFolii = new ComboBox<>();
    private ComboBox<String> comboBoxtasmaReparacyjna = new ComboBox<>();
    private ComboBox<String> comboBoxblachaAluminiowa = new ComboBox<>();
    private ComboBox<String> comboBoxceglaKlinkierowa = new ComboBox<>();

    private Button calculateAccesories = new Button("Oblicz Accesories");

    @Autowired
    public SelectAccesories(ControllerVaadin controllerVaadin, Accesories accesories) {
        this.controllerVaadin = Objects.requireNonNull(controllerVaadin);
        this.accesories = Objects.requireNonNull(accesories);

        setOrientation(Orientation.VERTICAL);

        addToPrimary(ustawieniaStrony(controllerVaadin));
        addToSecondary(getSideMenu(controllerVaadin));
    }

    private FormLayout formLayoutAccesories() {
        FormLayout board = new FormLayout();
        createValueComboBoxes();
        Label label = new Label(" ");
        board.add(calculateAccesories, label);

        board.add(comboBoxtasmaKalenicowa, comboBoxwspornikLatyKalenicowej,
                comboBoxtasmaDoObrobkiKomina, comboBoxlistwaWykonczeniowaAluminiowa);
        board.add(comboBoxkoszDachowyAluminiowy2mb, comboBoxklamraDoMocowaniaKosza,
                comboBoxklinUszczelniajacyKosz, comboBoxgrzebienOkapowy);
        board.add(comboBoxkratkaZabezpieczajacaPrzedPtactwem, comboBoxpasOkapowy,
                comboBoxklamraDoGasiora, comboBoxspinkaDoDachowki);
        board.add(comboBoxspinkaDoDachowkiCietej, comboBoxlawaKominiarska,
                comboBoxstopienKominiarski, comboBoxplotekPrzeciwsniegowy155mmx2mb);
        board.add(comboBoxplotekPrzeciwsniegowy155mmx3mb, comboBoxmembranaDachowa,
                comboBoxtasmaDoLaczeniaMembarnIFolii, comboBoxtasmaReparacyjna);
        board.add(comboBoxblachaAluminiowa, comboBoxceglaKlinkierowa);
        return board;
    }

    private void createValueComboBoxes() {
        Iterable<EntityAccesories> iterable = accesories.findAll();
        List<EntityAccesories> list = new ArrayList<>();
        iterable.forEach(list::add);
        List<String> listaNazw = new ArrayList<>();
        list.forEach(e -> listaNazw.add(e.getName()));
        if (list.size() > 0) {
            comboBoxtasmaKalenicowa.setItems(getSubList(listaNazw, 0, 9));
            comboBoxtasmaKalenicowa.setLabel(TASMA_KELNICOWA);
            comboBoxwspornikLatyKalenicowej.setItems(getSubList(listaNazw, 9, 14));
            comboBoxwspornikLatyKalenicowej.setLabel(WSPORNIK_LATY_KALENICOWEJ);
            comboBoxtasmaDoObrobkiKomina.setItems(getSubList(listaNazw, 14, 20));
            comboBoxtasmaDoObrobkiKomina.setLabel(TASMA_DO_OBROBKI_KOMINA);
            comboBoxlistwaWykonczeniowaAluminiowa.setItems(getSubList(listaNazw, 20, 21));
            comboBoxlistwaWykonczeniowaAluminiowa.setLabel(LISTWA_WYKONCZENIOWA_ALUMINIOWA);
            comboBoxkoszDachowyAluminiowy2mb.setItems(getSubList(listaNazw, 21, 23));
            comboBoxkoszDachowyAluminiowy2mb.setLabel(KOSZ_DACHOWY_ALUMINIOWY_2MB);
            comboBoxklamraDoMocowaniaKosza.setItems(getSubList(listaNazw, 24, 25));
            comboBoxklamraDoMocowaniaKosza.setLabel(KLAMRA_DO_MOCOWANIA_KOSZA);
            comboBoxklinUszczelniajacyKosz.setItems(getSubList(listaNazw, 25, 27));
            comboBoxklinUszczelniajacyKosz.setLabel(KLIN_USZCZELNIAJACY_KOSZ);
            comboBoxgrzebienOkapowy.setItems(getSubList(listaNazw, 27, 31));
            comboBoxgrzebienOkapowy.setLabel(GRZEBIEN_OKAPOWY);
            comboBoxkratkaZabezpieczajacaPrzedPtactwem.setItems(getSubList(listaNazw, 31, 33));
            comboBoxkratkaZabezpieczajacaPrzedPtactwem.setLabel(KRATKA_ZABEZPIECZAJACA_PRZED_PTACTWEM);
            comboBoxpasOkapowy.setItems(getSubList(listaNazw, 33, 35));
            comboBoxpasOkapowy.setLabel(PAS_OKAPOWY);
            comboBoxklamraDoGasiora.setItems(getSubList(listaNazw, 35, 39));
            comboBoxklamraDoGasiora.setLabel(KLAMRA_DO_GASIORA);
            comboBoxspinkaDoDachowki.setItems(getSubList(listaNazw, 39, 44));
            comboBoxspinkaDoDachowki.setLabel(SPINKA_DO_DACHOWKI);
            comboBoxspinkaDoDachowkiCietej.setItems(getSubList(listaNazw, 44, 46));
            comboBoxspinkaDoDachowkiCietej.setLabel(SPINKA_DO_DACHOWKI_CIETEJ);
            comboBoxlawaKominiarska.setItems(getSubList(listaNazw, 46, 54));
            comboBoxlawaKominiarska.setLabel(LAWA_KOMINIARSKA);
            comboBoxstopienKominiarski.setItems(getSubList(listaNazw, 55, 56));
            comboBoxstopienKominiarski.setLabel(STOPIEN_KOMINIARSKI);
            comboBoxplotekPrzeciwsniegowy155mmx2mb.setItems(getSubList(listaNazw, 56, 57));
            comboBoxplotekPrzeciwsniegowy155mmx2mb.setLabel(PLOTEK_PRZECIWSNIEGOWY_155MMX2MB);
            comboBoxplotekPrzeciwsniegowy155mmx3mb.setItems(getSubList(listaNazw, 57, 59));
            comboBoxplotekPrzeciwsniegowy155mmx3mb.setLabel(PLOTEK_PRZECIWSNIEGOWY_155MMX3MB);
            comboBoxmembranaDachowa.setItems(getSubList(listaNazw, 59, 64));
            comboBoxmembranaDachowa.setLabel(MEMBRANA_DACHOWA);
            comboBoxtasmaDoLaczeniaMembarnIFolii.setItems(getSubList(listaNazw, 64, 67));
            comboBoxtasmaDoLaczeniaMembarnIFolii.setLabel(TASMA_DO_LACZENIA_MEMBRAN_I_FOLII);
            comboBoxtasmaReparacyjna.setItems(getSubList(listaNazw, 67, 69));
            comboBoxtasmaReparacyjna.setLabel(TASMA_REPARACYJNA);
            comboBoxblachaAluminiowa.setItems(getSubList(listaNazw, 69, 72));
            comboBoxblachaAluminiowa.setLabel(BLACHA_ALUMINIOWA);
            comboBoxceglaKlinkierowa.setItems(getSubList(listaNazw, 72, 73));
            comboBoxceglaKlinkierowa.setLabel(CEGLA_KLINKIEROWA);
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

