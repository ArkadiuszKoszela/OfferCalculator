package pl.koszela.spring.crud;

import com.vaadin.flow.component.combobox.ComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.*;
import pl.koszela.spring.entities.personalData.EntityPersonalData;
import pl.koszela.spring.repositories.*;

import java.util.*;

import static pl.koszela.spring.service.ServiceNotification.getNotificationError;
import static pl.koszela.spring.service.ServiceNotification.getNotificationSucces;

@Service
public class DeleteUsers {

    private PersonalDataRepository personalDataRepository;
    private UsersRepo usersRepo;
    private InputRepository inputRepository;
    private GutterRepository gutterRepository;
    private TilesRepository tilesRepository;

    @Autowired
    public DeleteUsers(PersonalDataRepository personalDataRepository, UsersRepo usersRepo, InputRepository inputRepository, GutterRepository gutterRepository, TilesRepository tilesRepository) {
        this.personalDataRepository = Objects.requireNonNull(personalDataRepository);
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.inputRepository = Objects.requireNonNull(inputRepository);
        this.gutterRepository = Objects.requireNonNull(gutterRepository);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
    }

    public void removeUser(ComboBox<String> comboBox) {
        String[] nameAndSurname = comboBox.getValue().split(" ");

        Optional<EntityPersonalData> personalData = personalDataRepository.findEntityPersonalDataByNameAndSurnameEquals(nameAndSurname[0], nameAndSurname[1]);

        if (personalData.isPresent()) {
            Optional<EntityUser> userFromRepo = usersRepo.findEntityUserByEntityPersonalDataEquals(personalData.get());
            if (userFromRepo.isPresent()) {
                EntityUser userToRemove = userFromRepo.get();

                userToRemove.getEntityUserGutter().clear();
                gutterRepository.flush();
                userToRemove.getEntityUserGutter().clear();
                inputRepository.flush();
                userToRemove.getInputData().clear();
                userToRemove.getTiles().clear();
                tilesRepository.flush();
                usersRepo.deleteById(userToRemove.getId());
                usersRepo.flush();

                getNotificationSucces("Usunąłem użytkownika: " + userToRemove.getEntityPersonalData().getName() + " " + userToRemove.getEntityPersonalData().getSurname() + "   papa  :(");
            }
        } else {
            getNotificationError("Nie ma takiego użytkownika " + nameAndSurname[0] + " " + nameAndSurname[1] + " w bazie danych    :(");
        }
    }
}