package pl.koszela.spring.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.calculate.CalculateTiles;
import pl.koszela.spring.repositories.UsersRepo;
import pl.koszela.spring.service.Labels;
import pl.koszela.spring.service.MenuBarInterface;
import pl.koszela.spring.service.SaveUsers;

import java.util.Objects;

@Route(value = InputUser.INPUT_USER, layout = MainView.class)
public class InputUser extends VerticalLayout implements MenuBarInterface {

    public static final String INPUT_USER = "users/input";

    private SaveUsers saveUser;
    private Labels serviceDataCustomer;
    private CalculateTiles calculateTiles;
    private UsersRepo usersRepo;

    public TextField name = new TextField("Imię", "Arek", "Imię");
    private TextField surname = new TextField("Nazwisko", "Koszela", "Nazwisko");
    private TextField adress = new TextField("Adres zamieszkania", "Szczecin", "Adres zamieszkania");
    private TextField telephoneNumber = new TextField("Numer kontaktowy", "12345678", "Numer kontaktowy");
    private EmailField email = new EmailField("E-mail");

    private FormLayout board = new FormLayout();

    private ComboBox<String> priceListCB = new ComboBox<>("Podaj nazwę cennika: ");

    @Autowired
    public InputUser(SaveUsers saveUser, Labels serviceDataCustomer, UsersRepo usersRepo, CalculateTiles calculateTiles) {
        this.saveUser = Objects.requireNonNull(saveUser);
        this.serviceDataCustomer = Objects.requireNonNull(serviceDataCustomer);
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.calculateTiles = Objects.requireNonNull(calculateTiles);

        add(menu());
        if (!calculateTiles.getAvailablePriceList().isEmpty()) {
            priceListCB.setItems(calculateTiles.getAvailablePriceList());
        }
        add(getLayout());
    }

    private FormLayout getLayout() {
        board.add(name, surname, adress, telephoneNumber);
        board.add(email, Labels.getLabel(" "));
        board.add(priceListCB, saveUser());
        return board;
    }

    private Button saveUser() {
        Button saveUserWithCalculations = new Button("Zapisz użytkownika");
        saveUserWithCalculations.addClickListener(buttonClickEvent -> saveUser.saveUser(priceListCB, name, surname, adress, telephoneNumber, email));
        return saveUserWithCalculations;
    }

    @Override
    public MenuBar menu() {
        MenuBar menuBar = new MenuBar();
        menuBar.addItem(new RouterLink("Kolejne dane", EnterTiles.class));
        return menuBar;
    }
}
