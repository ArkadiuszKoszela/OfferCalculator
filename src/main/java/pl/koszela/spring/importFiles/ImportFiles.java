package pl.koszela.spring.importFiles;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.DAOs.*;
import pl.koszela.spring.repositories.*;
import pl.koszela.spring.service.NotificationInterface;

import java.util.Objects;

import static pl.koszela.spring.importFiles.Endpoint.*;

@Service
public class ImportFiles {
    private final static Logger logger = Logger.getLogger(ImportFiles.class);

    private AccesoriesRepository accesoriesRepository;
    private WindowsRepository windowsRepository;
    private CollarRepository collarRepository;
    private TilesRepository tilesRepository;
    private GutterRepository gutterRepository;

    private DaoTiles daoTiles;
    private DaoAccesories daoAccesories;
    private DaoCollar daoCollar;
    private DaoWindows daoWindows;
    private DaoGutter daoGutter;
    private DaoAccesoriesWindows daoAccesoriesWindows;
    private UsersRepo usersRepo;
    private DaoFireside daoFireSide;

    public ImportFiles() {
    }

    @Autowired
    public ImportFiles(AccesoriesRepository accesoriesRepository, WindowsRepository windowsRepository,
                       CollarRepository collarRepository, TilesRepository tilesRepository, GutterRepository gutterRepository, UsersRepo usersRepo) {
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);
        this.windowsRepository = Objects.requireNonNull(windowsRepository);
        this.collarRepository = Objects.requireNonNull(collarRepository);
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
    public void setDaoCollar(DaoCollar daoCollar) {
        this.daoCollar = daoCollar;
    }

    @Autowired
    public void setDaoTiles(DaoTiles daoTiles) {
        this.daoTiles = Objects.requireNonNull(daoTiles);
    }

    @Autowired
    public void setDaoAccesories(DaoAccesories daoAccesories) {
        this.daoAccesories = Objects.requireNonNull(daoAccesories);
    }

    @Autowired
    public void setDaoAccesoriesWindows(DaoAccesoriesWindows daoAccesoriesWindows) {
        this.daoAccesoriesWindows = Objects.requireNonNull(daoAccesoriesWindows);
    }

    @Autowired
    public void setDaoFireSide(DaoFireside daoFireSide) {
        this.daoFireSide = daoFireSide;
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
        collarRepository.deleteAll();
        logger.info("deleted all kolnierz");
        gutterRepository.deleteAll();
        logger.info("deleted all gutters");

        daoTiles.readAndSaveToORM(FILE_BOGEN_INNOVO_10_CZERWONA_ANGOBA_URL.location());
        daoTiles.readAndSaveToORM(FILE_BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA_URL.location());
        daoTiles.readAndSaveToORM(FILE_BOGEN_INNOVO_12_CZERWONA_ANGOBA_URL.location());
        daoAccesories.readAndSaveToORM(FILE_AKCESORIA_URL.location());
        daoCollar.readAndSaveToORM(FILE_COLLAR_FAKRO_URL.location());
        daoCollar.readAndSaveToORM(FILE_COLLAR_VELUX_URL.location());
        daoWindows.readAndSaveToORM(FILE_OKNA_FAKRO_DAKEA_URL.location());
        daoWindows.readAndSaveToORM(FILE_OKNA_VELUX_URL.location());
        daoAccesoriesWindows.readAndSaveToORM(FILE_ACCESORIES_WINDOWS_FAKRO_URL.location());
        daoAccesoriesWindows.readAndSaveToORM(FILE_ACCESORIES_WINDOWS_VELUX_URL.location());
        daoFireSide.readAndSaveToORM(FILE_PLEWA_URL.location());
        daoGutter.readAndSaveToORM(FILE_FLAMINGO_125x100_URL.location());
        daoGutter.readAndSaveToORM(FILE_FLAMINGO_125x90_URL.location());
        daoGutter.readAndSaveToORM(FILE_BRYZA_125x90_URL.location());
        daoGutter.readAndSaveToORM(FILE_BRYZA_150x100_URL.location());
        NotificationInterface.notificationOpen("Zaimportowano cenniki", NotificationVariant.LUMO_SUCCESS);
        UI.getCurrent().getPage().reload();
    }
}