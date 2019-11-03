package pl.koszela.spring.entities;

import pl.koszela.spring.entities.BaseEntity;
import pl.koszela.spring.entities.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gutter")
public class Gutter extends BaseEntity {

    private String category;
    private BigDecimal totalPrice;
    private BigDecimal totalProfit;
    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean main;
    @Column(name = "option_gutter", nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean option;
    @ManyToMany(mappedBy = "entityUserGutter")
    private List<User> userGutters = new ArrayList<>();

    public Gutter() {
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public boolean isOption() {
        return option;
    }

    public void setOption(boolean option) {
        this.option = option;
    }

    public List<User> getUserGutters() {
        return userGutters;
    }

    public void setUserGutters(List<User> userGutters) {
        this.userGutters = userGutters;
    }
}