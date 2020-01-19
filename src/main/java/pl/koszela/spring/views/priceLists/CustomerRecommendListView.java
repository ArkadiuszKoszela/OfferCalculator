package pl.koszela.spring.views.priceLists;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import pl.koszela.spring.entities.main.CustomerRecommend;
import pl.koszela.spring.entities.main.UserMobileApp;
import pl.koszela.spring.repositories.CustomerRecommendRepository;
import pl.koszela.spring.repositories.UserMobileAppRepository;
import pl.koszela.spring.service.NotificationInterface;
import pl.koszela.spring.views.MainView;

import java.util.List;
import java.util.Objects;

@Route(value = CustomerRecommendListView.CUSTOMER_RECOMMEND, layout = MainView.class)
public class CustomerRecommendListView extends VerticalLayout {

    public static final String CUSTOMER_RECOMMEND = "customerRecommend";

    private Grid<CustomerRecommend> grid = new Grid<>();
    private Grid<UserMobileApp> userMobileAppGrid = new Grid<>();
    private List<UserMobileApp> userMobileAppList;

    private CustomerRecommendRepository customerRecommendRepository;
    private UserMobileAppRepository userMobileAppRepository;
    private List<CustomerRecommend> all;
    private Dialog dialog = new Dialog();
    private Button btnAddUserMobile = new Button("Dodaj użytkownika");
    @Autowired
    Environment environment;

    @Autowired
    public CustomerRecommendListView(CustomerRecommendRepository customerRecommendRepository, UserMobileAppRepository userMobileAppRepository) {
        this.customerRecommendRepository = Objects.requireNonNull(customerRecommendRepository);
        this.userMobileAppRepository = Objects.requireNonNull(userMobileAppRepository);

        all = all();
        userMobileAppList = allUsers();
        add(gridd());
        add(griddUser());
        dialogUser.addDialogCloseActionListener(event -> {
            dialogUser.removeAll();
            dialogUser.close();
        });
    }

    private Grid<CustomerRecommend> gridd() {
        grid.addColumn(customerRecommend -> customerRecommend.getUserMobileApp().getName() + " " + customerRecommend.getUserMobileApp().getSurname()).setHeader("Właściciel");
        grid.addColumn(CustomerRecommend::getName).setHeader("Klient");
        grid.addColumn(CustomerRecommend::getPhone).setHeader("Telefon");
        grid.addComponentColumn(e -> {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            Button removeBtn = new Button(VaadinIcon.TRASH.create(), event -> {
                Dialog dialog = new Dialog();
                dialog.open();
                dialog.add(new Label("Czy napewno chcesz usunąć klienta ?"));
                dialog.add(new Button(VaadinIcon.CHECK.create(), event1 -> {
                    customerRecommendRepository.delete(e);
                    ListDataProvider<CustomerRecommend> dataProvider = (ListDataProvider<CustomerRecommend>) grid.getDataProvider();
                    dataProvider.getItems().remove(e);
                    NotificationInterface.notificationOpen("Dane zostały zaktualizowane", NotificationVariant.LUMO_SUCCESS);
                    dialog.close();
                    dataProvider.refreshAll();
                }));
                dialog.add(new Button(VaadinIcon.CLOSE.create(), event1 -> dialog.close()));
            });
            Select<String> select = new Select<>();
            select.setItems("Wysłany lead", "Lead sprawdzony i poprawny", "Negocjacje z klientem", "Umowa podpisana", "Umowa nie została podpisana");
            select.setValue(e.getStatus());
            Button changeBtn = new Button(VaadinIcon.CHECK.create(), event -> {
                e.setStatus(select.getValue());
                customerRecommendRepository.save(e);
                givePoints(e);
                NotificationInterface.notificationOpen("Status zaktualizowany", NotificationVariant.LUMO_SUCCESS);
            });
            horizontalLayout.add(select, changeBtn, removeBtn);
            return horizontalLayout;
        }).setHeader("Status klienta");
        grid.addComponentColumn(customerRecommend -> {
            return new Button("Zdjęcia", event -> {
                String username = environment.getProperty("spring.ftp.username");
                String password = environment.getProperty("spring.ftp.password");
                String host = environment.getProperty("spring.ftp.host");
                String url = "ftp://" + username + ":" + password + "@" + host + "/";
                if (!customerRecommend.getUrlImage().isEmpty()) {
                    getUI().get().getPage().open(url + customerRecommend.getUrlImage());
                } else {
                    NotificationInterface.notificationOpen("Nie ma zdjęcia", NotificationVariant.LUMO_ERROR);
                }
            });
        }).setHeader("Zdjęcia");
        grid.addComponentColumn(e -> {
            return new Button("Pokaż szczegóły", event -> {
                dialog.removeAll();
                createDialog(e);
                dialog.open();
            });
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
        userMobileAppGrid.addColumn(UserMobileApp::getName).setHeader("Imię").setFooter(addUser());
        userMobileAppGrid.addColumn(UserMobileApp::getSurname).setHeader("Nazwisko");
        userMobileAppGrid.addColumn(UserMobileApp::getPhone).setHeader("Telefon");
        userMobileAppGrid.addComponentColumn(e -> {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            Button button = new Button(VaadinIcon.TRASH.create(), event -> {
                Dialog dialog = new Dialog();
                dialog.open();
                dialog.add(new Label("Czy napewno chcesz usunąć użytkownika ?"));
                dialog.add(new Button(VaadinIcon.CHECK.create(), event1 -> {
                    userMobileAppRepository.delete(e);
                    ListDataProvider<UserMobileApp> dataProvider = (ListDataProvider<UserMobileApp>) userMobileAppGrid.getDataProvider();
                    dataProvider.getItems().remove(e);
                    NotificationInterface.notificationOpen("Dane zostały zaktualizowane", NotificationVariant.LUMO_SUCCESS);
                    dialog.close();
                    dataProvider.refreshAll();
                }));
                dialog.add(new Button(VaadinIcon.CLOSE.create(), event1 -> dialog.close()));
            });
            horizontalLayout.add(button);
            return horizontalLayout;
        });
        userMobileAppGrid.addColumn(UserMobileApp::getPoints).setHeader("Punkty");
        userMobileAppGrid.setDataProvider(new ListDataProvider<>(userMobileAppList));
        userMobileAppGrid.getColumns().forEach(e -> e.setAutoWidth(true));
        userMobileAppGrid.setMinHeight("450px");
        userMobileAppGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        return userMobileAppGrid;
    }

    private Button addUser() {
        btnAddUserMobile.addClickListener(event -> {
            add(createDialogg());
            dialogUser.open();
        });
        return btnAddUserMobile;
    }

    private Dialog dialogUser = new Dialog();

    private Dialog createDialogg() {
        UserMobileApp userMobileApp = new UserMobileApp();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        VerticalLayout verticalLayout = new VerticalLayout();
        VerticalLayout verticalLayout1 = new VerticalLayout();
        VerticalLayout verticalLayout2 = new VerticalLayout();
        VerticalLayout verticalLayout3 = new VerticalLayout();
        VerticalLayout verticalLayout4 = createVertical(userMobileApp);
        HorizontalLayout horizontalLayout3 = new HorizontalLayout();

        FormLayout formLayout = createFormLayout(5);
        formLayout.add(horizontalLayout3);
        verticalLayout.add(formLayout);
        horizontalLayout.add(verticalLayout, verticalLayout1, verticalLayout2, verticalLayout3);
        dialogUser.add(verticalLayout4);
        dialogUser.add(horizontalLayout);
        dialogUser.add(new Button("Zapisz użytkownika", event -> {
            userMobileApp.setName(tfName.getValue());
            userMobileApp.setSurname(tfSurname.getValue());
            userMobileApp.setEmail(tfEmail.getValue());
            String mobilePattern = "[0-9]{9}";
            if(!tfPhone.getValue().matches(mobilePattern)){
                NotificationInterface.notificationOpen("Popraw numer telefonu. Musi mieć 9 cyfr", NotificationVariant.LUMO_ERROR);
                return;
            }
            userMobileApp.setPhone("+48" + tfPhone.getValue());
            userMobileApp.setUsername(tfLogin.getValue());
            userMobileApp.setPassword(tfPassword.getValue());
            userMobileApp.setPoints(0);
            userMobileAppRepository.save(userMobileApp);
            ListDataProvider<UserMobileApp> dataProvider = (ListDataProvider<UserMobileApp>) userMobileAppGrid.getDataProvider();
            dataProvider.getItems().add(userMobileApp);
            dialogUser.close();
            dataProvider.refreshAll();
            NotificationInterface.notificationOpen("Dane zostały zapisane", NotificationVariant.LUMO_SUCCESS);
        }));
        return dialogUser;
    }

    TextField tfName;
    TextField tfSurname;
    TextField tfEmail;
    TextField tfPhone;
    TextField tfLogin;
    TextField tfPassword;

    private VerticalLayout createVertical(UserMobileApp userMobileApp) {
        VerticalLayout verticalLayout = new VerticalLayout();
        FormLayout formLayout = createFormLayout(3);
        Binder<UserMobileApp> binder = new Binder<>();
        tfName = new TextField("Imię");
        tfSurname = new TextField("Nazwisko");
        tfEmail = new TextField("E-mail");
        tfPhone = new TextField("Telefon");
        binder.forField(tfPhone)
                .withValidator(new RegexpValidator("Numer musi zawierać 9 cyfr", "\\d{9}"))
                .bind(UserMobileApp::getPhone, UserMobileApp::setPhone);
        tfLogin = new TextField("Login");
        tfPassword = new TextField("Hasło");
        formLayout.add(tfName, tfSurname, tfEmail);
        FormLayout formLayout1 = createFormLayout(3);
        formLayout1.add(tfPhone, tfLogin, tfPassword);
        verticalLayout.add(formLayout, formLayout1);
        return verticalLayout;
    }


    private FormLayout createFormLayout(int columns) {
        FormLayout formLayout = new FormLayout();
        FormLayout.ResponsiveStep responsiveStep = new FormLayout.ResponsiveStep("5px", columns);
        formLayout.setResponsiveSteps(responsiveStep);
        return formLayout;
    }

    private void createDialog(CustomerRecommend customerRecommend) {
        FormLayout formLayout = new FormLayout();
        FormLayout.ResponsiveStep responsiveStep = new FormLayout.ResponsiveStep("5px", 4);
        formLayout.setResponsiveSteps(responsiveStep);
        TextField tfName = new TextField("Imię", customerRecommend.getName(), "Imię");
        TextField tfPhone = new TextField("Nr telefonu", customerRecommend.getPhone(), "Nr telefonu");
        TextField tfOption = new TextField("Opcja wybrana", customerRecommend.getSelectOption(), "Opcja wybrana");
        NumberField numberField = new NumberField("Liczba");
        TextField tfNote = new TextField("Notatka", customerRecommend.getNote(), "notatka");

        Button button = new Button("Zmień dane", event -> {
            for (CustomerRecommend customerRecommend2 : all) {
                if (customerRecommend2.getId().equals(customerRecommend.getId())) {
                    customerRecommend2.setName(tfName.getValue());
                    customerRecommend2.setPhone(tfPhone.getValue());
                    customerRecommend2.setSelectOption(tfOption.getValue());
                }
            }
            customerRecommendRepository.save(customerRecommend);

            grid.getDataProvider().refreshAll();
            dialog.removeAll();
            dialog.close();
            NotificationInterface.notificationOpen("Klient zaktualizowany", NotificationVariant.LUMO_SUCCESS);
        });
        formLayout.add(tfName, tfPhone, tfOption, numberField);
        dialog.add(formLayout);
        dialog.add(tfNote);
        dialog.add(button);
    }

    private List<UserMobileApp> allUsers() {
        return userMobileAppRepository.findAll();
    }

    private List<CustomerRecommend> all() {
        return customerRecommendRepository.findAll();
    }
}