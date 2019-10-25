package pl.koszela.spring.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import pl.koszela.spring.entities.Competition;
import pl.koszela.spring.entities.tiles.CategoryTiles;
import pl.koszela.spring.entities.tiles.Tiles;
import pl.koszela.spring.service.GridInteraface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.koszela.spring.service.ServiceNotification.getNotificationError;

@Route(value = PriceListOfSalesCompetition.PRICE_LIST_OF_SALES_COMPETITION, layout = MainView.class)
public class PriceListOfSalesCompetition extends VerticalLayout implements GridInteraface {

    public static final String PRICE_LIST_OF_SALES_COMPETITION = "competition";
    private TreeGrid<Competition> treeGrid = new TreeGrid<>();
    CategoryTiles[] categories = CategoryTiles.values();
    List<CategoryTiles> ca = Arrays.asList(categories);
    List<Competition> competitions = new ArrayList<>();
    private Binder<Competition> binder;

    public PriceListOfSalesCompetition() {
        for (int i = 0; i < ca.size(); i++) {
            competitions.add(new Competition(ca.get(i).toString(), 0d));
        }
        add(createGrid());
    }

    @Override
    public TreeGrid createGrid() {
        Grid.Column<Competition> nazwa = treeGrid.addHierarchyColumn(Competition::getName).setHeader("Nazwa");
        Grid.Column<Competition> cena = treeGrid.addColumn(Competition::getPrice).setHeader("Cena");

        binder = new Binder<>(Competition.class);
        treeGrid.getEditor().setBinder(binder);

        TextField priceField = editField(new StringToIntegerConverter("Błąd"), new StringToDoubleConverter("Błąd"));
        addEnterEvent(priceField);
        cena.setEditorComponent(priceField);

        itemClickListener(priceField);

        closeListener();
        treeGrid.setDataProvider(new TreeDataProvider<>(addItems(new ArrayList())));
        treeGrid.setMinHeight("750px");
        return treeGrid;
    }

    @Override
    public void addEnterEvent(TextField textField) {
        textField.getElement()
                .addEventListener("keydown",
                        event -> treeGrid.getEditor().cancel())
                .setFilter("event.key === 'Enter'");
    }

    @Override
    public void itemClickListener(TextField textField) {
        treeGrid.addItemDoubleClickListener(e -> {
            treeGrid.getEditor().editItem(e.getItem());
            textField.focus();
        });
    }

    @Override
    public void closeListener() {
        treeGrid.getEditor().addCloseListener(event -> {
            if (binder.getBean() != null) {
                Competition competition = binder.getBean();
                binder.setBean(competition);
            }
        });
    }

    @Override
    public TreeData<Competition> addItems(List list) {
        TreeData<Competition> treeData = new TreeData<>();
        if (competitions != null) {
            Set<Competition> parents = competitions.stream().filter(e -> e.getName().equals(CategoryTiles.DACHOWKA_PODSTAWOWA.toString())).collect(Collectors.toSet());
            for (Competition parent : parents) {
                List<Competition> childrens = competitions.stream().filter(e -> !e.getName().equals(parent.getName())).collect(Collectors.toList());
                for (int i = 0; i < childrens.size(); i++) {
                    if (i == 0) {
                        treeData.addItem(null, parent);
                    } else {
                        treeData.addItem(parent, childrens.get(i));
                    }
                }
            }
        }
        return treeData;
    }

    @Override
    public TextField editField(StringToIntegerConverter stringToIntegerConverter, StringToDoubleConverter stringToDoubleConverter) {
        TextField textField = new TextField();
        addEnterEvent(textField);
        binder.forField(textField)
                .withConverter(stringToDoubleConverter)
                .bind(Competition::getPrice, Competition::setPrice);
        return textField;
    }

    @Override
    public ComponentRenderer createCheckboxes() {
        return null;
    }
}
