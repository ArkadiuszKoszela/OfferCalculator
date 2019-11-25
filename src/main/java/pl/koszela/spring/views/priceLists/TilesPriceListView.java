package pl.koszela.spring.views.priceLists;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.main.CategoryOfTiles;
import pl.koszela.spring.entities.main.Tiles;
import pl.koszela.spring.repositories.main.TilesRepository;
import pl.koszela.spring.service.GridInteraface;
import pl.koszela.spring.service.NotificationInterface;
import pl.koszela.spring.views.MainView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Route(value = TilesPriceListView.TILES_PRICE_LIST, layout = MainView.class)
public class TilesPriceListView extends VerticalLayout implements GridInteraface<Tiles> {

    public static final String TILES_PRICE_LIST = "tilesPriceList";

    private TilesRepository tilesRepository;

    private TreeGrid<Tiles> grid = new TreeGrid<>();
    private List<Tiles> allTilesRepo = new ArrayList<>();
    private Binder<Tiles> binder;

    @Autowired
    public TilesPriceListView(TilesRepository tilesRepository) {
        this.tilesRepository = Objects.requireNonNull(tilesRepository);

        allTilesRepo = allTilesFromRepository();
        add(createGrid());
        add(saveToRepo());
    }

    private List<Tiles> allTilesFromRepository() {
        return tilesRepository.findAll();
    }

    public Grid<Tiles> createGrid() {
        TextField filter = new TextField();
        TreeDataProvider<Tiles> treeDataProvider = new TreeDataProvider<>(addItems(allTilesRepo));
        grid.setDataProvider(treeDataProvider);

        Grid.Column<Tiles> priceListNameColumn = grid.addHierarchyColumn(Tiles::getManufacturer).setHeader("Nazwa Cennika");
        Grid.Column<Tiles> nameColumn = grid.addColumn(Tiles::getName).setHeader("Nazwa");
        Grid.Column<Tiles> priceColumn = grid.addColumn(Tiles::getUnitDetalPrice).setHeader("Cena detal");
        Grid.Column<Tiles> basicDiscountColumn = grid.addColumn(Tiles::getBasicDiscount).setHeader("Podstawowy rabat");
        Grid.Column<Tiles> promotionDiscountColumn = grid.addColumn(Tiles::getPromotionDiscount).setHeader("Promocja");
        Grid.Column<Tiles> additionalDiscountColumn = grid.addColumn(Tiles::getAdditionalDiscount).setHeader("Dodatkowy rabat");
        Grid.Column<Tiles> skontoDiscountColumn = grid.addColumn(Tiles::getSkontoDiscount).setHeader("Skonto");
        Grid.Column<Tiles> priceFromRepoColumn = grid.addColumn(Tiles::getUnitPurchasePrice).setHeader("Cena zakupu(z ręki)");
        Grid.Column<Tiles> date = grid.addColumn(Tiles::getDate).setHeader("Data zmiany");
        Grid.Column<Tiles> imageUrlColumn = grid.addComponentColumn(i -> {
            if (i.getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.toString())) {
                Image image = new Image(i.getImageUrl(), "");
                image.setMaxWidth("110px");
                image.setMaxHeight("50px");
                return image;
            } else {
                return new Image("", "");
            }
        }).setHeader("Zdjęcie");

        binder = new Binder<>(Tiles.class);
        grid.getEditor().setBinder(binder);

        HeaderRow filterRow = grid.appendHeaderRow();
        filter.addValueChangeListener(event -> treeDataProvider.addFilter(
                tiles -> StringUtils.containsIgnoreCase(tiles.getManufacturer(), filter.getValue())
        ));

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(priceListNameColumn).setComponent(filter);
        filter.setSizeFull();
        filter.setPlaceholder("Filter");

        TextField basicDiscount = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), Tiles::getBasicDiscount, Tiles::setBasicDiscount);
        itemClickListener(grid, basicDiscount);
        basicDiscountColumn.setEditorComponent(basicDiscount);

        TextField promotionDiscount = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), Tiles::getPromotionDiscount, Tiles::setPromotionDiscount);
        itemClickListener(grid, promotionDiscount);
        promotionDiscountColumn.setEditorComponent(promotionDiscount);

        TextField additionalDiscount = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), Tiles::getAdditionalDiscount, Tiles::setAdditionalDiscount);
        itemClickListener(grid, additionalDiscount);
        additionalDiscountColumn.setEditorComponent(additionalDiscount);

        TextField skontoDiscount = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), Tiles::getSkontoDiscount, Tiles::setSkontoDiscount);
        itemClickListener(grid, skontoDiscount);
        skontoDiscountColumn.setEditorComponent(skontoDiscount);

        closeListener(grid, binder);
        settingsGrid(grid);
        return grid;
    }

    @Override
    public TreeData<Tiles> addItems(List list) {
        TreeData<Tiles> treeData = new TreeData<>();
        if (allTilesRepo != null) {
            Set<Tiles> parents = allTilesRepo.stream().filter(e -> e.getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.toString())).collect(Collectors.toSet());
            for (Tiles parent : parents) {
                List<Tiles> childrens = allTilesRepo.stream().filter(e -> e.getManufacturer().equals(parent.getManufacturer())).collect(Collectors.toList());
                for (int i = 0; i < childrens.size(); i++) {
                    if (i == 0) {
                        treeData.addItem(null, parent);
                    } else if (!childrens.get(i).getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.toString())) {
                        treeData.addItem(parent, childrens.get(i));
                    }
                }
            }
        }
        return treeData;
    }

    @Override
    public ComponentRenderer createComponent() {
        return null;
    }

    private Button saveToRepo() {
        return new Button("Zapisz do bazy", event -> {
            List<Tiles> fromRepo = tilesRepository.findAll();

            for (Tiles old : fromRepo) {
                for (Tiles tiles : allTilesRepo) {
                    if (old.getManufacturer().equals(tiles.getManufacturer()) && old.getName().equals(tiles.getName())) {
                        if (!old.getBasicDiscount().equals(tiles.getBasicDiscount()) || !old.getAdditionalDiscount().equals(tiles.getAdditionalDiscount())
                                || !old.getPromotionDiscount().equals(tiles.getPromotionDiscount()) || !old.getSkontoDiscount().equals(tiles.getSkontoDiscount())
                                || !old.getImageUrl().equals(tiles.getImageUrl())) {
                            LocalDateTime dateTime = LocalDateTime.now();
                            DateTimeFormatter myDateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                            tiles.setDate(dateTime.format(myDateFormat));
                        }
                    }
                }
            }

            tilesRepository.saveAll(new HashSet<>(allTilesRepo));
            NotificationInterface.notificationOpen("Zmodyfikowano cenniki dn.    " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), NotificationVariant.LUMO_SUCCESS);
            grid.getDataProvider().refreshAll();
        });
    }
}