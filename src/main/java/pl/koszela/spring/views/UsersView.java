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
import pl.koszela.spring.crud.DeleteUsers;
import pl.koszela.spring.crud.ReadUser;
import pl.koszela.spring.entities.EntityPersonalData;
import pl.koszela.spring.entities.EntityUser;
import pl.koszela.spring.repositories.UsersRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private ComboBox<EntityPersonalData> combobox = new ComboBox<>();
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
            deleteUsers.removeUser(combobox.getValue());
            combobox.clear();
            loadUsers(usersRepo);
            combobox.getDataProvider().refreshAll();
        });
        removeUser.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return removeUser;
    }

    private Button selectUser() {
        loadUser.addClickListener(buttonClickEvent -> {
            EntityUser find = readUser.getUser(combobox.getValue());
            name.setValue(find.getEntityPersonalData().getName());
            surname.setValue(find.getEntityPersonalData().getSurname());
            adress.setValue(find.getEntityPersonalData().getAdress());
            telephoneNumber.setValue(find.getEntityPersonalData().getTelephoneNumber());
            email.setValue(find.getEntityPersonalData().getEmail());
        });
        loadUser.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        return loadUser;
    }

    private ComboBox<EntityPersonalData> loadUsers(UsersRepo usersRepo) {
        List<EntityUser> all = usersRepo.findEntityUserByEntityPersonalDataIsNotNull();
        List<EntityPersonalData> nameAndSurname = new ArrayList<>();
        all.forEach(e -> nameAndSurname.add(e.getEntityPersonalData()));
        combobox.setItems(nameAndSurname);
        combobox.setItemLabelGenerator(entityPersonalData -> entityPersonalData.getName() + " " + entityPersonalData.getSurname());
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
        VaadinSession.getCurrent().getSession().setAttribute("personalData", personalData);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        VaadinSession.getCurrent().getSession().removeAttribute("gutter");
        VaadinSession.getCurrent().getSession().removeAttribute("personalData");
        VaadinSession.getCurrent().getSession().removeAttribute("tiles");
        VaadinSession.getCurrent().getSession().removeAttribute("inputData");
        VaadinSession.getCurrent().getSession().removeAttribute("accesories");
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        EntityPersonalData entityPersonalData = (EntityPersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalData");
        if (entityPersonalData == null) {
            save();
            action.proceed();
        }
        action.proceed();
    }
}