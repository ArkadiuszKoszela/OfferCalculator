package pl.koszela.spring.views.priceLists;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.main.Gutter;
import pl.koszela.spring.repositories.main.GutterRepository;
import pl.koszela.spring.service.PriceListInterface;
import pl.koszela.spring.views.MainView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Route(value = GutterPriceListView.GUTTERS_PRICE, layout = MainView.class)
public class GutterPriceListView extends VerticalLayout implements PriceListInterface<Gutter> {

    public static final String GUTTERS_PRICE = "guttersPrice";

    private GutterRepository gutterRepository;

    private Grid<Gutter> grid = new Grid<>();
    private Binder<Gutter> binder;
    private List<Gutter> list = new ArrayList<>();

    @Autowired
    public GutterPriceListView(GutterRepository gutterRepository) {
        this.gutterRepository = Objects.requireNonNull(gutterRepository);

        list = allGuttersFromRepository();

        add(createGrid(grid, binder, list));
        add(saveToRepo(grid, new ArrayList<>(allGuttersFromRepository()), list, gutterRepository));
    }

    private List<Gutter> allGuttersFromRepository() {
        return gutterRepository.findAll();
    }

    @Override
    public TreeData addItems(List<Gutter> list) {
        return null;
    }

    @Override
    public ComponentRenderer createComponent() {
        return null;
    }
}