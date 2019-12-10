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
import org.springframework.transaction.annotation.Transactional;
import pl.koszela.spring.entities.main.Accessories;
import pl.koszela.spring.entities.main.InputData;
import pl.koszela.spring.repositories.AccesoriesRepository;
import pl.koszela.spring.service.GridInteraface;
import pl.koszela.spring.staticField.TitleNumberFields;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Route(value = AccesoriesView.SELECT_ACCESORIES, layout = MainView.class)
public class AccesoriesView extends VerticalLayout implements GridInteraface<Accessories>, BeforeLeaveObserver {
    static final String SELECT_ACCESORIES = "accesories";

    private TreeGrid<Accessories> treeGrid = new TreeGrid<>();
    private List<InputData> setInput = (List<InputData>) VaadinSession.getCurrent().getSession().getAttribute("inputData");
    private Set<Accessories> set = (Set<Accessories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");
    private AccesoriesRepository accesoriesRepository;
    private Binder<Accessories> binder;
    private RadioButtonGroup<String> checkboxGroup = new RadioButtonGroup<>();

    public AccesoriesView(AccesoriesRepository accesoriesRepository) {
        this.accesoriesRepository = accesoriesRepository;
        add(createCheckbox());
        add(createGrid());
    }

    public Grid createGrid() {
        Grid.Column<Accessories> nameColumn = treeGrid.addHierarchyColumn(Accessories::getName).setHeader("Nazwa");
        Grid.Column<Accessories> quantityColumn = treeGrid.addColumn(Accessories::getQuantity).setHeader("Ilość");
        Grid.Column<Accessories> discountColumn = treeGrid.addColumn(Accessories::getDiscount).setHeader("Rabat");
        Grid.Column<Accessories> detalPriceColumn = treeGrid.addColumn(Accessories::getUnitDetalPrice).setHeader("Cena jedn. detal");
        Grid.Column<Accessories> purchasePriceColumn = treeGrid.addColumn(Accessories::getUnitPurchasePrice).setHeader("Cena jedn. zakup");
        Grid.Column<Accessories> allPricePurchaseColumn = treeGrid.addColumn(Accessories::getAllpricePurchase).setHeader("Razem zakup");
        Grid.Column<Accessories> allPriceDetalColumn = treeGrid.addColumn(Accessories::getAllpriceAfterDiscount).setHeader("Razem Detal");
        Grid.Column<Accessories> profitColumn = treeGrid.addColumn(Accessories::getAllprofit).setHeader("Zysk");
        treeGrid.addColumn(createComponent()).setHeader("Opcje");

        binder = new Binder<>(Accessories.class);
        treeGrid.getEditor().setBinder(binder);

        checkbox();

        TextField discountEditField= bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), Accessories::getDiscount, Accessories::setDiscount);
        itemClickListener(treeGrid, discountEditField);
        discountColumn.setEditorComponent(discountEditField);

        TextField quantityField = bindTextFieldToDouble(binder, new StringToDoubleConverter("Błąd"), Accessories::getQuantity, Accessories::setQuantity);
        itemClickListener(treeGrid, quantityField);
        quantityColumn.setEditorComponent(quantityField);

        closeListener(treeGrid, binder);

        settingsGrid(treeGrid);
        return treeGrid;
    }

    @Override
    public TreeData<Accessories> addItems(List list) {
        TreeData<Accessories> treeData = new TreeData<>();
        if (set != null) {
            Set<String> parents = set.stream().map(Accessories::getCategory).collect(Collectors.toSet());
            for (String parent : parents) {
                List<Accessories> kids = set.stream().filter(e -> e.getCategory().equals(parent)).collect(Collectors.toList());
                for (int i = 0; i < kids.size(); i++) {
                    if (i == 0) {
                        treeData.addItem(null, kids.stream().findFirst().get());
                    } else {
                        treeData.addItem(kids.stream().findFirst().get(), kids.get(i));
                    }
                }
            }
        }
        return treeData;
    }

    @Override
    public ComponentRenderer<VerticalLayout, Accessories> createComponent() {
        return new ComponentRenderer<>(accesories -> {
            Checkbox mainCheckBox = new Checkbox("Dodać ?");
            mainCheckBox.setValue(accesories.isOffer());
            mainCheckBox.addValueChangeListener(event -> {
                accesories.setOffer(event.getValue());
            });
            return new VerticalLayout(mainCheckBox);
        });
    }

    private List<Accessories> addQuantityToList(List<Accessories> list) {
        for (Accessories accessories : list) {
            accessories.setOffer(false);
            accessories.setDiscount(0);
            accessories.setQuantity(value(accessories.getCategory()));
            accessories.setAllpricePurchase(new BigDecimal(accessories.getQuantity() * accessories.getUnitPurchasePrice()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            accessories.setAllpriceAfterDiscount(new BigDecimal(accessories.getQuantity() * accessories.getUnitDetalPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            accessories.setAllprofit(new BigDecimal(accessories.getAllpriceAfterDiscount() - accessories.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
        return list;
    }

    private Double value(String category) {
        Optional<InputData> dlugoscKalenic = setInput.stream().filter(e -> e.getName().equals(TitleNumberFields.DLUGOSC_KALENIC.toString())).findFirst();
        Optional<InputData> obwodOkapu = setInput.stream().filter(e -> e.getName().equals(TitleNumberFields.OBWOD_OKAPU.toString())).findFirst();
        Optional<InputData> dlugoscOkapu = setInput.stream().filter(e -> e.getName().equals(TitleNumberFields.DLUGOSC_OKAPU.toString())).findFirst();
        Optional<InputData> dlugoscKoszy = setInput.stream().filter(e -> e.getName().equals(TitleNumberFields.DLUGOSC_KOSZY.toString())).findFirst();
        Optional<InputData> powierzchniaPolaci = setInput.stream().filter(e -> e.getName().equals(TitleNumberFields.POWIERZCHNIA_POLACI.toString())).findFirst();
        switch (category) {
            case "wspornik":
                if(dlugoscKalenic.isPresent())
                return BigDecimal.valueOf(dlugoscKalenic.get().getValue() / 0.8).setScale(0, RoundingMode.UP).doubleValue();
            case "tasma kalenicowa":
                if(dlugoscKalenic.isPresent())
                return BigDecimal.valueOf(dlugoscKalenic.get().getValue()).setScale(0, RoundingMode.UP).doubleValue();
            case "tasma do obrobki":
                if(obwodOkapu.isPresent())
                return BigDecimal.valueOf((obwodOkapu.get().getValue() + 1) * 2).setScale(0, RoundingMode.UP).doubleValue();
            case "listwa":
                if(obwodOkapu.isPresent())
                return BigDecimal.valueOf((obwodOkapu.get().getValue() / 1.95) + 1).setScale(0, RoundingMode.UP).doubleValue();
            case "kosz":
                if(dlugoscKoszy.isPresent())
                return BigDecimal.valueOf((dlugoscKoszy.get().getValue() / 1.95) + 1).setScale(0, RoundingMode.UP).doubleValue();
            case "klamra do mocowania kosza":
                if(dlugoscKoszy.isPresent())
                return BigDecimal.valueOf((dlugoscKoszy.get().getValue() / 2) * 6).setScale(0, RoundingMode.UP).doubleValue();
            case "tasma samorozprezna":
                if(dlugoscKoszy.isPresent())
                return BigDecimal.valueOf(dlugoscKoszy.get().getValue() * 2).setScale(0, RoundingMode.UP).doubleValue();
            case "grzebien":
                if(dlugoscOkapu.isPresent())
                return BigDecimal.valueOf(dlugoscOkapu.get().getValue()).setScale(0, RoundingMode.UP).doubleValue();
            case "pas":
                if(dlugoscKalenic.isPresent())
                return BigDecimal.valueOf(dlugoscKalenic.get().getValue() / 1.9).setScale(0, RoundingMode.UP).doubleValue();
            case "klamra do gasiora":
                if(dlugoscKalenic.isPresent())
                return BigDecimal.valueOf(dlugoscKalenic.get().getValue() * 2.5).setScale(0, RoundingMode.UP).doubleValue();
            case "spinka":
                if(powierzchniaPolaci.isPresent())
                return BigDecimal.valueOf(powierzchniaPolaci.get().getValue() / 50).setScale(0, RoundingMode.UP).doubleValue();
            case "spinka cieta":
                if(dlugoscKoszy.isPresent())
                return BigDecimal.valueOf(dlugoscKoszy.get().getValue() * 0.6).setScale(0, RoundingMode.UP).doubleValue();
            case "membrana":
                if(powierzchniaPolaci.isPresent())
                return BigDecimal.valueOf(powierzchniaPolaci.get().getValue() * 1.1).setScale(0, RoundingMode.UP).doubleValue();
            default:
                return 0d;
        }
    }
    @Transactional("mainTransactionManager")
    void checkbox() {
        checkboxGroup.addValueChangeListener(e -> {
            List<Accessories> all = accesoriesRepository.findAll();
            List<Accessories> newValue = all.stream().filter(f -> f.getType().equals(e.getValue())).collect(Collectors.toList());
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
