package app.views;

import app.calculate.CalculateTiles;
import app.controllers.ControllerVaadin;
import app.entities.EntityUser;
import app.inputFields.ServiceDataCustomer;
import app.inputFields.ServiceNumberFiled;
import app.repositories.UsersRepo;
import app.service.Layout;
import app.service.SaveUsers;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static app.inputFields.ServiceNotification.getNotificationSucces;
import static app.inputFields.ServiceSplitLayout.getSideMenuSettings;
import static app.inputFields.ServiceSplitLayout.ustawieniaStrony;

@Route(value = InputUser.INPUT_USER)
public class InputUser extends SplitLayout implements Layout {

    public static final String INPUT_USER = "users/inputUser";

    private ControllerVaadin controllerVaadin;
    private SaveUsers saveUser;
    private ServiceDataCustomer serviceDataCustomer;
    private CalculateTiles calculateTiles;
    private UsersRepo usersRepo;
    private ServiceNumberFiled serviceNumberFiled;

    private FormLayout board = new FormLayout();

    private ComboBox<String> comboBoxUsers = new ComboBox<>();

    public ComboBox<String> priceListCB = new ComboBox<>();

    public InputUser() {
    }

    @Autowired
    public InputUser(ControllerVaadin controllerVaadin, SaveUsers saveUser, ServiceDataCustomer serviceDataCustomer, UsersRepo usersRepo,
                     ServiceNumberFiled serviceNumberFiled, CalculateTiles calculateTiles) {
        this.controllerVaadin = Objects.requireNonNull(controllerVaadin);
        this.saveUser = Objects.requireNonNull(saveUser);
        this.serviceDataCustomer = Objects.requireNonNull(serviceDataCustomer);
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.serviceNumberFiled = Objects.requireNonNull(serviceNumberFiled);
        this.calculateTiles = Objects.requireNonNull(calculateTiles);

        setOrientation(SplitLayout.Orientation.VERTICAL);
        Button selectUser = new Button("Wczytaj pola z usera");
        selectUser.addClickListener(buttonClickEvent -> loadUser());

        Label label = new Label(" ");

        /*serviceDataCustomer.createTextFieldsForUser();*/
        board.add(serviceDataCustomer.getName(), serviceDataCustomer.getSurname(), serviceDataCustomer.getAdress(), serviceDataCustomer.getTelephoneNumber());
        board.add(serviceDataCustomer.getEmail(), label);
        /*board.add(serviceDataCustomer.getDateOfMeeting(),label );*/

        loadUserComboBox();

        Label label1 = new Label("Wczytaj użytkownika: ");
        Label label2 = new Label(" ");
        Label label3 = new Label("Podaj nazwę cennika:");
        Label label4 = new Label(" ");

        board.add(label1, label2);
        board.add(comboBoxUsers, selectUser);
        board.add(label3, label4);
        board.add(priceListCB, saveUser(saveUser));

        if (!calculateTiles.getAvailablePriceList().isEmpty()) {
            priceListCB.setItems(calculateTiles.getAvailablePriceList());
        }

        addToPrimary(ustawieniaStrony(controllerVaadin));
        addToSecondary(getSideMenu(controllerVaadin));
    }

    private Button saveUser(SaveUsers saveUser) {
        Button saveUserWithCalculations = new Button("Zapisz użytkownika");
        saveUserWithCalculations.addClickListener(buttonClickEvent -> saveUser.saveUser(priceListCB));
        return saveUserWithCalculations;
    }

    @Override
    public SplitLayout getSideMenu(ControllerVaadin controllerVaadin) {
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.addToPrimary(controllerVaadin.sideMenuUser());
        splitLayout.addToSecondary(board);
        getSideMenuSettings(splitLayout);
        return splitLayout;
    }

    private void loadUserComboBox() {

        Iterable<EntityUser> allUsersFromRepository = usersRepo.findAll();
        List<String> nameAndSurname = new ArrayList<>();
        allUsersFromRepository.forEach(user -> nameAndSurname.add(user.getName().concat(" ").concat(user.getSurname())));
        if (nameAndSurname.size() > 0) {
            comboBoxUsers.setItems(nameAndSurname);
        }
    }

    private void loadUser() {

        String nameISurname = comboBoxUsers.getValue();
        String[] strings = nameISurname.split(" ");
        EntityUser entityUser = usersRepo.findUsersEntityByNameAndSurnameEquals(strings[0], strings[1]);

        if (entityUser.getEntityInputData().size() > 0) {
            serviceNumberFiled.numberField1.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getPowierzchniaPolaci()));
            serviceNumberFiled.numberField2.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getDlugoscKalenic()));
            serviceNumberFiled.numberField3.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getDlugoscKalenicSkosnych()));
            serviceNumberFiled.numberField4.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getDlugoscKalenicProstych()));
            serviceNumberFiled.numberField5.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getDlugoscKoszy()));
            serviceNumberFiled.numberField6.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getDlugoscKrawedziLewych()));
            serviceNumberFiled.numberField7.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getDlugoscKrawedziPrawych()));
            serviceNumberFiled.numberField8.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getObwodKomina()));
            serviceNumberFiled.numberField9.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getDlugoscOkapu()));
            serviceNumberFiled.numberField10.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getDachowkaWentylacyjna()));
            serviceNumberFiled.numberField11.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getKompletKominkaWentylacyjnego()));
            serviceNumberFiled.numberField12.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getGasiarPoczatkowyKalenicaProsta()));
            serviceNumberFiled.numberField13.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getGasiarKoncowyKalenicaProsta()));
            serviceNumberFiled.numberField14.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getGasiarZaokraglony()));
            serviceNumberFiled.numberField15.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getTrojnik()));
            serviceNumberFiled.numberField16.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getCzwornik()));
            serviceNumberFiled.numberField17.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getGasiarZPodwojnaMufa()));
            serviceNumberFiled.numberField18.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getDachowkaDwufalowa()));
            serviceNumberFiled.numberField19.setValue(Double.valueOf(entityUser.getEntityInputData().iterator().next().getOknoPolaciowe()));

            getNotificationSucces("Wczytałem usera wraz z danymi");

        }
    }
}
