package pl.koszela.spring.entities.main;

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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_windows",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "windows_id")
    )
    private Set<Windows> userWindows = new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_collars",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "collars_id")
    )
    private Set<Collar> userCollars;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_accesories",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "accesories_id")
    )
    private Set<Accessories> userAccesories = new HashSet<>();

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_accessories_windows",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "accessories_windows_id")
    )
    private Set<AccessoriesWindows> userAccesoriesWindows = new HashSet<>();

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

    public Set<Windows> getUserWindows() {
        return userWindows;
    }

    public void setUserWindows(Set<Windows> userWindows) {
        this.userWindows = userWindows;
    }

    public Set<Collar> getUserCollars() {
        return userCollars;
    }

    public void setUserCollars(Set<Collar> userCollars) {
        this.userCollars = userCollars;
    }

    public Set<Tiles> getTiles() {
        return userTiles;
    }

    public void setTiles(Set<Tiles> tiles) {
        this.userTiles = tiles;
    }

    public Set<Accessories> getUserAccesories() {
        return userAccesories;
    }

    public void setUserAccesories(Set<Accessories> userAccesories) {
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

    public Set<AccessoriesWindows> getUserAccesoriesWindows() {
        return userAccesoriesWindows;
    }

    public void setUserAccesoriesWindows(Set<AccessoriesWindows> userAccesoriesWindows) {
        this.userAccesoriesWindows = userAccesoriesWindows;
    }
}