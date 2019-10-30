package pl.koszela.spring.crud;

import com.vaadin.flow.server.VaadinSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.DAOs.DaoAccesories;
import pl.koszela.spring.entities.*;
import pl.koszela.spring.entities.accesories.EntityAccesories;
import pl.koszela.spring.entities.gutter.EntityGutter;
import pl.koszela.spring.entities.personalData.EntityPersonalData;
import pl.koszela.spring.entities.tiles.Tiles;
import pl.koszela.spring.repositories.*;

import java.util.*;

import static pl.koszela.spring.service.ServiceNotification.getNotificationSucces;

@Service
public class CreateUser {
    private final static Logger logger = Logger.getLogger(CreateUser.class);

    private PersonalDataRepository personalDataRepository;
    private UsersRepo usersRepo;

    private TilesRepository tilesRepository;
    private AccesoriesRepository accesoriesRepository;
    private GutterRepository gutterRepository;
    private InputRepository inputRepository;

    @Autowired
    public CreateUser(PersonalDataRepository personalDataRepository, UsersRepo usersRepo, TilesRepository tilesRepository, AccesoriesRepository accesoriesRepository, GutterRepository gutterRepository, InputRepository inputRepository) {
        this.personalDataRepository = Objects.requireNonNull(personalDataRepository);
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);
        this.gutterRepository = Objects.requireNonNull(gutterRepository);
        this.inputRepository = Objects.requireNonNull(inputRepository);
    }

    public void saveUser() {
        List<EntityGutter> list = (List<EntityGutter>) VaadinSession.getCurrent().getSession().getAttribute("gutter");
        EntityPersonalData entityPersonalData = (EntityPersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalData");
        Set<Tiles> allTiles = (Set<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("tiles");
        Set<EntityAccesories> resultAccesories = (Set<EntityAccesories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");
        List<InputData> setInput = (List<InputData>) VaadinSession.getCurrent().getSession().getAttribute("inputData");
        EntityUser newUser = new EntityUser();
        newUser.setEntityPersonalData(entityPersonalData);
        newUser.setInputData(setInput);
        newUser.setUserAccesories(resultAccesories);
        newUser.setTiles(allTiles);
        newUser.setEntityUserGutter(list);

        gutterRepository.saveAll(list);
        personalDataRepository.save(entityPersonalData);
        inputRepository.saveAll(setInput);
        accesoriesRepository.saveAll(resultAccesories);
        tilesRepository.saveAll(allTiles);

        usersRepo.save(newUser);
        logger.info("Saved users - " + entityPersonalData.getName() + " " + entityPersonalData.getSurname());
        getNotificationSucces("Zapisałem użytkownika - " + entityPersonalData.getName() + " " + entityPersonalData.getSurname() + "    :)");
    }
}