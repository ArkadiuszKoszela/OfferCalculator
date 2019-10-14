package pl.koszela.spring.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "OptionOffer")
public class OptionsOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String optionOffer;
    private String priceListName;
    private BigDecimal dicsountOffer;
    /*@OneToMany(mappedBy = "optionsOffer",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Tiles> optionsOfferList = new ArrayList<>();*/

    @ManyToMany(mappedBy = "entityUserOffer")
    private Set<EntityUser> userOptionOffer = new HashSet<>();

    public OptionsOffer() {
    }

    public OptionsOffer(String optionOffer) {
        this.optionOffer = optionOffer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionOffer() {
        return optionOffer;
    }

    public void setOptionOffer(String optionOffer) {
        this.optionOffer = optionOffer;
    }

    public String getPriceListName() {
        return priceListName;
    }

    public void setPriceListName(String priceListName) {
        this.priceListName = priceListName;
    }

    public BigDecimal getDicsountOffer() {
        return dicsountOffer;
    }

    public void setDicsountOffer(BigDecimal dicsountOffer) {
        this.dicsountOffer = dicsountOffer;
    }

    /*public List<Tiles> getOptionsOfferList() {
            return optionsOfferList;
        }

        public void setOptionsOfferList(List<Tiles> optionsOfferList) {
            this.optionsOfferList = optionsOfferList;
        }
    */
    @Override
    public String toString() {
        return optionOffer;
    }
}