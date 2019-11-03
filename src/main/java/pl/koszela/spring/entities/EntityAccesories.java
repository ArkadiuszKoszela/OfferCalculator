package pl.koszela.spring.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "accesories")
public class EntityAccesories extends BaseEntity {

    private String category;
    private Integer margin;
    @Column(name = "option_accesories")
    private String option;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean offer;
    private String dateChange;
    @ManyToMany(mappedBy = "userAccesories")
    private Set<User> accesoriesUser = new HashSet<>();

    public EntityAccesories() {
    }

    public Integer getMargin() {
        return margin;
    }

    public void setMargin(Integer margin) {
        this.margin = margin;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public boolean isOffer() {
        return offer;
    }

    public void setOffer(boolean offer) {
        this.offer = offer;
    }

    public Set<User> getAccesoriesUser() {
        return accesoriesUser;
    }

    public void setAccesoriesUser(Set<User> accesoriesUser) {
        this.accesoriesUser = accesoriesUser;
    }

    public String getDateChange() {
        return dateChange;
    }

    public void setDateChange(String dateChange) {
        this.dateChange = dateChange;
    }
}