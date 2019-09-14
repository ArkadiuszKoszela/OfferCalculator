package app.GUIs;

import app.controllers.ControllerVaadin;
import app.entities.EntityTiles;
import app.inputFields.ServiceTextFieldData;
import app.repositories.Tiles;
import app.service.Layout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static app.inputFields.ServiceSplitLayout.getSideMenuSettings;
import static app.inputFields.ServiceSplitLayout.ustawieniaStrony;

@Route(value = Cennik.CENNIK)
public class Cennik extends SplitLayout implements Layout {

    public static final String CENNIK = "Cennik";

    private Tiles tiles;
    private ControllerVaadin controllerVaadin;

    private Grid<EntityTiles> grid;

    public Cennik() {
    }

    @Autowired
    public Cennik(Tiles tiles, ServiceTextFieldData serviceTextFieldData,
                  ControllerVaadin controllerVaadin) {
        this.tiles = Objects.requireNonNull(tiles);
        this.controllerVaadin = Objects.requireNonNull(controllerVaadin);

        setOrientation(Orientation.VERTICAL);
        createGridd();

        addToPrimary(ustawieniaStrony(controllerVaadin));
        addToSecondary(getSideMenu(controllerVaadin));

    }
    @Override
    public SplitLayout getSideMenu(ControllerVaadin controllerVaadin) {
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.addToPrimary(controllerVaadin.sideMenuTiles());
        splitLayout.addToSecondary(grid);
        getSideMenuSettings(splitLayout);
        return splitLayout;
    }

    private void createGridd() {
        grid = new Grid<>(EntityTiles.class);
        grid.getColumnByKey("priceListName").setHeader("Nazwa Cennika");
        grid.getColumnByKey("type").setHeader("Typ dachówki");
        grid.getColumnByKey("name").setHeader("Kategoria");
        grid.getColumnByKey("unitRetailPrice").setHeader("Cena detaliczna").setWidth("30px");
        grid.getColumnByKey("profit").setHeader("Marża").setWidth("30px");
        grid.getColumnByKey("basicDiscount").setHeader("Rabat podstawowy").setWidth("30px");
        grid.getColumnByKey("supplierDiscount").setHeader("Promocja").setWidth("30px");
        grid.getColumnByKey("additionalDiscount").setHeader("Rabat dodatkowy").setWidth("30px");
        grid.getColumnByKey("skontoDiscount").setHeader("Skonto").setWidth("30px");
        grid.setColumns("priceListName", "type", "name", "unitRetailPrice", "profit", "basicDiscount",
                "supplierDiscount", "additionalDiscount", "skontoDiscount");
        grid.setItems(allDachowki(tiles));
    }

    private List<EntityTiles> allDachowki(Tiles tiles) {
        Iterable<EntityTiles> dachowkiIterable = tiles.findAll();
        List<EntityTiles> entityTilesList = new ArrayList<>();
        dachowkiIterable.forEach(entityTilesList::add);
        return entityTilesList;
    }


}
