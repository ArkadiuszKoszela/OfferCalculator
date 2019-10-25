package pl.koszela.spring.entities;

import pl.koszela.spring.entities.accesories.EntityAccesories;
import pl.koszela.spring.entities.gutter.EntityGutter;
import pl.koszela.spring.entities.personalData.EntityPersonalData;
import pl.koszela.spring.entities.tiles.EntityInputDataTiles;
import pl.koszela.spring.entities.tiles.Tiles;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class EntityUser {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String priceListName;
    @OneToOne
    private EntityPersonalData entityPersonalData;
    @OneToOne
    private EntityInputDataTiles entityInputDataTiles;
    @OneToOne
    private EntityWindows entityWindows;
    @OneToOne
    private EntityKolnierz entityKolnierz;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_resultAccesories",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "entityResultAccesories_id")
    )
    private Set<EntityAccesories> resultAccesories;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_tiles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tiles_id")
    )
    private Set<Tiles> entityUserTiles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_gutters",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "gutter_id")
    )
    private List<EntityGutter> entityUserGutter = new ArrayList<>();

    public EntityUser() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntityPersonalData getEntityPersonalData() {
        return entityPersonalData;
    }

    public void setEntityPersonalData(EntityPersonalData entityPersonalData) {
        this.entityPersonalData = entityPersonalData;
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

    public EntityWindows getEntityWindows() {
        return entityWindows;
    }

    public void setEntityWindows(EntityWindows entityWindows) {
        this.entityWindows = entityWindows;
    }

    public EntityKolnierz getEntityKolnierz() {
        return entityKolnierz;
    }

    public void setEntityKolnierz(EntityKolnierz entityKolnierz) {
        this.entityKolnierz = entityKolnierz;
    }

    public Set<Tiles> getTiles() {
        return entityUserTiles;
    }

    public void setTiles(Set<Tiles> tiles) {
        this.entityUserTiles = tiles;
    }

    public Set<EntityAccesories> getResultAccesories() {
        return resultAccesories;
    }

    public void setResultAccesories(Set<EntityAccesories> resultAccesories) {
        this.resultAccesories = resultAccesories;
    }

    public List<EntityGutter> getEntityUserGutter() {
        return entityUserGutter;
    }

    public void setEntityUserGutter(List<EntityGutter> entityUserGutter) {
        this.entityUserGutter = entityUserGutter;
    }
}
