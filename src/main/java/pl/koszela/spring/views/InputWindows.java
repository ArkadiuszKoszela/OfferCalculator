package pl.koszela.spring.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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

@Route(value = InputWindows.WINDOWS, layout = MainView.class)
public class InputWindows extends VerticalLayout  implements MenuBarInterface {

    private WindowsRepository windowsRepository;
    private KolnierzRepository kolnierzRepository;
    private UsersRepo usersRepo;

    public static final String WINDOWS = "windows";

    private ComboBox<String> comboboxWindows = new ComboBox<>("Okna");
    private ComboBox<String> comboboxKolnierz = new ComboBox<>("Kołnierz");
    private Button save = new Button("Zapisz dane");

    public InputWindows(WindowsRepository windowsRepository, KolnierzRepository kolnierzRepository, UsersRepo usersRepo) {
        this.windowsRepository = Objects.requireNonNull(windowsRepository);
        this.kolnierzRepository = Objects.requireNonNull(kolnierzRepository);
        this.usersRepo = Objects.requireNonNull(usersRepo);

        save.addClickListener(buttonClickEvent -> loadUser());
        add(putDataInComboBox(), save);
        add(putInComboBox(),getLabel(" "));
    }

    private void loadUser() {
        EntityUser entityUser = (EntityUser) VaadinSession.getCurrent().getAttribute("user");

        if (saveInputWindows() != null) {
            entityUser.setEntityWindows(saveInputWindows());
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

    private ComboBox<String> putInComboBox(){
        comboboxKolnierz.setItems(getAllNameKolnierz());
        return comboboxKolnierz;
    }

    private EntityWindows saveInputWindows() {
        EntityWindows entityWindows = new EntityWindows();
        entityWindows.setName(comboboxWindows.getValue());

        windowsRepository.save(entityWindows);
        return entityWindows;
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
        menuBar.addItem(new RouterLink("Dalej", CreateOffer.class));
        return menuBar;
    }

}
