package pl.koszela.spring.views;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.main.Gutter;
import pl.koszela.spring.entities.main.LightningProtectionSystem;
import pl.koszela.spring.repositories.LightningProtectionSystemRepository;
import pl.koszela.spring.service.GridInteraface;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Route(value = LightningProtectionSystemView.PROTECTION_SYSTEM, layout = MainView.class)
public class LightningProtectionSystemView extends VerticalLayout implements GridInteraface<LightningProtectionSystem>, BeforeLeaveObserver {

    static final String PROTECTION_SYSTEM = "protection_system";
    private TreeGrid<LightningProtectionSystem> treeGrid = new TreeGrid<>();
    private List<Gutter> list = (List<Gutter>) VaadinSession.getCurrent().getSession().getAttribute("gutter");
    private List<LightningProtectionSystem> listLightningProtectionSystem = (List<LightningProtectionSystem>) VaadinSession.getCurrent().getSession().getAttribute("lightningProtectionSystem");
    private Binder<LightningProtectionSystem> binder;

    private LightningProtectionSystemRepository lightningProtectionSystemRepository;

    @Autowired
    public LightningProtectionSystemView(LightningProtectionSystemRepository lightningProtectionSystemRepository) {
        this.lightningProtectionSystemRepository = Objects.requireNonNull(lightningProtectionSystemRepository);

        if(listLightningProtectionSystem == null){
            listLightningProtectionSystem = allLightningSystem();
        }
        add(createGrid());
    }

    public TreeGrid createGrid() {
        Column<LightningProtectionSystem> nameColumn = treeGrid.addHierarchyColumn(LightningProtectionSystem::getName).setResizable(true).setHeader("Nazwa");
        Column<LightningProtectionSystem> categoryColumn = treeGrid.addColumn(LightningProtectionSystem::getCategory).setHeader("Kategoria");
        Column<LightningProtectionSystem> quantityColumn = treeGrid.addColumn(LightningProtectionSystem::getQuantity).setHeader("Ilość");
        Column<LightningProtectionSystem> discountColumn = treeGrid.addColumn(LightningProtectionSystem::getDiscount).setHeader("Rabat");
        Column<LightningProtectionSystem> unitPurchaseColumn = treeGrid.addColumn(LightningProtectionSystem::getUnitPurchasePrice).setHeader("Cena jedn. zakup");
        Column<LightningProtectionSystem> unitDetalColumn = treeGrid.addColumn(LightningProtectionSystem::getUnitDetalPrice).setHeader("Cena jedn. detal");
        Column<LightningProtectionSystem> allPurchaseColumn = treeGrid.addColumn(LightningProtectionSystem::getAllpricePurchase).setHeader("Razem cena netto");
        Column<LightningProtectionSystem> allDetalColumn = treeGrid.addColumn(LightningProtectionSystem::getAllpriceAfterDiscount).setHeader("Razem cena detal");
        Column<LightningProtectionSystem> protitColumn = treeGrid.addColumn(LightningProtectionSystem::getAllprofit).setHeader("Zysk");
        treeGrid.addColumn(createComponent()).setHeader("Opcje");

        binder = new Binder<>(LightningProtectionSystem.class);
        treeGrid.getEditor().setBinder(binder);

        TextField discountField= bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), LightningProtectionSystem::getDiscount, LightningProtectionSystem::setDiscount);
        itemClickListener(treeGrid, discountField);
        discountColumn.setEditorComponent(discountField);

        TextField quantityField = bindTextFieldToDouble(binder, new StringToDoubleConverter("Błąd"), LightningProtectionSystem::getQuantity, LightningProtectionSystem::setQuantity);
        itemClickListener(treeGrid, quantityField);
        quantityColumn.setEditorComponent(quantityField);

        FooterRow footerRow = treeGrid.appendFooterRow();

        closeListener(treeGrid, binder);
        treeGrid.setDataProvider(new TreeDataProvider<>(addItems(list)));

        settingsGrid(treeGrid);
        return treeGrid;
    }

    @Override
    public TreeData<LightningProtectionSystem> addItems(List list) {
        TreeData<LightningProtectionSystem> treeData = new TreeData<>();
        if (listLightningProtectionSystem != null) {
            List<LightningProtectionSystem> parents = listLightningProtectionSystem.stream().filter(gutter -> gutter.getName().equals("Złącze krzyżowe")).collect(Collectors.toList());
            for (LightningProtectionSystem parent : parents) {
                List<LightningProtectionSystem> childrens = listLightningProtectionSystem.stream().filter(gutter -> gutter.getCategory().equals(parent.getCategory())).collect(Collectors.toList());
                for (int i = 0; i < childrens.size(); i++) {
                    if (i == 0) {
                        treeData.addItem(null, parent);
                    } else if (!childrens.get(i).getName().equals("Złącze krzyżowe")) {
                        treeData.addItem(parent, childrens.get(i));
                    }
                }
            }
        }
        return treeData;
    }

    private List<LightningProtectionSystem> allLightningSystem(){
        return lightningProtectionSystemRepository.findAll();
    }

    @Override
    public ComponentRenderer<VerticalLayout, LightningProtectionSystem> createComponent() {
        return new ComponentRenderer<>(lightningProtectionSystem -> {
            Checkbox mainCheckBox = new Checkbox("Dodać ?");
            mainCheckBox.setValue(lightningProtectionSystem.isOffer());
            mainCheckBox.addValueChangeListener(event -> {
                lightningProtectionSystem.setOffer(event.getValue());
            });
            return new VerticalLayout(mainCheckBox);
        });
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        VaadinSession.getCurrent().getSession().setAttribute("gutter", list);
        action.proceed();
    }
}