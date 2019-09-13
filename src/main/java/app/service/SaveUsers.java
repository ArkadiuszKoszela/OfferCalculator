package app.service;

import app.GUIs.ShowTableTiles;
import app.calculate.CalculateTiles;
import app.entities.EntityInputData;
import app.entities.EntityUser;
import app.inputFields.ServiceDataCustomer;
import app.inputFields.ServiceNumberFiled;
import app.repositories.InputData;
import app.repositories.UsersRepo;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SaveUsers {

    private UsersRepo usersRepo;
    private ServiceNumberFiled serviceNumberFiled;
    private ServiceDataCustomer serviceDataCustomer;

    private InputData inputData;
    private NazwaCennika nazwaCennika;
    private CalculateTiles calculateTiles;
    ShowTableTiles inputUser = new ShowTableTiles();

    @Autowired
    public SaveUsers(UsersRepo usersRepo, ServiceNumberFiled serviceNumberFiled,
                     ServiceDataCustomer serviceDataCustomer, InputData inputData, NazwaCennika nazwaCennika,
                     CalculateTiles calculateTiles) {
        this.usersRepo = Objects.requireNonNull(usersRepo);
        this.serviceNumberFiled = Objects.requireNonNull(serviceNumberFiled);
        this.serviceDataCustomer = Objects.requireNonNull(serviceDataCustomer);
        this.inputData = Objects.requireNonNull(inputData);
        this.nazwaCennika = Objects.requireNonNull(nazwaCennika);
        this.calculateTiles = Objects.requireNonNull(calculateTiles);
    }

    public void saveUser() {
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

        if (czyWszystkieDane()) {

            // SAVE TO DATABASE
            inputData.save(entityInputData);
            Set<EntityInputData> entityInputDataList = new LinkedHashSet<>();
            entityInputDataList.add(entityInputData);
            // SAVE TO DATABASE
            EntityUser newUser = new EntityUser();

            newUser.setName(serviceDataCustomer.getName().getValue());
            newUser.setSurname(serviceDataCustomer.getSurname().getValue());
            newUser.setAdress(serviceDataCustomer.getAdress().getValue());
            newUser.setTelephoneNumber(serviceDataCustomer.getTelephoneNumber().getValue());
            newUser.setDateOfMeeting(serviceDataCustomer.getDateOfMeeting().getValue());
            newUser.setEntityInputData(entityInputDataList);
            newUser.setPriceListName(inputUser.priceListCB.getValue());
            List<EntityUser> sprawdzanaLista = allUser();
            if (sprawdzanaLista.size() == 0) {
                usersRepo.save(newUser);
                getNotification("Zapisałem użytkownika wraz z danymi");
            }
            if (sprawdzanaLista.size() > 0) {
                for (EntityUser oldUser : sprawdzanaLista) {
                    if (!oldUser.getName().equals(newUser.getName()) && !oldUser.getSurname().equals(newUser.getSurname())
                            && !oldUser.getAdress().equals(newUser.getAdress())) {
                        usersRepo.save(newUser);
                        getNotification("Zapisałem użytkownika wraz z danymi");
                    } else if (oldUser.getName().equals(newUser.getName()) && oldUser.getSurname().equals(newUser.getSurname())
                            && oldUser.getAdress().equals(newUser.getAdress()) && !oldUser.getPriceListName().contains(newUser.getPriceListName())) {

                        ConfirmDialog editUserDialog = new ConfirmDialog("Podany użytkownik już istnieje"
                                , "Czy chcesz dodać nowy cennik ?"
                                , "Dodaj", event -> {
                            EntityUser editUser = new EntityUser();
                            editUser.setName(oldUser.getName());
                            editUser.setSurname(oldUser.getSurname());
                            editUser.setAdress(oldUser.getAdress());
                            editUser.setDateOfMeeting(oldUser.getDateOfMeeting());
                            editUser.setEntityInputData(oldUser.getEntityInputData());
                            editUser.setPriceListName(oldUser.getPriceListName() + " , " + inputUser.priceListCB.getValue());
                            usersRepo.delete(oldUser);
                            usersRepo.save(editUser);
                            getNotification("Dodałem do użytkownika nowy cennik");
                        }
                                , "Nie dodawaj", event -> getNotification("Anulowano"));
                        editUserDialog.open();
                    } else {
                        getNotification("Podany użytkownik jest już w bazie");
                    }
                }
            }
        }
    }

    private boolean czyWszystkieDane() {
        if (!serviceDataCustomer.getName().getValue().isEmpty() && !serviceDataCustomer.getSurname().getValue().isEmpty()
                && !serviceDataCustomer.getAdress().getValue().isEmpty() && !serviceDataCustomer.getTelephoneNumber().getValue().isEmpty()) {
            return true;
        } else {
            Notification notification = new Notification("Uzupełnij wszystkie dane !!", 4000);
            notification.setPosition(Notification.Position.TOP_CENTER);
            notification.open();
            return false;
        }
    }

    private List<EntityUser> allUser() {
        Iterable<EntityUser> usersEntityIterable = usersRepo.findAll();
        List<EntityUser> entityUserList = new ArrayList<>();
        usersEntityIterable.forEach(entityUserList::add);
        return entityUserList;
    }

    private Notification getNotification(String komunikat) {
        Notification notification = new Notification(komunikat, 5000);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.open();
        return notification;
    }

}
