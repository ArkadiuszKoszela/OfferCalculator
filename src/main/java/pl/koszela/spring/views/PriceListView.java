package pl.koszela.spring.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.Tiles;
import pl.koszela.spring.repositories.TilesRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static pl.koszela.spring.service.ServiceNotification.getNotificationError;
import static pl.koszela.spring.service.ServiceNotification.getNotificationSucces;

@Route(value = PriceListView.PRICE_LIST, layout = MainView.class)
public class PriceListView extends VerticalLayout {

    static final String PRICE_LIST = "priceList";

    private TilesRepository tilesRepository;

    private VerticalLayout cennik = new VerticalLayout();

    private Grid<Tiles> grid = new Grid<>();
    private List<Tiles> allTilesRepo = new ArrayList<>();

    private TextField textField = new TextField();

    @Autowired
    public PriceListView(TilesRepository tilesRepository) {
        this.tilesRepository = Objects.requireNonNull(tilesRepository);

        add(createGrid());
        add(refresh());
        add(saveToRepo());
        add(textField);
    }

    private List<Tiles> allTilesFromRespository() {
        return tilesRepository.findAll();
    }

    private VerticalLayout createGrid() {
        TextField filter = new TextField();
        allTilesRepo = allTilesFromRespository();
        ListDataProvider<Tiles> dataProvider = new ListDataProvider<>(allTilesRepo);
        grid.setDataProvider(dataProvider);

        Grid.Column<Tiles> priceListNameColumn = grid.addColumn(Tiles::getPriceListName).setHeader("Nazwa Cennika");
        Grid.Column<Tiles> nameColumn = grid.addColumn(Tiles::getName).setHeader("Nazwa");
        Grid.Column<Tiles> priceColumn = grid.addColumn(Tiles::getPrice).setHeader("Cena detal");
        Grid.Column<Tiles> basicDiscountColumn = grid.addColumn(Tiles::getBasicDiscount).setHeader("Podstawowy rabat");
        Grid.Column<Tiles> promotionDiscountColumn = grid.addColumn(Tiles::getPromotionDiscount).setHeader("Promocja");
        Grid.Column<Tiles> additionalDiscountColumn = grid.addColumn(Tiles::getAdditionalDiscount).setHeader("Dodatkowy rabat");
        Grid.Column<Tiles> skontoDiscountColumn = grid.addColumn(Tiles::getSkontoDiscount).setHeader("Skonto");
        Grid.Column<Tiles> priceFromRepoColumn = grid.addColumn(Tiles::getPriceFromRepo).setHeader("Cena zakupu(z ręki)");
        Grid.Column<Tiles> date = grid.addColumn(Tiles::getDate).setHeader("Data zmiany");

        HeaderRow filterRow = grid.appendHeaderRow();
        filter.addValueChangeListener(event -> dataProvider.addFilter(
                tiles -> StringUtils.containsIgnoreCase(tiles.getPriceListName(), filter.getValue())
        ));

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(priceListNameColumn).setComponent(filter);
        filter.setSizeFull();
        filter.setPlaceholder("Filter");

        grid.getColumns().forEach(column -> column.setAutoWidth(true));

//        Editor(grid, basicDiscountColumn);
        editor2(basicDiscountColumn, promotionDiscountColumn, additionalDiscountColumn, skontoDiscountColumn);

        grid.setMinHeight("500px");
        grid.setMinWidth("1200px");
        cennik.add(grid);
        return cennik;
    }

    private void editor2(Grid.Column<Tiles> basicDiscountColumn, Grid.Column<Tiles> promotionDiscountColumn, Grid.Column<Tiles> additionalDiscountColumn,
                         Grid.Column<Tiles> skontoDiscountColumn) {
        Binder<Tiles> binder = new Binder<>(Tiles.class);
        grid.getEditor().setBinder(binder);

        TextField basicDiscount = new TextField();
        TextField promotionDiscount = new TextField();
        TextField additionalDiscount = new TextField();
        TextField skontoDiscount = new TextField();
        binder(basicDiscountColumn, binder, basicDiscount, "basicDiscount");
        binder(promotionDiscountColumn, binder, promotionDiscount, "promotionDiscount");
        binder(additionalDiscountColumn, binder, additionalDiscount, "additionalDiscount");
        binder(skontoDiscountColumn, binder, skontoDiscount, "skontoDiscount");

        grid.addItemDoubleClickListener(event -> {
            grid.getEditor().editItem(event.getItem());
            basicDiscount.focus();
            promotionDiscount.focus();
            additionalDiscount.focus();
            skontoDiscount.focus();
        });
    }

    private void binder(Grid.Column<Tiles> column, Binder<Tiles> binder, TextField ageField, String toBind) {
        ageField.getElement()
                .addEventListener("keydown",
                        event -> grid.getEditor().cancel())
                .setFilter("event.key === 'Enter'");
        binder.forField(ageField)
                .withConverter(
                        new StringToIntegerConverter("Age must be a number."))
                .bind(toBind);
        column.setEditorComponent(ageField);
    }

    private Button refresh() {
        Button refresh = new Button("Refresh");
        refresh.addClickListener(event -> {
            for (Tiles tiles : allTilesRepo) {
                BigDecimal constance = new BigDecimal(100);
                BigDecimal pricePurchase = tiles.getPrice();
                BigDecimal firstDiscount = (constance.subtract(new BigDecimal(tiles.getBasicDiscount()))).divide(constance, 2, RoundingMode.HALF_UP);
                BigDecimal secondDiscount = (constance.subtract(new BigDecimal(tiles.getPromotionDiscount()))).divide(constance, 2, RoundingMode.HALF_UP);
                BigDecimal thirdDiscount = (constance.subtract(new BigDecimal(tiles.getAdditionalDiscount()))).divide(constance, 2, RoundingMode.HALF_UP);
                BigDecimal fourthDiscount = (constance.subtract(new BigDecimal(tiles.getSkontoDiscount()))).divide(constance, 2, RoundingMode.HALF_UP);
                BigDecimal result = pricePurchase.multiply(firstDiscount).multiply(secondDiscount).multiply(thirdDiscount).multiply(fourthDiscount).setScale(2, RoundingMode.HALF_UP);
                tiles.setPriceFromRepo(result.doubleValue());
            }
            grid.getDataProvider().refreshAll();
        });
        return refresh;
    }

    private Button saveToRepo() {
        Button save = new Button("Zapisz do bazy");
        save.addClickListener(event -> {
            List<Tiles> fromRepo = tilesRepository.findAll();

            for (Tiles old : fromRepo) {
                for (Tiles tiles : allTilesRepo) {
                    if (old.getPriceListName().equals(tiles.getPriceListName()) && old.getName().equals(tiles.getName())) {
                        if (!old.getBasicDiscount().equals(tiles.getBasicDiscount()) || !old.getAdditionalDiscount().equals(tiles.getAdditionalDiscount())
                                || !old.getPromotionDiscount().equals(tiles.getPromotionDiscount()) || !old.getSkontoDiscount().equals(tiles.getSkontoDiscount())) {
                            LocalDateTime dateTime = LocalDateTime.now();
                            DateTimeFormatter myDateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                            tiles.setDate(dateTime.format(myDateFormat));
                        }
                    }
                }
            }

            tilesRepository.saveAll(new HashSet<>(allTilesRepo));
            getNotificationSucces("Zmodyfikowano cenniki dn.    " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "         :)");
            grid.getDataProvider().refreshAll();
//            grid.getColumns().forEach(e -> e.setAutoWidth(true));
        });
        return save;
    }
}