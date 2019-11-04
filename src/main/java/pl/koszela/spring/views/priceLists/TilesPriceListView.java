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
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.CategoryOfTiles;
import pl.koszela.spring.entities.Tiles;
import pl.koszela.spring.repositories.TilesRepository;
import pl.koszela.spring.service.NotificationInterface;
import pl.koszela.spring.views.MainView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Route(value = TilesPriceListView.TILES_PRICE_LIST, layout = MainView.class)
public class TilesPriceListView extends VerticalLayout {

    public static final String TILES_PRICE_LIST = "tilesPriceList";

    private TilesRepository tilesRepository;

    private VerticalLayout mainLayout = new VerticalLayout();

    private TreeGrid<Tiles> grid = new TreeGrid<>();
    private List<Tiles> allTilesRepo = new ArrayList<>();

    @Autowired
    public TilesPriceListView(TilesRepository tilesRepository) {
        this.tilesRepository = Objects.requireNonNull(tilesRepository);

        add(createGrid());
        add(refresh());
        add(saveToRepo());
    }

    private List<Tiles> allTilesFromRepository() {
        return tilesRepository.findAll();
    }

    private VerticalLayout createGrid() {
        TextField filter = new TextField();
        allTilesRepo = allTilesFromRepository();
        ListDataProvider<Tiles> dataProvider = new ListDataProvider<>(allTilesRepo);
        grid.setDataProvider(new TreeDataProvider<>(addItems()));

        Grid.Column<Tiles> priceListNameColumn = grid.addHierarchyColumn(Tiles::getPriceListName).setHeader("Nazwa Cennika");
        Grid.Column<Tiles> nameColumn = grid.addColumn(Tiles::getName).setHeader("Nazwa");
        Grid.Column<Tiles> priceColumn = grid.addColumn(Tiles::getUnitDetalPrice).setHeader("Cena detal");
        Grid.Column<Tiles> basicDiscountColumn = grid.addColumn(Tiles::getBasicDiscount).setHeader("Podstawowy rabat");
        Grid.Column<Tiles> promotionDiscountColumn = grid.addColumn(Tiles::getPromotionDiscount).setHeader("Promocja");
        Grid.Column<Tiles> additionalDiscountColumn = grid.addColumn(Tiles::getAdditionalDiscount).setHeader("Dodatkowy rabat");
        Grid.Column<Tiles> skontoDiscountColumn = grid.addColumn(Tiles::getSkontoDiscount).setHeader("Skonto");
        Grid.Column<Tiles> priceFromRepoColumn = grid.addColumn(Tiles::getUnitPurchasePrice).setHeader("Cena zakupu(z ręki)");
        Grid.Column<Tiles> date = grid.addColumn(Tiles::getDate).setHeader("Data zmiany");
        Grid.Column<Tiles> imageUrlColumn = grid.addComponentColumn(i -> {
            if (i.getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.name())) {
                Image image = new Image(i.getImageUrl(), "");
                image.setMaxWidth("110px");
                image.setMaxHeight("50px");
                return image;
            } else {
                return new Image("", "");
            }
        }).setHeader("Zdjęcie");

        HeaderRow filterRow = grid.appendHeaderRow();
        filter.addValueChangeListener(event -> dataProvider.addFilter(
                tiles -> StringUtils.containsIgnoreCase(tiles.getPriceListName(), filter.getValue())
        ));

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(priceListNameColumn).setComponent(filter);
        filter.setSizeFull();
        filter.setPlaceholder("Filter");

        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        getBinder(basicDiscountColumn, promotionDiscountColumn, additionalDiscountColumn, skontoDiscountColumn, imageUrlColumn);

        grid.setMinHeight("500px");
        grid.setMinWidth("1200px");
        mainLayout.add(grid);
        return mainLayout;
    }

    private void getBinder(Grid.Column<Tiles> basicDiscountColumn, Grid.Column<Tiles> promotionDiscountColumn, Grid.Column<Tiles> additionalDiscountColumn,
                           Grid.Column<Tiles> skontoDiscountColumn, Grid.Column<Tiles> imageUrlColumn) {
        Binder<Tiles> binder = new Binder<>(Tiles.class);
        grid.getEditor().setBinder(binder);

        TextField basicDiscount = new TextField();
        TextField promotionDiscount = new TextField();
        TextField additionalDiscount = new TextField();
        TextField skontoDiscount = new TextField();
        TextField imageUrl = new TextField();
        binder(basicDiscountColumn, binder, basicDiscount, "basicDiscount");
        binder(promotionDiscountColumn, binder, promotionDiscount, "promotionDiscount");
        binder(additionalDiscountColumn, binder, additionalDiscount, "additionalDiscount");
        binder(skontoDiscountColumn, binder, skontoDiscount, "skontoDiscount");

        imageUrl.getElement()
                .addEventListener("keydown",
                        event -> grid.getEditor().cancel())
                .setFilter("event.key === 'Enter'");
        binder.forField(imageUrl)
                .bind("imageUrl");
        imageUrlColumn.setEditorComponent(imageUrl);

        grid.addItemDoubleClickListener(event -> {
            grid.getEditor().editItem(event.getItem());
            basicDiscount.focus();
            promotionDiscount.focus();
            additionalDiscount.focus();
            skontoDiscount.focus();
            imageUrl.focus();
        });
    }

    private void binder(Grid.Column<Tiles> column, Binder<Tiles> binder, TextField ageField, String toBind) {
        ageField.getElement()
                .addEventListener("keydown",
                        event -> grid.getEditor().cancel())
                .setFilter("event.key === 'Enter'");
        binder.forField(ageField)
                .withConverter(new StringToIntegerConverter("Age must be a number."))
                .bind(toBind);
        column.setEditorComponent(ageField);
    }

    private Button refresh() {
        return new Button("Refresh", event -> {
            for (Tiles tiles : allTilesRepo) {
                BigDecimal constance = new BigDecimal(100);
                BigDecimal pricePurchase = BigDecimal.valueOf(tiles.getUnitDetalPrice());
                BigDecimal firstDiscount = (constance.subtract(new BigDecimal(tiles.getBasicDiscount()))).divide(constance, 2, RoundingMode.HALF_UP);
                BigDecimal secondDiscount = (constance.subtract(new BigDecimal(tiles.getPromotionDiscount()))).divide(constance, 2, RoundingMode.HALF_UP);
                BigDecimal thirdDiscount = (constance.subtract(new BigDecimal(tiles.getAdditionalDiscount()))).divide(constance, 2, RoundingMode.HALF_UP);
                BigDecimal fourthDiscount = (constance.subtract(new BigDecimal(tiles.getSkontoDiscount()))).divide(constance, 2, RoundingMode.HALF_UP);
                BigDecimal result = pricePurchase.multiply(firstDiscount).multiply(secondDiscount).multiply(thirdDiscount).multiply(fourthDiscount).setScale(2, RoundingMode.HALF_UP);
                tiles.setUnitPurchasePrice(result.doubleValue());
            }
            grid.getDataProvider().refreshAll();
        });
    }

    private Button saveToRepo() {
        return new Button("Zapisz do bazy", event -> {
            List<Tiles> fromRepo = tilesRepository.findAll();

            for (Tiles old : fromRepo) {
                for (Tiles tiles : allTilesRepo) {
                    if (old.getPriceListName().equals(tiles.getPriceListName()) && old.getName().equals(tiles.getName())) {
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

    private TreeData<Tiles> addItems() {
        TreeData<Tiles> treeData = new TreeData<>();
        if (allTilesRepo != null) {
            Set<Tiles> parents = allTilesRepo.stream().filter(e -> e.getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.toString())).collect(Collectors.toSet());
            for (Tiles parent : parents) {
                List<Tiles> childrens = allTilesRepo.stream().filter(e -> e.getPriceListName().equals(parent.getPriceListName())).collect(Collectors.toList());
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
}