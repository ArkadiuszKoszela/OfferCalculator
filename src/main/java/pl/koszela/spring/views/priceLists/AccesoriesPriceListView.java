package pl.koszela.spring.views.priceLists;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.accesories.EntityAccesories;
import pl.koszela.spring.repositories.AccesoriesRepository;
import pl.koszela.spring.views.MainView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static pl.koszela.spring.service.ServiceNotification.getNotificationSucces;

@Route(value = AccesoriesPriceListView.ACCESORIES_PRICE_LIST, layout = MainView.class)
public class AccesoriesPriceListView extends VerticalLayout {

   public static final String ACCESORIES_PRICE_LIST = "accesoriesPriceList";

    private AccesoriesRepository accesoriesRepository;

    private VerticalLayout cennik = new VerticalLayout();

    private Grid<EntityAccesories> grid = new Grid<>();
    private List<EntityAccesories> allAccesoriesRepo = new ArrayList<>();

    @Autowired
    public AccesoriesPriceListView(AccesoriesRepository accesoriesRepository) {
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);

        add(createGrid());
        add(refresh());
        add(saveToRepo());
    }

    private List<EntityAccesories> allTilesFromRespository() {
        List<EntityAccesories> all = accesoriesRepository.findAll();
        all.forEach(e -> {
            if (e.getOption() == null) {
                e.setOption("BRAK");
            }
        });
        return all;
    }

    private VerticalLayout createGrid() {
        allAccesoriesRepo = allTilesFromRespository();
        ListDataProvider<EntityAccesories> dataProvider = new ListDataProvider<>(allAccesoriesRepo);
        grid.setDataProvider(dataProvider);

        Grid.Column<EntityAccesories> nameColumn = grid.addColumn(EntityAccesories::getName).setHeader("Nazwa");
        Grid.Column<EntityAccesories> purchaseColumn = grid.addColumn(EntityAccesories::getPurchasePrice).setHeader("Cena zakupu");
        Grid.Column<EntityAccesories> detalPriceColumn = grid.addColumn(EntityAccesories::getDetalPrice).setHeader("Cena detal");
        Grid.Column<EntityAccesories> marginColumn = grid.addColumn(EntityAccesories::getMargin).setHeader("Narzut");
        Grid.Column<EntityAccesories> optionColumn = grid.addColumn(EntityAccesories::getOption).setHeader("Opcja");
        Grid.Column<EntityAccesories> dateColumn = grid.addColumn(EntityAccesories::getDate).setHeader("Data modyfikacji");
        setPriceRetail();
        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        getBinder(purchaseColumn, detalPriceColumn, marginColumn, optionColumn);

        grid.setMinHeight("500px");
        grid.setMinWidth("1200px");
        cennik.add(grid);
        return cennik;
    }

    private void getBinder(Grid.Column<EntityAccesories> purchaseColumn, Grid.Column<EntityAccesories> detalPriceColumn
            , Grid.Column<EntityAccesories> marginColumn, Grid.Column<EntityAccesories> optionColumn) {
        Binder<EntityAccesories> binder = new Binder<>(EntityAccesories.class);
        grid.getEditor().setBinder(binder);

        TextField purchase = new TextField();
        TextField detal = new TextField();
        TextField margin = new TextField();
        eventListener(purchase);
        binder.forField(purchase)
                .withConverter(
                        new StringToDoubleConverter("Cena zakupu musi być liczbą"))
                .bind("purchasePrice");
        purchaseColumn.setEditorComponent(purchase);
        eventListener(detal);
        binder.forField(detal)
                .withConverter(
                        new StringToDoubleConverter("Cena detaliczna musi być liczbą"))
                .bind("detalPrice");
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
        for (EntityAccesories accesories : allAccesoriesRepo) {
            BigDecimal constance = new BigDecimal(100);
            BigDecimal margin = BigDecimal.valueOf(accesories.getMargin());
            BigDecimal pricePurchase = BigDecimal.valueOf(accesories.getPurchasePrice());
            BigDecimal priceDetal = pricePurchase.multiply(margin.divide(constance, 2, RoundingMode.HALF_UP)).add(pricePurchase).setScale(2, RoundingMode.HALF_UP);
            accesories.setDetalPrice(priceDetal.doubleValue());
        }
        grid.getDataProvider().refreshAll();
    }

    private Button saveToRepo() {
        Button save = new Button("Zapisz do bazy");
        save.addClickListener(event -> {
            List<EntityAccesories> all = accesoriesRepository.findAll();
            for (EntityAccesories old : all) {
                for (EntityAccesories accesories : allAccesoriesRepo) {
                    if (old.getName().equals(accesories.getName())) {
                        if (!old.getPurchasePrice().equals(accesories.getPurchasePrice())
                                || !old.getMargin().equals(accesories.getMargin()) || !old.getOption().equals(accesories.getOption())) {
                            LocalDateTime dateTime = LocalDateTime.now();
                            DateTimeFormatter myDateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                            accesories.setDate(dateTime.format(myDateFormat));
                        }
                    }
                }
            }
            accesoriesRepository.saveAll(new HashSet<>(allAccesoriesRepo));
            getNotificationSucces("Zmodyfikowano cenniki dn.    " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "         :)");
            grid.getDataProvider().refreshAll();
        });
        return save;
    }
}