package app.inputFields;

import app.entities.EntityAccesories;
import app.repositories.Accesories;
import com.google.common.base.Joiner;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.NumberField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class ServiceNumberFiled {

    private Accesories accesories;

    @Autowired
    public ServiceNumberFiled(Accesories accesories) {
        this.accesories = Objects.requireNonNull(accesories);
    }

    public NumberField numberField1 = new NumberField("Powierzchnia połaci");
    public NumberField numberField2 = new NumberField("Długość kalenic");
    public NumberField numberField3 = new NumberField("Długość kalenic skośnych");
    public NumberField numberField4 = new NumberField("Długość kalenic prostych");
    public NumberField numberField5 = new NumberField("Długość koszy");
    public NumberField numberField6 = new NumberField("Długość krawędzi lewych");
    public NumberField numberField7 = new NumberField("Długość krawędzi prawych");
    public NumberField numberField8 = new NumberField("Obwód komina");
    public NumberField numberField9 = new NumberField("Długość okapu");
    public NumberField numberField10 = new NumberField("Dachówka wentylacyjna");
    public NumberField numberField11 = new NumberField("Komplet kominka wentylacyjnego");
    public NumberField numberField12 = new NumberField("Gąsior początkowy kalenica prosta");
    public NumberField numberField13 = new NumberField("Gąsior końcowy kalenica prosta");
    public NumberField numberField14 = new NumberField("Gąsior zaokrąglony");
    public NumberField numberField15 = new NumberField("Trójnik");
    public NumberField numberField16 = new NumberField("Czwórnik");
    public NumberField numberField17 = new NumberField("Gąsior z podwójną mufą");
    public NumberField numberField18 = new NumberField("Dachówka dwufalowa");
    public NumberField numberField19 = new NumberField("Okno połaciowe");

    private NumberField customerDiscount = new NumberField("Podaj rabat dla klienta:");

    private List<NumberField> listOfNumberFields;

    public void setValuesNumberFields() {
        setValues(getNumberField1(), "m²", 300d);
        setValues(getNumberField2(), "mb", 65d);
        setValues(getNumberField3(), "mb", 65d);
        setValues(getNumberField4(), "mb", 1d);
        setValues(getNumberField5(), "mb", 8d);
        setValues(getNumberField6(), "mb", 5d);
        setValues(getNumberField7(), "mb", 5d);
        setValues(getNumberField8(), "mb", 3d);
        setValues(getNumberField9(), "mb", 38d);
        setValues(getNumberField10(), "szt", 1d);
        setValues(getNumberField11(), "szt", 1d);
        setValues(getNumberField12(), "szt", 1d);
        setValues(getNumberField13(), "mb", 1d);
        setValues(getNumberField14(), "mb", 6d);
        setValues(getNumberField15(), "szt", 1d);
        setValues(getNumberField16(), "szt", 1d);
        setValues(getNumberField17(), "mb", 1d);
        setValues(getNumberField18(), "szt", 1d);
        setValues(getNumberField19(), "szt", 1d);
        setTitle();
        getListNumberFields();
    }

    private void getListNumberFields() {
        listOfNumberFields = Arrays.asList(getNumberField1(), getNumberField2(), getNumberField3(), getNumberField4(), getNumberField5(), getNumberField6(), getNumberField7(),
                getNumberField8(), getNumberField9(), getNumberField10(), getNumberField11(), getNumberField12(), getNumberField13(), getNumberField14(), getNumberField15(), getNumberField16(),
                getNumberField17(), getNumberField18(), getNumberField19());
    }

    private void setTitle() {
        Iterable<EntityAccesories> iterable = accesories.findAll();
        List<String> names = new ArrayList<>();
        iterable.forEach(e -> names.add(e.getName()));
        if (names.size() != 0) {
            getNumberField1().setTitle(getString(names, 39, 44)
                    .concat(getString(names, 59, 64)));

            getNumberField2().setTitle(getString(names, 0, 9)
                    .concat(getString(names, 9, 14))
                    .concat(getString(names, 35, 39)));

            getNumberField5().setTitle(getString(names, 21, 23)
                    .concat(getString(names, 24, 25))
                    .concat(getString(names, 25, 27))
                    .concat(getString(names, 44, 46)));

            getNumberField8().setTitle(getString(names, 14, 20)
                    .concat(getString(names, 20, 21)));

            getNumberField9().setTitle(getString(names, 27, 31)
                    .concat(getString(names, 31, 33))
                    .concat(getString(names, 33, 35)));
        }
    }

    private String getString(List<String> listaNazw, int i, int i2) {
        return Joiner.on(" ").join(getSubList(listaNazw, i, i2));
    }

    public NumberField setValuesCustomerDiscount() {
        getCustomerDiscount().setValue(0d);
        getCustomerDiscount().setMin(0);
        getCustomerDiscount().setMax(30);
        getCustomerDiscount().setHasControls(true);
        getCustomerDiscount().setSuffixComponent(new Span("%"));
        return getCustomerDiscount();
    }

    private List<String> getSubList(List<String> listaNazw, int poczatek, int koniec) {
        return listaNazw.subList(poczatek, koniec);
    }

    private void setValues(NumberField numberField, String jednostka, Double domyslnaWartosc) {
        numberField.setValue(domyslnaWartosc);
        numberField.setMin(0);
        numberField.setMax(500);
        numberField.setHasControls(true);
        numberField.setSuffixComponent(new Span(jednostka));
    }

    public List<NumberField> getListOfNumberFields() {
        return listOfNumberFields;
    }

    public void setListOfNumberFields(List<NumberField> listOfNumberFields) {
        this.listOfNumberFields = listOfNumberFields;
    }

    public NumberField getCustomerDiscount() {
        return customerDiscount;
    }

    private void setCustomerDiscount(NumberField customerDiscount) {
        this.customerDiscount = customerDiscount;
    }

    public NumberField getNumberField1() {
        return numberField1;
    }

    private void setNumberField1(NumberField numberField1) {
        this.numberField1 = numberField1;
    }

    public NumberField getNumberField2() {
        return numberField2;
    }

    private void setNumberField2(NumberField numberField2) {
        this.numberField2 = numberField2;
    }

    public NumberField getNumberField3() {
        return numberField3;
    }

    private void setNumberField3(NumberField numberField3) {
        this.numberField3 = numberField3;
    }

    public NumberField getNumberField4() {
        return numberField4;
    }

    private void setNumberField4(NumberField numberField4) {
        this.numberField4 = numberField4;
    }

    public NumberField getNumberField5() {
        return numberField5;
    }

    private void setNumberField5(NumberField numberField5) {
        this.numberField5 = numberField5;
    }

    public NumberField getNumberField6() {
        return numberField6;
    }

    private void setNumberField6(NumberField numberField6) {
        this.numberField6 = numberField6;
    }

    public NumberField getNumberField7() {
        return numberField7;
    }

    private void setNumberField7(NumberField numberField7) {
        this.numberField7 = numberField7;
    }

    public NumberField getNumberField8() {
        return numberField8;
    }

    private void setNumberField8(NumberField numberField8) {
        this.numberField8 = numberField8;
    }

    public NumberField getNumberField9() {
        return numberField9;
    }

    private void setNumberField9(NumberField numberField9) {
        this.numberField9 = numberField9;
    }

    public NumberField getNumberField10() {
        return numberField10;
    }

    private void setNumberField10(NumberField numberField10) {
        this.numberField10 = numberField10;
    }

    public NumberField getNumberField11() {
        return numberField11;
    }

    private void setNumberField11(NumberField numberField11) {
        this.numberField11 = numberField11;
    }

    public NumberField getNumberField12() {
        return numberField12;
    }

    private void setNumberField12(NumberField numberField12) {
        this.numberField12 = numberField12;
    }

    public NumberField getNumberField13() {
        return numberField13;
    }

    private void setNumberField13(NumberField numberField13) {
        this.numberField13 = numberField13;
    }

    public NumberField getNumberField14() {
        return numberField14;
    }

    private void setNumberField14(NumberField numberField14) {
        this.numberField14 = numberField14;
    }

    public NumberField getNumberField15() {
        return numberField15;
    }

    private void setNumberField15(NumberField numberField15) {
        this.numberField15 = numberField15;
    }

    public NumberField getNumberField16() {
        return numberField16;
    }

    private void setNumberField16(NumberField numberField16) {
        this.numberField16 = numberField16;
    }

    public NumberField getNumberField17() {
        return numberField17;
    }

    private void setNumberField17(NumberField numberField17) {
        this.numberField17 = numberField17;
    }

    public NumberField getNumberField18() {
        return numberField18;
    }

    private void setNumberField18(NumberField numberField18) {
        this.numberField18 = numberField18;
    }

    public NumberField getNumberField19() {
        return numberField19;
    }

    private void setNumberField19(NumberField numberField19) {
        this.numberField19 = numberField19;
    }
}
