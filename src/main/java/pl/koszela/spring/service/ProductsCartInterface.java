package pl.koszela.spring.service;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import pl.koszela.spring.entities.main.BaseEntity;

import java.util.List;

public interface ProductsCartInterface<T extends BaseEntity> {

    default FormLayout hm(List<T> list) {
        FormLayout formLayout = new FormLayout();
        FormLayout.ResponsiveStep responsiveStep = new FormLayout.ResponsiveStep("5px", 3);
        formLayout.setResponsiveSteps(responsiveStep);
        for (T v : list) {
            if (v.isOffer()) {
                TextField name = new TextField("nazwa");
                name.setValue(v.getName());
                TextField manufacturer = new TextField("producent");
                manufacturer.setValue(v.getManufacturer());
                TextField quantity = new TextField("ilość");
                quantity.setValue(v.getQuantity().toString());
                formLayout.add(name, manufacturer, quantity);
            }
        }
        return formLayout;
    }
}
