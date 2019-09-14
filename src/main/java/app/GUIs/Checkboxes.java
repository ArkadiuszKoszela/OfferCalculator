package app.GUIs;

import app.controllers.ControllerVaadin;
import app.service.Layout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.formlayout.FormLayout;
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

@Route(value = Checkboxes.CHECKBOXES)
public class Checkboxes extends SplitLayout implements Layout {

    public static final String CHECKBOXES = "Checkboxes";
    private static final String LABEL_CHECKBOXES = "ALL CHECKBOXES";

    private CheckboxGroup<String> checkboxes;

    private ControllerVaadin controllerVaadin;

    private List<String> allLabelsToComboBox = new ArrayList<>();
    private FormLayout board;


    @Autowired
    public Checkboxes(ControllerVaadin controllerVaadin) {
        this.controllerVaadin = Objects.requireNonNull(controllerVaadin);

        setOrientation(Orientation.VERTICAL);

        addToPrimary(ustawieniaStrony(controllerVaadin));

        board = new FormLayout();
        setCheckboxes(new CheckboxGroup<>());
        createListOfLabels();
        getCheckboxes().setItems(allLabelsToComboBox);
        getCheckboxes().setLabel(LABEL_CHECKBOXES);
        board.add(clearButton());
        Button createComboboxes = new Button("Utwórz ComboBoxy");
        board.add(createComboboxes);
        board.add(getCheckboxes());

        addToSecondary(getSideMenu(controllerVaadin));
    }

    private void createListOfLabels() {
        allLabelsToComboBox = Arrays.asList(TASMA_KELNICOWA, WSPORNIK_LATY_KALENICOWEJ, TASMA_DO_OBROBKI_KOMINA, LISTWA_WYKONCZENIOWA_ALUMINIOWA, KOSZ_DACHOWY_ALUMINIOWY_2MB,
                KLAMRA_DO_MOCOWANIA_KOSZA, KLIN_USZCZELNIAJACY_KOSZ, GRZEBIEN_OKAPOWY, KRATKA_ZABEZPIECZAJACA_PRZED_PTACTWEM, PAS_OKAPOWY, KLAMRA_DO_GASIORA,
                SPINKA_DO_DACHOWKI, SPINKA_DO_DACHOWKI_CIETEJ, LAWA_KOMINIARSKA, STOPIEN_KOMINIARSKI, PLOTEK_PRZECIWSNIEGOWY_155MMX2MB, PLOTEK_PRZECIWSNIEGOWY_155MMX3MB,
                MEMBRANA_DACHOWA, TASMA_DO_LACZENIA_MEMBRAN_I_FOLII, TASMA_REPARACYJNA, BLACHA_ALUMINIOWA, CEGLA_KLINKIEROWA);
    }

    @Override
    public SplitLayout getSideMenu(ControllerVaadin controllerVaadin) {
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.addToPrimary(controllerVaadin.sideMenuAccesories());
        splitLayout.addToSecondary(board);
        getSideMenuSettings(splitLayout);
        return splitLayout;
    }

    private Button clearButton() {
        Button button = new Button("Wyczyść zaznaczone");
        button.addClickListener(click -> getCheckboxes().clear());
        return button;
    }

    private CheckboxGroup<String> getCheckboxes() {
        return checkboxes;
    }

    private void setCheckboxes(CheckboxGroup<String> checkboxes) {
        this.checkboxes = checkboxes;
    }
}
