package pl.koszela.spring.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import pl.koszela.spring.entities.EntityKolnierz;
import pl.koszela.spring.entities.EntityUser;
import pl.koszela.spring.entities.EntityWindows;
import pl.koszela.spring.repositories.KolnierzRepository;
import pl.koszela.spring.repositories.UsersRepo;
import pl.koszela.spring.repositories.WindowsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationError;
import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationSucces;
import static pl.koszela.spring.service.Labels.getLabel;

@Route(value = InputWindows.WINODWS, layout = MainView.class)
public class InputWindows extends VerticalLayout {

    private WindowsRepository windowsRepository;
    private KolnierzRepository kolnierzRepository;
    private UsersRepo usersRepo;

    public static final String WINODWS = "windows";

    private ComboBox<String> comboboxWindows = new ComboBox<>("Okna");
    private ComboBox<String> comboboxKolnierz = new ComboBox<>("Kołnierz");
    private ComboBox<String> comboBoxUsers = new ComboBox<>("Wczytaj klienta: ");
    private Button save = new Button("Zapisz dane");

    public InputWindows(WindowsRepository windowsRepository, KolnierzRepository kolnierzRepository, UsersRepo usersRepo) {
        this.windowsRepository = Objects.requireNonNull(windowsRepository);
        this.kolnierzRepository = Objects.requireNonNull(kolnierzRepository);
        this.usersRepo = Objects.requireNonNull(usersRepo);

        save.addClickListener(buttonClickEvent -> loadUser());
        loadUserComboBox();
        add(putDataInComboBox(), save);
        add(putInComboBox(),getLabel(" "));
    }

    private void loadUser() {
        String nameISurname = comboBoxUsers.getValue();
        String[] strings = nameISurname.split(" ");
        EntityUser entityUser = usersRepo.findUsersEntityByNameAndSurnameEquals(strings[0], strings[1]);

        if (saveInputWindows() != null) {
            entityUser.setEntityWindows(saveInputWindows());
            entityUser.setHasWindows(true);
            usersRepo.save(entityUser);
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

    private void loadUserComboBox() {
        if (nameAndSurname().size() > 0) {
            comboBoxUsers.setItems(nameAndSurname());
        }
    }

    private List<String> nameAndSurname() {
        Iterable<EntityUser> allUsersFromRepository = usersRepo.findAll();
        List<String> nameAndSurname = new ArrayList<>();
        allUsersFromRepository.forEach(user -> nameAndSurname.add(user.getName().concat(" ").concat(user.getSurname())));
        return nameAndSurname;
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

}
