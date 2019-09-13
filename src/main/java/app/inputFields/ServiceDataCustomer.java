package app.inputFields;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ServiceDataCustomer {

    private TextField name;
    private TextField surname;
    private TextField adress;
    private TextField telephoneNumber;
    private DatePicker dateOfMeeting;


    public void createTextFieldsForUser() {
        setName(new TextField("Imię"));
        getName().setValue("Arek");
        getName().setRequired(true);
        setSurname(new TextField("Nazwisko"));
        getSurname().setValue("Koszela");
        getSurname().setRequired(true);
        setAdress(new TextField("Adres zamieszkania"));
        getAdress().setValue("Szczecin");
        getAdress().setRequired(true);
        setTelephoneNumber(new TextField("Numer kontaktowy"));
        getTelephoneNumber().setValue("1234");
        getTelephoneNumber().setRequired(true);
        setDateOfMeeting(new DatePicker("Data spotkania"));
        getDateOfMeeting().setLocale(Locale.getDefault());
        getDateOfMeeting().setRequired(true);
    }

    public TextField getName() {
        return name;
    }

    public void setName(TextField name) {
        this.name = name;
    }

    public TextField getSurname() {
        return surname;
    }

    public void setSurname(TextField surname) {
        this.surname = surname;
    }

    public TextField getAdress() {
        return adress;
    }

    public void setAdress(TextField adress) {
        this.adress = adress;
    }

    public TextField getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(TextField telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public DatePicker getDateOfMeeting() {
        return dateOfMeeting;
    }

    public void setDateOfMeeting(DatePicker dateOfMeeting) {
        this.dateOfMeeting = dateOfMeeting;
    }
}
