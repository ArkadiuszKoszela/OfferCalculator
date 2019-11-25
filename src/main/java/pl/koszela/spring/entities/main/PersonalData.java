package pl.koszela.spring.entities.main;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "personal_data")
public class PersonalData {

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

    public PersonalData() {
    }

    public static PersonalData.Builder builder() {
        return new PersonalData.Builder();
    }


    public static final class Builder {
        private String name;
        private String surname;
        private String adress;
        private String telephoneNumber;
        private LocalDate dateOfMeeting;
        private String email;

        public PersonalData build() {
            PersonalData personalData = new PersonalData();
            personalData.name = this.name;
            personalData.surname = this.surname;
            personalData.adress = this.adress;
            personalData.email = this.email;
            personalData.telephoneNumber = this.telephoneNumber;
            personalData.dateOfMeeting = this.dateOfMeeting;
            return personalData;
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
