package app.inputFields;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.stereotype.Service;

@Service
public class ServiceTextFieldData {

    private TextField priceListName;
    private TextField type;
    private TextField category;
    private TextField unitRetailPrice;
    private TextField profit;
    private TextField basicDiscount;
    private TextField supplierDiscount;
    private TextField additionalDiscount;
    private TextField skontoDiscount;

    public void createTextFieldsForGrid() {
        setPriceListName(new TextField("priceListName"));
        getPriceListName().setRequiredIndicatorVisible(true);
        setColspan(getPriceListName(), 2);

        setType(new TextField("Typ dachówki"));
        setColspan(getType(), 2);
        setCategory(new TextField("category"));
        setColspan(getCategory(), 4);
        setUnitRetailPrice(new TextField("unitRetailPrice"));
        setColspan(getUnitRetailPrice(), 2);

        setProfit(new TextField("marża"));
        setColspan(getProfit(), 2);
        setBasicDiscount(new TextField("rabatPodstawy"));
        setColspan(getBasicDiscount(), 2);
        setSupplierDiscount(new TextField("supplierDiscount"));
        setColspan(getSupplierDiscount(), 2);
        setAdditionalDiscount(new TextField("rabatdodatkowy"));
        setColspan(getAdditionalDiscount(), 2);
        setSkontoDiscount(new TextField("skontoDiscount"));
        setColspan(getSkontoDiscount(), 2);
    }

    private void setColspan(Component component, int colspan) {
        component.getElement().setAttribute("colspan", Integer.toString(colspan));
    }


    public TextField getPriceListName() {
        return priceListName;
    }

    public void setPriceListName(TextField priceListName) {
        this.priceListName = priceListName;
    }

    public TextField getType() {
        return type;
    }

    public void setType(TextField type) {
        this.type = type;
    }

    public TextField getCategory() {
        return category;
    }

    public void setCategory(TextField category) {
        this.category = category;
    }

    public TextField getUnitRetailPrice() {
        return unitRetailPrice;
    }

    public void setUnitRetailPrice(TextField unitRetailPrice) {
        this.unitRetailPrice = unitRetailPrice;
    }

    public TextField getProfit() {
        return profit;
    }

    public void setProfit(TextField profit) {
        this.profit = profit;
    }

    public TextField getBasicDiscount() {
        return basicDiscount;
    }

    public void setBasicDiscount(TextField basicDiscount) {
        this.basicDiscount = basicDiscount;
    }

    public TextField getSupplierDiscount() {
        return supplierDiscount;
    }

    public void setSupplierDiscount(TextField supplierDiscount) {
        this.supplierDiscount = supplierDiscount;
    }

    public TextField getAdditionalDiscount() {
        return additionalDiscount;
    }

    public void setAdditionalDiscount(TextField additionalDiscount) {
        this.additionalDiscount = additionalDiscount;
    }

    public TextField getSkontoDiscount() {
        return skontoDiscount;
    }

    public void setSkontoDiscount(TextField skontoDiscount) {
        this.skontoDiscount = skontoDiscount;
    }
}
