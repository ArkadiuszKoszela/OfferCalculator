package pl.koszela.spring.entities;

import pl.koszela.spring.entities.BaseEntity;
import pl.koszela.spring.entities.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tiles")
public class Tiles extends BaseEntity {

    private BigDecimal totalPrice;
    private BigDecimal totalProfit;
//    private String date;
    @Column(columnDefinition = "TEXT")
    private String imageUrl = "";
    @ManyToMany(mappedBy = "userTiles")
    private Set<User> tilesUser = new HashSet<>();
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean main;
    @Column(name = "option_tiles", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean option;

    public Tiles() {
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public Set<User> getTilesUser() {
        return tilesUser;
    }

    public void setTilesUser(Set<User> tilesUser) {
        this.tilesUser = tilesUser;
    }

    public boolean isOption() {
        return option;
    }

    public void setOption(boolean option) {
        this.option = option;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}