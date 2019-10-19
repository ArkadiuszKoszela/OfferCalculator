package pl.koszela.spring.crud;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.*;
import pl.koszela.spring.repositories.*;

import java.util.*;

import static pl.koszela.spring.service.ServiceNotification.getNotificationSucces;

@Service
public class CreateUser {

    private PersonalDataRepository personalDataRepository;
    private UsersRepo usersRepo;
    private InputDataTilesRepository inputDataTilesRepository;
    private TilesRepository tilesRepository;
    private AccesoriesRepository accesoriesRepository;

    @Autowired
    public CreateUser(PersonalDataRepository personalDataRepository, UsersRepo usersRepo, InputDataTilesRepository inputDataTilesRepository, TilesRepository tilesRepository, AccesoriesRepository accesoriesRepository) {
        this.personalDataRepository = Objects.requireNonNull(personalDataRepository);
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.inputDataTilesRepository = Objects.requireNonNull(inputDataTilesRepository);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);
    }

    public void saveUser() {
        EntityPersonalData entityPersonalData = (EntityPersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalDataFromRepo");
        EntityInputDataTiles entityInputDataTiles = (EntityInputDataTiles) VaadinSession.getCurrent().getSession().getAttribute("tilesInputFromRepo");
        Set<Tiles> allTiles = (Set<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("allTilesFromRepo");
        Set<EntityAccesories> resultAccesories = (Set<EntityAccesories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");

        EntityUser newUser = new EntityUser();
        newUser.setEntityPersonalData(entityPersonalData);
        newUser.setEntityInputDataTiles(entityInputDataTiles);
        newUser.setResultAccesories(resultAccesories);
        newUser.setTiles(allTiles);

        personalDataRepository.save(entityPersonalData);
        inputDataTilesRepository.save(entityInputDataTiles);
        accesoriesRepository.saveAll(resultAccesories);
        tilesRepository.saveAll(allTiles);

        usersRepo.save(newUser);
        getNotificationSucces("Zapisałem użytkownika - " + entityPersonalData.getName() + " " + entityPersonalData.getSurname() + "    :)");

//        VaadinSession.getCurrent().getSession().removeAttribute("personalData");
//        VaadinSession.getCurrent().getSession().removeAttribute("tilesInput");
//        VaadinSession.getCurrent().getSession().removeAttribute("accesoriesInput");
//        VaadinSession.getCurrent().getSession().removeAttribute("entityWindows");
//        VaadinSession.getCurrent().getSession().removeAttribute("entityKolnierz");
//        VaadinSession.getCurrent().getSession().removeAttribute("allTiles");
    }
}