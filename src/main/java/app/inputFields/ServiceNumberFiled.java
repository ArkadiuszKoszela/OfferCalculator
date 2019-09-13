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

    public NumberField numberField1;
    public NumberField numberField2;
    public NumberField numberField3;
    public NumberField numberField4;
    public NumberField numberField5;
    public NumberField numberField6;
    public NumberField numberField7;
    public NumberField numberField8;
    public NumberField numberField9;
    public NumberField numberField10;
    public NumberField numberField11;
    public NumberField numberField12;
    public NumberField numberField13;
    public NumberField numberField14;
    public NumberField numberField15;
    public NumberField numberField16;
    public NumberField numberField17;
    public NumberField numberField18;
    public NumberField numberField19;

    private NumberField customerDiscount;

    private List<NumberField> listOfNumberFields;

    public void createNumberFields() {
        setNumberField1(new NumberField("Powierzchnia połaci: "));
        setValues(getNumberField1(), "m²", 300d);
        setNumberField2(new NumberField("Długość kalenic"));
        setValues(getNumberField2(), "mb", 65d);
        setNumberField3(new NumberField("Długość kalenic skośnych"));
        setValues(getNumberField3(), "mb", 65d);
        setNumberField4(new NumberField("Długość kalenic prostych"));
        setValues(getNumberField4(), "mb", 1d);
        setNumberField5(new NumberField("Długość koszy"));
        setValues(getNumberField5(), "mb", 8d);
        setNumberField6(new NumberField("Długość krawędzi lewych"));
        setValues(getNumberField6(), "mb", 5d);
        setNumberField7(new NumberField("Długość krawędzi prawych"));
        setValues(getNumberField7(), "mb", 5d);
        setNumberField8(new NumberField("Obwód komina"));
        setValues(getNumberField8(), "mb", 3d);
        setNumberField9(new NumberField("Długość okapu"));
        setValues(getNumberField9(), "mb", 38d);
        setNumberField10(new NumberField("Dachówka wentylacyjna"));
        setValues(getNumberField10(), "szt", 1d);
        setNumberField11(new NumberField("Komplet kominka wentylacyjnego"));
        setValues(getNumberField11(), "szt", 1d);
        setNumberField12(new NumberField("Gąsior początkowy kalenica prosta "));
        setValues(getNumberField12(), "szt", 1d);
        setNumberField13(new NumberField("Gąsior końcowy kalenica prosta"));
        setValues(getNumberField13(), "mb", 1d);
        setNumberField14(new NumberField("Gąsior zaokrąglony"));
        setValues(getNumberField14(), "mb", 6d);
        setNumberField15(new NumberField("Trójnik"));
        setValues(getNumberField15(), "szt", 1d);
        setNumberField16(new NumberField("Czwórnik"));
        setValues(getNumberField16(), "szt", 1d);
        setNumberField17(new NumberField("Gąsior z podwójną mufą"));
        setValues(getNumberField17(), "mb", 1d);
        setNumberField18(new NumberField("Dachówka dwufalowa"));
        setValues(getNumberField18(), "szt", 1d);
        setNumberField19(new NumberField("Okno połaciowe"));
        setValues(getNumberField19(), "szt", 1d);
        setTitle();
        listOfNumberFields = Arrays.asList(getNumberField1(), getNumberField2(), getNumberField3(), getNumberField4(), getNumberField5(), getNumberField6(), getNumberField7(),
                getNumberField8(), getNumberField9(), getNumberField10(), getNumberField11(), getNumberField12(), getNumberField13(), getNumberField14(), getNumberField15(), getNumberField16(),
                getNumberField17(), getNumberField18(), getNumberField19());
    }

    private void setTitle() {
        Iterable<EntityAccesories> iterable = accesories.findAll();
        List<EntityAccesories> list = new ArrayList<>();
        iterable.forEach(list::add);
        List<String> listaNazw = new ArrayList<>();
        list.forEach(e -> listaNazw.add(e.getName()));
        if (listaNazw.size() != 0) {
            String joined = Joiner.on(" ").join(getSubList(listaNazw, 0, 9));
            String joined1 = Joiner.on(" ").join(getSubList(listaNazw, 9, 14));
            String joined2 = Joiner.on(" ").join(getSubList(listaNazw, 35, 39));
            String joined3 = Joiner.on(" ").join(getSubList(listaNazw, 39, 44));
            String joined4 = Joiner.on(" ").join(getSubList(listaNazw, 59, 64));
            String joined5 = Joiner.on(" ").join(getSubList(listaNazw, 21, 23));
            String joined6 = Joiner.on(" ").join(getSubList(listaNazw, 24, 25));
            String joined7 = Joiner.on(" ").join(getSubList(listaNazw, 25, 27));
            String joined8 = Joiner.on(" ").join(getSubList(listaNazw, 44, 46));
            String joined9 = Joiner.on(" ").join(getSubList(listaNazw, 14, 20));
            String joined10 = Joiner.on(" ").join(getSubList(listaNazw, 20, 21));
            String joined11 = Joiner.on(" ").join(getSubList(listaNazw, 27, 31));
            String joined12 = Joiner.on(" ").join(getSubList(listaNazw, 31, 33));
            String joined13 = Joiner.on(" ").join(getSubList(listaNazw, 33, 35));
            getNumberField1().setTitle(joined3 + joined4);
            getNumberField2().setTitle(joined + joined1 + joined2);
            getNumberField5().setTitle(joined5 + joined6 + joined7 + joined8);
            getNumberField8().setTitle(joined9 + joined10);
            getNumberField9().setTitle(joined11 + joined12 + joined13);
        }
    }

    public NumberField createPoleRabat() {
        setCustomerDiscount(new NumberField("Podaj rabat dla klienta:"));
        getCustomerDiscount().setValue(0d);
        getCustomerDiscount().setMin(0);
        getCustomerDiscount().setMax(30);
        getCustomerDiscount().setHasControls(true);
        getCustomerDiscount().setSuffixComponent(new Span("%"));
        return getCustomerDiscount();
    }

    public NumberField getCustomerDiscount() {
        return customerDiscount;
    }

    private void setCustomerDiscount(NumberField customerDiscount) {
        this.customerDiscount = customerDiscount;
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
