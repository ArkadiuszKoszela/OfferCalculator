package pl.koszela.spring.service;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.EntityPersonalData;
import pl.koszela.spring.entities.EntityUser;
import pl.koszela.spring.repositories.PersonalDataRepository;
import pl.koszela.spring.repositories.UsersRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationError;
import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationSucces;

@Service
public class SaveUsers {

    private PersonalDataRepository personalDataRepository;

    @Autowired
    public SaveUsers(PersonalDataRepository personalDataRepository) {
        this.personalDataRepository = Objects.requireNonNull(personalDataRepository);
    }

    public void saveUser(TextField name, TextField surname,
                         TextField adress, TextField telephoneNumber, EmailField email) {
        if (allDataIsCompleted(name, surname)) {

            EntityUser newUser = new EntityUser();
            EntityPersonalData personalData = EntityPersonalData.builder()
                    .name(name.getValue())
                    .surname(surname.getValue())
                    .adress(adress.getValue())
                    .telephoneNumber(telephoneNumber.getValue())
                    .email(email.getValue())
                    .build();
            newUser.setEntityPersonalData(personalData);

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
        EntityPersonalData personalData = personalDataRepository.findUsersEntityByNameAndSurnameEquals(name.getValue(), surname.getValue());
        return personalData != null;
    }

    private List<EntityPersonalData> allUsers() {
        Iterable<EntityPersonalData> iterable = personalDataRepository.findAll();
        List<EntityPersonalData> userList = new ArrayList<>();
        iterable.forEach(userList::add);
        return userList;
    }
}
