package pl.koszela.spring.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import pl.koszela.spring.entities.Accessories;
import pl.koszela.spring.entities.InputData;
import pl.koszela.spring.repositories.AccesoriesRepository;
import pl.koszela.spring.service.GridInteraface;
import staticField.TitleNumberFields;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Route(value = LoginView.LOGIN)
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    public static final String LOGIN = "login";


    public LoginView() {
        LoginForm component = new LoginForm();
        component.addLoginListener(e -> {
            boolean isAuthenticated = true;
            if (isAuthenticated) {
                UI.getCurrent().navigate(IncludeDataView.class);
            } else {
                component.setError(true);
            }
        });
        add(component);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!LoginView.class.equals(event.getNavigationTarget())) { //
            event.rerouteTo(LoginView.class); //
        }
    }
}
