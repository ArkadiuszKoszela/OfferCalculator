package pl.koszela.spring.entities;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
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
}
