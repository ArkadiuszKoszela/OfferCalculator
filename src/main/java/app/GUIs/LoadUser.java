package app.GUIs;

import app.controllers.ControllerVaadin;
import app.entities.EntityUser;
import app.inputFields.ServiceNumberFiled;
import app.repositories.UsersRepo;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Route(value = LoadUser.ENTER_DATA)
public class LoadUser extends SplitLayout {

    public static final String ENTER_DATA = "LoadUser";

    private ControllerVaadin controllerVaadin;
    private UsersRepo usersRepo;
    private ServiceNumberFiled serviceNumberFiled;

    private ComboBox<String> comboBoxUsers;

    private List<NumberField> listOfNumberFields;

    public LoadUser() {
    }

    @Autowired
    public LoadUser(UsersRepo usersRepo, ServiceNumberFiled serviceNumberFiled, ControllerVaadin controllerVaadin) {
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.serviceNumberFiled = Objects.requireNonNull(serviceNumberFiled);
        this.controllerVaadin = Objects.requireNonNull(controllerVaadin);

        comboBoxUsers = new ComboBox<>();
        setOrientation(Orientation.VERTICAL);
        addToPrimary(controllerVaadin.routerLink());

        Button selectUser = new Button("Wczytaj pola z usera");

        loadUserComboBox(comboBoxUsers);

        selectUser.addClickListener(buttonClickEvent -> loadUser());

        Board board = new Board();
        Label label2 = new Label("Podaj użytkownika: ");
        Label label3 = new Label(" ");

        board.addRow(label2, label3);
        board.addRow(comboBoxUsers, selectUser);

        addToSecondary(board);

        ustawieniaStrony();
    }

    private void getNotification(String komunikat) {
        Notification notification = new Notification(komunikat, 5000);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.open();
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

            getNotification("Wczytałem usera wraz z danymi");

        }
    }

    private void ustawieniaStrony() {
        Board board = new Board();
        board.addRow(controllerVaadin.routerLink());
        board.getStyle().set("background", "#DCDCDC");
        addToPrimary(board);
        setPrimaryStyle("minWidth", "1280px");
        setPrimaryStyle("maxWidth", "1280px");
        setPrimaryStyle("minHeight", "70px");
        setPrimaryStyle("maxHeight", "700px");
        setSecondaryStyle("minWidth", "1280px");
        setSecondaryStyle("maxWidth", "1280px");
        setSecondaryStyle("minHeight", "500px");
        setSecondaryStyle("maxHeight", "500px");
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

}

