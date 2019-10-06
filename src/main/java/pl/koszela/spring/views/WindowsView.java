package pl.koszela.spring.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import pl.koszela.spring.entities.EntityKolnierz;
import pl.koszela.spring.entities.EntityUser;
import pl.koszela.spring.entities.EntityWindows;
import pl.koszela.spring.repositories.KolnierzRepository;
import pl.koszela.spring.repositories.UsersRepo;
import pl.koszela.spring.repositories.WindowsRepository;
import pl.koszela.spring.service.MenuBarInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationError;
import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationSucces;
import static pl.koszela.spring.service.Labels.getLabel;
import static pl.koszela.spring.views.AccesoriesView.SELECT_ACCESORIES;
import static pl.koszela.spring.views.OfferView.CREATE_OFFER;

@Route(value = WindowsView.WINDOWS, layout = MainView.class)
public class WindowsView extends VerticalLayout implements MenuBarInterface {

    private WindowsRepository windowsRepository;
    private KolnierzRepository kolnierzRepository;

    public static final String WINDOWS = "windows";

    private ComboBox<String> comboboxWindows = new ComboBox<>("Okna");
    private ComboBox<String> comboboxKolnierz = new ComboBox<>("Kołnierz");

    public WindowsView(WindowsRepository windowsRepository, KolnierzRepository kolnierzRepository) {
        this.windowsRepository = Objects.requireNonNull(windowsRepository);
        this.kolnierzRepository = Objects.requireNonNull(kolnierzRepository);

        add(menu());
        add(addLayout());
    }

    private FormLayout addLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(putDataInComboBox(), getLabel(" "));
        formLayout.add(putInComboBox(), getLabel(" "));
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

    @Override
    public MenuBar menu() {
        MenuBar menuBar = new MenuBar();
        Button button = new Button("Dalej");
        button.addClickListener(buttonClickEvent -> {
            getUI().ifPresent(ui -> ui.navigate(CREATE_OFFER));
        });
        menuBar.addItem(button);
        return menuBar;
    }

}
