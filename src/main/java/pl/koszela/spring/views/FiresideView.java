package pl.koszela.spring.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.main.Accessories;
import pl.koszela.spring.entities.main.Fireside;
import pl.koszela.spring.entities.main.FiresideDTO;
import pl.koszela.spring.entities.main.Windows;
import pl.koszela.spring.repositories.FiresideRepository;
import pl.koszela.spring.service.GridInteraface;

import java.util.*;
import java.util.stream.Collectors;

@PreserveOnRefresh
@Route(value = FiresideView.FIRESIDE, layout = MainView.class)
public class FiresideView extends VerticalLayout implements GridInteraface<FiresideDTO>, BeforeLeaveObserver {
    static final String FIRESIDE = "fireside";

    private FiresideRepository firesideRepository;

    private Grid<FiresideDTO> grid = new Grid<>();
    private List<Fireside> listFireside = (List<Fireside>) VaadinSession.getCurrent().getSession().getAttribute("fireside");
    private Set<Windows> setWindows = (Set<Windows>) VaadinSession.getCurrent().getSession().getAttribute("windowsAfterChoose");
    private Binder<FiresideDTO> binder;
    private List<FiresideDTO> firesideInGrid = new ArrayList<>();

    private Button button = new Button("Dodaj do tabeli");
    private ComboBox<String> comboBoxManufacturer = new ComboBox<>();
    private ComboBox<String> comboBoxCategory = new ComboBox<>();
    private ComboBox<String> comboBoxSize = new ComboBox<>();
    private ComboBox<Fireside> comboBoxSearch = new ComboBox<>();


    @Autowired
    public FiresideView(FiresideRepository firesideRepository) {
        this.firesideRepository = Objects.requireNonNull(firesideRepository);

        add(addLayout());
        add(createGrid());
    }

    private FormLayout addLayout() {
        FormLayout formLayout = new FormLayout();
        FormLayout.ResponsiveStep responsiveStep = new FormLayout.ResponsiveStep("5px", 3);
        formLayout.setResponsiveSteps(responsiveStep);
        formLayout.add(createComboBoxManufacturer(), createComboBoxCategory(), createComboBoxSize(), createComboBoxSearch(), button);
        return formLayout;
    }

    public Grid createGrid() {
        Grid.Column<FiresideDTO> nameColumn = grid.addColumn(FiresideDTO::getName).setHeader("Nazwa");
        grid.addColumn(FiresideDTO::getCategory).setHeader("Kategoria");
        grid.addColumn(FiresideDTO::getManufacturer).setHeader("Producent");
        Grid.Column<FiresideDTO> quantityColumn = grid.addColumn(FiresideDTO::getQuantity).setHeader("Ilość");
        Grid.Column<FiresideDTO> discountColumn = grid.addColumn(FiresideDTO::getDiscount).setHeader("Rabat");
        Grid.Column<FiresideDTO> detalPriceColumn = grid.addColumn(FiresideDTO::getUnitDetalPrice).setHeader("Cena jedn. detal");
        Grid.Column<FiresideDTO> purchasePriceColumn = grid.addColumn(FiresideDTO::getUnitPurchasePrice).setHeader("Cena jedn. zakup");
        Grid.Column<FiresideDTO> allPriceDetalColumn = grid.addColumn(FiresideDTO::getAllpriceAfterDiscount).setHeader("Razem Detal");
        Grid.Column<FiresideDTO> allPricePurchaseColumn = grid.addColumn(FiresideDTO::getAllpricePurchase).setHeader("Razem zakup");
        Grid.Column<FiresideDTO> profitColumn = grid.addColumn(FiresideDTO::getAllprofit).setHeader("Zysk");
        grid.addColumn(createComponent()).setHeader("Opcje");

        binder = new Binder<>(FiresideDTO.class);

        grid.getEditor().setBinder(binder);

        TextField discountEditField = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), FiresideDTO::getDiscount, FiresideDTO::setDiscount);
        itemClickListener(grid, discountEditField);
        discountColumn.setEditorComponent(discountEditField);

        TextField quantityField = bindTextFieldToDouble(binder, new StringToDoubleConverter("Błąd"), FiresideDTO::getQuantity, FiresideDTO::setQuantity);
        itemClickListener(grid, quantityField);
        quantityColumn.setEditorComponent(quantityField);

        closeListener(grid, binder);

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        button.addClickListener(buttonClickEvent -> {
            FiresideDTO firesideDTO = change(comboBoxSearch.getValue());
            firesideInGrid.add(firesideDTO);
            grid.setDataProvider(new ListDataProvider<>(firesideInGrid));
            grid.getDataProvider().refreshAll();
        });

        settingsGrid(grid);
        return grid;
    }

    private FiresideDTO change(Fireside fireside) {
        FiresideDTO firesideDTO = new FiresideDTO();
        BeanUtils.copyProperties(fireside, firesideDTO);
        return firesideDTO;
    }

    @Override
    public TreeData<Accessories> addItems(List list) {
        return null;
    }

    @Override
    public ComponentRenderer<VerticalLayout, FiresideDTO> createComponent() {
        return new ComponentRenderer<>(firesideDTO -> {
            Checkbox mainCheckBox = new Checkbox("Dodać ?");
            mainCheckBox.setValue(firesideDTO.isOffer());
            mainCheckBox.addValueChangeListener(event -> {
                firesideDTO.setOffer(event.getValue());
            });
            return new VerticalLayout(mainCheckBox);
        });
    }

    private ComboBox<String> createComboBoxManufacturer() {
        Set<String> collect = firesideRepository.findAll().stream().map(Fireside::getManufacturer).collect(Collectors.toSet());
        comboBoxManufacturer.setItems(collect);
        return comboBoxManufacturer;
    }

    private ComboBox<String> createComboBoxCategory() {
        Set<String> collect = firesideRepository.findAll().stream().map(Fireside::getCategory).collect(Collectors.toSet());
        comboBoxCategory.setItems(collect);
        return comboBoxCategory;
    }

    private ComboBox<String> createComboBoxSize() {
        List<Fireside> firesideRepositoryAll = firesideRepository.findAll();
        Set<String> sizes = new HashSet<>();
        for (Fireside fireside : firesideRepositoryAll) {
            int i = fireside.getName().lastIndexOf("/");
            sizes.add(fireside.getName().substring(i - 2, i + 3));
        }
        comboBoxSize.addValueChangeListener(e -> {
            Set<Fireside> search = firesideRepositoryAll.stream().filter(fireside -> fireside.getManufacturer().equals(comboBoxManufacturer.getValue())
                    && fireside.getCategory().equals(comboBoxCategory.getValue())
                    && fireside.getName().contains(comboBoxSize.getValue())).collect(Collectors.toSet());
            comboBoxSearch.setItems(search);
        });
        comboBoxSize.setItems(sizes);
        return comboBoxSize;
    }

    private ComboBox<Fireside> createComboBoxSearch() {
        comboBoxSearch.setItemLabelGenerator(Fireside::getName);
        return comboBoxSearch;
    }

    private List<Fireside> makeDTOtoEntity(List<FiresideDTO> list) {
        listFireside = new ArrayList<>();
        for (FiresideDTO firesideDTO : list) {
            Fireside fireside = convertDTOtoEntity(firesideDTO);
            listFireside.add(fireside);
        }
        return listFireside;
    }

    private Fireside convertDTOtoEntity(FiresideDTO firesideDTO) {
        Fireside fireside = new Fireside();
        BeanUtils.copyProperties(firesideDTO, fireside, "id");
        return fireside;
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        VaadinSession.getCurrent().getSession().setAttribute("fireside", makeDTOtoEntity(firesideInGrid));
        action.proceed();
    }
}