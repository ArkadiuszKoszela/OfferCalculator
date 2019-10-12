package pl.koszela.spring.service;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.*;
import pl.koszela.spring.repositories.*;

import java.util.*;

import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationSucces;

@Service
public class SaveUser {

    private PersonalDataRepository personalDataRepository;
    private UsersRepo usersRepo;
    private InputDataTilesRepository inputDataTilesRepository;
    private InputDataAccesoriesRespository inputDataAccesoriesRespository;
    private WindowsRepository windowsRepository;
    private KolnierzRepository kolnierzRepository;
    private TilesRepository tilesRepository;
    private OptionsOfferRepository optionsOfferRepository;

    @Autowired
    public SaveUser(PersonalDataRepository personalDataRepository, UsersRepo usersRepo, InputDataTilesRepository inputDataTilesRepository, InputDataAccesoriesRespository inputDataAccesoriesRespository, WindowsRepository windowsRepository, KolnierzRepository kolnierzRepository, TilesRepository tilesRepository, OptionsOfferRepository optionsOfferRepository) {
        this.personalDataRepository = Objects.requireNonNull(personalDataRepository);
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.inputDataTilesRepository = Objects.requireNonNull(inputDataTilesRepository);
        this.inputDataAccesoriesRespository = Objects.requireNonNull(inputDataAccesoriesRespository);
        this.windowsRepository = Objects.requireNonNull(windowsRepository);
        this.kolnierzRepository = Objects.requireNonNull(kolnierzRepository);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
        this.optionsOfferRepository = Objects.requireNonNull(optionsOfferRepository);
    }

    public void saveUser() {
        EntityPersonalData entityPersonalData = (EntityPersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalData");
        EntityInputDataTiles entityInputDataTiles = (EntityInputDataTiles) VaadinSession.getCurrent().getSession().getAttribute("tilesInput");
        EntityInputDataAccesories entityInputDataAccesories = (EntityInputDataAccesories) VaadinSession.getCurrent().getSession().getAttribute("accesoriesInput");
        EntityWindows entityWindows = (EntityWindows) VaadinSession.getCurrent().getSession().getAttribute("entityWindows");
        EntityKolnierz entityKolnierz = (EntityKolnierz) VaadinSession.getCurrent().getSession().getAttribute("entityKolnierz");
        List<Tiles> allTiles = (List<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("allTiles");
        EntityUser newUser = new EntityUser();
        newUser.setEntityPersonalData(entityPersonalData);
        newUser.setEntityInputDataTiles(entityInputDataTiles);
        /*newUser.setEntityInputDataAccesories(entityInputDataAccesories);*/
        /*newUser.setEntityWindows(entityWindows);
        newUser.setEntityKolnierz(entityKolnierz);*/
        newUser.setTiles(new HashSet<>(allTiles));

        personalDataRepository.save(entityPersonalData);
        inputDataTilesRepository.save(entityInputDataTiles);
        /*inputDataAccesoriesRespository.save(entityInputDataAccesories);*/
        /*windowsRepository.save(entityWindows);
        kolnierzRepository.save(entityKolnierz);*/
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
