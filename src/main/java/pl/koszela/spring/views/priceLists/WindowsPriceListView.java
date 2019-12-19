package pl.koszela.spring.views.priceLists;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.apache.catalina.Context;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import pl.koszela.spring.entities.main.Windows;
import pl.koszela.spring.repositories.WindowsRepository;
import pl.koszela.spring.service.PriceListInterface;
import pl.koszela.spring.views.MainView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Route(value = WindowsPriceListView.WINDOWS_PRICE, layout = MainView.class)
public class WindowsPriceListView extends VerticalLayout implements PriceListInterface<Windows> {

    public static final String WINDOWS_PRICE = "windowsPrice";

    private WindowsRepository windowsRepository;

    private Grid<Windows> grid = new Grid<>();
    private Binder<Windows> binder;
    private List<Windows> list = new ArrayList<>();

    @Autowired
    public WindowsPriceListView(WindowsRepository windowsRepository) {
        this.windowsRepository = Objects.requireNonNull(windowsRepository);

        list = allGuttersFromRepository();

        grid.addColumn(Windows::getManufacturer).setHeader("Nazwa Cennika");
        grid.setDataProvider(new ListDataProvider<>(list));
        add(createGrid(grid, binder, list, windowsRepository));
        add(saveToRepo(grid, new ArrayList<>(allGuttersFromRepository()), list, windowsRepository));
    }


    private List<Windows> allGuttersFromRepository() {
        return windowsRepository.findAll();
    }

}