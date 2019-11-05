package pl.koszela.spring.crud;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.*;
import pl.koszela.spring.entities.PersonalData;
import pl.koszela.spring.repositories.*;

import java.util.Objects;
import java.util.Optional;

@Service
public class ReadUser {

    private UsersRepo usersRepo;

    @Autowired
    public ReadUser(UsersRepo usersRepo) {
        this.usersRepo = Objects.requireNonNull(usersRepo);
    }

    public User getUser(PersonalData personalData) {
        Optional<User> find = usersRepo.findUserByPersonalDataEquals(personalData);

        if (find.isPresent()) {
            VaadinSession.getCurrent().getSession().setAttribute("personalData", find.get().getPersonalData());
            VaadinSession.getCurrent().getSession().setAttribute("inputData", find.get().getInputData());
            VaadinSession.getCurrent().getSession().setAttribute("entityWindowsFromRepo", find.get().getWindows());
            VaadinSession.getCurrent().getSession().setAttribute("entityKolnierzFromRepo", find.get().getCollar());
            VaadinSession.getCurrent().getSession().setAttribute("tiles", find.get().getTiles());
            VaadinSession.getCurrent().getSession().setAttribute("accesories", find.get().getUserAccesories());
            VaadinSession.getCurrent().getSession().setAttribute("gutter", find.get().getEntityUserGutter());
            return find.get();
        }else {
            return new User();
        }
    }
}