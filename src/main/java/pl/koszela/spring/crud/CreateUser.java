package pl.koszela.spring.crud;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.*;
import pl.koszela.spring.entities.accesories.EntityAccesories;
import pl.koszela.spring.entities.gutter.EntityGutter;
import pl.koszela.spring.entities.gutter.InputGutterData;
import pl.koszela.spring.entities.personalData.EntityPersonalData;
import pl.koszela.spring.entities.tiles.EntityInputDataTiles;
import pl.koszela.spring.entities.tiles.Tiles;
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
    private GutterRepository gutterRepository;
    private InputGutterDataRepository inputGutterDataRepository;

    @Autowired
    public CreateUser(PersonalDataRepository personalDataRepository, UsersRepo usersRepo, InputDataTilesRepository inputDataTilesRepository, TilesRepository tilesRepository, AccesoriesRepository accesoriesRepository, GutterRepository gutterRepository, InputGutterDataRepository inputGutterDataRepository) {
        this.personalDataRepository = Objects.requireNonNull(personalDataRepository);
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.inputDataTilesRepository = Objects.requireNonNull(inputDataTilesRepository);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);
        this.gutterRepository = Objects.requireNonNull(gutterRepository);
        this.inputGutterDataRepository = Objects.requireNonNull(inputGutterDataRepository);
    }

    public void saveUser() {
        List<EntityGutter> list = (List<EntityGutter>) VaadinSession.getCurrent().getSession().getAttribute("allGutter");
        EntityPersonalData entityPersonalData = (EntityPersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalDataFromRepo");
        EntityInputDataTiles entityInputDataTiles = (EntityInputDataTiles) VaadinSession.getCurrent().getSession().getAttribute("tilesInputFromRepo");
        Set<Tiles> allTiles = (Set<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("allTilesFromRepo");
        Set<EntityAccesories> resultAccesories = (Set<EntityAccesories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");
        List<InputGutterData> listInputGutter = (List<InputGutterData>) VaadinSession.getCurrent().getSession().getAttribute("inputGutterData");


        EntityUser newUser = new EntityUser();
        newUser.setEntityPersonalData(entityPersonalData);
        newUser.setEntityInputDataTiles(entityInputDataTiles);
        newUser.setResultAccesories(resultAccesories);
        newUser.setTiles(allTiles);
        newUser.setEntityUserGutter(list);

        inputGutterDataRepository.saveAll(listInputGutter);
        gutterRepository.saveAll(list);
        personalDataRepository.save(entityPersonalData);
        inputDataTilesRepository.save(entityInputDataTiles);
        accesoriesRepository.saveAll(resultAccesories);
        tilesRepository.saveAll(allTiles);

        usersRepo.save(newUser);
        getNotificationSucces("Zapisałem użytkownika - " + entityPersonalData.getName() + " " + entityPersonalData.getSurname() + "    :)");
    }
}