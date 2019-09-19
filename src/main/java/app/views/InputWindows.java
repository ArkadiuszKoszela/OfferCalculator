package app.views;

import app.entities.EntityUser;
import app.entities.EntityWindows;
import app.repositories.UsersRepo;
import app.repositories.WindowsRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static app.inputFields.ServiceNotification.getNotificationError;
import static app.inputFields.ServiceNotification.getNotificationSucces;

@Route(value = InputWindows.WINODWS, layout = MainView.class)
public class InputWindows extends VerticalLayout {

    private WindowsRepository windowsRepository;
    private UsersRepo usersRepo;

    public static final String WINODWS = "windows";

    private ComboBox<String> comboboxWindows = new ComboBox<>();
    private ComboBox<String> comboBoxUsers = new ComboBox<>("Wczytaj klienta: ");
    private Button save = new Button("Zapisz dane");

    public InputWindows(WindowsRepository windowsRepository, UsersRepo usersRepo) {
        this.windowsRepository = Objects.requireNonNull(windowsRepository);
        this.usersRepo = Objects.requireNonNull(usersRepo);

        save.addClickListener(buttonClickEvent -> loadUser());
        loadUserComboBox();
        add(putDataInComboBox(), save);
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

    private EntityWindows saveInputWindows(){
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
        List<String> allName = new ArrayList<>();
        allWindows().forEach(e -> allName.add(e.getName()));
        return allName;
    }

    private List<EntityWindows> allWindows() {
        Iterable<EntityWindows> allWindowsFromRepository = windowsRepository.findAll();
        List<EntityWindows> allWindows = new ArrayList<>();
        allWindowsFromRepository.forEach(allWindows::add);
        return allWindows;
    }


}
