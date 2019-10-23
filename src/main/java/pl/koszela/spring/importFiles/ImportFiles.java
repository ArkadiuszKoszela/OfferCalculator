package pl.koszela.spring.importFiles;

import com.vaadin.flow.component.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.DAOs.*;
import pl.koszela.spring.service.AvailablePriceList;
import pl.koszela.spring.repositories.*;

import java.util.Objects;

import static pl.koszela.spring.service.ServiceNotification.getNotificationSucces;

@Service
public class ImportFiles {

    private static final String BOGEN_INNOVO_10_CZERWONA_ANGOBA = "src/main/resources/assets/Bogen Innovo 10 czerwona angoba.csv";
    private static final String BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA = "src/main/resources/assets/Bogen Innovo 10 miedziano-brązowa angoba.csv";
    private static final String BOGEN_INNOVO_12_CZERWONA_ANGOBA = "src/main/resources/assets/Bogen Innovo 12 czerwona angoba.csv";
    private static final String AKCESORIA = "src/main/resources/assets/akcesoria.csv";
    private static final String OKNA_OKPOL_DAKEA = "src/main/resources/assets/OknaOkpolDakea.csv";
    private static final String KOLNIERZ_OKPOL_DAKEA = "src/main/resources/assets/KołnierzOkpolDakea.csv";
    private static final String FLAMINGO_GUTTER = "src/main/resources/assets/FlamingoGutter.csv";

    private static final String NAME_BOGEN_INNOVO_10_CZERWONA_ANGOBA = "Bogen Innovo 10 Czerwona Angoba";
    private static final String NAME_BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA = "Bogen Innovo 10 Miedziano-brązowa Angoba";
    private static final String NAME_BOGEN_INNOVO_12_CZERWONA_ANGOBA = "Bogen Innovo 12 Czerwona Angoba";


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
    private AvailablePriceList availablePriceList;

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
    public void setAvailablePriceList(AvailablePriceList availablePriceList) {
        this.availablePriceList = Objects.requireNonNull(availablePriceList);
    }

    @Autowired
    public void setDaoAccesories(DaoAccesories daoAccesories) {
        this.daoAccesories = Objects.requireNonNull(daoAccesories);
    }

    public void csv() {
        usersRepo.deleteAll();
        tilesRepository.deleteAll();
        accesoriesRepository.deleteAll();
        windowsRepository.deleteAll();
        kolnierzRepository.deleteAll();
        gutterRepository.deleteAll();

        daoTiles.save(BOGEN_INNOVO_10_CZERWONA_ANGOBA, NAME_BOGEN_INNOVO_10_CZERWONA_ANGOBA);
        daoTiles.save(BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA, NAME_BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA);
        daoTiles.save(BOGEN_INNOVO_12_CZERWONA_ANGOBA, NAME_BOGEN_INNOVO_12_CZERWONA_ANGOBA);
        daoAccesories.save(AKCESORIA, "do usunięcia");
        daoKolnierz.save(KOLNIERZ_OKPOL_DAKEA, "do usunięcia");
        daoWindows.save(OKNA_OKPOL_DAKEA, "do usunięcia");
        daoGutter.save(FLAMINGO_GUTTER, "do usunięcia");
        getNotificationSucces("Zaimportowano cenniki");
        UI.getCurrent().getPage().reload();
    }
}