package pl.koszela.spring.importFiles;

import com.vaadin.flow.component.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.DAOs.DaoAccesories;
import pl.koszela.spring.DAOs.DaoKolnierz;
import pl.koszela.spring.DAOs.DaoTiles;
import pl.koszela.spring.DAOs.DaoWindows;
import pl.koszela.spring.calculate.CalculateTiles;
import pl.koszela.spring.repositories.AccesoriesRepository;
import pl.koszela.spring.repositories.KolnierzRepository;
import pl.koszela.spring.repositories.TilesRepository;
import pl.koszela.spring.repositories.WindowsRepository;

import java.util.Objects;

import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationSucces;

@Service
public class ImportFiles {

    private static final String BOGEN_INNOVO_10_CZERWONA_ANGOBA = "src/main/resources/assets/Bogen Innovo 10 czerwona angoba.csv";
    private static final String BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA = "src/main/resources/assets/Bogen Innovo 10 miedziano-brązowa angoba.csv";
    private static final String BOGEN_INNOVO_12_CZERWONA_ANGOBA = "src/main/resources/assets/Bogen Innovo 12 czerwona angoba.csv";
    private static final String TEST_INNOVO_10_CZERWONA = "src/main/resources/assets/TestInnovo10Czerwona.csv";
    private static final String AKCESORIA = "src/main/resources/assets/akcesoria.csv";
    private static final String OKNA_OKPOL_DAKEA = "src/main/resources/assets/OknaOkpolDakea.csv";
    private static final String KOLNIERZ_OKPOL_DAKEA = "src/main/resources/assets/KołnierzOkpolDakea.csv";

    private static final String NAME_BOGEN_INNOVO_10_CZERWONA_ANGOBA = "Bogen Innovo 10 Czerwona Angoba";
    private static final String NAME_BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA = "Bogen Innovo 10 Miedziano-brązowa Angoba";


    private AccesoriesRepository accesoriesRepository;
    private WindowsRepository windowsRepository;
    private KolnierzRepository kolnierzRepository;
    private TilesRepository tilesRepository;

    private DaoTiles daoTiles;
    private DaoAccesories daoAccesories;
    private DaoWindows daoWindows;
    private DaoKolnierz daoKolnierz;
    private CalculateTiles calculateTiles;


    public ImportFiles() {
    }

    @Autowired
    public ImportFiles(AccesoriesRepository accesoriesRepository, WindowsRepository windowsRepository,
                       KolnierzRepository kolnierzRepository, TilesRepository tilesRepository) {
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);
        this.windowsRepository = Objects.requireNonNull(windowsRepository);
        this.kolnierzRepository = Objects.requireNonNull(kolnierzRepository);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
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
        tilesRepository.deleteAll();
        accesoriesRepository.deleteAll();
        windowsRepository.deleteAll();
        kolnierzRepository.deleteAll();

        daoTiles.save(BOGEN_INNOVO_10_CZERWONA_ANGOBA, NAME_BOGEN_INNOVO_10_CZERWONA_ANGOBA);
        daoTiles.save(BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA, NAME_BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA);
        /*daoTiles.save(BOGEN_INNOVO_12_CZERWONA_ANGOBA);
        daoTiles.save(TEST_INNOVO_10_CZERWONA);*/
        daoAccesories.save(AKCESORIA, "do usunięcia");
        daoWindows.save(OKNA_OKPOL_DAKEA,"do usunięcia");
        daoKolnierz.save(KOLNIERZ_OKPOL_DAKEA,"do usunięcia");
        calculateTiles.getAvailablePriceList();
        getNotificationSucces("Zaimportowano cenniki");
        UI.getCurrent().getPage().reload();
        /*history.getUI().navigate(UsersView.class);*/
    }
}
