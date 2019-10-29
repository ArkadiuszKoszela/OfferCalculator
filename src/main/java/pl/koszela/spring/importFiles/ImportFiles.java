package pl.koszela.spring.importFiles;

import com.vaadin.flow.component.UI;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.DAOs.*;
import pl.koszela.spring.repositories.*;

import java.util.Objects;

import static pl.koszela.spring.service.ServiceNotification.getNotificationSucces;

@Service
public class ImportFiles {
    private final static Logger logger = Logger.getLogger(ImportFiles.class);

    private static final String BOGEN_INNOVO_10_CZERWONA_ANGOBA = "src/main/resources/assets/Bogen Innovo 10 czerwona angoba.csv";
    private static final String BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA = "src/main/resources/assets/Bogen Innovo 10 miedziano-brazowa angoba.csv";
    private static final String BOGEN_INNOVO_12_CZERWONA_ANGOBA = "src/main/resources/assets/Bogen Innovo 12 czerwona angoba.csv";
    private static final String AKCESORIA = "src/main/resources/assets/akcesoria.csv";
    private static final String OKNA_OKPOL_DAKEA = "src/main/resources/assets/OknaOkpolDakea.csv";
    private static final String KOLNIERZ_OKPOL_DAKEA = "src/main/resources/assets/KolnierzOkpolDakea.csv";
    private static final String FLAMINGO_125x100 = "src/main/resources/assets/Flamingo 125100.csv";
    private static final String FLAMINGO_125x90 = "src/main/resources/assets/Flamingo 12590.csv";
    private static final String BRYZA_125x90 = "src/main/resources/assets/Bryza 12590.csv";
    private static final String BRYZA_150x100 = "src/main/resources/assets/Bryza 150100.csv";

    private static final String NAME_BOGEN_INNOVO_10_CZERWONA_ANGOBA = "Bogen Innovo 10 Czerwona Angoba";
    private static final String NAME_BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA = "Bogen Innovo 10 Miedziano-brazowa Angoba";
    private static final String NAME_BOGEN_INNOVO_12_CZERWONA_ANGOBA = "Bogen Innovo 12 Czerwona Angoba";
    private static final String NAME_FLAMINGO_125x100 = "Flamingo 125x100";
    private static final String NAME_FLAMINGO_125x90 = "Flamingo 125x90";
    private static final String NAME_BRYZA_125x90 = "Bryza 125x90";
    private static final String NAME_BRYZA_150x100 = "Bryza 150x100";

    private AccesoriesRepository accesoriesRepository;
    private WindowsRepository windowsRepository;
    private KolnierzRepository kolnierzRepository;
    private TilesRepository tilesRepository;
    private GutterRepository gutterRepository;

    private DaoTiles daoTiles;
    private DaoAccesories daoAccesories;
    private DaoKolnierz daoKolnierz;
    private DaoWindows daoWindows;
    private DaoGutter daoGutter;
    private UsersRepo usersRepo;

    public ImportFiles() {
    }

    @Autowired
    public ImportFiles(AccesoriesRepository accesoriesRepository, WindowsRepository windowsRepository,
                       KolnierzRepository kolnierzRepository, TilesRepository tilesRepository, GutterRepository gutterRepository, UsersRepo usersRepo) {
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);
        this.windowsRepository = Objects.requireNonNull(windowsRepository);
        this.kolnierzRepository = Objects.requireNonNull(kolnierzRepository);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
        this.gutterRepository = Objects.requireNonNull(gutterRepository);
        this.usersRepo = Objects.requireNonNull(usersRepo);
    }

    @Autowired
    public void setDaoGutter(DaoGutter daoGutter) {
        this.daoGutter = daoGutter;
    }

    @Autowired
    public void setDaoWindows(DaoWindows daoWindows) {
        this.daoWindows = daoWindows;
    }

    @Autowired
    public void setDaoKolnierz(DaoKolnierz daoKolnierz) {
        this.daoKolnierz = daoKolnierz;
    }

    @Autowired
    public void setDaoTiles(DaoTiles daoTiles) {
        this.daoTiles = Objects.requireNonNull(daoTiles);
    }

    @Autowired
    public void setDaoAccesories(DaoAccesories daoAccesories) {
        this.daoAccesories = Objects.requireNonNull(daoAccesories);
    }

    public void csv() {
        usersRepo.deleteAll();
        logger.info("deleted all users");
        tilesRepository.deleteAll();
        logger.info("deleted all tiles");
        accesoriesRepository.deleteAll();
        logger.info("deleted all accesories");
        windowsRepository.deleteAll();
        logger.info("deleted all windows");
        kolnierzRepository.deleteAll();
        logger.info("deleted all kolnierz");
        gutterRepository.deleteAll();
        logger.info("deleted all gutters");

        daoTiles.save(BOGEN_INNOVO_10_CZERWONA_ANGOBA, NAME_BOGEN_INNOVO_10_CZERWONA_ANGOBA);
        daoTiles.save(BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA, NAME_BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA);
        daoTiles.save(BOGEN_INNOVO_12_CZERWONA_ANGOBA, NAME_BOGEN_INNOVO_12_CZERWONA_ANGOBA);
        daoAccesories.save(AKCESORIA, "do usunięcia");
        daoKolnierz.save(KOLNIERZ_OKPOL_DAKEA, "do usunięcia");
        daoWindows.save(OKNA_OKPOL_DAKEA, "do usunięcia");
        daoGutter.save(FLAMINGO_125x100, NAME_FLAMINGO_125x100);
        daoGutter.save(FLAMINGO_125x90, NAME_FLAMINGO_125x90);
        daoGutter.save(BRYZA_125x90, NAME_BRYZA_125x90);
        daoGutter.save(BRYZA_150x100, NAME_BRYZA_150x100);
        getNotificationSucces("Zaimportowano cenniki");
        UI.getCurrent().getPage().reload();
    }
}