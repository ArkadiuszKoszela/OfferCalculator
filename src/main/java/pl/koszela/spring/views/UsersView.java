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
import pl.koszela.spring.entities.*;
import pl.koszela.spring.repositories.UsersRepo;
import pl.koszela.spring.crud.DeleteUsers;

import java.util.*;

import static pl.koszela.spring.service.ServiceNotification.getNotificationError;
import static pl.koszela.spring.service.ServiceNotification.getNotificationSucces;

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
    private ComboBox<String> combobox = new ComboBox<>();
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
            deleteUsers.removeUser(combobox);
            combobox.clear();
            loadUsers(usersRepo);
            combobox.getDataProvider().refreshAll();
        });
        removeUser.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return removeUser;
    }

    private Button selectUser() {
        loadUser.addClickListener(buttonClickEvent -> {
            String[] split = combobox.getValue().split(" ");
            EntityUser find = readUser.getUser(split[0], split[1]);

            name.setValue(find.getEntityPersonalData().getName());
            surname.setValue(find.getEntityPersonalData().getSurname());
            adress.setValue(find.getEntityPersonalData().getAdress());
            telephoneNumber.setValue(find.getEntityPersonalData().getTelephoneNumber());
            email.setValue(find.getEntityPersonalData().getEmail());
        });
        loadUser.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
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
        VaadinSession.getCurrent().getSession().setAttribute("personalDataFromRepo", personalData);
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
        VaadinSession.getCurrent().getSession().removeAttribute("personalData");
        VaadinSession.getCurrent().getSession().removeAttribute("personalDataFromRepo");

        VaadinSession.getCurrent().getSession().removeAttribute("accesoriesInput");
        VaadinSession.getCurrent().getSession().removeAttribute("entityWindows");
        VaadinSession.getCurrent().getSession().removeAttribute("entityKolnierz");
        VaadinSession.getCurrent().getSession().removeAttribute("allTiles");

        VaadinSession.getCurrent().getSession().removeAttribute("accesoriesInputFromRepo");
        VaadinSession.getCurrent().getSession().removeAttribute("entityWindowsFromRepo");
        VaadinSession.getCurrent().getSession().removeAttribute("entityKolnierzFromRepo");
        VaadinSession.getCurrent().getSession().removeAttribute("allTilesFromRepo");
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        EntityPersonalData entityPersonalData = (EntityPersonalData) VaadinSession.getCurrent().getSession().getAttribute("personalDataFromRepo");
        if(entityPersonalData == null){
            save();
            VaadinSession.getCurrent().getSession().setAttribute("tilesInputFromRepo", defaultValues());
            action.proceed();
        }
        VaadinSession.getCurrent().getSession().removeAttribute("personalData");
        VaadinSession.getCurrent().getSession().removeAttribute("tilesInput");
        VaadinSession.getCurrent().getSession().removeAttribute("resultTiles");
        action.proceed();
    }

    private EntityInputDataTiles defaultValues(){
        return EntityInputDataTiles.builder()
                .powierzchniaPolaci(300d)
                .dlugoscKalenic(65d)
                .dlugoscKalenicProstych(65d)
                .dlugoscKalenicSkosnych(1d)
                .dlugoscKoszy(8d)
                .dlugoscKrawedziLewych(5d)
                .dlugoscKrawedziPrawych(5d)
                .obwodKomina(3d)
                .dlugoscOkapu(38d)
                .dachowkaWentylacyjna(1d)
                .kompletKominkaWentylacyjnego(1d)
                .gasiarPoczatkowyKalenicaProsta(1d)
                .gasiarKoncowyKalenicaProsta(1d)
                .gasiarZaokraglony(1d)
                .trojnik(6d)
                .czwornik(1d)
                .gasiarZPodwojnaMufa(1d)
                .dachowkaDwufalowa(1d)
                .oknoPolaciowe(1d)
                .build();
    }
}