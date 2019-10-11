package pl.koszela.spring.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OptionOffer")
public class OptionsOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    /*@OneToMany(mappedBy = "optionsOffer",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Tiles> optionsOfferList = new ArrayList<>();*/

    public OptionsOffer() {
    }

    public OptionsOffer(String name) {
        this.name = name;
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

    /*public List<Tiles> getOptionsOfferList() {
        return optionsOfferList;
    }

    public void setOptionsOfferList(List<Tiles> optionsOfferList) {
        this.optionsOfferList = optionsOfferList;
    }
*/
    @Override
    public String toString() {
        return name;
    }
}