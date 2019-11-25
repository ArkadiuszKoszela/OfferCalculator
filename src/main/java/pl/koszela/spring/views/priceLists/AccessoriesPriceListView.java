package pl.koszela.spring.views.priceLists;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.main.Accessories;
import pl.koszela.spring.repositories.main.AccesoriesRepository;
import pl.koszela.spring.service.PriceListInterface;
import pl.koszela.spring.views.MainView;

import java.util.*;

@Route(value = AccessoriesPriceListView.ACCESSORIES_PRICE, layout = MainView.class)
public class AccessoriesPriceListView extends VerticalLayout implements PriceListInterface<Accessories> {

    public static final String ACCESSORIES_PRICE = "accessoriesPrice";

    private AccesoriesRepository accesoriesRepository;

    private Grid<Accessories> grid = new Grid<>();
    private Binder<Accessories> binder;
    private List<Accessories> list = new ArrayList<>();

    @Autowired
    public AccessoriesPriceListView(AccesoriesRepository accesoriesRepository) {
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);

        list = allAccesoriesFromRepository();

        add(createGrid(grid, binder, list));
        add(saveToRepo(grid, new ArrayList<>(allAccesoriesFromRepository()), list, accesoriesRepository));
    }

    private List<Accessories> allAccesoriesFromRepository() {
        return accesoriesRepository.findAll();
    }

    @Override
    public TreeData addItems(List<Accessories> list) {
        return null;
    }

    @Override
    public ComponentRenderer createComponent() {
        return null;
    }
}