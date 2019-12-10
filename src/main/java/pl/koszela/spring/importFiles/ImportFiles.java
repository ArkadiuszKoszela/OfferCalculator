package pl.koszela.spring.importFiles;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.DAOs.*;
import pl.koszela.spring.entities.main.*;
import pl.koszela.spring.repositories.*;
import pl.koszela.spring.service.NotificationInterface;

import java.util.Objects;

import static pl.koszela.spring.staticField.Endpoint.*;

@Service
public class ImportFiles<T extends BaseEntity>{

    private AccesoriesRepository accesoriesRepository;
    private WindowsRepository windowsRepository;
    private CollarRepository collarRepository;
    private TilesRepository tilesRepository;
    private GutterRepository gutterRepository;
    private FiresideRepository firesideRepository;
    private AccesoriesWindowsRepository accesoriesWindowsRepository;
    private LightningProtectionSystemRepository lightningProtectionSystemRepository;
    private BaseRepository<Accessories> baseRepository;

    private Dao dao;
    private UsersRepo usersRepo;


    @Autowired
    public ImportFiles(AccesoriesRepository accesoriesRepository, WindowsRepository windowsRepository,
                       CollarRepository collarRepository, TilesRepository tilesRepository, GutterRepository gutterRepository, FiresideRepository firesideRepository, AccesoriesWindowsRepository accesoriesWindowsRepository, LightningProtectionSystemRepository lightningProtectionSystemRepository, BaseRepository<Accessories> baseRepository, UsersRepo usersRepo) {
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);
        this.windowsRepository = Objects.requireNonNull(windowsRepository);
        this.collarRepository = Objects.requireNonNull(collarRepository);
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
        this.gutterRepository = Objects.requireNonNull(gutterRepository);
        this.firesideRepository = Objects.requireNonNull(firesideRepository);
        this.accesoriesWindowsRepository = Objects.requireNonNull(accesoriesWindowsRepository);
        this.lightningProtectionSystemRepository = Objects.requireNonNull(lightningProtectionSystemRepository);
        this.baseRepository = Objects.requireNonNull(baseRepository);
        this.usersRepo = Objects.requireNonNull(Objects.requireNonNull(usersRepo));
    }

    @Autowired
    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public void csv() {
        dao.deleteAllFromRepo(tilesRepository);
        dao.deleteAllFromRepo(accesoriesRepository);
        dao.deleteAllFromRepo(windowsRepository);
        dao.deleteAllFromRepo(collarRepository);
        dao.deleteAllFromRepo(gutterRepository);
        dao.deleteAllFromRepo(firesideRepository);
        dao.deleteAllFromRepo(accesoriesWindowsRepository);
        dao.deleteAllFromRepo(lightningProtectionSystemRepository);

        dao.readAndSaveToORMTest(FILE_BOGEN_INNOVO_10_CZERWONA_ANGOBA_URL.location(), Tiles.class, new Tiles(), tilesRepository);
        dao.readAndSaveToORMTest(FILE_BOGEN_INNOVO_10_MIEDZIANO_BRAZOWA_ANGOBA_URL.location(), Tiles.class, new Tiles(), tilesRepository);
        dao.readAndSaveToORMTest(FILE_BOGEN_INNOVO_12_CZERWONA_ANGOBA_URL.location(), Tiles.class, new Tiles(), tilesRepository);
        dao.<Accessories>readAndSaveToORMTest(FILE_AKCESORIA_URL.location(), Accessories.class, new Accessories(), baseRepository);
        dao.readAndSaveToORMTest(FILE_COLLAR_FAKRO_URL.location(), Collar.class, new Collar(), collarRepository);
        dao.readAndSaveToORMTest(FILE_COLLAR_VELUX_URL.location(), Collar.class, new Collar(), collarRepository);
        dao.readAndSaveToORMTest(FILE_OKNA_FAKRO_DAKEA_URL.location(), Windows.class, new Windows(), windowsRepository);
        dao.readAndSaveToORMTest(FILE_OKNA_VELUX_URL.location(), Windows.class, new Windows(), windowsRepository);
        dao.readAndSaveToORMTest(FILE_ACCESORIES_WINDOWS_FAKRO_URL.location(), AccessoriesWindows.class, new AccessoriesWindows(), accesoriesWindowsRepository);
        dao.readAndSaveToORMTest(FILE_ACCESORIES_WINDOWS_VELUX_URL.location(), AccessoriesWindows.class, new AccessoriesWindows(), accesoriesWindowsRepository);
        dao.readAndSaveToORMTest(FILE_PLEWA_URL.location(), Fireside.class, new Fireside(), firesideRepository);
        dao.readAndSaveToORMTest(FILE_SYSTEM_PROTECTION_URL.location(), LightningProtectionSystem.class, new LightningProtectionSystem(), lightningProtectionSystemRepository);
        dao.readAndSaveToORMTest(FILE_FLAMINGO_125x100_URL.location(), Gutter.class, new Gutter(), gutterRepository);
        dao.readAndSaveToORMTest(FILE_FLAMINGO_125x90_URL.location(), Gutter.class, new Gutter(), gutterRepository);
        dao.readAndSaveToORMTest(FILE_BRYZA_125x90_URL.location(), Gutter.class, new Gutter(), gutterRepository);
        dao.readAndSaveToORMTest(FILE_BRYZA_150x100_URL.location(), Gutter.class, new Gutter(), gutterRepository);
        NotificationInterface.notificationOpen("Zaimportowano cenniki", NotificationVariant.LUMO_SUCCESS);
        UI.getCurrent().getPage().reload();
    }


}