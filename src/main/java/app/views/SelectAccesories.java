package app.views;

import app.entities.EntityAccesories;
import app.repositories.AccesoriesRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static app.service.Labels.*;

@Route(value = SelectAccesories.SELECT_ACCESORIES, layout = MainView.class)
public class SelectAccesories extends VerticalLayout {

    public static final String SELECT_ACCESORIES = "accesoriesRepository/select";
    private AccesoriesRepository accesoriesRepository;

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

    private Button calculateAccesories = new Button("Oblicz AccesoriesRepository");

    @Autowired
    public SelectAccesories(AccesoriesRepository accesoriesRepository) {
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);

        add(formLayoutAccesories());
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

    private void setValues(ComboBox<String> comboBox, List<String> listaNazw, int poczatek, int koniec){
        comboBox.setItems(getSubList(listaNazw, poczatek, koniec));
        comboBox.setValue(listaNazw.get(poczatek));
    }

    private List<String> getSubList(List<String> listaNazw, int poczatek, int koniec) {
        return listaNazw.subList(poczatek, koniec);
    }
}

