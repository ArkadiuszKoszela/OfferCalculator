package pl.koszela.spring.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.*;
import pl.koszela.spring.repositories.UsersRepo;
import pl.koszela.spring.service.SaveUsers;

import java.util.*;

import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationError;
import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationSucces;
import static pl.koszela.spring.views.TilesView.ENTER_TILES;

@Route(value = UsersView.INPUT_USER, layout = MainView.class)
public class UsersView extends VerticalLayout {

    static final String INPUT_USER = "users/input";

    private UsersRepo usersRepo;

    public TextField name = new TextField("Imię", "Arek", "Imię");
    private TextField surname = new TextField("Nazwisko", "Koszela", "Nazwisko");
    private TextField adress = new TextField("Adres zamieszkania", "Szczecin", "Adres zamieszkania");
    private TextField telephoneNumber = new TextField("Numer kontaktowy", "12345678", "Numer kontaktowy");
    private EmailField email = new EmailField("E-mail");
    private ComboBox<String> combobox = new ComboBox<>();
    private Button loadUser = new Button("Wczytaj klienta");
    private FormLayout board = new FormLayout();

    @Autowired
    public UsersView(UsersRepo usersRepo) {
        this.usersRepo = Objects.requireNonNull(usersRepo);

        email.setErrorMessage("Popraw E-mail");
        email.setAutoselect(true);
        email.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        add(getLayout(usersRepo));
        UI.getCurrent().addBeforeLeaveListener(e -> {
            Tabs tabs = (Tabs) VaadinSession.getCurrent().getAttribute("tabs");
            if (tabs != null && !tabs.getSelectedTab().getLabel().equals("Klienci")) {
                save();
                getNotificationSucces("User data save");
            } else {
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

    private FormLayout getLayout(UsersRepo usersRepo) {
        FormLayout.ResponsiveStep responsiveStep = new FormLayout.ResponsiveStep("5px", 5);
        board.setResponsiveSteps(responsiveStep);
        board.add(loadUsers(usersRepo), selectUser());
        board.add(setValidation(name), setValidation(surname), setValidation(adress), setValidation(telephoneNumber));
        board.add(email);
        return board;
    }

    private Button selectUser() {
        loadUser.addClickListener(buttonClickEvent -> {
            String[] split = combobox.getValue().split(" ");
            EntityUser find = usersRepo.findEntityUserByEntityPersonalDataNameAndEntityPersonalDataSurname(split[0], split[1]);

            VaadinSession.getCurrent().setAttribute("personalDataFromRepo", find.getEntityPersonalData());
            VaadinSession.getCurrent().setAttribute("tilesInputFromRepo", find.getEntityInputDataTiles());
            VaadinSession.getCurrent().setAttribute("accesoriesInputFromRepo", find.getEntityInputDataAccesories());
            VaadinSession.getCurrent().setAttribute("entityWindowsFromRepo", find.getEntityWindows());
            VaadinSession.getCurrent().setAttribute("entityKolnierzFromRepo", find.getEntityKolnierz());
            VaadinSession.getCurrent().setAttribute("allTilesFromRepo", find.getTiles());

            EntityPersonalData data = (EntityPersonalData) VaadinSession.getCurrent().getAttribute("personalDataFromRepo");
            name.setValue(data.getName());
            surname.setValue(data.getSurname());
            adress.setValue(data.getAdress());
            telephoneNumber.setValue(data.getTelephoneNumber());
            email.setValue(data.getEmail());
        });
        return loadUser;
    }

    private ComboBox<String> loadUsers(UsersRepo usersRepo) {
        List<EntityUser> all = usersRepo.findEntityUserByEntityPersonalDataIsNotNull();
        List<String> nameAndSurname = new ArrayList<>();
        if (all.size() > 0) {
            all.forEach(e -> nameAndSurname.add(e.getEntityPersonalData().getName() + " " + e.getEntityPersonalData().getSurname()));
            combobox.setItems(nameAndSurname);
        }
        return combobox;
    }

    private void save() {
        EntityPersonalData personalData = EntityPersonalData.builder()
                .name(name.getValue())
                .surname(surname.getValue())
                .adress(adress.getValue())
                .telephoneNumber(telephoneNumber.getValue())
                .email(email.getValue())
                .build();
        VaadinSession.getCurrent().setAttribute("personalData", personalData);
    }
}