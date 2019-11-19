package pl.koszela.spring.crud;

import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.VaadinSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.*;
import pl.koszela.spring.entities.Accessories;
import pl.koszela.spring.entities.Gutter;
import pl.koszela.spring.entities.PersonalData;
import pl.koszela.spring.entities.Tiles;
import pl.koszela.spring.repositories.*;
import pl.koszela.spring.service.NotificationInterface;

import java.util.*;

@Service
public class CreateUser {
    private final static Logger logger = Logger.getLogger(CreateUser.class);

    private PersonalDataRepository personalDataRepository;
    private UsersRepo usersRepo;

    private TilesRepository tilesRepository;
    private AccesoriesRepository accesoriesRepository;
    private GutterRepository gutterRepository;
    private InputRepository inputRepository;
    private WindowsRepository windowsRepository;
    private CollarRepository collarRepository;
    private AccesoriesWindowsRepository accesoriesWindowsRepository;

    @Autowired
    public CreateUser(PersonalDataRepository personalDataRepository, UsersRepo usersRepo, TilesRepository tilesRepository, AccesoriesRepository accesoriesRepository, GutterRepository gutterRepository, InputRepository inputRepository, WindowsRepository windowsRepository, CollarRepository collarRepository, AccesoriesWindowsRepository accesoriesWindowsRepository) {
        this.personalDataRepository = Objects.requireNonNull(personalDataRepository);
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);
        this.gutterRepository = Objects.requireNonNull(gutterRepository);
        this.inputRepository = Objects.requireNonNull(inputRepository);
        this.windowsRepository = Objects.requireNonNull(windowsRepository);
        this.collarRepository = Objects.requireNonNull(collarRepository);
        this.accesoriesWindowsRepository = Objects.requireNonNull(accesoriesWindowsRepository);
    }

    public void saveUser() {
        List<Gutter> list = (List<Gutter>) VaadinSession.getCurrent().getSession().getAttribute("gutter");
        PersonalData personalData = (PersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalData");
        Set<Tiles> allTiles = (Set<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("tiles");
        Set<Accessories> resultAccesories = (Set<Accessories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");
        List<InputData> setInput = (List<InputData>) VaadinSession.getCurrent().getSession().getAttribute("inputData");
        Set<Windows> setWindows = (Set<Windows>) VaadinSession.getCurrent().getSession().getAttribute("windowsAfterChoose");
        Set<AccessoriesWindows> setAccessoriesWindows = (Set<AccessoriesWindows>) VaadinSession.getCurrent().getSession().getAttribute("accesoriesWindows");
        Set<Collar> setCollars = (Set<Collar>) VaadinSession.getCurrent().getSession().getAttribute("collar");


        User newUser = new User();
        newUser.setPersonalData(personalData);
        newUser.setInputData(setInput);
        newUser.setUserAccesories(resultAccesories);
        newUser.setTiles(allTiles);
        newUser.setEntityUserGutter(list);
        newUser.setUserWindows(setWindows);
        newUser.setUserCollars(setCollars);
        newUser.setUserAccesoriesWindows(setAccessoriesWindows);

        gutterRepository.saveAll(list);
        personalDataRepository.save(personalData);
        inputRepository.saveAll(setInput);
        accesoriesRepository.saveAll(resultAccesories);
        tilesRepository.saveAll(allTiles);
        windowsRepository.saveAll(setWindows);
        collarRepository.saveAll(setCollars);
        accesoriesWindowsRepository.saveAll(setAccessoriesWindows);

        usersRepo.save(newUser);
        logger.info("Saved users - " + personalData.getName() + " " + personalData.getSurname());
        NotificationInterface.notificationOpen("Zapisano klienta - " + personalData.getName() + " " + personalData.getSurname() + "    :)", NotificationVariant.LUMO_SUCCESS);
    }
}