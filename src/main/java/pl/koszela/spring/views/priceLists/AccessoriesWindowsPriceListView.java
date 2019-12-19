package pl.koszela.spring.views.priceLists;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.main.AccessoriesWindows;
import pl.koszela.spring.entities.main.Windows;
import pl.koszela.spring.repositories.AccesoriesWindowsRepository;
import pl.koszela.spring.service.PriceListInterface;
import pl.koszela.spring.views.MainView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Route(value = AccessoriesWindowsPriceListView.ACCESSORIES_WINDOWS_PRICE, layout = MainView.class)
public class AccessoriesWindowsPriceListView extends VerticalLayout implements PriceListInterface<AccessoriesWindows> {

    public static final String ACCESSORIES_WINDOWS_PRICE = "accessoriesWindowsPrice";

    private AccesoriesWindowsRepository accesoriesWindowsRepository;

    private Grid<AccessoriesWindows> grid = new Grid<>();
    private Binder<AccessoriesWindows> binder;
    private List<AccessoriesWindows> list = new ArrayList<>();

    @Autowired
    public AccessoriesWindowsPriceListView(AccesoriesWindowsRepository accesoriesWindowsRepository) {
        this.accesoriesWindowsRepository = Objects.requireNonNull(accesoriesWindowsRepository);

        list = allAccessoriesWindowsFromRepository();

        grid.addColumn(AccessoriesWindows::getManufacturer).setHeader("Nazwa Cennika");
        grid.setDataProvider(new ListDataProvider<>(list));
        add(createGrid(grid, binder, list, accesoriesWindowsRepository));
        add(saveToRepo(grid, new ArrayList<>(allAccessoriesWindowsFromRepository()), list, accesoriesWindowsRepository));
    }

    private List<AccessoriesWindows> allAccessoriesWindowsFromRepository() {
        return accesoriesWindowsRepository.findAll();
    }
}