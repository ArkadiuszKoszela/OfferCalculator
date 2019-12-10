package pl.koszela.spring.views.priceLists;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.main.Collar;
import pl.koszela.spring.repositories.CollarRepository;
import pl.koszela.spring.service.PriceListInterface;
import pl.koszela.spring.views.MainView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Route(value = CollarsPriceListView.COLLARS_PRICE, layout = MainView.class)
public class CollarsPriceListView extends VerticalLayout implements PriceListInterface<Collar> {

    public static final String COLLARS_PRICE = "collarsPrice";

    private CollarRepository collarRepository;

    private Grid<Collar> grid = new Grid<>();
    private Binder<Collar> binder;
    private List<Collar> list = new ArrayList<>();

    @Autowired
    public CollarsPriceListView(CollarRepository collarRepository) {
        this.collarRepository = Objects.requireNonNull(collarRepository);

        list = allCollarsFromRepository();

        add(createGrid(grid, binder, list, collarRepository));
        add(saveToRepo(grid, new ArrayList<>(allCollarsFromRepository()), list, collarRepository));
    }

    private List<Collar> allCollarsFromRepository() {
        return collarRepository.findAll();
    }

}