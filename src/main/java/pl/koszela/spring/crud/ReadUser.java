package pl.koszela.spring.crud;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.*;
import pl.koszela.spring.repositories.*;

import java.util.Objects;

@Service
public class ReadUser {

    private UsersRepo usersRepo;

    @Autowired
    public ReadUser(UsersRepo usersRepo) {
        this.usersRepo = Objects.requireNonNull(usersRepo);
    }

    public EntityUser getUser(String name, String surname) {
        EntityUser find = usersRepo.findEntityUserByEntityPersonalDataNameAndEntityPersonalDataSurname(name, surname);

        VaadinSession.getCurrent().getSession().setAttribute("personalDataFromRepo", find.getEntityPersonalData());
        VaadinSession.getCurrent().getSession().setAttribute("tilesInputFromRepo", find.getEntityInputDataTiles());
        VaadinSession.getCurrent().getSession().setAttribute("entityWindowsFromRepo", find.getEntityWindows());
        VaadinSession.getCurrent().getSession().setAttribute("entityKolnierzFromRepo", find.getEntityKolnierz());
        VaadinSession.getCurrent().getSession().setAttribute("allTilesFromRepo", find.getTiles());
        VaadinSession.getCurrent().getSession().setAttribute("accesories", find.getAccesories());
        VaadinSession.getCurrent().getSession().setAttribute("allGutter", find.getEntityUserGutter());
        VaadinSession.getCurrent().getSession().setAttribute("inputGutterData", find.getEntityUserGutter().get(0).getInputGutter());

        return find;
    }
}