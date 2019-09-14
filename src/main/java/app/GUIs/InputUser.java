package app.GUIs;

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

    public static final String INPUT_USER = "InputUser";

    private ControllerVaadin controllerVaadin;
    private SaveUsers saveUser;
    private ServiceDataCustomer serviceDataCustomer;
    private UsersRepo usersRepo;
    private ServiceNumberFiled serviceNumberFiled;

    private FormLayout board;

    private ComboBox<String> comboBoxUsers;

    public InputUser() {
    }

    @Autowired
    public InputUser(ControllerVaadin controllerVaadin, SaveUsers saveUser, ServiceDataCustomer serviceDataCustomer, UsersRepo usersRepo,
                     ServiceNumberFiled serviceNumberFiled) {
        this.controllerVaadin = Objects.requireNonNull(controllerVaadin);
        this.saveUser = Objects.requireNonNull(saveUser);
        this.serviceDataCustomer = Objects.requireNonNull(serviceDataCustomer);
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.serviceNumberFiled = Objects.requireNonNull(serviceNumberFiled);

        setOrientation(Orientation.VERTICAL);

        Button selectUser = new Button("Wczytaj pola z usera");
        selectUser.addClickListener(buttonClickEvent -> loadUser());

        comboBoxUsers = new ComboBox<>();
        board = new FormLayout();
        Label label = new Label(" ");

        /*serviceDataCustomer.createTextFieldsForUser();*/
        board.add(serviceDataCustomer.getName(), serviceDataCustomer.getSurname(), serviceDataCustomer.getAdress(), serviceDataCustomer.getTelephoneNumber());
        /*board.add(serviceDataCustomer.getDateOfMeeting(),label );*/

        loadUserComboBox(comboBoxUsers);

        Label label2 = new Label("Wczytaj użytkownika: ");
        Label label3 = new Label(" ");

        board.add(label2, label3);
        board.add(comboBoxUsers, selectUser);

        addToPrimary(ustawieniaStrony(controllerVaadin));
        addToSecondary(getSideMenu(controllerVaadin));
    }

    @Override
    public SplitLayout getSideMenu(ControllerVaadin controllerVaadin) {
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.addToPrimary(controllerVaadin.sideMenuUser());
        splitLayout.addToSecondary(board);
        getSideMenuSettings(splitLayout);
        return splitLayout;
    }

    private void loadUserComboBox(ComboBox<String> comboBoxUsers) {

        Iterable<EntityUser> allUsersFromRepository = usersRepo.findAll();
        List<EntityUser> allUser = new ArrayList<>();
        allUsersFromRepository.forEach(allUser::add);
        if (allUser.size() > 0) {
            List<String> nameAndSurname = new ArrayList<>();
            allUser.forEach(user -> nameAndSurname.add(user.getName() + " " + user.getSurname()));
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

    /*private void getNotificationSucces(String komunikat) {
        Notification notification = new Notification(komunikat, 5000);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.open();
    }*/

}
