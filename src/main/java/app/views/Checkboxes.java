package app.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static app.service.Labels.*;

@Route(value = Checkboxes.CHECKBOXES, layout = MainView.class)
public class Checkboxes extends VerticalLayout {

    public static final String CHECKBOXES = "accesories/checkboxes";
    private static final String LABEL_CHECKBOXES = "ALL CHECKBOXES";

    private CheckboxGroup<String> checkboxes = new CheckboxGroup<>();

    private List<String> allLabelsToComboBox = new ArrayList<>();
    private VerticalLayout verticalLayout = new VerticalLayout();

    Checkboxes() {

        createListOfLabels();
        getCheckboxes().setItems(allLabelsToComboBox);
        getCheckboxes().setLabel(LABEL_CHECKBOXES);
        Label label = new Label(" ");
        FormLayout board = new FormLayout();
        board.add(clearButton(), label);
        verticalLayout.add(board);
        verticalLayout.add(getCheckboxes());

        add(verticalLayout);
    }

    private void createListOfLabels() {
        allLabelsToComboBox = Arrays.asList(TASMA_KELNICOWA, WSPORNIK_LATY_KALENICOWEJ, TASMA_DO_OBROBKI_KOMINA, LISTWA_WYKONCZENIOWA_ALUMINIOWA, KOSZ_DACHOWY_ALUMINIOWY_2MB,
                KLAMRA_DO_MOCOWANIA_KOSZA, KLIN_USZCZELNIAJACY_KOSZ, GRZEBIEN_OKAPOWY, KRATKA_ZABEZPIECZAJACA_PRZED_PTACTWEM, PAS_OKAPOWY, KLAMRA_DO_GASIORA,
                SPINKA_DO_DACHOWKI, SPINKA_DO_DACHOWKI_CIETEJ, LAWA_KOMINIARSKA, STOPIEN_KOMINIARSKI, PLOTEK_PRZECIWSNIEGOWY_155MMX2MB, PLOTEK_PRZECIWSNIEGOWY_155MMX3MB,
                MEMBRANA_DACHOWA, TASMA_DO_LACZENIA_MEMBRAN_I_FOLII, TASMA_REPARACYJNA, BLACHA_ALUMINIOWA, CEGLA_KLINKIEROWA);
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
