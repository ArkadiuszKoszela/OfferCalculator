package app.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "users")
public class EntityUser {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private String adress;

    private String telephoneNumber;

    private LocalDate dateOfMeeting;

    private String priceListName;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<EntityInputData> entityInputData = new LinkedHashSet<>();

    public EntityUser() {
    }

    public EntityUser(String name, String surname, String adress, String telephoneNumber, LocalDate date, String priceListName) {
        this.name = name;
        this.surname = surname;
        this.adress = adress;
        this.telephoneNumber = telephoneNumber;
        this.dateOfMeeting = date;
        this.priceListName = priceListName;
    }

    public EntityUser(String name, String surname, String adress, String telephoneNumber, LocalDate date,
                      Set<EntityInputData> entityInputData) {
        this.name = name;
        this.surname = surname;
        this.adress = adress;
        this.telephoneNumber = telephoneNumber;
        this.dateOfMeeting = date;
        this.entityInputData = entityInputData;
    }

    public EntityUser(String name, String surname, String adress, String telephoneNumber, LocalDate date, String priceListName, Set<EntityInputData> entityInputData) {
        this.name = name;
        this.surname = surname;
        this.adress = adress;
        this.telephoneNumber = telephoneNumber;
        this.dateOfMeeting = date;
        this.priceListName = priceListName;
        this.entityInputData = entityInputData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public LocalDate getDateOfMeeting() {
        return dateOfMeeting;
    }

    public void setDateOfMeeting(LocalDate dateOfMeeting) {
        this.dateOfMeeting = dateOfMeeting;
    }

    public String getPriceListName() {
        return priceListName;
    }

    public void setPriceListName(String priceListName) {
        this.priceListName = priceListName;
    }

    public Set<EntityInputData> getEntityInputData() {
        return entityInputData;
    }

    public void setEntityInputData(Set<EntityInputData> entityInputData) {
        this.entityInputData = entityInputData;
    }

    @Override
    public String toString() {
        return "EntityUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", adress='" + adress + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", dateOfMeeting=" + dateOfMeeting +
                ", priceListName='" + priceListName + '\'' +
                ", entityInputData=" + entityInputData +
                '}';
    }
}
