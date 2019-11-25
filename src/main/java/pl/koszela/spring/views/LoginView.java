package pl.koszela.spring.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

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
