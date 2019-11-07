package pl.koszela.spring.entities;

public class FiresideDTO extends BaseEntity{

    private static int id;
    private String manufacturer;
    private String category;
    private boolean offer;

    public FiresideDTO() {
        id++;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isOffer() {
        return offer;
    }

    public void setOffer(boolean offer) {
        this.offer = offer;
    }
}
