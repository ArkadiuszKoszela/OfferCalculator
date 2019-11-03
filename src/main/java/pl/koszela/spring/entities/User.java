package pl.koszela.spring.entities;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private PersonalData personalData;
    @OneToOne
    private Windows windows;
    @OneToOne
    private Kolnierz kolnierz;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_accesories",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "accesories_id")
    )
    private Set<Accesories> userAccesories = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_tiles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tiles_id")
    )
    private Set<Tiles> userTiles = new HashSet<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany
    @JoinTable(name = "user_gutters",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "gutter_id")
    )
    private List<Gutter> entityUserGutter = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany
    @JoinTable(name = "user_input_data",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "input_data_id"))
    private List<InputData> inputData = new ArrayList<>();

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }

    public Windows getWindows() {
        return windows;
    }

    public void setWindows(Windows windows) {
        this.windows = windows;
    }

    public Kolnierz getKolnierz() {
        return kolnierz;
    }

    public void setKolnierz(Kolnierz kolnierz) {
        this.kolnierz = kolnierz;
    }

    public Set<Tiles> getTiles() {
        return userTiles;
    }

    public void setTiles(Set<Tiles> tiles) {
        this.userTiles = tiles;
    }

    public Set<Accesories> getUserAccesories() {
        return userAccesories;
    }

    public void setUserAccesories(Set<Accesories> userAccesories) {
        this.userAccesories = userAccesories;
    }

    public List<Gutter> getEntityUserGutter() {
        return entityUserGutter;
    }

    public void setEntityUserGutter(List<Gutter> entityUserGutter) {
        this.entityUserGutter = entityUserGutter;
    }

    public List<InputData> getInputData() {
        return inputData;
    }

    public void setInputData(List<InputData> inputData) {
        this.inputData = inputData;
    }
}