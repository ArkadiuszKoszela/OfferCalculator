package pl.koszela.spring.views.priceLists;

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
import pl.koszela.spring.entities.CategoryOfTiles;
import pl.koszela.spring.service.GridInteraface;
import pl.koszela.spring.views.MainView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = PriceListOfSalesCompetition.PRICE_LIST_OF_SALES_COMPETITION, layout = MainView.class)
public class PriceListOfSalesCompetition extends VerticalLayout implements GridInteraface {

    public static final String PRICE_LIST_OF_SALES_COMPETITION = "competition";
    private TreeGrid<Competition> treeGrid = new TreeGrid<>();
    private List<Competition> competitions = new ArrayList<>();
    private Binder<Competition> binder;

    public PriceListOfSalesCompetition() {
        CategoryOfTiles[] categories = CategoryOfTiles.values();
        for (CategoryOfTiles category : categories) {
            competitions.add(new Competition(category.toString(), 0d));
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
        itemClickListener(treeGrid, priceField);
        cena.setEditorComponent(priceField);


//        closeListener(treeGrid, binder, binder.getBean());
        treeGrid.setDataProvider(new TreeDataProvider<>(addItems(new ArrayList())));
        treeGrid.setMinHeight("750px");
        return treeGrid;
    }

    @Override
    public TreeData<Competition> addItems(List list) {
        TreeData<Competition> treeData = new TreeData<>();
        if (competitions != null) {
            Set<Competition> parents = competitions.stream().filter(e -> e.getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.toString())).collect(Collectors.toSet());
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
        addEnterEvent(treeGrid, textField);
        binder.forField(textField)
                .withConverter(stringToDoubleConverter)
                .bind(Competition::getPrice, Competition::setPrice);
        return textField;
    }

    @Override
    public ComponentRenderer createComponent() {
        return null;
    }
}
