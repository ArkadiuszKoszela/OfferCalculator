package pl.koszela.spring.crud;

import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.main.*;
import pl.koszela.spring.repositories.*;
import pl.koszela.spring.service.HasLogger;
import pl.koszela.spring.service.NotificationInterface;

import java.util.*;

@Service
public class UpdateUser  implements HasLogger {

    private PersonalDataRepository personalDataRepository;
    private UsersRepo usersRepo;
    private InputRepository inputRepository;
    private TilesRepository tilesRepository;
    private AccesoriesRepository accesoriesRepository;
    private GutterRepository gutterRepository;

    @Autowired
    public UpdateUser(PersonalDataRepository personalDataRepository, UsersRepo usersRepo, InputRepository inputRepository, TilesRepository tilesRepository, AccesoriesRepository accesoriesRepository, GutterRepository gutterRepository) {
        this.personalDataRepository = Objects.requireNonNull(personalDataRepository);
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.inputRepository = Objects.requireNonNull(inputRepository);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);
        this.gutterRepository = Objects.requireNonNull(gutterRepository);
    }

    public void updateUser() {
        PersonalData data = (PersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalData");
        Optional<PersonalData> personalData = personalDataRepository.findEntityPersonalDataByNameAndSurnameEquals(data.getName(), data.getSurname());

        List<InputData> setInput = (List<InputData>) VaadinSession.getCurrent().getSession().getAttribute("inputData");
        Set<Accessories> resultAccesories = (Set<Accessories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");

        if (personalData.isPresent()) {
            Optional<User> userFromRepo = usersRepo.findUserByPersonalDataEquals(personalData.get());
            if (userFromRepo.isPresent()) {
                Set<Tiles> allTilesFromRepo = (Set<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("tiles");
                List<Gutter> list = (List<Gutter>) VaadinSession.getCurrent().getSession().getAttribute("gutter");
                User userToUpdate = userFromRepo.get();
                userToUpdate.setEntityUserGutter(list);
                userToUpdate.setInputData(setInput);
                userToUpdate.setUserAccesories(resultAccesories);
                userToUpdate.getTiles().clear();
                userToUpdate.setTiles(allTilesFromRepo);

                gutterRepository.saveAll(list);
                accesoriesRepository.saveAll(resultAccesories);
                inputRepository.saveAll(setInput);
                tilesRepository.saveAll(allTilesFromRepo);

                usersRepo.save(userToUpdate);
                getLogger().info("Updated user - " + userFromRepo.get().getPersonalData().getName() + " " + userFromRepo.get().getPersonalData().getSurname());
                NotificationInterface.notificationOpen("Zaktualizowałem dane dla użytkownika: " + userToUpdate.getPersonalData().getName() + " " + userToUpdate.getPersonalData().getSurname() + "    :)", NotificationVariant.LUMO_SUCCESS);
            } else {
                NotificationInterface.notificationOpen("Coś poszło nie tak", NotificationVariant.LUMO_ERROR);
            }
        } else {
            NotificationInterface.notificationOpen("Coś poszło nie tak - nie ma takich danych", NotificationVariant.LUMO_ERROR);
        }
    }
}