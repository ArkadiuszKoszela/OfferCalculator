package pl.koszela.spring.crud;

import com.vaadin.flow.server.VaadinSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.*;
import pl.koszela.spring.entities.accesories.EntityAccesories;
import pl.koszela.spring.entities.gutter.EntityGutter;
import pl.koszela.spring.entities.personalData.EntityPersonalData;
import pl.koszela.spring.entities.tiles.Tiles;
import pl.koszela.spring.repositories.*;

import java.util.*;

import static pl.koszela.spring.service.ServiceNotification.getNotificationError;
import static pl.koszela.spring.service.ServiceNotification.getNotificationSucces;

@Service
public class UpdateUser {
    private final static Logger logger = Logger.getLogger(UpdateUser.class);

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
        EntityPersonalData data = (EntityPersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalData");
        Optional<EntityPersonalData> personalData = personalDataRepository.findEntityPersonalDataByNameAndSurnameEquals(data.getName(), data.getSurname());

        List<InputData> setInput = (List<InputData>) VaadinSession.getCurrent().getSession().getAttribute("inputData");
        Set<EntityAccesories> resultAccesories = (Set<EntityAccesories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");

        if (personalData.isPresent()) {
            Optional<EntityUser> userFromRepo = usersRepo.findEntityUserByEntityPersonalDataEquals(personalData.get());
            if (userFromRepo.isPresent()) {
                Set<Tiles> allTilesFromRepo = (Set<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("tiles");
                List<EntityGutter> list = (List<EntityGutter>) VaadinSession.getCurrent().getSession().getAttribute("gutter");
                EntityUser userToUpdate = userFromRepo.get();
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
                logger.info("Updated user - " + userFromRepo.get().getEntityPersonalData().getName() + " " + userFromRepo.get().getEntityPersonalData().getSurname());
                getNotificationSucces("Zaktualizowałem dane dla użytkownika: " + userToUpdate.getEntityPersonalData().getName() + " " + userToUpdate.getEntityPersonalData().getSurname() + "    :)");
            } else {
                getNotificationError("Coś poszło nie tak");
            }
        } else {
            getNotificationError("Coś poszło nie tak - nie ma takich danych");
        }
    }
}