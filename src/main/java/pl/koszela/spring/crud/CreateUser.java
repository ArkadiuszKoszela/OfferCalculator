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
    private ResultAccesoriesRepository resultAccesoriesRepository;

    @Autowired
    public CreateUser(PersonalDataRepository personalDataRepository, UsersRepo usersRepo, InputDataTilesRepository inputDataTilesRepository, TilesRepository tilesRepository, ResultAccesoriesRepository resultAccesoriesRepository) {
        this.personalDataRepository = Objects.requireNonNull(personalDataRepository);
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.inputDataTilesRepository = Objects.requireNonNull(inputDataTilesRepository);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
        this.resultAccesoriesRepository = Objects.requireNonNull(resultAccesoriesRepository);
    }

    public void saveUser() {
        EntityPersonalData entityPersonalData = (EntityPersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalData");
        EntityInputDataTiles entityInputDataTiles = (EntityInputDataTiles) VaadinSession.getCurrent().getSession().getAttribute("tilesInputFromRepo");
        List<Tiles> allTiles = (List<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("allTiles");
        Set<EntityResultAccesories> resultAccesories = (Set<EntityResultAccesories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");

        EntityUser newUser = new EntityUser();
        newUser.setEntityPersonalData(entityPersonalData);
        newUser.setEntityInputDataTiles(entityInputDataTiles);
        newUser.setResultAccesories(resultAccesories);
        newUser.setTiles(new HashSet<>(allTiles));

        personalDataRepository.save(entityPersonalData);
        inputDataTilesRepository.save(entityInputDataTiles);
        resultAccesoriesRepository.saveAll(resultAccesories);
        tilesRepository.saveAll(allTiles);

        usersRepo.save(newUser);
        getNotificationSucces("Zapisałem użytkownika - " + entityPersonalData.getName() + " " + entityPersonalData.getSurname() + "    :)");

        VaadinSession.getCurrent().getSession().removeAttribute("personalData");
        VaadinSession.getCurrent().getSession().removeAttribute("tilesInput");
        VaadinSession.getCurrent().getSession().removeAttribute("accesoriesInput");
        VaadinSession.getCurrent().getSession().removeAttribute("entityWindows");
        VaadinSession.getCurrent().getSession().removeAttribute("entityKolnierz");
        VaadinSession.getCurrent().getSession().removeAttribute("allTiles");
    }
}