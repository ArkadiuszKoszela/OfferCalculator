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
public class SaveUsers {

    private PersonalDataRepository personalDataRepository;
    private UsersRepo usersRepo;
    private InputDataTilesRepository inputDataTilesRepository;
    private InputDataAccesoriesRespository inputDataAccesoriesRespository;
    private WindowsRepository windowsRepository;
    private KolnierzRepository kolnierzRepository;
    private TilesRepository tilesRepository;

    @Autowired
    public SaveUsers(PersonalDataRepository personalDataRepository, UsersRepo usersRepo, InputDataTilesRepository inputDataTilesRepository, InputDataAccesoriesRespository inputDataAccesoriesRespository, WindowsRepository windowsRepository, KolnierzRepository kolnierzRepository, TilesRepository tilesRepository) {
        this.personalDataRepository = Objects.requireNonNull(personalDataRepository);
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.inputDataTilesRepository = Objects.requireNonNull(inputDataTilesRepository);
        this.inputDataAccesoriesRespository = Objects.requireNonNull(inputDataAccesoriesRespository);
        this.windowsRepository = Objects.requireNonNull(windowsRepository);
        this.kolnierzRepository = Objects.requireNonNull(kolnierzRepository);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
    }

    public void saveUser() {
        EntityPersonalData entityPersonalData = (EntityPersonalData) VaadinSession.getCurrent().getAttribute("personalData");
        /* if (allDataIsCompleted(entityPersonalData.getName(), entityPersonalData.getSurname())) {*/

        EntityInputDataTiles entityInputDataTiles = (EntityInputDataTiles) VaadinSession.getCurrent().getAttribute("tilesInput");
        EntityInputDataAccesories entityInputDataAccesories = (EntityInputDataAccesories) VaadinSession.getCurrent().getAttribute("accesoriesInput");
        EntityWindows entityWindows = (EntityWindows) VaadinSession.getCurrent().getAttribute("entityWindows");
        EntityKolnierz entityKolnierz = (EntityKolnierz) VaadinSession.getCurrent().getAttribute("entityKolnierz");
        EntityUser newUser = new EntityUser();
        newUser.setEntityPersonalData(entityPersonalData);
        newUser.setEntityInputDataTiles(entityInputDataTiles);
        newUser.setEntityInputDataAccesories(entityInputDataAccesories);
        newUser.setEntityWindows(entityWindows);
        newUser.setEntityKolnierz(entityKolnierz);

        Optional<EntityPersonalData> personalData = personalDataRepository.findEntityPersonalDataByNameAndSurnameAndAdressEquals(entityPersonalData.getName(), entityPersonalData.getSurname(), entityPersonalData.getAdress());

        if (personalData.isPresent()) {
            Optional<EntityUser> userFromRepo = usersRepo.findEntityUserByEntityPersonalDataEquals(personalData.get());
            if (userFromRepo.isPresent()) {
                Set<Tiles> allTilesFromRepo = (Set<Tiles>) VaadinSession.getCurrent().getAttribute("allTilesFromRepo");
                EntityUser userToUpdate = userFromRepo.get();
                userToUpdate.setEntityInputDataTiles(entityInputDataTiles);
                userToUpdate.setEntityInputDataAccesories(entityInputDataAccesories);
                userToUpdate.setEntityWindows(entityWindows);
                userToUpdate.setEntityKolnierz(entityKolnierz);
                userToUpdate.getTiles().clear();
                userToUpdate.setTiles(allTilesFromRepo);

                inputDataTilesRepository.save(entityInputDataTiles);
                inputDataAccesoriesRespository.save(entityInputDataAccesories);
                windowsRepository.save(entityWindows);
                kolnierzRepository.save(entityKolnierz);
                tilesRepository.saveAll(allTilesFromRepo);

                usersRepo.save(userToUpdate);
                getNotificationSucces("Zaktualizowałem dane dla użytkownika: " + userToUpdate.getEntityPersonalData().getName() + " " + userToUpdate.getEntityPersonalData().getSurname() + "    :)");
            }
        } else {
            List<Tiles> allTiles = (List<Tiles>) VaadinSession.getCurrent().getAttribute("allTiles");
            newUser.setTiles(new HashSet<>(allTiles));
            personalDataRepository.save(entityPersonalData);
            inputDataTilesRepository.save(entityInputDataTiles);
            inputDataAccesoriesRespository.save(entityInputDataAccesories);
            windowsRepository.save(entityWindows);
            kolnierzRepository.save(entityKolnierz);
            tilesRepository.saveAll(allTiles);

            usersRepo.save(newUser);
            getNotificationSucces("Zapisałem użytkownika - " + entityPersonalData.getName() + " " + entityPersonalData.getSurname() + "    :)");
        }
//        }/* else {
//            EntityUser userToUpdate = userFromRepo.get();
//            inputDataTilesRepository.save(entityInputDataTiles);
//            inputDataAccesoriesRespository.save(entityInputDataAccesories);
//            windowsRepository.save(entityWindows);
//            kolnierzRepository.save(entityKolnierz);
//            tilesRepository.saveAll(allTiles);
//
//            usersRepo.save(userToUpdate);
//            getNotificationSucces("Zaktualizowałem dane dla użytkownika: " + userToUpdate.getEntityPersonalData().getName() + " " + userToUpdate.getEntityPersonalData().getSurname() + "    :)");
//        }

        VaadinSession.getCurrent().setAttribute("user", newUser);
        VaadinSession.getCurrent().setAttribute("personalDataFromRepo", null);
        VaadinSession.getCurrent().setAttribute("tilesInputFromRepo", null);
        VaadinSession.getCurrent().setAttribute("accesoriesInputFromRepo", null);
        VaadinSession.getCurrent().setAttribute("entityWindowsFromRepo", null);
        VaadinSession.getCurrent().setAttribute("entityKolnierzFromRepo", null);
        VaadinSession.getCurrent().setAttribute("allTilesFromRepo", null);

        VaadinSession.getCurrent().setAttribute("tilesInput", null);
        VaadinSession.getCurrent().setAttribute("accesoriesInput", null);
        VaadinSession.getCurrent().setAttribute("entityWindows", null);
        VaadinSession.getCurrent().setAttribute("entityKolnierz", null);
        VaadinSession.getCurrent().setAttribute("allTiles", null);
//    }

    }

//    private boolean allDataIsCompleted(String name, String surname) {
//        if (!isUserInDatabase(name, surname)) {
//            return true;
//        } else if (allUsers().size() > 0 && isUserInDatabase(name, surname)) {
//            getNotificationError("Podany użytkownik znajduję się już w bazie danych");
//            return false;
//        } else {
//            getNotificationError("Nieznany błąd");
//            return false;
//        }
//    }
//
//    private boolean isUserInDatabase(String name, String surname) {
//        EntityPersonalData personalData = personalDataRepository.findUsersEntityByNameAndSurnameEquals(name, surname);
//        return personalData != null;
//    }
//
//    private List<EntityPersonalData> allUsers() {
//        Iterable<EntityPersonalData> iterable = personalDataRepository.findAll();
//        List<EntityPersonalData> userList = new ArrayList<>();
//        iterable.forEach(userList::add);
//        return userList;
//    }
}
