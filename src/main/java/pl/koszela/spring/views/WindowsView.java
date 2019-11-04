package pl.koszela.spring.views;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.Accesories;
import pl.koszela.spring.entities.Kolnierz;
import pl.koszela.spring.entities.Tiles;
import pl.koszela.spring.entities.Windows;
import pl.koszela.spring.repositories.KolnierzRepository;
import pl.koszela.spring.repositories.WindowsRepository;
import pl.koszela.spring.service.GridInteraface;
import pl.koszela.spring.service.NotificationInterface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Route(value = WindowsView.WINDOWS, layout = MainView.class)
public class WindowsView extends VerticalLayout implements GridInteraface {

    static final String WINDOWS = "windows";

    private WindowsRepository windowsRepository;

    private Set<Windows> set = (Set<Windows>) VaadinSession.getCurrent().getSession().getAttribute("windows");

    private TreeGrid<Windows> treeGrid = new TreeGrid<>();
    private Binder<Windows> binder;

    @Autowired
    public WindowsView(WindowsRepository windowsRepository) {
        this.windowsRepository = Objects.requireNonNull(windowsRepository);

        if(set == null){
            set = allWindows();
        }
        add(createGrid());
    }

    @Override
    public TreeGrid<Windows> createGrid() {
        treeGrid.addHierarchyColumn(windows -> {
            int i = windows.getName().lastIndexOf(" ");
            return windows.getName().substring(0, i);
        }).setHeader("Nazwa");
        treeGrid.addColumn(Windows::getSize).setHeader("Rozmiar");
        treeGrid.addColumn(Windows::getManufacturer).setHeader("Producent");
        Grid.Column<Windows> quantityColumn = treeGrid.addColumn(Windows::getQuantity).setHeader("Ilość");
        Grid.Column<Windows> discountColumn = treeGrid.addColumn(Windows::getDiscount).setHeader("Rabat");
        treeGrid.addColumn(Windows::getUnitDetalPrice).setHeader("Cena detal");
        treeGrid.addColumn(Windows::getUnitPurchasePrice).setHeader("Cena zakupu");
        treeGrid.addColumn(Windows::getAllpriceAfterDiscount).setHeader("Cena razem detal");
        treeGrid.addColumn(Windows::getAllpricePurchase).setHeader("Cena razem zakup");
        treeGrid.addColumn(Windows::getAllprofit).setHeader("Zysk");
        treeGrid.addColumn(createComponent()).setHeader("Opcje");

        binder = new Binder<>(Windows.class);
        treeGrid.getEditor().setBinder(binder);
        readBeans(binder);

        TextField discountEditField = new TextField();
        binder.forField(discountEditField)
                .withConverter(new StringToIntegerConverter("Błąd"))
                .bind(Windows::getDiscount, Windows::setDiscount);
        addEnterEvent(treeGrid, discountEditField);
        itemClickListener(discountEditField);
        discountColumn.setEditorComponent(discountEditField);

        TextField quantityField = new TextField();
        binder.forField(quantityField)
                .withConverter(new StringToDoubleConverter("Błąd"))
                .bind(Windows::getQuantity, Windows::setQuantity);
        addEnterEvent(treeGrid, quantityField);
        itemClickListener(quantityField);
        quantityColumn.setEditorComponent(quantityField);

        closeListener();

        treeGrid.setDataProvider(new TreeDataProvider<>(addItems(new ArrayList())));
        treeGrid.setMinHeight("700px");
        treeGrid.getColumns().forEach(e -> e.setAutoWidth(true));
        return treeGrid;
    }

    @Override
    public void itemClickListener(TextField textField) {
        treeGrid.addItemDoubleClickListener(event -> {
            treeGrid.getEditor().editItem(event.getItem());
            textField.focus();
        });
    }

    @Override
    public void closeListener() {
        treeGrid.getEditor().addCloseListener(event -> {
            if (binder.getBean() != null) {
                Windows windows = binder.getBean();
                windows.setAllpricePurchase(BigDecimal.valueOf(windows.getQuantity() * windows.getUnitDetalPrice() * 70 / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                windows.setAllpriceAfterDiscount(BigDecimal.valueOf(windows.getQuantity() * windows.getUnitDetalPrice() * (100 - windows.getDiscount()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                windows.setAllprofit(BigDecimal.valueOf(windows.getAllpriceAfterDiscount() - windows.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                if (binder.getBean().getDiscount() <= 30) {
                    binder.setBean(windows);
                } else {
                    windows.setDiscount(30);
                    NotificationInterface.notificationOpen("Maksymalny rabat to 30 %", NotificationVariant.LUMO_ERROR);
                    binder.setBean(windows);
                }
            }
        });
    }

    @Override
    public TreeData<Windows> addItems(List list) {
        TreeData<Windows> treeData = new TreeData<>();
        Set<Windows> collect = set.stream().filter(e -> e.getName().equals("Okno FTP-V U3 55x78") || e.getName().equals("Okno GZL 1059  55x78")).collect(Collectors.toSet());
        set.removeAll(collect);
        for (Windows parent : collect) {
            List<Windows> childrens = new ArrayList<>(set);
            for (int i = 0; i < childrens.size(); i++) {
                if (i == 0) {
                    treeData.addItem(null, parent);
                } else if (childrens.get(i).getManufacturer().equals(parent.getManufacturer())) {
                    treeData.addItem(parent, childrens.get(i));
                }
            }
        }
        return treeData;
    }

    @Override
    public TextField editField(StringToIntegerConverter stringToIntegerConverter, StringToDoubleConverter stringToDoubleConverter) {
        return null;
    }

    @Override
    public ComponentRenderer<VerticalLayout, Windows> createComponent() {
        return new ComponentRenderer<>(windows -> {
            Checkbox mainCheckBox = new Checkbox("Dodać ?");
            mainCheckBox.setValue(windows.isOffer());
            mainCheckBox.addValueChangeListener(event -> {
                windows.setOffer(event.getValue());
            });
            return new VerticalLayout(mainCheckBox);
        });
    }

    private Set<Windows> allWindows(){
        return new HashSet<>(windowsRepository.findAll());
    }

    private void readBeans(Binder<Windows> binder) {
        for (Windows windows : set) {
            windows.setAllpricePurchase(BigDecimal.valueOf(windows.getUnitDetalPrice() * windows.getQuantity() * 70 / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
            windows.setAllpriceAfterDiscount(BigDecimal.valueOf(windows.getUnitDetalPrice() * windows.getQuantity() * (100 - windows.getDiscount()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
            windows.setAllprofit(BigDecimal.valueOf(windows.getAllpriceAfterDiscount() - windows.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            binder.setBean(windows);
        }
    }
}