package pl.koszela.spring.views;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import pl.koszela.spring.calculate.CalculateTiles;
import pl.koszela.spring.entities.EntityResultTiles;
import pl.koszela.spring.entities.EntityUser;
import pl.koszela.spring.repositories.UsersRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Route(value = CreateOffer.CREATE_OFFER, layout = MainView.class)
public class CreateOffer extends VerticalLayout {

    public static final String CREATE_OFFER = "createOffer";

    private UsersRepo usersRepo;
    private CalculateTiles calculateTiles;

    private ComboBox<String> selectUser = new ComboBox<>("Wybierz klienta: ");
    private ComboBox<String> priceList = new ComboBox<>("Dostępne cenniki: ");

    private Grid<EntityResultTiles> resultTiles = new Grid<>();

    public CreateOffer(UsersRepo usersRepo, CalculateTiles calculateTiles) {
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.calculateTiles = Objects.requireNonNull(calculateTiles);

        loadUserComboBox();
        add(selectUser, getAvailablePriceList());

    }

    private Grid<EntityResultTiles> createGrid() {
        resultTiles.getColumnByKey("name").setWidth("Nazwa");
        resultTiles.getColumnByKey("priceListName").setWidth("Nazwa cennika");
        resultTiles.getColumnByKey("priceAfterDiscount").setWidth("Cena po rabacie");
        resultTiles.getColumnByKey("purchasePrice").setWidth("Cena Detaliczna");
        resultTiles.getColumnByKey("profit").setWidth("Zysk");
        resultTiles.removeColumnByKey("id");
        return resultTiles;
    }

    private void loadUser() {
        String nameISurname = selectUser.getValue();
        String[] strings = nameISurname.split(" ");
        EntityUser entityUser = usersRepo.findUsersEntityByNameAndSurnameEquals(strings[0], strings[1]);
    }

    private void loadUserComboBox() {
        if (nameAndSurname().size() > 0) {
            selectUser.setItems(nameAndSurname());
        }
    }

    private List<String> nameAndSurname() {
        Iterable<EntityUser> allUsersFromRepository = usersRepo.findAll();
        List<String> nameAndSurname = new ArrayList<>();
        allUsersFromRepository.forEach(user -> nameAndSurname.add(user.getName().concat(" ").concat(user.getSurname())));
        return nameAndSurname;
    }

    private ComboBox<String> getAvailablePriceList() {
        if (!calculateTiles.getAvailablePriceList().isEmpty()) {
            priceList.setItems(calculateTiles.getAvailablePriceList());
            return priceList;
        } else {
            priceList.setItems("Brak zaimportowanych cenników");
            return priceList;
        }
    }

    private void putDataToGrid() {

        resultTiles.setItems();
    }
}
