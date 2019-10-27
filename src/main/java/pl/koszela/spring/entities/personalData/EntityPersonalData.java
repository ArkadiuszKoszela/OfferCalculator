package pl.koszela.spring.entities.personalData;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "personal_data")
public class EntityPersonalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String adress;
    private String telephoneNumber;
    private LocalDate dateOfMeeting;
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EntityPersonalData() {
    }

    public static EntityPersonalData.Builder builder() {
        return new EntityPersonalData.Builder();
    }


    public static final class Builder {
        private String name;
        private String surname;
        private String adress;
        private String telephoneNumber;
        private LocalDate dateOfMeeting;
        private String email;

        public EntityPersonalData build() {
            EntityPersonalData entityPersonalData = new EntityPersonalData();
            entityPersonalData.name = this.name;
            entityPersonalData.surname = this.surname;
            entityPersonalData.adress = this.adress;
            entityPersonalData.email = this.email;
            entityPersonalData.telephoneNumber = this.telephoneNumber;
            entityPersonalData.dateOfMeeting = this.dateOfMeeting;
            return entityPersonalData;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder adress(String adress) {
            this.adress = adress;
            return this;
        }

        public Builder telephoneNumber(String telephoneNumber) {
            this.telephoneNumber = telephoneNumber;
            return this;
        }

        public Builder dateOfMeeting(LocalDate dateOfMeeting) {
            this.dateOfMeeting = dateOfMeeting;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }
    }
}
