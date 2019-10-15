package pl.koszela.spring.service;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.*;
import pl.koszela.spring.repositories.*;

import java.util.*;

import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationError;
import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationSucces;

@Service
public class UpdateUser {

    private PersonalDataRepository personalDataRepository;
    private UsersRepo usersRepo;
    private InputDataTilesRepository inputDataTilesRepository;
    private InputDataAccesoriesRespository inputDataAccesoriesRespository;
    private WindowsRepository windowsRepository;
    private KolnierzRepository kolnierzRepository;
    private TilesRepository tilesRepository;
    private OptionsOfferRepository optionsOfferRepository;
    private ResultAccesoriesRepository resultAccesoriesRepository;

    @Autowired
    public UpdateUser(PersonalDataRepository personalDataRepository, UsersRepo usersRepo, InputDataTilesRepository inputDataTilesRepository, InputDataAccesoriesRespository inputDataAccesoriesRespository, WindowsRepository windowsRepository, KolnierzRepository kolnierzRepository, TilesRepository tilesRepository, OptionsOfferRepository optionsOfferRepository, ResultAccesoriesRepository resultAccesoriesRepository) {
        this.personalDataRepository = Objects.requireNonNull(personalDataRepository);
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.inputDataTilesRepository = Objects.requireNonNull(inputDataTilesRepository);
        this.inputDataAccesoriesRespository = Objects.requireNonNull(inputDataAccesoriesRespository);
        this.windowsRepository = Objects.requireNonNull(windowsRepository);
        this.kolnierzRepository = Objects.requireNonNull(kolnierzRepository);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
        this.optionsOfferRepository = Objects.requireNonNull(optionsOfferRepository);
        this.resultAccesoriesRepository = Objects.requireNonNull(resultAccesoriesRepository);
    }

    public void updateUser() {
        EntityPersonalData data = (EntityPersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalDataFromRepo");
        Optional<EntityPersonalData> personalData = personalDataRepository.findEntityPersonalDataByNameAndSurnameEquals(data.getName(), data.getSurname());

        EntityInputDataTiles entityInputDataTiles = (EntityInputDataTiles) VaadinSession.getCurrent().getSession().getAttribute("tilesInputFromRepo");
        EntityInputDataAccesories entityInputDataAccesories = (EntityInputDataAccesories) VaadinSession.getCurrent().getSession().getAttribute("accesoriesInputFromRepo");
        EntityWindows entityWindows = (EntityWindows) VaadinSession.getCurrent().getSession().getAttribute("entityWindowsFromRepo");
        EntityKolnierz entityKolnierz = (EntityKolnierz) VaadinSession.getCurrent().getSession().getAttribute("entityKolnierzFromRepo");
        Set<EntityResultAccesories> resultAccesories = (Set<EntityResultAccesories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");

        if (personalData.isPresent()) {
            Optional<EntityUser> userFromRepo = usersRepo.findEntityUserByEntityPersonalDataEquals(personalData.get());
            if (userFromRepo.isPresent()) {
                Set<Tiles> allTilesFromRepo = (Set<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("allTilesFromRepo");
                EntityUser userToUpdate = userFromRepo.get();
                userToUpdate.setEntityInputDataTiles(entityInputDataTiles);
                userToUpdate.setResultAccesories(resultAccesories);
                /*userToUpdate.setEntityInputDataAccesories(entityInputDataAccesories);
                userToUpdate.setEntityWindows(entityWindows);
                userToUpdate.setEntityKolnierz(entityKolnierz);*/
                userToUpdate.getTiles().clear();
                userToUpdate.setTiles(allTilesFromRepo);

                resultAccesoriesRepository.saveAll(resultAccesories);
                inputDataTilesRepository.save(entityInputDataTiles);

                /*inputDataAccesoriesRespository.save(entityInputDataAccesories);
                windowsRepository.save(entityWindows);
                kolnierzRepository.save(entityKolnierz);*/
                tilesRepository.saveAll(allTilesFromRepo);

                usersRepo.save(userToUpdate);
                getNotificationSucces("Zaktualizowałem dane dla użytkownika: " + userToUpdate.getEntityPersonalData().getName() + " " + userToUpdate.getEntityPersonalData().getSurname() + "    :)");
            } else {
                getNotificationError("Coś poszło nie tak");
            }
        } else {
            getNotificationError("Coś poszło nie tak - nie ma takich danych");
        }

        VaadinSession.getCurrent().getSession().removeAttribute("personalDataFromRepo");
        VaadinSession.getCurrent().getSession().removeAttribute("tilesInputFromRepo");
        VaadinSession.getCurrent().getSession().removeAttribute("accesoriesInputFromRepo");
        VaadinSession.getCurrent().getSession().removeAttribute("entityWindowsFromRepo");
        VaadinSession.getCurrent().getSession().removeAttribute("entityKolnierzFromRepo");
        VaadinSession.getCurrent().getSession().removeAttribute("allTilesFromRepo");
    }
}