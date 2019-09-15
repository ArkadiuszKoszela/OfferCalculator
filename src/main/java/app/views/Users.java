package app.views;

import app.controllers.ControllerVaadin;
import app.entities.EntityUser;
import app.repositories.UsersRepo;
import app.service.Layout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static app.inputFields.ServiceSplitLayout.getSideMenuSettings;
import static app.inputFields.ServiceSplitLayout.ustawieniaStrony;

@Route(value = Users.USERS)
public class Users extends SplitLayout implements Layout {

    public static final String USERS = "users/allUsers";

    private UsersRepo usersRepo;
    private ControllerVaadin controllerVaadin;

    private Grid<EntityUser> grid;

    @Autowired
    public Users(UsersRepo usersRepo, ControllerVaadin controllerVaadin) {
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.controllerVaadin = Objects.requireNonNull(controllerVaadin);
        setOrientation(SplitLayout.Orientation.VERTICAL);
        createGrid();
        addToPrimary(ustawieniaStrony(controllerVaadin));
        addToSecondary(getSideMenu(controllerVaadin));
    }


    private Grid<EntityUser> createGrid() {
        grid = new Grid<>(EntityUser.class);
        grid.getColumnByKey("name").setHeader("Nazwa Cennika");
        grid.getColumnByKey("surname").setHeader("Typ dachówki");
        grid.getColumnByKey("adress").setHeader("Kategoria");
        grid.getColumnByKey("telephoneNumber").setHeader("Cena detaliczna");
        grid.getColumnByKey("dateOfMeeting").setHeader("Marża");
        grid.getColumnByKey("priceListName").setHeader("Rabat podstawowy");
        grid.setColumns("name", "surname", "adress", "telephoneNumber", "dateOfMeeting", "priceListName");
        grid.setItems(allUser(usersRepo));
        return grid;
    }

    private List<EntityUser> allUser(UsersRepo usersRepo) {
        Iterable<EntityUser> iterable = usersRepo.findAll();
        List<EntityUser> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

    @Override
    public SplitLayout getSideMenu(ControllerVaadin controllerVaadin) {
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.addToPrimary(controllerVaadin.sideMenuUser());
        splitLayout.addToSecondary(grid);
        getSideMenuSettings(splitLayout);
        return splitLayout;
    }
}
