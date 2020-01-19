package pl.koszela.spring.entities.main;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ak_accesories")
public class Accessories extends BaseEntity {

    @Column(name = "option_accesories")
    private String type;
//    @Column(nullable = false, columnDefinition = "TINYINT(1)")
//    private boolean offer;
    private String dateChange;
    @ManyToMany(mappedBy = "userAccesories")
    private Set<User> accesoriesUser = new HashSet<>();

    public Accessories() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public boolean isOffer() {
//        return offer;
//    }
//
//    public void setOffer(boolean offer) {
//        this.offer = offer;
//    }

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