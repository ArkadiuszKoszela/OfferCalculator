package pl.koszela.spring.entities;

public class Competition extends BaseEntity {

    private String name;
    private Double price;

    public Competition() {
    }

    public Competition(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
