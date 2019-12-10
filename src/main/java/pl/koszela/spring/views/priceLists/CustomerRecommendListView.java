package pl.koszela.spring.views.priceLists;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.main.CustomerRecommend;
import pl.koszela.spring.entities.main.UserMobileApp;
import pl.koszela.spring.repositories.CustomerRecommendRepository;
import pl.koszela.spring.repositories.UserMobileAppRepository;
import pl.koszela.spring.service.NotificationInterface;
import pl.koszela.spring.views.MainView;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@UIScope
@Route(value = CustomerRecommendListView.CUSTOMER_RECOMMEND, layout = MainView.class)
public class CustomerRecommendListView extends VerticalLayout {

    public static final String CUSTOMER_RECOMMEND = "customerRecommend";

    private Grid<CustomerRecommend> grid = new Grid<>();
    private Grid<UserMobileApp> userMobileAppGrid = new Grid<>();
    private List<UserMobileApp> userMobileAppList;

    private CustomerRecommendRepository customerRecommendRepository;
    private UserMobileAppRepository userMobileAppRepository;
    private Binder<CustomerRecommend> binder;
    private List<CustomerRecommend> all;
    private Dialog dialog = new Dialog();

    @Autowired
    public CustomerRecommendListView(CustomerRecommendRepository customerRecommendRepository, UserMobileAppRepository userMobileAppRepository) {
        this.customerRecommendRepository = Objects.requireNonNull(customerRecommendRepository);
        this.userMobileAppRepository = Objects.requireNonNull(userMobileAppRepository);

        all = all();
        userMobileAppList = allUsers();
        add(gridd());
        add(griddUser());
    }


    private Grid<CustomerRecommend> gridd() {
        binder = new Binder<>(CustomerRecommend.class);
        grid.addColumn(customerRecommend -> customerRecommend.getUserMobileApp().getName() + " " + customerRecommend.getUserMobileApp().getSurname()).setHeader("Właściciel");
        grid.addColumn(CustomerRecommend::getName).setHeader("Klient");
        grid.addColumn(CustomerRecommend::getPhone).setHeader("Telefon");
        grid.addComponentColumn(e -> {
            return new Button("Pokaż szczegóły", event -> {
                createDialog(e);
                dialog.open();
            });
        });
        grid.addComponentColumn(e -> {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            Select<String> select = new Select<>();
            select.setItems("Wysłany lead", "Lead sprawdzony i poprawny", "Negocjacje z klientem", "Umowa podpisana", "Umowa nie została podpisana");
            select.setValue(e.getStatus());
            Button button = new Button(VaadinIcon.CHECK.create(), event -> {
                e.setStatus(select.getValue());
                customerRecommendRepository.save(e);
                givePoints(e);
                NotificationInterface.notificationOpen("Status zaktualizowany", NotificationVariant.LUMO_SUCCESS);
            });
            horizontalLayout.add(select, button);
            return horizontalLayout;
        });


        grid.setDataProvider(new ListDataProvider<>(all));
        grid.getColumns().forEach(e -> e.setAutoWidth(true));
        grid.setMinHeight("450px");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        return grid;
    }

    private void givePoints(CustomerRecommend customerRecommend) {
        UserMobileApp userMobileApp = userMobileAppRepository.findById(customerRecommend.getUserMobileApp().getId()).orElse(new UserMobileApp());
        userMobileApp.setPoints(userMobileApp.getPoints() + 200);
        userMobileAppRepository.save(userMobileApp);
        for (UserMobileApp userMobileApp1 : userMobileAppList) {
            if (userMobileApp1.getId().equals(userMobileApp.getId())) {
                userMobileApp1.setPoints(userMobileApp1.getPoints() + 200);
            }
        }
        userMobileAppGrid.getDataProvider().refreshAll();
    }

    private Grid<UserMobileApp> griddUser() {
        userMobileAppGrid.addColumn(UserMobileApp::getName).setHeader("Imię");
        userMobileAppGrid.addColumn(UserMobileApp::getSurname).setHeader("Nazwisko");
        userMobileAppGrid.addColumn(UserMobileApp::getPhone).setHeader("Telefon");
        userMobileAppGrid.addColumn(UserMobileApp::getPoints).setHeader("Punkty");

        userMobileAppGrid.setDataProvider(new ListDataProvider<>(userMobileAppList));
        userMobileAppGrid.getColumns().forEach(e -> e.setAutoWidth(true));
        userMobileAppGrid.setMinHeight("450px");
        userMobileAppGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        return userMobileAppGrid;
    }

    private void createDialog(CustomerRecommend customerRecommend) {
        FormLayout formLayout = new FormLayout();
        FormLayout.ResponsiveStep responsiveStep = new FormLayout.ResponsiveStep("5px", 4);
        formLayout.setResponsiveSteps(responsiveStep);
        TextField tfName = new TextField("Imię", customerRecommend.getName(), "Imię");
        TextField tfPhone = new TextField("Nr telefonu", customerRecommend.getPhone(), "Nr telefonu");
        TextField tfOption = new TextField("Opcja wybrana", customerRecommend.getSelectOption(), "Opcja wybrana");
        Checkbox checkbox1 = new Checkbox("Opcja 1", customerRecommend.isChecked1());
        Checkbox checkbox2 = new Checkbox("Opcja 2", customerRecommend.isChecked2());
        Checkbox checkbox3 = new Checkbox("Opcja 3", customerRecommend.isChecked3());
        Checkbox checkbox4 = new Checkbox("Opcja 4", customerRecommend.isChecked4());
        NumberField numberField = new NumberField("Liczba");
        numberField.setValue(customerRecommend.getNumber().doubleValue());

        Button button = new Button("Zmień dane", event -> {
            for (CustomerRecommend customerRecommend2 : all) {
                if (customerRecommend2.getId().equals(customerRecommend.getId())) {
                    customerRecommend2.setName(tfName.getValue());
                    customerRecommend2.setPhone(tfPhone.getValue());
                    customerRecommend2.setNumber(numberField.getValue().intValue());
                    customerRecommend2.setChecked1(checkbox1.getValue());
                    customerRecommend2.setChecked2(checkbox2.getValue());
                    customerRecommend2.setChecked3(checkbox3.getValue());
                    customerRecommend2.setChecked4(checkbox4.getValue());
                    customerRecommend2.setSelectOption(tfOption.getValue());
                }
            }
            customerRecommendRepository.save(customerRecommend);

            grid.getDataProvider().refreshAll();
            dialog.removeAll();
            dialog.close();
            NotificationInterface.notificationOpen("Klient zaktualizowany", NotificationVariant.LUMO_SUCCESS);
        });
        formLayout.add(tfName, tfPhone, tfOption, numberField, checkbox1, checkbox2, checkbox3, checkbox4, button);
        dialog.add(formLayout);
    }

    private List<UserMobileApp> allUsers() {
        return userMobileAppRepository.findAll();
    }

    private List<CustomerRecommend> all() {
        return customerRecommendRepository.findAll();
    }
}