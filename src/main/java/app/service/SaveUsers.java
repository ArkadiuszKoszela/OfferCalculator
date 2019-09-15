package app.service;

import app.entities.EntityInputData;
import app.entities.EntityUser;
import app.inputFields.ServiceDataCustomer;
import app.inputFields.ServiceNumberFiled;
import app.repositories.InputData;
import app.repositories.UsersRepo;
import app.views.InputUser;
import com.vaadin.flow.component.combobox.ComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static app.inputFields.ServiceNotification.getNotificationError;
import static app.inputFields.ServiceNotification.getNotificationSucces;

@Service
public class SaveUsers {

    private UsersRepo usersRepo;
    private ServiceNumberFiled serviceNumberFiled;
    @Autowired
    private ServiceDataCustomer serviceDataCustomer;

    private InputData inputData;
    InputUser inputUser = new InputUser();

    @Autowired
    public SaveUsers(UsersRepo usersRepo, ServiceNumberFiled serviceNumberFiled,
                     ServiceDataCustomer serviceDataCustomer, InputData inputData) {
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.serviceNumberFiled = Objects.requireNonNull(serviceNumberFiled);
        this.serviceDataCustomer = Objects.requireNonNull(serviceDataCustomer);
        this.inputData = Objects.requireNonNull(inputData);
    }

    public void saveUser(ComboBox<String> comboBox) {

        if (allDataIsCompleted()) {

            EntityUser newUser = new EntityUser();

            newUser.setName(serviceDataCustomer.getName().getValue());
            newUser.setSurname(serviceDataCustomer.getSurname().getValue());
            newUser.setAdress(serviceDataCustomer.getAdress().getValue());
            newUser.setTelephoneNumber(serviceDataCustomer.getTelephoneNumber().getValue());
            /*newUser.setDateOfMeeting(serviceDataCustomer.getDateOfMeeting().getValue());*/
            newUser.setEntityInputData(saveInputData());
            newUser.setPriceListName(comboBox.getValue());

            usersRepo.save(newUser);
            getNotificationSucces("Zapisałem użytkownika wraz z danymi");
        }
    }

    private Set<EntityInputData> saveInputData() {
        EntityInputData entityInputData = new EntityInputData();

        entityInputData.setPowierzchniaPolaci(String.valueOf(serviceNumberFiled.numberField1.getValue()));
        entityInputData.setDlugoscKalenic(String.valueOf(serviceNumberFiled.numberField2.getValue()));
        entityInputData.setDlugoscKalenicSkosnych(String.valueOf(serviceNumberFiled.numberField3.getValue()));
        entityInputData.setDlugoscKalenicProstych(String.valueOf(serviceNumberFiled.numberField4.getValue()));
        entityInputData.setDlugoscKoszy(String.valueOf(serviceNumberFiled.numberField5.getValue()));
        entityInputData.setDlugoscKrawedziLewych(String.valueOf(serviceNumberFiled.numberField6.getValue()));
        entityInputData.setDlugoscKrawedziPrawych(String.valueOf(serviceNumberFiled.numberField7.getValue()));
        entityInputData.setObwodKomina(String.valueOf(serviceNumberFiled.numberField8.getValue()));
        entityInputData.setDlugoscOkapu(String.valueOf(serviceNumberFiled.numberField9.getValue()));
        entityInputData.setDachowkaWentylacyjna(String.valueOf(serviceNumberFiled.numberField10.getValue()));
        entityInputData.setKompletKominkaWentylacyjnego(String.valueOf(serviceNumberFiled.numberField11.getValue()));
        entityInputData.setGasiarPoczatkowyKalenicaProsta(String.valueOf(serviceNumberFiled.numberField12.getValue()));
        entityInputData.setGasiarKoncowyKalenicaProsta(String.valueOf(serviceNumberFiled.numberField13.getValue()));
        entityInputData.setGasiarZaokraglony(String.valueOf(serviceNumberFiled.numberField14.getValue()));
        entityInputData.setTrojnik(String.valueOf(serviceNumberFiled.numberField15.getValue()));
        entityInputData.setCzwornik(String.valueOf(serviceNumberFiled.numberField16.getValue()));
        entityInputData.setGasiarZPodwojnaMufa(String.valueOf(serviceNumberFiled.numberField17.getValue()));
        entityInputData.setDachowkaDwufalowa(String.valueOf(serviceNumberFiled.numberField18.getValue()));
        entityInputData.setOknoPolaciowe(String.valueOf(serviceNumberFiled.numberField19.getValue()));
        Set<EntityInputData> entityInputDataList = new LinkedHashSet<>();
        entityInputDataList.add(entityInputData);

        inputData.save(entityInputData);
        return entityInputDataList;
    }

    private boolean allDataIsCompleted() {
        if (!serviceDataCustomer.getName().getValue().isEmpty() && !serviceDataCustomer.getSurname().getValue().isEmpty()
                && !serviceDataCustomer.getAdress().getValue().isEmpty() && !serviceDataCustomer.getTelephoneNumber().getValue().isEmpty()) {
            return true;
        } else {
            getNotificationError("Uzupełnij wszystkie dane !!");
            return false;
        }
    }

}
