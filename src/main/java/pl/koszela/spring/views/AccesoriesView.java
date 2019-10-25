package pl.koszela.spring.views;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import pl.koszela.spring.entities.accesories.EntityAccesories;
import pl.koszela.spring.entities.tiles.EntityInputDataTiles;
import pl.koszela.spring.repositories.AccesoriesRepository;
import pl.koszela.spring.service.GridInteraface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.koszela.spring.service.ServiceNotification.getNotificationError;

@Route(value = AccesoriesView.SELECT_ACCESORIES, layout = MainView.class)
public class AccesoriesView extends VerticalLayout implements GridInteraface, BeforeLeaveObserver {
    static final String SELECT_ACCESORIES = "accesories";

    private TreeGrid<EntityAccesories> treeGrid = new TreeGrid<>();
    private EntityInputDataTiles dataTilesRepo = (EntityInputDataTiles) VaadinSession.getCurrent().getSession().getAttribute("tilesInputFromRepo");
    private Set<EntityAccesories> set = (Set<EntityAccesories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");
    private AccesoriesRepository accesoriesRepository;
    private Binder<EntityAccesories> binder;
    private RadioButtonGroup<String> checkboxGroup = new RadioButtonGroup<>();


    public AccesoriesView(AccesoriesRepository accesoriesRepository) {
        this.accesoriesRepository = accesoriesRepository;
        if (set == null) {
            List<EntityAccesories> all = accesoriesRepository.findAll();
            set = new HashSet<>(addQuantityToList(all));
        }
        add(createCheckbox());
        add(createGrid());
    }

    @Override
    public TreeGrid createGrid() {
        Grid.Column<EntityAccesories> nameColumn = treeGrid.addHierarchyColumn(EntityAccesories::getName).setHeader("Nazwa");
        Grid.Column<EntityAccesories> quantityColumn = treeGrid.addColumn(EntityAccesories::getQuantity).setHeader("Ilość");
        Grid.Column<EntityAccesories> discountColumn = treeGrid.addColumn(EntityAccesories::getDiscount).setHeader("Rabat");
        Grid.Column<EntityAccesories> detalPriceColumn = treeGrid.addColumn(EntityAccesories::getDetalPrice).setHeader("Cena jedn. detal");
        Grid.Column<EntityAccesories> purchasePriceColumn = treeGrid.addColumn(EntityAccesories::getPurchasePrice).setHeader("Cena jedn. zakup");
        Grid.Column<EntityAccesories> allPricePurchaseColumn = treeGrid.addColumn(EntityAccesories::getAllPricePurchase).setHeader("Razem zakup");
        Grid.Column<EntityAccesories> allPriceDetalColumn = treeGrid.addColumn(EntityAccesories::getAllPriceRetail).setHeader("Razem Detal");
        Grid.Column<EntityAccesories> profitColumn = treeGrid.addColumn(EntityAccesories::getProfit).setHeader("Zysk");
        treeGrid.addColumn(createCheckboxes()).setHeader("Opcje");

        binder = new Binder<>(EntityAccesories.class);

        treeGrid.getEditor().setBinder(binder);

        checkbox();

        TextField discountEditField = editField(new StringToIntegerConverter("Błąd"), new StringToDoubleConverter("Błąd"));
        itemClickListener(discountEditField);
        addEnterEvent(discountEditField);
        discountColumn.setEditorComponent(discountEditField);


        closeListener();

        treeGrid.setDataProvider(new TreeDataProvider<>(addItems(new ArrayList())));
        treeGrid.getColumns().forEach(e -> e.setAutoWidth(true));
        treeGrid.setMinHeight("750px");
        return treeGrid;
    }

    @Override
    public void addEnterEvent(TextField textField) {
        textField.getElement()
                .addEventListener("keydown",
                        event -> treeGrid.getEditor().cancel())
                .setFilter("event.key === 'Enter'");
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
                EntityAccesories accesories = binder.getBean();
                accesories.setAllPricePurchase(new BigDecimal(accesories.getQuantity() * accesories.getPurchasePrice() * 70 / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                accesories.setAllPriceRetail(new BigDecimal(accesories.getQuantity() * accesories.getDetalPrice() * (100 - accesories.getDiscount()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                accesories.setProfit(new BigDecimal(accesories.getAllPriceRetail() - accesories.getAllPricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                if (binder.getBean().getDiscount() <= 30) {
                    binder.setBean(accesories);
                } else {
                    accesories.setDiscount(30);
                    getNotificationError("Maksymalny rabat to 30 %");
                    binder.setBean(accesories);
                }
            }
        });
    }

    @Override
    public TreeData<EntityAccesories> addItems(List list) {
        TreeData<EntityAccesories> treeData = new TreeData<>();
        if (set != null) {
            Set<String> parents = new HashSet<>();
            Set<EntityAccesories> duplicates = set.stream().filter(e -> !parents.add(e.getCategory())).collect(Collectors.toSet());
            for (String parent : parents) {
                List<EntityAccesories> childrens = set.stream().filter(e -> e.getCategory().equals(parent)).collect(Collectors.toList());
                for (int i = 0; i < childrens.size(); i++) {
                    if (i == 0) {
                        treeData.addItem(null, childrens.stream().findFirst().get());
                    } else {
                        treeData.addItem(childrens.stream().findFirst().get(), childrens.get(i));
                    }
                }
            }
        }
        return treeData;
    }

    @Override
    public TextField editField(StringToIntegerConverter stringToIntegerConverter, StringToDoubleConverter stringToDoubleConverter) {
        TextField textField = new TextField();
        addEnterEvent(textField);
        binder.forField(textField)
                .withConverter(stringToIntegerConverter)
                .bind(EntityAccesories::getDiscount, EntityAccesories::setDiscount);
        return textField;
    }

    @Override
    public ComponentRenderer<VerticalLayout, EntityAccesories> createCheckboxes() {
        return new ComponentRenderer<>(accesories -> {
            Checkbox mainCheckBox = new Checkbox("Dodać ?");
            mainCheckBox.setValue(accesories.isOffer());
            mainCheckBox.addValueChangeListener(event -> {
                accesories.setOffer(event.getValue());
            });
            return new VerticalLayout(mainCheckBox);
        });
    }

    private List<EntityAccesories> addQuantityToList(List<EntityAccesories> list) {
        for (EntityAccesories accesories : list) {
            accesories.setDiscount(0);
            accesories.setQuantity(value(accesories.getCategory()));
            accesories.setAllPricePurchase(new BigDecimal(accesories.getQuantity() * accesories.getPurchasePrice()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            accesories.setAllPriceRetail(new BigDecimal(accesories.getQuantity() * accesories.getDetalPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            accesories.setProfit(new BigDecimal(accesories.getAllPriceRetail() - accesories.getAllPricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
        return list;
    }

    private Double value(String category) {
        switch (category) {
            case "wspornik":
                return BigDecimal.valueOf(dataTilesRepo.getDlugoscKalenic() / 0.8).setScale(0, RoundingMode.UP).doubleValue();
            case "tasma kalenicowa":
                return BigDecimal.valueOf(dataTilesRepo.getDlugoscKalenic()).setScale(0, RoundingMode.UP).doubleValue();
            case "tasma do obrobki":
                return BigDecimal.valueOf((dataTilesRepo.getObwodKomina() + 1) * 2).setScale(0, RoundingMode.UP).doubleValue();
            case "listwa":
                return BigDecimal.valueOf((dataTilesRepo.getObwodKomina() / 1.95) + 1).setScale(0, RoundingMode.UP).doubleValue();
            case "kosz":
                return BigDecimal.valueOf((dataTilesRepo.getDlugoscKoszy() / 1.95) + 1).setScale(0, RoundingMode.UP).doubleValue();
            case "klamra do mocowania kosza":
                return BigDecimal.valueOf((dataTilesRepo.getDlugoscKoszy() / 2) * 6).setScale(0, RoundingMode.UP).doubleValue();
            case "tasma samorozprezna":
                return BigDecimal.valueOf(dataTilesRepo.getDlugoscKoszy() * 2).setScale(0, RoundingMode.UP).doubleValue();
            case "grzebien":
                return BigDecimal.valueOf(dataTilesRepo.getDlugoscOkapu()).setScale(0, RoundingMode.UP).doubleValue();
            case "pas":
                return BigDecimal.valueOf(dataTilesRepo.getDlugoscKalenic() / 1.95).setScale(0, RoundingMode.UP).doubleValue();
            case "klamra do gasiora":
                return BigDecimal.valueOf(dataTilesRepo.getDlugoscKalenic() * 2.5).setScale(0, RoundingMode.UP).doubleValue();
            case "spinka":
                return BigDecimal.valueOf(dataTilesRepo.getPowierzchniaPolaci() / 50).setScale(0, RoundingMode.UP).doubleValue();
            case "spinka cieta":
                return BigDecimal.valueOf(dataTilesRepo.getDlugoscKoszy() * 0.6).setScale(0, RoundingMode.UP).doubleValue();
            case "membrana":
                return BigDecimal.valueOf(dataTilesRepo.getPowierzchniaPolaci() * 1.1).setScale(0, RoundingMode.UP).doubleValue();
            default:
                return 1d;
        }
    }

    private void checkbox() {
        checkboxGroup.addValueChangeListener(e -> {
            List<EntityAccesories> all = accesoriesRepository.findAll();
            List<EntityAccesories> newValue = all.stream().filter(f -> f.getOption().equals(e.getValue())).collect(Collectors.toList());
            set = new HashSet<>(addQuantityToList(newValue));
            treeGrid.setDataProvider(new TreeDataProvider<>(addItems(new ArrayList<>())));
            treeGrid.getDataProvider().refreshAll();
        });
    }

    private FormLayout createCheckbox() {
        FormLayout formLayout = new FormLayout();
        checkboxGroup.setItems("PODSTAWOWY", "PREMIUM", "LUX");
        formLayout.add(checkboxGroup);
        return formLayout;
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        VaadinSession.getCurrent().getSession().setAttribute("accesories", set);
        action.proceed();
    }
}