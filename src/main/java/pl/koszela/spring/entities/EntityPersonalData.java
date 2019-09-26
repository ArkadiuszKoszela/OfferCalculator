package pl.koszela.spring.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
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
