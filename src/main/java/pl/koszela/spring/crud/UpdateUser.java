package pl.koszela.spring.crud;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.*;
import pl.koszela.spring.repositories.*;

import java.util.*;

import static pl.koszela.spring.service.ServiceNotification.getNotificationError;
import static pl.koszela.spring.service.ServiceNotification.getNotificationSucces;

@Service
public class UpdateUser {

    private PersonalDataRepository personalDataRepository;
    private UsersRepo usersRepo;
    private InputDataTilesRepository inputDataTilesRepository;
    private TilesRepository tilesRepository;
    private ResultAccesoriesRepository resultAccesoriesRepository;

    @Autowired
    public UpdateUser(PersonalDataRepository personalDataRepository, UsersRepo usersRepo, InputDataTilesRepository inputDataTilesRepository, TilesRepository tilesRepository, ResultAccesoriesRepository resultAccesoriesRepository) {
        this.personalDataRepository = Objects.requireNonNull(personalDataRepository);
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.inputDataTilesRepository = Objects.requireNonNull(inputDataTilesRepository);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
        this.resultAccesoriesRepository = Objects.requireNonNull(resultAccesoriesRepository);
    }

    public void updateUser() {
        EntityPersonalData data = (EntityPersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalDataFromRepo");
        Optional<EntityPersonalData> personalData = personalDataRepository.findEntityPersonalDataByNameAndSurnameEquals(data.getName(), data.getSurname());

        EntityInputDataTiles entityInputDataTiles = (EntityInputDataTiles) VaadinSession.getCurrent().getSession().getAttribute("tilesInputFromRepo");
        Set<EntityResultAccesories> resultAccesories = (Set<EntityResultAccesories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");

        if (personalData.isPresent()) {
            Optional<EntityUser> userFromRepo = usersRepo.findEntityUserByEntityPersonalDataEquals(personalData.get());
            if (userFromRepo.isPresent()) {
                Set<Tiles> allTilesFromRepo = (Set<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("allTilesFromRepo");
                EntityUser userToUpdate = userFromRepo.get();
                userToUpdate.setEntityInputDataTiles(entityInputDataTiles);
                userToUpdate.setResultAccesories(resultAccesories);
                userToUpdate.getTiles().clear();
                userToUpdate.setTiles(allTilesFromRepo);

                resultAccesoriesRepository.saveAll(resultAccesories);
                inputDataTilesRepository.save(entityInputDataTiles);
                tilesRepository.saveAll(allTilesFromRepo);

                usersRepo.save(userToUpdate);
                getNotificationSucces("Zaktualizowałem dane dla użytkownika: " + userToUpdate.getEntityPersonalData().getName() + " " + userToUpdate.getEntityPersonalData().getSurname() + "    :)");
            } else {
                getNotificationError("Coś poszło nie tak");
            }
        } else {
            getNotificationError("Coś poszło nie tak - nie ma takich danych");
        }

//        VaadinSession.getCurrent().getSession().removeAttribute("personalDataFromRepo");
//        VaadinSession.getCurrent().getSession().removeAttribute("tilesInputFromRepo");
//        VaadinSession.getCurrent().getSession().removeAttribute("accesoriesInputFromRepo");
//        VaadinSession.getCurrent().getSession().removeAttribute("entityWindowsFromRepo");
//        VaadinSession.getCurrent().getSession().removeAttribute("entityKolnierzFromRepo");
//        VaadinSession.getCurrent().getSession().removeAttribute("allTilesFromRepo");
    }
}