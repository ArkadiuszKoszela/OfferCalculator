package pl.koszela.spring.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import pl.koszela.spring.entities.main.Gutter;
import pl.koszela.spring.service.GridInteraface;
import pl.koszela.spring.service.NotificationInterface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Route(value = GutterView.GUTTER_VIEW, layout = MainView.class)
public class GutterView extends VerticalLayout implements GridInteraface<Gutter>, BeforeLeaveObserver, BeforeEnterObserver {

    public static final String GUTTER_VIEW = "gutter";
    private TreeGrid<Gutter> treeGrid = new TreeGrid<>();
    private Optional<List<Gutter>> optionalGutters = Optional.ofNullable((List<Gutter>) VaadinSession.getCurrent().getSession().getAttribute("gutter"));
    private List<Gutter> gutterList = optionalGutters.orElse(new ArrayList<>());
    private Binder<Gutter> binder;

    public GutterView() {
        addPriceToList(gutterList);
        add(checkboxGroupType());
        add(createGrida());
    }

    public TreeGrid<Gutter> createGrida() {
        treeGrid.addHierarchyColumn(Gutter::getName).setResizable(true).setHeader("Nazwa");
        GridInteraface.super.createGridd(treeGrid, binder);
        treeGrid.setDataProvider(new TreeDataProvider<>(addItems(gutterList)));
        return treeGrid;
    }

    public TreeData<Gutter> addItems(List<Gutter> list) {
        TreeData<Gutter> treeData = new TreeData<>();
        List<Gutter> parents = list.stream().filter(gutter -> gutter.getName().equals("rynna 3mb")).collect(Collectors.toList());
        for (Gutter parent : parents) {
            List<Gutter> childrens = list.stream().filter(gutter -> gutter.getManufacturer().equals(parent.getManufacturer()) && !gutter.getUnitDetalPrice().equals(0d)).collect(Collectors.toList());
            for (int i = 0; i < childrens.size(); i++) {
                if (i == 0) {
                    treeData.addItem(null, parent);
                } else if (!childrens.get(i).getName().equals("rynna 3mb")) {
                    treeData.addItem(parent, childrens.get(i));
                }
            }
        }
        return treeData;
    }

    private void addPriceToList(List<Gutter> list) {
        if (list == null) {
            UI.getCurrent().navigate(IncludeDataView.class);
            NotificationInterface.notificationOpen("Na początku proszę wprowadzić dane !!", NotificationVariant.LUMO_ERROR);
        } else {
            for (Gutter gutter : list) {
                gutter.setUnitPurchasePrice(new BigDecimal(gutter.getUnitDetalPrice() * 0.7).setScale(2, RoundingMode.HALF_UP).doubleValue());
                gutter.setAllpriceAfterDiscount(new BigDecimal(gutter.getQuantity() * gutter.getUnitDetalPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                gutter.setAllpricePurchase(new BigDecimal(gutter.getQuantity() * gutter.getUnitPurchasePrice()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                gutter.setAllprofit(new BigDecimal(gutter.getAllpriceAfterDiscount() - gutter.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
        }
    }

    @Override
    public ComponentRenderer<VerticalLayout, Gutter> createComponent() {
        return new ComponentRenderer<>(gutter -> {
            Checkbox mainCheckBox = new Checkbox("Główna");
            Checkbox optionCheckbox = new Checkbox("Opcjonalna");
            mainCheckBox.setValue(gutter.isMain());
            optionCheckbox.setValue(gutter.isOption());
            mainCheckBox.addValueChangeListener(event -> {
                gutter.setMain(event.getValue());
                gutter.setOption(!event.getValue());
                optionCheckbox.setValue(!mainCheckBox.getValue());
            });
            optionCheckbox.addValueChangeListener(event -> {
                gutter.setMain(!event.getValue());
                gutter.setOption(event.getValue());
                mainCheckBox.setValue(!optionCheckbox.getValue());
            });
            return new VerticalLayout(mainCheckBox, optionCheckbox);
        });
    }

    private RadioButtonGroup<String> checkboxGroupType() {
        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems("Stalowa", "PCV", "Tytan-cynk", "Ocynk", "Wszystko");
        radioButtonGroup.addValueChangeListener(e -> {
            String value = e.getValue();
            switch (value) {
                case "Stalowa": {
                    List<Gutter> searchCategory = gutterList.stream().filter(gutter -> gutter.getCategory().contains("Flamingo")).collect(Collectors.toList());
                    treeGrid.setDataProvider(new TreeDataProvider<>(addItems(searchCategory)));
                    treeGrid.getDataProvider().refreshAll();
                    break;
                }
                case "PCV": {
                    List<Gutter> searchCategory = gutterList.stream().filter(gutter -> gutter.getCategory().contains("Bryza")).collect(Collectors.toList());
                    treeGrid.setDataProvider(new TreeDataProvider<>(addItems(searchCategory)));
                    treeGrid.getDataProvider().refreshAll();
                    break;
                }
                default:
                    treeGrid.setDataProvider(new TreeDataProvider<>(addItems(gutterList)));
                    treeGrid.getDataProvider().refreshAll();
                    break;
            }
        });
        return radioButtonGroup;
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        VaadinSession.getCurrent().getSession().setAttribute("gutter", gutterList);
        action.proceed();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (gutterList == null) {
            event.rerouteTo(IncludeDataView.class);
        }
    }
}