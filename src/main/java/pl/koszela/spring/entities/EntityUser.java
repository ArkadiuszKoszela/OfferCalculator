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
    @OneToMany(fetch = FetchType.LAZY)
    private List<EntityAccesories> entityAccesories;

    @OneToMany
    private List<Tiles> tiles;

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

    public List<EntityAccesories> getEntityAccesories() {
        return entityAccesories;
    }

    public void setEntityAccesories(List<EntityAccesories> entityAccesories) {
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

    public List<Tiles> getTiles() {
        return tiles;
    }

    public void setTiles(List<Tiles> tiles) {
        this.tiles = tiles;
    }
}
