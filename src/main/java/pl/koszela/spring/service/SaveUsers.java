package pl.koszela.spring.service;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.EntityUser;
import pl.koszela.spring.repositories.UsersRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationError;
import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationSucces;

@Service
public class SaveUsers {

    private UsersRepo usersRepo;

    @Autowired
    public SaveUsers(UsersRepo usersRepo) {
        this.usersRepo = Objects.requireNonNull(usersRepo);
    }

    public void saveUser(TextField name, TextField surname,
                         TextField adress, TextField telephoneNumber, EmailField email) {
        if (allDataIsCompleted(name, surname)) {

            EntityUser newUser = new EntityUser();
            newUser.setName(name.getValue());
            newUser.setSurname(surname.getValue());
            newUser.setAdress(adress.getValue());
            newUser.setTelephoneNumber(telephoneNumber.getValue());
            newUser.setEmail(email.getValue());
            VaadinSession.getCurrent().setAttribute("user", newUser);
            getNotificationSucces("Zapisałem użytkownika wraz z danymi");
        }
    }

    private boolean allDataIsCompleted(TextField name, TextField surname) {
        if (!isUserInDatabase(name, surname)) {
            return true;
        } else if (allUsers().size() > 0 && isUserInDatabase(name, surname)) {
            getNotificationError("Podany użytkownik znajduję się już w bazie danych");
            return false;
        } else {
            getNotificationError("Nieznany błąd");
            return false;
        }
    }

    private boolean isUserInDatabase(TextField name, TextField surname) {
        EntityUser entityUser = usersRepo.findUsersEntityByNameAndSurnameEquals(name.getValue(), surname.getValue());
        return entityUser != null;
    }

    private List<EntityUser> allUsers() {
        Iterable<EntityUser> iterable = usersRepo.findAll();
        List<EntityUser> userList = new ArrayList<>();
        iterable.forEach(userList::add);
        return userList;
    }
}
