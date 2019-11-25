package pl.koszela.spring.entities.main;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Windows extends BaseEntity {

    private String manufacturer;
    private String size;
    private boolean offer;

    @ManyToMany(mappedBy = "userWindows")
    private Set<User> windowsUser = new HashSet<>();

    public Windows() {
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public boolean isOffer() {
        return offer;
    }

    public void setOffer(boolean offer) {
        this.offer = offer;
    }

    public Set<User> getWindowsUser() {
        return windowsUser;
    }

    public void setWindowsUser(Set<User> windowsUser) {
        this.windowsUser = windowsUser;
    }
}