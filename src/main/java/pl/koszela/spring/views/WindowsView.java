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
    private UsersRepo usersRepo;

    public static final String WINDOWS = "windows";

    private ComboBox<String> comboboxWindows = new ComboBox<>("Okna");
    private ComboBox<String> comboboxKolnierz = new ComboBox<>("Kołnierz");
    private Button save = new Button("Zapisz dane");

    public WindowsView(WindowsRepository windowsRepository, KolnierzRepository kolnierzRepository, UsersRepo usersRepo) {
        this.windowsRepository = Objects.requireNonNull(windowsRepository);
        this.kolnierzRepository = Objects.requireNonNull(kolnierzRepository);
        this.usersRepo = Objects.requireNonNull(usersRepo);

        add(menu());
        add(addLayout());
    }

    private FormLayout addLayout() {
        save.addClickListener(buttonClickEvent -> loadUser());
        FormLayout formLayout = new FormLayout();
        formLayout.add(putDataInComboBox(), save);
        formLayout.add(putInComboBox(), getLabel(" "));
        return formLayout;
    }

    private void loadUser() {
        EntityUser entityUser = (EntityUser) VaadinSession.getCurrent().getAttribute("user");

        if (findWindowsByName() != null) {
            entityUser.setEntityWindows(findWindowsByName());
            entityUser.setHasWindows(true);
            getNotificationSucces("Okna zapisane");
        } else {
            getNotificationError("Okna niezapisane");
        }
    }

    private ComboBox<String> putDataInComboBox() {
        comboboxWindows.setItems(getAllNameWindows());
        return comboboxWindows;
    }

    private ComboBox<String> putInComboBox() {
        comboboxKolnierz.setItems(getAllNameKolnierz());
        return comboboxKolnierz;
    }

    private EntityWindows findWindowsByName() {
        EntityWindows entityWindows = windowsRepository.findByName(comboboxWindows.getValue());
        return entityWindows;
    }

    private EntityKolnierz findKolnierzByName() {
        EntityKolnierz entityKolnierz = kolnierzRepository.findByName(comboboxKolnierz.getValue());
        return entityKolnierz;
    }

    private void saveUser() {
        EntityUser entityUser = (EntityUser) VaadinSession.getCurrent().getAttribute("user");
        entityUser.setEntityWindows(findWindowsByName());
        entityUser.setEntityKolnierz(findKolnierzByName());
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
            saveUser();
            getUI().ifPresent(ui -> ui.navigate(CREATE_OFFER));
        });
        menuBar.addItem(button);
        return menuBar;
    }

}
