package pl.koszela.spring.views;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.Category;
import pl.koszela.spring.entities.EntityInputDataTiles;
import pl.koszela.spring.entities.Tiles;
import pl.koszela.spring.repositories.TilesRepository;
import pl.koszela.spring.service.AvailablePriceList;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Route(value = PriceListView.PRICE_LIST, layout = MainView.class)
public class PriceListView extends VerticalLayout {

    static final String PRICE_LIST = "priceList";

    private TilesRepository tilesRepository;
    private AvailablePriceList availablePriceList;

    private VerticalLayout cennik = new VerticalLayout();

    private Grid<Tiles> grid = new Grid<>(Tiles.class);

    @Autowired
    public PriceListView(AvailablePriceList availablePriceList, TilesRepository tilesRepository) {
        this.availablePriceList = Objects.requireNonNull(availablePriceList);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);

        add(createGrid());
    }

    private List<Tiles> allTilesFromRespository() {
        Iterable<Tiles> allTilesFromRepository = tilesRepository.findAll();
        List<Tiles> allTiles = new ArrayList<>();
        allTilesFromRepository.forEach(allTiles::add);
        return allTiles;
    }

    private VerticalLayout createGrid() {
        TextField filter = new TextField();
        ListDataProvider<Tiles> dataProvider = new ListDataProvider<>(allTilesFromRespository());
        grid.setDataProvider(dataProvider);
        HeaderRow filterRow = grid.appendHeaderRow();
        filter.addValueChangeListener(event -> dataProvider.addFilter(
                tiles -> StringUtils.containsIgnoreCase(tiles.getPriceListName(), filter.getValue())
        ));
        Grid.Column<Tiles> priceListName = grid.getColumnByKey("priceListName").setHeader("Nazwa Cennika");
        grid.getColumnByKey("name").setHeader("Nazwa");
        grid.getColumnByKey("price").setHeader("Cena");
        grid.removeColumnByKey("id");
        grid.removeColumnByKey("userTiles");
        grid.removeColumnByKey("quantity");
        grid.removeColumnByKey("discount");
        grid.removeColumnByKey("priceAfterDiscount");
        grid.removeColumnByKey("pricePurchase");
        grid.removeColumnByKey("profit");
        grid.removeColumnByKey("totalPrice");
        grid.removeColumnByKey("totalProfit");
        grid.removeColumnByKey("option");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(priceListName).setComponent(filter);
        filter.setSizeFull();
        filter.setPlaceholder("Filter");

        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        cennik.add(grid);
        return cennik;
    }
}