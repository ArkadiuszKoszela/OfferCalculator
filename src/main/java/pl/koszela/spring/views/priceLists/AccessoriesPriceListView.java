package pl.koszela.spring.views.priceLists;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.crudFiles.CreateFile;
import pl.koszela.spring.crudFiles.DeleteFile;
import pl.koszela.spring.crudFiles.ReadFile;
import pl.koszela.spring.entities.main.Accessories;
import pl.koszela.spring.entities.main.UploadFile;
import pl.koszela.spring.repositories.AccesoriesRepository;
import pl.koszela.spring.repositories.BaseRepository;
import pl.koszela.spring.repositories.UploadFileRepository;
import pl.koszela.spring.service.NotificationInterface;
import pl.koszela.spring.service.PriceListInterface;
import pl.koszela.spring.views.MainView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

import static pl.koszela.spring.crudFiles.ConfigToFtpServer.REMOTE_PATH;

@Route(value = AccessoriesPriceListView.ACCESSORIES_PRICE, layout = MainView.class)
public class AccessoriesPriceListView extends VerticalLayout implements PriceListInterface<Accessories> {

    public static final String ACCESSORIES_PRICE = "accessoriesPrice";

    private AccesoriesRepository accesoriesRepository;
    private UploadFileRepository uploadFileRepository;
    private ReadFile readFile;
    private DeleteFile deleteFile;
    private CreateFile createFile;

    private Grid<Accessories> grid = new Grid<>();
    private Binder<Accessories> binder;
    private List<Accessories> list = new ArrayList<>();

    private Set<UploadFile> uploadFiles;
    private Dialog dialog = new Dialog();
    private Button btnDiscounts;

    @Autowired
    public AccessoriesPriceListView(AccesoriesRepository accesoriesRepository, UploadFileRepository uploadFileRepository, ReadFile readFile, DeleteFile deleteFile, CreateFile createFile) throws IOException {
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);
        this.uploadFileRepository = Objects.requireNonNull(uploadFileRepository);
        this.readFile = Objects.requireNonNull(readFile);
        this.deleteFile = Objects.requireNonNull(deleteFile);
        this.createFile = Objects.requireNonNull(createFile);

        list = allAccessoriesFromRepository();
        uploadFiles = new HashSet<>(allUploadFilesFromRepository());

        if (uploadFiles.size() == 0) {
            readFile.listFolder(REMOTE_PATH, uploadFiles, uploadFileRepository);
            for (UploadFile uploadFile : uploadFiles) {
                uploadFileRepository.save(uploadFile);
            }
        }
        add(createNewGrid(grid, binder, list, accesoriesRepository));
    }

    private Grid<UploadFile> uploadFileGrid = new Grid<>();

    private void createDialogGrid(UploadFileRepository uploadFileRepository, Accessories accessories) {
        uploadFileGrid.setDataProvider(new ListDataProvider<>(uploadFiles));
        uploadFileGrid.addColumn(UploadFile::getNameFolder).setHeader("Nazwa Folderu").setFooter(new Button("Odśwież listę", e -> {
            try {
                uploadFiles.clear();
                readFile.listFolder(REMOTE_PATH, uploadFiles, uploadFileRepository);
                uploadFileRepository.saveAll(uploadFiles);
                uploadFileGrid.setDataProvider(new ListDataProvider<>(uploadFiles));
                uploadFileGrid.getDataProvider().refreshAll();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }));
        uploadFileGrid.addColumn(UploadFile::getNameFile).setHeader("Nazwa pliku").setFooter(createFileInServer(accessories));
        uploadFileGrid.addComponentColumn(uploadFile -> {
            return buttonDeleteFile(uploadFileRepository, uploadFile);
        });
        uploadFileGrid.getColumns().forEach(e -> e.setAutoWidth(true));
        dialog.add(uploadFileGrid);
        dialog.setWidth("600px");
    }

    private Button buttonDeleteFile(UploadFileRepository uploadFileRepository, UploadFile uploadFile) {
        return new Button("Usuń", event -> {
            if (deleteFile.deleteFile(uploadFile, REMOTE_PATH, uploadFiles, uploadFileRepository)) {
                NotificationInterface.notificationOpen("Usunięto instrukcję z bazy", NotificationVariant.LUMO_SUCCESS);
            } else {
                NotificationInterface.notificationOpen("Nie możn usunąć instrukcji", NotificationVariant.LUMO_ERROR);
            }
            uploadFileGrid.getDataProvider().refreshAll();
        });
    }

    private List<UploadFile> allUploadFilesFromRepository() {
        return uploadFileRepository.findAll();
    }

    private Grid<Accessories> createNewGrid(Grid<Accessories> grid, Binder<Accessories> binder, List<Accessories> list, BaseRepository<Accessories> repository) {
        TextField filter = new TextField();
        ListDataProvider<Accessories> listDataProvider = new ListDataProvider<>(list);
        grid.setDataProvider(listDataProvider);

        Grid.Column<Accessories> priceListNameColumn = grid.addColumn(Accessories::getManufacturer).setHeader("Nazwa Cennika");
        Grid.Column<Accessories> nameColumn = grid.addColumn(Accessories::getName).setHeader("Nazwa");
        Grid.Column<Accessories> priceColumn = grid.addColumn(Accessories::getUnitDetalPrice).setHeader("Cena detal");
        Grid.Column<Accessories> basicDiscountColumn = grid.addColumn(Accessories::getBasicDiscount).setHeader("Podstawowy rabat");
        Grid.Column<Accessories> promotionDiscountColumn = grid.addColumn(Accessories::getPromotionDiscount).setHeader("Promocja");
        Grid.Column<Accessories> additionalDiscountColumn = grid.addColumn(Accessories::getAdditionalDiscount).setHeader("Dodatkowy rabat");
        Grid.Column<Accessories> skontoDiscountColumn = grid.addColumn(Accessories::getSkontoDiscount).setHeader("Skonto");
        Grid.Column<Accessories> priceFromRepoColumn = grid.addColumn(Accessories::getUnitPurchasePrice).setHeader("Cena zakupu(z ręki)");
        Grid.Column<Accessories> date = grid.addColumn(Accessories::getDate).setHeader("Data zmiany");
        Grid.Column<Accessories> fileUrl = grid.addColumn(Accessories::getUrlToDownloadFile).setHeader("URL");
        Grid.Column<Accessories> add = grid.addComponentColumn(event -> {
            return new Button("Pliki", e -> {
                createDialogGrid(uploadFileRepository, event);
                dialog.open();
            });
        });

        binder = new Binder<>();
        grid.getEditor().setBinder(binder);

        HeaderRow filterRow = grid.appendHeaderRow();
        filter.addValueChangeListener(event -> listDataProvider.addFilter(
                baseEntity -> StringUtils.containsIgnoreCase(baseEntity.getName(), filter.getValue())
        ));

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(nameColumn).setComponent(filter);
        filter.setSizeFull();
        filter.setPlaceholder("Filter");

        TextField basicDiscount = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), Accessories::getBasicDiscount, Accessories::setBasicDiscount);
        itemClickListener(grid, basicDiscount);
        basicDiscountColumn.setEditorComponent(basicDiscount);

        TextField promotionDiscount = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), Accessories::getPromotionDiscount, Accessories::setPromotionDiscount);
        itemClickListener(grid, promotionDiscount);
        promotionDiscountColumn.setEditorComponent(promotionDiscount);

        TextField additionalDiscount = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), Accessories::getAdditionalDiscount, Accessories::setAdditionalDiscount);
        itemClickListener(grid, additionalDiscount);
        additionalDiscountColumn.setEditorComponent(additionalDiscount);

        TextField skontoDiscount = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), Accessories::getSkontoDiscount, Accessories::setSkontoDiscount);
        itemClickListener(grid, skontoDiscount);
        skontoDiscountColumn.setEditorComponent(skontoDiscount);

        closeListener(grid, binder);
        settingsGrid(grid);
        return grid;
    }

    private Button createFileInServer(Accessories accessories) {
        return new Button("Wrzuć plik", event1 -> {
            UI.getCurrent().getPage().executeJs("return window.location.href;").then(String.class, value -> {
                if (!value.equals("/")) {

                    String id = String.valueOf(accessories.getId());
                    Cookie urlCookie = new Cookie("url", value);
                    Cookie idProductCookie = new Cookie("idProduct", id);
                    idProductCookie.setPath("/");
                    urlCookie.setPath("/");
                    VaadinService.getCurrentResponse().addCookie(idProductCookie);
                    VaadinService.getCurrentResponse().addCookie(urlCookie);
                }
                int i = value.lastIndexOf("/");
                String substring = value.substring(0, i) + "/index";
                UI.getCurrent().getPage().open(substring);
            });
        });
    }

    private List<Accessories> allAccessoriesFromRepository() {
        return accesoriesRepository.findAll();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        VaadinRequest vaadinRequest = VaadinService.getCurrentRequest();
        HttpServletRequest httpServletRequest = ((VaadinServletRequest) vaadinRequest).getHttpServletRequest();
        Cookie[] cookies = httpServletRequest.getCookies();
        String findURI = null;
        String nameFolder = null;
        String fileName = null;
        String nameProduct = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("URI")) {
                findURI = cookie.getValue();
            } else if (cookie.getName().equals("nameFolder")) {
                nameFolder = cookie.getValue();
            } else if (cookie.getName().equals("fileName")) {
                fileName = cookie.getValue();
            } else if (cookie.getName().equals("idProduct")) {
                nameProduct = cookie.getValue();
            }
        }
        if (findURI != null && Objects.requireNonNull(findURI).equals("/uploadStatus")) {
            System.out.println("Mamy Cię");
            changeURL(nameProduct, nameFolder, fileName);
        } else {
            System.out.println("Nie mamy Cię :(");
        }
    }

    private void changeURL(String id, String nameFolder, String fileName) {
        Long longId = Long.valueOf(id);
        Optional<Accessories> byId = accesoriesRepository.findById(longId);
        byId.ifPresent(accessories -> accessories.setUrlToDownloadFile("http://begginerwebsite.000webhostapp.com/" + nameFolder + "/" + fileName));
        byId.ifPresent(accessories -> accesoriesRepository.save(accessories));
        VaadinRequest vaadinRequest = VaadinService.getCurrentRequest();
        HttpServletRequest httpServletRequest = ((VaadinServletRequest) vaadinRequest).getHttpServletRequest();
        Cookie[] cookies = httpServletRequest.getCookies();
        clearAllCookies(cookies);
    }

    private void clearAllCookies(Cookie[] cookies) {
        if (cookies != null)
            for (int i = 0, cookiesLength = cookies.length; i < cookiesLength; i++) {
                Cookie cookie = cookies[i];
                if (i != 0) {
                    cookie.setMaxAge(0);
                    VaadinService.getCurrentResponse().addCookie(cookie);
                }
            }
    }
}