package app.importFiles;

import app.DAOs.DaoAccesories;
import app.DAOs.DaoTiles;
import app.GUIs.LoadUser;
import app.calculate.CalculateTiles;
import app.repositories.Accesories;
import app.repositories.Tiles;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ImportFiles {

    private Tiles tiles;
    private Accesories accesories;

    private DaoTiles daoTiles;
    private DaoAccesories daoAccesories;
    private CalculateTiles calculateTiles;


    public ImportFiles() {
    }

    @Autowired
    public ImportFiles(Tiles tiles, Accesories accesories) {
        this.tiles = Objects.requireNonNull(tiles);
        this.accesories = Objects.requireNonNull(accesories);
    }

    @Autowired
    public void setDaoTiles(DaoTiles daoTiles) {
        this.daoTiles = Objects.requireNonNull(daoTiles);
    }

    @Autowired
    public void setCalculateTiles(CalculateTiles calculateTiles) {
        this.calculateTiles = Objects.requireNonNull(calculateTiles);
    }

    @Autowired
    public void setDaoAccesories(DaoAccesories daoAccesories) {
        this.daoAccesories = Objects.requireNonNull(daoAccesories);
        }

    public void csv() {
        if (this.tiles.count() == 0 && this.accesories.count() == 0) {

            daoTiles.save("C:\\Users\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\Bogen Innovo 10 czerwona angoba.csv");
            daoTiles.save("C:\\Users\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\Bogen Innovo 10 miedziano-brązowa angoba.csv");
            daoTiles.save("C:\\Users\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\Bogen Innovo 12 czerwona angoba.csv");
            daoAccesories.save("C:\\Users\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\akcesoria.csv");
            calculateTiles.getAvailablePriceList();
            Notification zaimportowano = new Notification("Zaimportowano cenniki", 3000);
            zaimportowano.setPosition(Notification.Position.TOP_CENTER);
            zaimportowano.open();
            History history = UI.getCurrent().getPage().getHistory();
            history.getUI().navigate(LoadUser.class);
        } else {
            Notification zaimportowano = new Notification("Cenniki są już zaimportowane", 3000);
            zaimportowano.setPosition(Notification.Position.TOP_CENTER);
            zaimportowano.open();
        }

    }
}
