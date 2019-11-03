package pl.koszela.spring.views;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
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
import pl.koszela.spring.entities.EntityAccesories;
import pl.koszela.spring.entities.InputData;
import pl.koszela.spring.repositories.AccesoriesRepository;
import pl.koszela.spring.service.GridInteraface;
import pl.koszela.spring.service.NameNumberFields;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static pl.koszela.spring.service.ServiceNotification.getNotificationError;

@Route(value = AccesoriesView.SELECT_ACCESORIES, layout = MainView.class)
public class AccesoriesView extends VerticalLayout implements GridInteraface, BeforeLeaveObserver {
    static final String SELECT_ACCESORIES = "accesories";

    private TreeGrid<EntityAccesories> treeGrid = new TreeGrid<>();
    private List<InputData> setInput = (List<InputData>) VaadinSession.getCurrent().getSession().getAttribute("inputData");
    private Set<EntityAccesories> set = (Set<EntityAccesories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");
    private AccesoriesRepository accesoriesRepository;
    private Binder<EntityAccesories> binder;
    private RadioButtonGroup<String> checkboxGroup = new RadioButtonGroup<>();

    public AccesoriesView(AccesoriesRepository accesoriesRepository) {
        this.accesoriesRepository = accesoriesRepository;
        if (set == null) {
            List<EntityAccesories> all = accesoriesRepository.findAll();
            List<EntityAccesories> newValue = all.stream().filter(f -> f.getOption().equals("PODSTAWOWY")).collect(Collectors.toList());
            set = new HashSet<>(addQuantityToList(newValue));
        }
        add(createCheckbox());
        add(createGrid());
    }

    @Override
    public TreeGrid createGrid() {
        Grid.Column<EntityAccesories> nameColumn = treeGrid.addHierarchyColumn(EntityAccesories::getName).setHeader("Nazwa");
        Grid.Column<EntityAccesories> quantityColumn = treeGrid.addColumn(EntityAccesories::getQuantity).setHeader("Ilość");
        Grid.Column<EntityAccesories> discountColumn = treeGrid.addColumn(EntityAccesories::getDiscount).setHeader("Rabat");
        Grid.Column<EntityAccesories> detalPriceColumn = treeGrid.addColumn(EntityAccesories::getUnitDetalPrice).setHeader("Cena jedn. detal");
        Grid.Column<EntityAccesories> purchasePriceColumn = treeGrid.addColumn(EntityAccesories::getUnitPurchasePrice).setHeader("Cena jedn. zakup");
        Grid.Column<EntityAccesories> allPricePurchaseColumn = treeGrid.addColumn(EntityAccesories::getAllpricePurchase).setHeader("Razem zakup");
        Grid.Column<EntityAccesories> allPriceDetalColumn = treeGrid.addColumn(EntityAccesories::getAllpriceAfterDiscount).setHeader("Razem Detal");
        Grid.Column<EntityAccesories> profitColumn = treeGrid.addColumn(EntityAccesories::getAllprofit).setHeader("Zysk");
        treeGrid.addColumn(createCheckboxes()).setHeader("Opcje");

        binder = new Binder<>(EntityAccesories.class);

        treeGrid.getEditor().setBinder(binder);

        checkbox();

        TextField discountEditField = new TextField();
        addEnterEvent(treeGrid, discountEditField);
        binder.forField(discountEditField)
                .withConverter(new StringToIntegerConverter("Błąd"))
                .bind(EntityAccesories::getDiscount, EntityAccesories::setDiscount);
        itemClickListener(discountEditField);
        discountColumn.setEditorComponent(discountEditField);

        TextField quantityField = new TextField();
        binder.forField(quantityField)
                .withConverter(new StringToDoubleConverter("Błąd"))
                .bind(EntityAccesories::getQuantity, EntityAccesories::setQuantity);
        addEnterEvent(treeGrid, quantityField);
        itemClickListener(quantityField);

        quantityColumn.setEditorComponent(quantityField);

        closeListener();

        treeGrid.setDataProvider(new TreeDataProvider<>(addItems(new ArrayList())));
        treeGrid.getColumns().forEach(e -> e.setAutoWidth(true));
        treeGrid.setMinHeight("750px");
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
                EntityAccesories accesories = binder.getBean();
                accesories.setAllpricePurchase(new BigDecimal(accesories.getQuantity() * accesories.getUnitPurchasePrice() * 70 / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                accesories.setAllpriceAfterDiscount(new BigDecimal(accesories.getQuantity() * accesories.getUnitDetalPrice() * (100 - accesories.getDiscount()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                accesories.setAllprofit(new BigDecimal(accesories.getAllpriceAfterDiscount() - accesories.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
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
        return null;
    }

    public ComponentRenderer<Span, EntityAccesories> booleanRenderer =
            new ComponentRenderer<>(accesories ->
                    new Span(accesories.isOffer() ? "Tak" : "Nie"));

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
            accesories.setOffer(false);
            accesories.setDiscount(0);
            accesories.setQuantity(value(accesories.getCategory()));
            accesories.setAllpricePurchase(new BigDecimal(accesories.getQuantity() * accesories.getUnitPurchasePrice()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            accesories.setAllpriceAfterDiscount(new BigDecimal(accesories.getQuantity() * accesories.getUnitDetalPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            accesories.setAllprofit(new BigDecimal(accesories.getAllpriceAfterDiscount() - accesories.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
        return list;
    }

    private Double value(String category) {
        Optional<InputData> dlugoscKalenic = setInput.stream().filter(e -> e.getName().equals(NameNumberFields.DLUGOSC_KALENIC.toString())).findFirst();
        Optional<InputData> obwodOkapu = setInput.stream().filter(e -> e.getName().equals(NameNumberFields.OBWOD_OKAPU.toString())).findFirst();
        Optional<InputData> dlugoscOkapu = setInput.stream().filter(e -> e.getName().equals(NameNumberFields.DLUGOSC_OKAPU.toString())).findFirst();
        Optional<InputData> dlugoscKoszy = setInput.stream().filter(e -> e.getName().equals(NameNumberFields.DLUGOSC_KOSZY.toString())).findFirst();
        Optional<InputData> powierzchniaPolaci = setInput.stream().filter(e -> e.getName().equals(NameNumberFields.POWIERZCHNIA_POLACI.toString())).findFirst();
        switch (category) {
            case "wspornik":
                return BigDecimal.valueOf(dlugoscKalenic.get().getValue() / 0.8).setScale(0, RoundingMode.UP).doubleValue();
            case "tasma kalenicowa":
                return BigDecimal.valueOf(dlugoscKalenic.get().getValue()).setScale(0, RoundingMode.UP).doubleValue();
            case "tasma do obrobki":
                return BigDecimal.valueOf((obwodOkapu.get().getValue() + 1) * 2).setScale(0, RoundingMode.UP).doubleValue();
            case "listwa":
                return BigDecimal.valueOf((obwodOkapu.get().getValue() / 1.95) + 1).setScale(0, RoundingMode.UP).doubleValue();
            case "kosz":
                return BigDecimal.valueOf((dlugoscKoszy.get().getValue() / 1.95) + 1).setScale(0, RoundingMode.UP).doubleValue();
            case "klamra do mocowania kosza":
                return BigDecimal.valueOf((dlugoscKoszy.get().getValue() / 2) * 6).setScale(0, RoundingMode.UP).doubleValue();
            case "tasma samorozprezna":
                return BigDecimal.valueOf(dlugoscKoszy.get().getValue() * 2).setScale(0, RoundingMode.UP).doubleValue();
            case "grzebien":
                return BigDecimal.valueOf(dlugoscOkapu.get().getValue()).setScale(0, RoundingMode.UP).doubleValue();
            case "pas":
                return BigDecimal.valueOf(dlugoscKalenic.get().getValue() / 1.95).setScale(0, RoundingMode.UP).doubleValue();
            case "klamra do gasiora":
                return BigDecimal.valueOf(dlugoscKalenic.get().getValue() * 2.5).setScale(0, RoundingMode.UP).doubleValue();
            case "spinka":
                return BigDecimal.valueOf(powierzchniaPolaci.get().getValue() / 50).setScale(0, RoundingMode.UP).doubleValue();
            case "spinka cieta":
                return BigDecimal.valueOf(dlugoscKoszy.get().getValue() * 0.6).setScale(0, RoundingMode.UP).doubleValue();
            case "membrana":
                return BigDecimal.valueOf(powierzchniaPolaci.get().getValue() * 1.1).setScale(0, RoundingMode.UP).doubleValue();
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