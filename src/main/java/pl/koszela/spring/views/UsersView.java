package pl.koszela.spring.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.*;
import pl.koszela.spring.repositories.UsersRepo;
import pl.koszela.spring.service.RemoveUsers;

import java.util.*;

import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationError;
import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationSucces;

@Route(value = UsersView.INPUT_USER, layout = MainView.class)
public class UsersView extends VerticalLayout implements BeforeLeaveObserver {

    static final String INPUT_USER = "users/input";

    private UsersRepo usersRepo;

    public TextField name = new TextField("Imię", "Arek", "Imię");
    private TextField surname = new TextField("Nazwisko", "Koszela", "Nazwisko");
    private TextField adress = new TextField("Adres zamieszkania", "Szczecin", "Adres zamieszkania");
    private TextField telephoneNumber = new TextField("Numer kontaktowy", "12345678", "Numer kontaktowy");
    private EmailField email = new EmailField("E-mail");
    private ComboBox<String> combobox = new ComboBox<>();
    private Button loadUser = new Button("Wczytaj klienta");
    private Button removeUser = new Button(" Usuń użytkownika");
    private FormLayout board = new FormLayout();
    private EntityPersonalData entityPersonalData = (EntityPersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalDataFromRepo");

    private RemoveUsers removeUsers;

    @Autowired
    public UsersView(UsersRepo usersRepo, RemoveUsers removeUsers) {
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.removeUsers = Objects.requireNonNull(removeUsers);

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
            removeUsers.removeUser(combobox);
            combobox.clear();
            loadUsers(usersRepo);
            combobox.getDataProvider().refreshAll();
        });
        return removeUser;
    }

    private Button selectUser() {
        loadUser.addClickListener(buttonClickEvent -> {
            String[] split = combobox.getValue().split(" ");
            EntityUser find = usersRepo.findEntityUserByEntityPersonalDataNameAndEntityPersonalDataSurname(split[0], split[1]);

            VaadinSession.getCurrent().getSession().setAttribute("personalDataFromRepo", find.getEntityPersonalData());
            VaadinSession.getCurrent().getSession().setAttribute("tilesInputFromRepo", find.getEntityInputDataTiles());
            VaadinSession.getCurrent().getSession().setAttribute("accesoriesInputFromRepo", find.getEntityInputDataAccesories());
            VaadinSession.getCurrent().getSession().setAttribute("entityWindowsFromRepo", find.getEntityWindows());
            VaadinSession.getCurrent().getSession().setAttribute("entityKolnierzFromRepo", find.getEntityKolnierz());
            VaadinSession.getCurrent().getSession().setAttribute("allTilesFromRepo", find.getTiles());
            VaadinSession.getCurrent().getSession().setAttribute("accesoriesFromRepo", find.getResultAccesories());

            EntityPersonalData data = (EntityPersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalDataFromRepo");
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
        VaadinSession.getCurrent().getSession().setAttribute("personalData", personalData);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        VaadinSession.getCurrent().getSession().removeAttribute("resultTiles");
        VaadinSession.getCurrent().getSession().removeAttribute("resultTilesFromRepo");
        VaadinSession.getCurrent().getSession().removeAttribute("tilesInput");
        VaadinSession.getCurrent().getSession().removeAttribute("tilesInputFromRepo");
        VaadinSession.getCurrent().getSession().removeAttribute("allTilesFromRepo");
        VaadinSession.getCurrent().getSession().removeAttribute("accesoriesFromRepo");
        VaadinSession.getCurrent().getSession().removeAttribute("accesories");
        if (entityPersonalData != null) {
            VaadinSession.getCurrent().getSession().removeAttribute("personalDataFromRepo");
            getNotificationSucces("WEJSCIE Klienci - wszystko ok (repo)");
        } else if (entityPersonalData == null) {
            VaadinSession.getCurrent().getSession().removeAttribute("personalData");
            getNotificationSucces("WEJSCIE Klienci - wszystko ok (bez repo)");
        } else {
            getNotificationError("WEJSCIE Klienci - coś poszło nie tak");
        }
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        EntityPersonalData entityPersonalData = (EntityPersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalDataFromRepo");
        if(entityPersonalData != null){
            VaadinSession.getCurrent().getSession().removeAttribute("personalData");
            getNotificationSucces("Wszystko ok (repo)");
            action.proceed();
        } else if (entityPersonalData == null){
            VaadinSession.getCurrent().getSession().removeAttribute("tilesInput");
            VaadinSession.getCurrent().getSession().removeAttribute("resultTiles");
            save();
            action.proceed();
            getNotificationSucces("Wszystko ok (bez repo)");
        }else {
            getNotificationError("Klienci - coś poszło nie tak");
            action.proceed();
        }
    }
}