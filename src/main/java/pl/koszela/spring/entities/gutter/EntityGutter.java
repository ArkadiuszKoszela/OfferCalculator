package pl.koszela.spring.entities.gutter;

import pl.koszela.spring.entities.BaseEntity;
import pl.koszela.spring.entities.EntityUser;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "gutter")
public class EntityGutter extends BaseEntity {

    private String category;
    private BigDecimal totalPrice;
    private BigDecimal totalProfit;
    private boolean main;
    private boolean option;
    @ManyToMany(mappedBy = "entityUserGutter")
    private List<EntityUser> userGutters = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "INPUT_GUTTER",
            joinColumns = @JoinColumn(name = "OWNER_ID")
    )
    private List<InputGutterData> inputGutter = new ArrayList<>();

    public EntityGutter() {
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

    public List<EntityUser> getUserGutters() {
        return userGutters;
    }

    public void setUserGutters(List<EntityUser> userGutters) {
        this.userGutters = userGutters;
    }

    public List<InputGutterData> getInputGutter() {
        return inputGutter;
    }

    public void setInputGutter(List<InputGutterData> inputGutter) {
        this.inputGutter = inputGutter;
    }
}