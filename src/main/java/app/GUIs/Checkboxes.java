package app.GUIs;

import app.controllers.ControllerVaadin;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static app.service.AllFields.*;

@Route(value = Checkboxes.CHECKBOXES)
public class Checkboxes extends SplitLayout {

    public static final String CHECKBOXES = "Checkboxes";
    private static final String LABEL_CHECKBOXES = "ALL CHECKBOXES";

    private CheckboxGroup<String> checkboxes;

    private Button createComboboxes = new Button("utworz te jebane comboboxy");

    private ControllerVaadin controllerVaadin;

    public List<String> allLabelsToComboBox = new ArrayList<>();


    @Autowired
    public Checkboxes(ControllerVaadin controllerVaadin) {
        this.controllerVaadin = Objects.requireNonNull(controllerVaadin);

        setOrientation(Orientation.VERTICAL);

        addToPrimary(controllerVaadin.routerLink());

        Board board = new Board();
        setCheckboxes(new CheckboxGroup<>());
        createListOfLabels();
        getCheckboxes().setItems(allLabelsToComboBox);
        getCheckboxes().setLabel(LABEL_CHECKBOXES);
        board.add(clearButton());
        board.add(createComboboxes);
        board.add(getCheckboxes());

        addToSecondary(board);

        ustawieniaStrony();
    }

    public void createListOfLabels() {
        allLabelsToComboBox = Arrays.asList(TASMA_KELNICOWA, WSPORNIK_LATY_KALENICOWEJ, TASMA_DO_OBROBKI_KOMINA, LISTWA_WYKONCZENIOWA_ALUMINIOWA, KOSZ_DACHOWY_ALUMINIOWY_2MB,
                KLAMRA_DO_MOCOWANIA_KOSZA, KLIN_USZCZELNIAJACY_KOSZ, GRZEBIEN_OKAPOWY, KRATKA_ZABEZPIECZAJACA_PRZED_PTACTWEM, PAS_OKAPOWY, KLAMRA_DO_GASIORA,
                SPINKA_DO_DACHOWKI, SPINKA_DO_DACHOWKI_CIETEJ, LAWA_KOMINIARSKA, STOPIEN_KOMINIARSKI, PLOTEK_PRZECIWSNIEGOWY_155MMX2MB, PLOTEK_PRZECIWSNIEGOWY_155MMX3MB,
                MEMBRANA_DACHOWA, TASMA_DO_LACZENIA_MEMBRAN_I_FOLII, TASMA_REPARACYJNA, BLACHA_ALUMINIOWA, CEGLA_KLINKIEROWA);
    }

    private void ustawieniaStrony() {
        Board board = new Board();
        board.addRow(controllerVaadin.routerLink());
        board.getStyle().set("background", "#DCDCDC");
        addToPrimary(board);
        setPrimaryStyle("minWidth", "1280px");
        setPrimaryStyle("maxWidth", "1280px");
        setPrimaryStyle("minHeight", "70px");
        setPrimaryStyle("maxHeight", "700px");
        setSecondaryStyle("minWidth", "1280px");
        setSecondaryStyle("maxWidth", "1280px");
        setSecondaryStyle("minHeight", "500px");
        setSecondaryStyle("maxHeight", "500px");
    }

    private Button clearButton() {
        Button button = new Button("Wyczyść zaznaczone");
        button.addClickListener(click -> getCheckboxes().clear());
        return button;
    }

    public CheckboxGroup<String> getCheckboxes() {
        return checkboxes;
    }

    public void setCheckboxes(CheckboxGroup<String> checkboxes) {
        this.checkboxes = checkboxes;
    }
}
