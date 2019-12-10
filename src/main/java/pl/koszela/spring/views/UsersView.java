package pl.koszela.spring.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.crud.ReadUser;
import pl.koszela.spring.entities.main.PersonalData;
import pl.koszela.spring.entities.main.User;
import pl.koszela.spring.repositories.UsersRepo;
import pl.koszela.spring.crud.DeleteUsers;

import java.util.*;

@Route(value = UsersView.INPUT_USER, layout = MainView.class)
public class UsersView extends VerticalLayout implements BeforeLeaveObserver {

    static final String INPUT_USER = "users";

    private UsersRepo usersRepo;
    private DeleteUsers deleteUsers;
    private ReadUser readUser;

    public TextField name = new TextField("Imię", "Arek", "Imię");
    private TextField surname = new TextField("Nazwisko", "Koszela", "Nazwisko");
    private TextField adress = new TextField("Adres zamieszkania", "Szczecin", "Adres zamieszkania");
    private TextField telephoneNumber = new TextField("Numer kontaktowy", "12345678", "Numer kontaktowy");
    private EmailField email = new EmailField("E-mail");
    private ComboBox<PersonalData> combobox = new ComboBox<>();
    private Button loadUser = new Button("Wczytaj klienta");
    private Button removeUser = new Button(" Usuń użytkownika");
    private FormLayout board = new FormLayout();

    @Autowired
    public UsersView(UsersRepo usersRepo, DeleteUsers deleteUsers, ReadUser readUser) {
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.deleteUsers = Objects.requireNonNull(deleteUsers);
        this.readUser = Objects.requireNonNull(readUser);

        email.setErrorMessage("Popraw E-mail");
        email.setAutoselect(true);
        email.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        add(getLayout(usersRepo));
    }

    private TextField setValidation(TextField textField) {
        textField.setRequired(true);
        textField.setAutoselect(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        return textField;
    }

    private FormLayout getLayout(UsersRepo usersRepo) {
        FormLayout formLayout = new FormLayout();
        FormLayout.ResponsiveStep responsiveStep = new FormLayout.ResponsiveStep("5px", 5);
        board.setResponsiveSteps(responsiveStep);
        formLayout.add(loadUsers(usersRepo));
        formLayout.add(selectUser(), removeUser(usersRepo));
        board.add(formLayout);
        board.add(setValidation(name), setValidation(surname), setValidation(adress), setValidation(telephoneNumber));
        board.add(email);
        return board;
    }

    private Button removeUser(UsersRepo usersRepo) {
        removeUser.addClickListener(buttonClickEvent -> {
            deleteUsers.removeUser(combobox.getValue()  );
            combobox.clear();
            loadUsers(usersRepo);
            combobox.getDataProvider().refreshAll();
        });
        removeUser.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return removeUser;
    }

    private Button selectUser() {
        loadUser.addClickListener(buttonClickEvent -> {
            User find = readUser.getUser(combobox.getValue());

            name.setValue(find.getPersonalData().getName());
            surname.setValue(find.getPersonalData().getSurname());
            adress.setValue(find.getPersonalData().getAdress());
            telephoneNumber.setValue(find.getPersonalData().getTelephoneNumber());
            email.setValue(find.getPersonalData().getEmail());
        });
        loadUser.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        return loadUser;
    }

    private ComboBox<PersonalData> loadUsers(UsersRepo usersRepo) {
        List<User> all = usersRepo.findByPersonalDataIsNotNull();
        List<PersonalData> nameAndSurname = new ArrayList<>();
        all.forEach(e -> nameAndSurname.add(e.getPersonalData()));
        combobox.setItems(nameAndSurname);
        combobox.setItemLabelGenerator(entityPersonalData -> entityPersonalData.getName() + " " + entityPersonalData.getSurname());
        return combobox;
    }

    private void save() {
        PersonalData personalData = PersonalData.builder()
                .name(name.getValue())
                .surname(surname.getValue())
                .adress(adress.getValue())
                .telephoneNumber(telephoneNumber.getValue())
                .email(email.getValue())
                .build();
        VaadinSession.getCurrent().getSession().setAttribute("personalData", personalData);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        VaadinSession.getCurrent().getSession().removeAttribute("gutter");
        VaadinSession.getCurrent().getSession().removeAttribute("personalData");
        VaadinSession.getCurrent().getSession().removeAttribute("tiles");
        VaadinSession.getCurrent().getSession().removeAttribute("inputData");
        VaadinSession.getCurrent().getSession().removeAttribute("accesories");
        VaadinSession.getCurrent().getSession().removeAttribute("collar");
        VaadinSession.getCurrent().getSession().removeAttribute("windows");
        VaadinSession.getCurrent().getSession().removeAttribute("windowsAfterChoose");
        VaadinSession.getCurrent().getSession().removeAttribute("accesoriesWindows");

    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        PersonalData personalData = (PersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalData");
        if (personalData == null) {
            save();
            action.proceed();
        }
        action.proceed();
    }
}