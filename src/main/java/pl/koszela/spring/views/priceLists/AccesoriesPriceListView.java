package pl.koszela.spring.views.priceLists;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.Accesories;
import pl.koszela.spring.repositories.AccesoriesRepository;
import pl.koszela.spring.service.NotificationInterface;
import pl.koszela.spring.views.MainView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Route(value = AccesoriesPriceListView.ACCESORIES_PRICE_LIST, layout = MainView.class)
public class AccesoriesPriceListView extends VerticalLayout {

   public static final String ACCESORIES_PRICE_LIST = "accesoriesPriceList";

    private AccesoriesRepository accesoriesRepository;

    private VerticalLayout cennik = new VerticalLayout();

    private Grid<Accesories> grid = new Grid<>();
    private List<Accesories> allAccesoriesRepo = new ArrayList<>();

    @Autowired
    public AccesoriesPriceListView(AccesoriesRepository accesoriesRepository) {
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);

        add(createGrid());
        add(refresh());
        add(saveToRepo());
    }

    private List<Accesories> allTilesFromRespository() {
        List<Accesories> all = accesoriesRepository.findAll();
        all.forEach(e -> {
            if (e.getOption() == null) {
                e.setOption("BRAK");
            }
        });
        return all;
    }

    private VerticalLayout createGrid() {
        TextField filter = new TextField();
        allAccesoriesRepo = allTilesFromRespository();
        ListDataProvider<Accesories> dataProvider = new ListDataProvider<>(allAccesoriesRepo);
        grid.setDataProvider(dataProvider);

        Grid.Column<Accesories> nameColumn = grid.addColumn(Accesories::getName).setHeader("Nazwa");
        Grid.Column<Accesories> purchaseColumn = grid.addColumn(Accesories::getUnitPurchasePrice).setHeader("Cena zakupu");
        Grid.Column<Accesories> detalPriceColumn = grid.addColumn(Accesories::getUnitDetalPrice).setHeader("Cena detal");
        Grid.Column<Accesories> marginColumn = grid.addColumn(Accesories::getMargin).setHeader("Narzut");
        Grid.Column<Accesories> optionColumn = grid.addColumn(Accesories::getOption).setHeader("Opcja");
        Grid.Column<Accesories> dateColumn = grid.addColumn(Accesories::getDateChange).setHeader("Data modyfikacji");
        setPriceRetail();

        HeaderRow filterRow = grid.appendHeaderRow();
        filter.addValueChangeListener(event -> dataProvider.addFilter(
                accesories -> StringUtils.containsIgnoreCase(accesories.getName(), filter.getValue())
        ));

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(nameColumn).setComponent(filter);
        filter.setSizeFull();
        filter.setPlaceholder("Filter");

        getBinder(purchaseColumn, detalPriceColumn, marginColumn, optionColumn);

        grid.setMinHeight("500px");
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        cennik.add(grid);
        return cennik;
    }

    private void getBinder(Grid.Column<Accesories> purchaseColumn, Grid.Column<Accesories> detalPriceColumn
            , Grid.Column<Accesories> marginColumn, Grid.Column<Accesories> optionColumn) {
        Binder<Accesories> binder = new Binder<>(Accesories.class);
        grid.getEditor().setBinder(binder);

        TextField purchase = new TextField();
        TextField detal = new TextField();
        TextField margin = new TextField();
        eventListener(purchase);
        binder.forField(purchase)
                .withConverter(
                        new StringToDoubleConverter("Cena zakupu musi być liczbą"))
                .bind("unitPurchasePrice");
        purchaseColumn.setEditorComponent(purchase);
        eventListener(detal);
        binder.forField(detal)
                .withConverter(
                        new StringToDoubleConverter("Cena detaliczna musi być liczbą"))
                .bind("unitDetalPrice");
        detalPriceColumn.setEditorComponent(detal);
        eventListener(margin);
        binder.forField(margin)
                .withConverter(
                        new StringToIntegerConverter("Narzut musi być liczbą"))
                .bind("margin");
        marginColumn.setEditorComponent(margin);
        Select<String> select = new Select<>("PODSTAWOWY", "PREMIUM", "LUX", "BRAK");
        binder.bind(select, "option");
        optionColumn.setEditorComponent(select);

        grid.addItemDoubleClickListener(event -> {
            grid.getEditor().editItem(event.getItem());
            purchase.focus();
            detal.focus();
            margin.focus();
        });
    }

    private void eventListener(TextField textField) {
        textField.getElement()
                .addEventListener("keydown",
                        event -> grid.getEditor().cancel())
                .setFilter("event.key === 'Enter'");
    }

    private Button refresh() {
        Button refresh = new Button("Refresh");
        refresh.addClickListener(event -> {
            setPriceRetail();
        });
        return refresh;
    }

    private void setPriceRetail() {
        for (Accesories accesories : allAccesoriesRepo) {
            BigDecimal constance = new BigDecimal(100);
            BigDecimal margin = BigDecimal.valueOf(accesories.getMargin());
            BigDecimal pricePurchase = BigDecimal.valueOf(accesories.getUnitPurchasePrice());
            BigDecimal priceDetal = pricePurchase.multiply(margin.divide(constance, 2, RoundingMode.HALF_UP)).add(pricePurchase).setScale(2, RoundingMode.HALF_UP);
            accesories.setUnitDetalPrice(priceDetal.doubleValue());
        }
        grid.getDataProvider().refreshAll();
    }

    private Button saveToRepo() {
        Button save = new Button("Zapisz do bazy");
        save.addClickListener(event -> {
            List<Accesories> all = accesoriesRepository.findAll();
            for (Accesories old : all) {
                for (Accesories accesories : allAccesoriesRepo) {
                    if (old.getName().equals(accesories.getName())) {
                        if (!old.getUnitPurchasePrice().equals(accesories.getUnitPurchasePrice())
                                || !old.getMargin().equals(accesories.getMargin()) || !old.getOption().equals(accesories.getOption())) {
                            LocalDateTime dateTime = LocalDateTime.now();
                            DateTimeFormatter myDateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                            accesories.setDateChange(dateTime.format(myDateFormat));
                        }
                    }
                }
            }
            accesoriesRepository.saveAll(new HashSet<>(allAccesoriesRepo));
            NotificationInterface.notificationOpen("Zmodyfikowano cenniki dn.    " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), NotificationVariant.LUMO_SUCCESS);
            grid.getDataProvider().refreshAll();
        });
        return save;
    }
}