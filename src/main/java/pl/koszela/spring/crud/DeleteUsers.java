package pl.koszela.spring.crud;

import com.vaadin.flow.component.notification.NotificationVariant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.*;
import pl.koszela.spring.entities.PersonalData;
import pl.koszela.spring.repositories.*;
import pl.koszela.spring.service.NotificationInterface;

import java.util.*;

@Service
public class DeleteUsers {
    private final static Logger logger = Logger.getLogger(DeleteUsers.class);

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

    public void removeUser(PersonalData entityPersonalData) {
        Optional<PersonalData> personalData = personalDataRepository.findById(entityPersonalData.getId());

        if (personalData.isPresent()) {
            Optional<User> userFromRepo = usersRepo.findUserByPersonalDataEquals(personalData.get());
            if (userFromRepo.isPresent()) {
                User userToRemove = userFromRepo.get();

                userToRemove.getEntityUserGutter().clear();
                gutterRepository.flush();
                userToRemove.getEntityUserGutter().clear();
                inputRepository.flush();
                userToRemove.getInputData().clear();
                userToRemove.getTiles().clear();
                tilesRepository.flush();
                usersRepo.deleteById(userToRemove.getId());
                usersRepo.flush();
                logger.info("Deleted user - " + personalData.get().getName() + " " + personalData.get().getSurname());
                NotificationInterface.notificationOpen("Usunąłem użytkownika: " + userToRemove.getPersonalData().getName() + " " + userToRemove.getPersonalData().getSurname() + "   papa  :(", NotificationVariant.LUMO_SUCCESS);
            }
        } else {
            NotificationInterface.notificationOpen("Nie ma takiego użytkownika " + entityPersonalData.getName() + " " + entityPersonalData.getSurname() + " w bazie danych    :(", NotificationVariant.LUMO_ERROR);
        }
    }
}