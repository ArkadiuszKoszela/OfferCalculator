package pl.koszela.spring.entities.gutter;

import pl.koszela.spring.entities.EntityUser;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "gutter")
public class EntityGutter{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double quantity;
    private Integer discount;
    private Double unitPriceDetal;
    private Double unitPricePurchase;
    private Double allPriceDetal;
    private Double allPricePurchase;
    private Double profit;
    private String category;
    private BigDecimal totalPrice;
    private BigDecimal totalProfit;
    private boolean main;
    private boolean option;
    @ManyToMany(mappedBy = "entityUserGutter")
    private List<EntityUser> userGutters = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "input_id")
    private List<InputGutterData> inputGutter = new ArrayList<>();

    public EntityGutter() {
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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPriceDetal() {
        return unitPriceDetal;
    }

    public void setUnitPriceDetal(Double unitPriceDetal) {
        this.unitPriceDetal = unitPriceDetal;
    }

    public Double getUnitPricePurchase() {
        return unitPricePurchase;
    }

    public void setUnitPricePurchase(Double unitPricePurchase) {
        this.unitPricePurchase = unitPricePurchase;
    }

    public Double getAllPriceDetal() {
        return allPriceDetal;
    }

    public void setAllPriceDetal(Double allPriceDetal) {
        this.allPriceDetal = allPriceDetal;
    }

    public Double getAllPricePurchase() {
        return allPricePurchase;
    }

    public void setAllPricePurchase(Double allPricePurchase) {
        this.allPricePurchase = allPricePurchase;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
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