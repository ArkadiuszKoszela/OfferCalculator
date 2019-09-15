package app.importFiles;

import app.DAOs.DaoAccesories;
import app.DAOs.DaoTiles;
import app.views.EnterTiles;
import app.calculate.CalculateTiles;
import app.repositories.Accesories;
import app.repositories.Tiles;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static app.inputFields.ServiceNotification.getNotificationError;
import static app.inputFields.ServiceNotification.getNotificationSucces;

@Service
public class ImportFiles {

    private static final String BOGEN_INNOVO_10_CZERWONA_ANGOBA = "C:\\Users\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\Bogen Innovo 10 czerwona angoba.csv";
    private static final String BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA = "C:\\Users\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\Bogen Innovo 10 miedziano-brązowa angoba.csv";
    private static final String BOGEN_INNOVO_12_CZERWONA_ANGOBA = "C:\\Users\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\Bogen Innovo 12 czerwona angoba.csv";
    private static final String AKCESORIA = "C:\\Users\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\akcesoria.csv";

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

            daoTiles.save(BOGEN_INNOVO_10_CZERWONA_ANGOBA);
            daoTiles.save(BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA);
            daoTiles.save(BOGEN_INNOVO_12_CZERWONA_ANGOBA);
            daoAccesories.save(AKCESORIA);
            calculateTiles.getAvailablePriceList();
            getNotificationSucces("Zaimportowano cenniki");
            History history = UI.getCurrent().getPage().getHistory();
            history.getUI().navigate(EnterTiles.class);
        } else {
            getNotificationError("Cenniki są już zaimportowane");
        }

    }
}
