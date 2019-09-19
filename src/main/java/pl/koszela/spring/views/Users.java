package pl.koszela.spring.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.EntityUser;
import pl.koszela.spring.repositories.UsersRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Route(value = Users.USERS, layout = MainView.class)
public class Users extends VerticalLayout {

    public static final String USERS = "users/all";

    private UsersRepo usersRepo;

    private Grid<EntityUser> grid;

    @Autowired
    public Users(UsersRepo usersRepo) {
        this.usersRepo = Objects.requireNonNull(usersRepo);

        createGrid();
        add(grid);
    }


    private Grid<EntityUser> createGrid() {
        grid = new Grid<>(EntityUser.class);
        grid.getColumnByKey("name").setHeader("Nazwa Cennika");
        grid.getColumnByKey("surname").setHeader("Typ dachówki");
        grid.getColumnByKey("adress").setHeader("Kategoria");
        grid.getColumnByKey("telephoneNumber").setHeader("Cena detaliczna");
        grid.getColumnByKey("hasTiles").setHeader("Dachówki");
        grid.getColumnByKey("hasAccesories").setHeader("Akcesoria");
        grid.getColumnByKey("priceListName").setHeader("Rabat podstawowy");
        grid.setColumns("name", "surname", "adress", "telephoneNumber", "hasTiles", "hasAccesories", "priceListName");
        grid.setItems(allUser(usersRepo));
        return grid;
    }

    private List<EntityUser> allUser(UsersRepo usersRepo) {
        Iterable<EntityUser> iterable = usersRepo.findAll();
        List<EntityUser> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }
}
