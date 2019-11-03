package pl.koszela.spring.crud;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.EntityPersonalData;
import pl.koszela.spring.entities.EntityUser;
import pl.koszela.spring.repositories.*;

import java.util.Objects;
import java.util.Optional;

import static pl.koszela.spring.service.ServiceNotification.getNotificationError;
import static pl.koszela.spring.service.ServiceNotification.getNotificationSucces;

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

    public void removeUser(EntityPersonalData personalDataFromCombobox) {
        Optional<EntityPersonalData> personalData = personalDataRepository.findById(personalDataFromCombobox.getId());

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
                logger.info("Deleted user - " + personalData.get().getName() + " " +personalData.get().getSurname());
                getNotificationSucces("Usunąłem użytkownika: " + userToRemove.getEntityPersonalData().getName() + " " + userToRemove.getEntityPersonalData().getSurname() + "   papa  :(");
            }
        } else {
            getNotificationError("Nie ma takiego użytkownika " + personalData.get().getName() + " " + personalData.get().getSurname() + " w bazie danych    :(");
        }
    }
}