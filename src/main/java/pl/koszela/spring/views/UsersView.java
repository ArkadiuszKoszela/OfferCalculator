package pl.koszela.spring.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.calculate.CalculateTiles;
import pl.koszela.spring.entities.EntityPersonalData;
import pl.koszela.spring.entities.Tiles;
import pl.koszela.spring.repositories.UsersRepo;
import pl.koszela.spring.service.Labels;
import pl.koszela.spring.service.MenuBarInterface;
import pl.koszela.spring.service.SaveUsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationError;
import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationSucces;
import static pl.koszela.spring.views.TilesView.ENTER_TILES;

@Route(value = UsersView.INPUT_USER, layout = MainView.class)
public class UsersView extends VerticalLayout implements MenuBarInterface {

    public static final String INPUT_USER = "users/input";

    private SaveUsers saveUser;

    public TextField name = new TextField("Imię", "Arek", "Imię");
    private TextField surname = new TextField("Nazwisko", "Koszela", "Nazwisko");
    private TextField adress = new TextField("Adres zamieszkania", "Szczecin", "Adres zamieszkania");
    private TextField telephoneNumber = new TextField("Numer kontaktowy", "12345678", "Numer kontaktowy");
    private EmailField email = new EmailField("E-mail");

    private FormLayout board = new FormLayout();

    @Autowired
    public UsersView(SaveUsers saveUser) {
        this.saveUser = Objects.requireNonNull(saveUser);

        email.setErrorMessage("Popraw E-mail");
        email.setAutoselect(true);
        email.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        add(menu());
        add(getLayout());
        UI.getCurrent().addBeforeLeaveListener(e -> {
            Tabs tabs = (Tabs) VaadinSession.getCurrent().getAttribute("tabs");
            if (tabs != null && !tabs.getSelectedTab().getLabel().equals("Klienci")) {
                save();
                getNotificationSucces("User data save");
            }else{
                getNotificationError("User data don't save");
            }
        });
    }

    private TextField setValidation(TextField textField) {
        textField.setRequired(true);
        textField.setAutoselect(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        return textField;
    }

    private FormLayout getLayout() {
        FormLayout.ResponsiveStep responsiveStep = new FormLayout.ResponsiveStep("5px", 5);
        board.setResponsiveSteps(responsiveStep);
        board.add(setValidation(name), setValidation(surname), setValidation(adress), setValidation(telephoneNumber));
        board.add(email);
        return board;
    }

    private void save() {
        EntityPersonalData personalData = EntityPersonalData.builder()
                .name(name.getValue())
                .surname(surname.getValue())
                .adress(adress.getValue())
                .telephoneNumber(telephoneNumber.getValue())
                .email(email.getValue())
                .build();
        saveUser.saveUser(personalData, new ArrayList<>());
    }

    @Override
    public MenuBar menu() {
        MenuBar menuBar = new MenuBar();
        Button button = new Button("Dalej");
        button.addClickListener(event -> {
            /*saveUser.saveUser(name, surname, adress, telephoneNumber, email);*/
            getUI().ifPresent(ui -> ui.navigate(ENTER_TILES));
        });
        menuBar.addItem(button);
        return menuBar;
    }
}
