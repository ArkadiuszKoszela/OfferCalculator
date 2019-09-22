package pl.koszela.spring.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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
    private String email;
    private String priceListName;
    private boolean hasTiles;
    private boolean hasAccesories;
    private boolean hasWindows;

    @OneToOne
    private EntityInputDataTiles entityInputDataTiles;
    @OneToOne
    private EntityInputDataAccesories entityInputDataAccesories;
    @OneToOne
    private EntityWindows entityWindows;
    @OneToMany(fetch = FetchType.LAZY)
    private List<EntityAccesories> entityAccesories;
    @OneToMany(fetch = FetchType.LAZY)
    private List<EntityTiles> entityTiles;

    public EntityUser() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPriceListName() {
        return priceListName;
    }

    public void setPriceListName(String priceListName) {
        this.priceListName = priceListName;
    }

    public EntityInputDataTiles getEntityInputDataTiles() {
        return entityInputDataTiles;
    }

    public void setEntityInputDataTiles(EntityInputDataTiles entityInputDataTiles) {
        this.entityInputDataTiles = entityInputDataTiles;
    }

    public EntityInputDataAccesories getEntityInputDataAccesories() {
        return entityInputDataAccesories;
    }

    public void setEntityInputDataAccesories(EntityInputDataAccesories entityInputDataAccesories) {
        this.entityInputDataAccesories = entityInputDataAccesories;
    }

    public EntityWindows getEntityWindows() {
        return entityWindows;
    }

    public void setEntityWindows(EntityWindows entityWindows) {
        this.entityWindows = entityWindows;
    }

    public List<EntityAccesories> getEntityAccesories() {
        return entityAccesories;
    }

    public void setEntityAccesories(List<EntityAccesories> entityAccesories) {
        this.entityAccesories = entityAccesories;
    }

    public List<EntityTiles> getEntityTiles() {
        return entityTiles;
    }

    public void setEntityTiles(List<EntityTiles> entityTiles) {
        this.entityTiles = entityTiles;
    }

    public boolean isHasTiles() {
        return hasTiles;
    }

    public void setHasTiles(boolean hasTiles) {
        this.hasTiles = hasTiles;
    }

    public boolean isHasAccesories() {
        return hasAccesories;
    }

    public void setHasAccesories(boolean hasAccesories) {
        this.hasAccesories = hasAccesories;
    }

    public boolean isHasWindows() {
        return hasWindows;
    }

    public void setHasWindows(boolean hasWindows) {
        this.hasWindows = hasWindows;
    }
}
