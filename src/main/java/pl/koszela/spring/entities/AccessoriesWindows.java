package pl.koszela.spring.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class AccessoriesWindows extends BaseEntity{

    private String size;
    private boolean offer;

    @ManyToMany(mappedBy = "userAccesoriesWindows")
    private Set<User> accessoriesWindowsUser = new HashSet<>();

    public AccessoriesWindows() {
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isOffer() {
        return offer;
    }

    public void setOffer(boolean offer) {
        this.offer = offer;
    }

    public Set<User> getAccessoriesWindowsUser() {
        return accessoriesWindowsUser;
    }

    public void setAccessoriesWindowsUser(Set<User> accessoriesWindowsUser) {
        this.accessoriesWindowsUser = accessoriesWindowsUser;
    }
}