package pl.koszela.spring.importFiles;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.DAOs.DaoAccesories;
import pl.koszela.spring.DAOs.DaoKolnierz;
import pl.koszela.spring.DAOs.DaoTiles;
import pl.koszela.spring.DAOs.DaoWindows;
import pl.koszela.spring.calculate.CalculateTiles;
import pl.koszela.spring.repositories.AccesoriesRepository;
import pl.koszela.spring.repositories.TilesRepository;
import pl.koszela.spring.views.TilesView;
import pl.koszela.spring.views.UsersView;

import java.util.Objects;

import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationError;
import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationSucces;

@Service
public class ImportFiles {

    private static final String BOGEN_INNOVO_10_CZERWONA_ANGOBA = "src/main/resources/assets/Bogen Innovo 10 czerwona angoba.csv";
    private static final String BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA = "src/main/resources/assets/Bogen Innovo 10 miedziano-brązowa angoba.csv";
    private static final String BOGEN_INNOVO_12_CZERWONA_ANGOBA = "src/main/resources/assets/Bogen Innovo 12 czerwona angoba.csv";
    private static final String AKCESORIA = "src/main/resources/assets/akcesoria.csv";
    private static final String OKNA_OKPOL_DAKEA = "src/main/resources/assets/OknaOkpolDakea.csv";
    private static final String KOLNIERZ_OKPOL_DAKEA = "src/main/resources/assets/KołnierzOkpolDakea.csv";

    private TilesRepository tilesRepository;
    private AccesoriesRepository accesoriesRepository;

    private DaoTiles daoTiles;
    private DaoAccesories daoAccesories;
    private DaoWindows daoWindows;
    private DaoKolnierz daoKolnierz;
    private CalculateTiles calculateTiles;


    public ImportFiles() {
    }

    @Autowired
    public ImportFiles(TilesRepository tilesRepository, AccesoriesRepository accesoriesRepository) {
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);
    }

    @Autowired
    public void setDaoKolnierz(DaoKolnierz daoKolnierz) {
        this.daoKolnierz = daoKolnierz;
    }

    @Autowired
    public void setDaoWindows(DaoWindows daoWindows) {
        this.daoWindows = daoWindows;
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
        if (this.tilesRepository.count() == 0 && this.accesoriesRepository.count() == 0) {

            daoTiles.save(BOGEN_INNOVO_10_CZERWONA_ANGOBA);
            daoTiles.save(BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA);
            daoTiles.save(BOGEN_INNOVO_12_CZERWONA_ANGOBA);
            daoAccesories.save(AKCESORIA);
            daoWindows.save(OKNA_OKPOL_DAKEA);
            daoKolnierz.save(KOLNIERZ_OKPOL_DAKEA);
            calculateTiles.getAvailablePriceList();
            getNotificationSucces("Zaimportowano cenniki");
            UI.getCurrent().getPage().reload();
            /*history.getUI().navigate(UsersView.class);*/
        } else {
            getNotificationError("Cenniki są już zaimportowane");
        }

    }
}
