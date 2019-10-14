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
    @Column(name="USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String priceListName;
    private boolean hasTiles;
    private boolean hasAccesories;
    private boolean hasWindows;

    @OneToOne
    private EntityPersonalData entityPersonalData;
    @OneToOne
    private EntityInputDataTiles entityInputDataTiles;
    @OneToOne
    private EntityInputDataAccesories entityInputDataAccesories;
    @OneToOne
    private EntityWindows entityWindows;
    @OneToOne
    private EntityKolnierz entityKolnierz;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "entityUserAccesories")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<EntityAccesories> entityAccesories;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_tiles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tiles_id")
    )
    private Set<Tiles> entityUserTiles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_optionoffer",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "optionoffer_id")
    )
    private Set<OptionsOffer> entityUserOffer = new HashSet<>();

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

    public EntityKolnierz getEntityKolnierz() {
        return entityKolnierz;
    }

    public void setEntityKolnierz(EntityKolnierz entityKolnierz) {
        this.entityKolnierz = entityKolnierz;
    }

    public Set<EntityAccesories> getEntityAccesories() {
        return entityAccesories;
    }

    public void setEntityAccesories(Set<EntityAccesories> entityAccesories) {
        this.entityAccesories = entityAccesories;
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

    public Set<Tiles> getTiles() {
        return entityUserTiles;
    }

    public void setTiles(Set<Tiles> tiles) {
        this.entityUserTiles = tiles;
    }

    public Set<OptionsOffer> getEntityUserOffer() {
        return entityUserOffer;
    }

    public void setEntityUserOffer(Set<OptionsOffer> entityUserOffer) {
        this.entityUserOffer = entityUserOffer;
    }
}
