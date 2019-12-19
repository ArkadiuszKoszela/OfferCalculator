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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Route(value = LightningProtectionSystemView.PROTECTION_SYSTEM, layout = MainView.class)
public class LightningProtectionSystemView extends VerticalLayout implements GridInteraface<LightningProtectionSystem>, BeforeLeaveObserver {

    static final String PROTECTION_SYSTEM = "protection_system";
    private TreeGrid<LightningProtectionSystem> treeGrid = new TreeGrid<>();
    private Optional<List<Gutter>> optionalGutters = Optional.ofNullable((List<Gutter>) VaadinSession.getCurrent().getSession().getAttribute("gutter"));
    private List<Gutter> list = optionalGutters.orElse(new ArrayList<>());
    private Optional<List<LightningProtectionSystem>> optionalLightningProtectionSystems = Optional.ofNullable((List<LightningProtectionSystem>) VaadinSession.getCurrent().getSession().getAttribute("lightningProtectionSystem"));
    private List<LightningProtectionSystem> listLightningProtectionSystem = optionalLightningProtectionSystems.orElse(new ArrayList<>());
    private Binder<LightningProtectionSystem> binder;

    private LightningProtectionSystemRepository lightningProtectionSystemRepository;

    @Autowired
    public LightningProtectionSystemView(LightningProtectionSystemRepository lightningProtectionSystemRepository) {
        this.lightningProtectionSystemRepository = Objects.requireNonNull(lightningProtectionSystemRepository);

        listLightningProtectionSystem = allLightningSystem();
        add(createGrida());
    }

    public TreeGrid<LightningProtectionSystem> createGrida() {
        treeGrid.addHierarchyColumn(LightningProtectionSystem::getName).setResizable(true).setHeader("Nazwa");
        GridInteraface.super.createGridd(treeGrid, binder);
        treeGrid.setDataProvider(new TreeDataProvider<>(addItems(list)));
        return treeGrid;
    }

    public TreeData<LightningProtectionSystem> addItems(List list) {
        TreeData<LightningProtectionSystem> treeData = new TreeData<>();
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
        return treeData;
    }

    private List<LightningProtectionSystem> allLightningSystem() {
        return lightningProtectionSystemRepository.findAll();
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        VaadinSession.getCurrent().getSession().setAttribute("gutter", list);
        action.proceed();
    }
}