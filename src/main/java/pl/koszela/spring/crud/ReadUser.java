package pl.koszela.spring.crud;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.EntityPersonalData;
import pl.koszela.spring.entities.EntityUser;
import pl.koszela.spring.repositories.UsersRepo;

import java.util.Objects;
import java.util.Optional;

@Service
public class ReadUser {

    private UsersRepo usersRepo;

    @Autowired
    public ReadUser(UsersRepo usersRepo) {
        this.usersRepo = Objects.requireNonNull(usersRepo);
    }

    public EntityUser getUser(EntityPersonalData personalData) {
        Optional<EntityUser> find = usersRepo.findEntityUserByEntityPersonalDataEquals(personalData);
        if (find.isPresent()) {
            VaadinSession.getCurrent().getSession().setAttribute("personalData", find.get().getEntityPersonalData());
            VaadinSession.getCurrent().getSession().setAttribute("inputData", find.get().getInputData());
            VaadinSession.getCurrent().getSession().setAttribute("entityWindowsFromRepo", find.get().getEntityWindows());
            VaadinSession.getCurrent().getSession().setAttribute("entityKolnierzFromRepo", find.get().getEntityKolnierz());
            VaadinSession.getCurrent().getSession().setAttribute("tiles", find.get().getTiles());
            VaadinSession.getCurrent().getSession().setAttribute("accesories", find.get().getUserAccesories());
            VaadinSession.getCurrent().getSession().setAttribute("gutter", find.get().getEntityUserGutter());
            return find.get();
        } else return new EntityUser();
    }
}