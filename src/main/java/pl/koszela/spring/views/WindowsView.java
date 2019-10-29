package pl.koszela.spring.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import pl.koszela.spring.entities.EntityKolnierz;
import pl.koszela.spring.entities.EntityWindows;
import pl.koszela.spring.service.ServiceNotification;
import pl.koszela.spring.repositories.KolnierzRepository;
import pl.koszela.spring.repositories.WindowsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Route(value = WindowsView.WINDOWS, layout = MainView.class)
public class WindowsView extends VerticalLayout {

    private WindowsRepository windowsRepository;
    private KolnierzRepository kolnierzRepository;

    static final String WINDOWS = "windows";

    private ComboBox<String> comboboxWindows = new ComboBox<>("Okna");
    private ComboBox<String> comboboxKolnierz = new ComboBox<>("Kołnierz");

    public WindowsView(WindowsRepository windowsRepository, KolnierzRepository kolnierzRepository) {
        this.windowsRepository = Objects.requireNonNull(windowsRepository);
        this.kolnierzRepository = Objects.requireNonNull(kolnierzRepository);

        add(addLayout());
    }

    private FormLayout addLayout() {
        FormLayout formLayout = new FormLayout();
        FormLayout.ResponsiveStep responsiveStep = new FormLayout.ResponsiveStep("5px", 1);
        formLayout.setResponsiveSteps(responsiveStep);
        formLayout.add(putDataInComboBox(), putInComboBox());
        return formLayout;
    }

    private ComboBox<String> putDataInComboBox() {
        comboboxWindows.setItems(getAllNameWindows());
        return comboboxWindows;
    }

    private ComboBox<String> putInComboBox() {
        comboboxKolnierz.setItems(getAllNameKolnierz());
        return comboboxKolnierz;
    }

    private List<String> getAllNameWindows() {
        Iterable<EntityWindows> allWindowsFromRepository = windowsRepository.findAll();
        List<String> allWindows = new ArrayList<>();
        allWindowsFromRepository.forEach(e -> allWindows.add(e.getName()));
        return allWindows;
    }

    private List<String> getAllNameKolnierz() {
        Iterable<EntityKolnierz> allKolnierzFromRepository = kolnierzRepository.findAll();
        List<String> allKolnierz = new ArrayList<>();
        allKolnierzFromRepository.forEach(e -> allKolnierz.add(e.getName()));
        return allKolnierz;
    }

    private void save() {
        EntityKolnierz entityKolnierz = new EntityKolnierz();
        entityKolnierz.setName(comboboxKolnierz.getValue());
        EntityWindows entityWindows = new EntityWindows();
        entityWindows.setName(comboboxWindows.getValue());
        VaadinSession.getCurrent().setAttribute("entityKolnierz", entityKolnierz);
        VaadinSession.getCurrent().setAttribute("entityWindows", entityWindows);
    }
}