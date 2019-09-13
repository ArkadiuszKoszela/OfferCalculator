package app.GUIs;

import app.controllers.ControllerVaadin;
import app.entities.EntityTiles;
import app.inputFields.ServiceTextFieldData;
import app.repositories.Tiles;
import app.repositories.UsersRepo;
import app.service.AllFields;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.crud.CrudI18n;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.ui.Tree;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Route(value = Cennik.CENNIK)
public class Cennik extends SplitLayout {

    public static final String CENNIK = "Cennik";

    private UsersRepo usersRepo;
    private Tiles tiles;
    private AllFields allFields;
    private ServiceTextFieldData serviceTextFieldData;
    private ControllerVaadin controllerVaadin;

    private Crud<EntityTiles> crud;

    private ListDataProvider<EntityTiles> listDataProvider;

    public Cennik() {
    }

    @Autowired
    public Cennik(Tiles tiles, UsersRepo users, AllFields allFields, ServiceTextFieldData serviceTextFieldData,
                  ControllerVaadin controllerVaadin) {
        this.tiles = Objects.requireNonNull(tiles);
        this.usersRepo = Objects.requireNonNull(users);
        this.allFields = Objects.requireNonNull(allFields);
        this.serviceTextFieldData = Objects.requireNonNull(serviceTextFieldData);
        this.controllerVaadin = Objects.requireNonNull(controllerVaadin);

        setOrientation(Orientation.VERTICAL);
        Tree tree = new Tree();


        addToPrimary(controllerVaadin.routerLink());

        crud = new Crud<>(EntityTiles.class, createGrid(), createCompanyEditor());

        listDataProvider = new ListDataProvider<>(allDachowki(tiles));

        crud.setDataProvider(listDataProvider);

        addToSecondary(crud);

        CrudI18n customI18n = CrudI18n.createDefault();
        customI18n.setEditItem("Edytowanie cennika");
        crud.setI18n(customI18n);

        saveListener(tiles);

        newListener(tiles);

        ustawieniaStrony();
    }

    void ustawieniaStrony() {
        Board board = new Board();
        board.addRow(controllerVaadin.routerLink());
        board.getStyle().set("background", "#DCDCDC");
        addToPrimary(board);
        setPrimaryStyle("minWidth", "1280px");
        setPrimaryStyle("maxWidth", "1280px");
        setPrimaryStyle("minHeight", "70px");
        setPrimaryStyle("maxHeight", "700px");
        setSecondaryStyle("minWidth", "1280px");
        setSecondaryStyle("maxWidth", "1280px");
        setSecondaryStyle("minHeight", "500px");
        setSecondaryStyle("maxHeight", "500px");
    }

    private List<EntityTiles> allDachowki(Tiles tiles) {
        Iterable<EntityTiles> dachowkiIterable = tiles.findAll();
        List<EntityTiles> entityTilesList = new ArrayList<>();
        dachowkiIterable.forEach(entityTilesList::add);
        return entityTilesList;
    }

    private void saveListener(Tiles tiles) {
        crud.addSaveListener(saveEvent -> {
            EntityTiles toSave = saveEvent.getItem();
            for (EntityTiles entityTiles : allDachowki(tiles)) {
                if (entityTiles.getId().equals(toSave.getId())) {
                    tiles.save(toSave);
                    Notification notification = new Notification("Zmieniono dane cennika", 4000);
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.open();
                }
            }
        });
    }

    private void newListener(Tiles tiles) {
        crud.addNewListener(newEvent -> {
            EntityTiles toNew = newEvent.getItem();
            if (!allDachowki(tiles).contains(toNew)) {
                tiles.save(toNew);
            }
        });
    }

    private Grid<EntityTiles> createGrid() {
        Grid<EntityTiles> grid = new Grid<>();
        Grid.Column<EntityTiles> priceListName = grid.addColumn(EntityTiles::getPriceListName).setHeader("Nazwa cennika");
        Grid.Column<EntityTiles> type = grid.addColumn(EntityTiles::getType).setHeader("Typ dachówki");
        Grid.Column<EntityTiles> name = grid.addColumn(EntityTiles::getName).setHeader("Kategoria");
        Grid.Column<EntityTiles> unitRetailPrice = grid.addColumn(EntityTiles::getUnitRetailPrice).setHeader("Cena detaliczna").setWidth("30px");
        Grid.Column<EntityTiles> profit = grid.addColumn(EntityTiles::getProfit).setHeader("Marża").setWidth("30px");
        Grid.Column<EntityTiles> basicDiscount = grid.addColumn(EntityTiles::getBasicDiscount).setHeader("Rabat podstawowy").setWidth("30px");
        Grid.Column<EntityTiles> supplierDiscount = grid.addColumn(EntityTiles::getSupplierDiscount).setHeader("Promocja").setWidth("30px");
        Grid.Column<EntityTiles> additionalDiscount = grid.addColumn(EntityTiles::getAdditionalDiscount).setHeader("Rabat dodatkowy").setWidth("30px");
        Grid.Column<EntityTiles> skontoDiscount = grid.addColumn(EntityTiles::getSkontoDiscount).setHeader("Skonto").setWidth("30px");

        createFilter(grid, type);
        Crud.addEditColumn(grid);
        return grid;
    }

    private void createFilter(Grid<EntityTiles> grid, Grid.Column<EntityTiles> type) {
        HeaderRow filterRow = grid.appendHeaderRow();
        TextField filter = new TextField();
        filter.addValueChangeListener(event -> listDataProvider.addFilter(
                tiles -> StringUtils.containsIgnoreCase(tiles.getType(),
                        filter.getValue())));

        filter.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(type).setComponent(filter);
        filter.setSizeFull();
        filter.setPlaceholder("Filter");
    }

    private CrudEditor<EntityTiles> createCompanyEditor() {
        serviceTextFieldData.createTextFieldsForGrid();

        Binder<EntityTiles> binder = new Binder<>(EntityTiles.class);
        binder.bind(serviceTextFieldData.getPriceListName(), EntityTiles::getPriceListName, EntityTiles::setPriceListName);
        binder.bind(serviceTextFieldData.getType(), EntityTiles::getType, EntityTiles::setType);
        binder.bind(serviceTextFieldData.getCategory(), EntityTiles::getName, EntityTiles::setName);
        binder.forField(serviceTextFieldData.getUnitRetailPrice()).withConverter(new DoubleToBigDecimalConverter()).bind(EntityTiles::getUnitRetailPrice, EntityTiles::setUnitRetailPrice);
        binder.forField(serviceTextFieldData.getProfit()).withConverter(new IntToStringConverter()).bind(EntityTiles::getProfit, EntityTiles::setProfit);
        binder.forField(serviceTextFieldData.getAdditionalDiscount()).withConverter(new IntToStringConverter()).bind(EntityTiles::getBasicDiscount, EntityTiles::setBasicDiscount);
        binder.forField(serviceTextFieldData.getSupplierDiscount()).withConverter(new IntToStringConverter()).bind(EntityTiles::getSupplierDiscount, EntityTiles::setSupplierDiscount);
        binder.forField(serviceTextFieldData.getAdditionalDiscount()).withConverter(new IntToStringConverter()).bind(EntityTiles::getAdditionalDiscount, EntityTiles::setAdditionalDiscount);
        binder.forField(serviceTextFieldData.getSkontoDiscount()).withConverter(new IntToStringConverter()).bind(EntityTiles::getSkontoDiscount, EntityTiles::setSkontoDiscount);
        return new BinderCrudEditor<>(binder, getTable());
    }

    private Board getTable() {
        Board table = new Board();
        table.addRow(serviceTextFieldData.getPriceListName(), serviceTextFieldData.getType(), serviceTextFieldData.getCategory());
        table.addRow(serviceTextFieldData.getUnitRetailPrice(), serviceTextFieldData.getProfit(), serviceTextFieldData.getBasicDiscount(), serviceTextFieldData.getSupplierDiscount());
        table.addRow(serviceTextFieldData.getAdditionalDiscount(), serviceTextFieldData.getSkontoDiscount());
        return table;
    }

    public class IntToStringConverter implements Converter<String, Integer> {

        @Override
        public Result<Integer> convertToModel(String value, ValueContext context) {
            return Result.ok(new Integer(value));
        }

        @Override
        public String convertToPresentation(Integer value, ValueContext context) {
            return String.valueOf(value);
        }
    }

    public class DoubleToBigDecimalConverter implements Converter<String, BigDecimal> {
        @Override
        public Result<BigDecimal> convertToModel(String value, ValueContext context) {
            return Result.ok(new BigDecimal(value));
        }

        @Override
        public String convertToPresentation(BigDecimal value, ValueContext context) {
            return String.valueOf(value);
        }
        /*@Override
        public Result<BigDecimal> convertToModel(Double presentation, ValueContext valueContext) {
            return Result.ok(BigDecimal.valueOf(presentation));
        }

        @Override
        public Double convertToPresentation(BigDecimal model, ValueContext valueContext) {
            return model.doubleValue();
        }*/
    }

}
