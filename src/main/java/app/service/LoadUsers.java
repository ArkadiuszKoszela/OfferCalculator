package app.service;

import app.inputFields.ServiceNumberFiled;
import app.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoadUsers {

    private UsersRepo usersRepo;
    private ServiceNumberFiled serviceNumberFiled;

    @Autowired
    public LoadUsers(UsersRepo usersRepo, ServiceNumberFiled serviceNumberFiled) {
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.serviceNumberFiled = Objects.requireNonNull(serviceNumberFiled);
    }

    /*public ComboBox<String> comboBoxUsers = new ComboBox<>();*/


    /*private void getNotification(String komunikat) {
        Notification notification = new Notification(komunikat, 5000);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.open();
    }


    public void loadUser() {

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
    }*/

}
