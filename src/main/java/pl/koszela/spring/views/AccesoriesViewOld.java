package pl.koszela.spring.views;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.accesories.EntityAccesories;
import pl.koszela.spring.entities.tiles.EntityInputDataTiles;
import pl.koszela.spring.repositories.AccesoriesRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

//@Route(value = AccesoriesViewOld.SELECT_ACCESORIES, layout = MainView.class)
public class AccesoriesViewOld extends VerticalLayout implements BeforeLeaveObserver {

//    static final String SELECT_ACCESORIES = "accesories";
    private AccesoriesRepository accesoriesRepository;

    private static final String NAZWA = "Nazwa";
    private static final String WYBIERZ = "Wybierz";
    private static final String ILOSC = "Ilość";
    private static final String CENA_ZAKUPU = "Cena zakupu";
    private static final String CENA_DETAL = "Cena detal";
    private static final String CENA_RAZEM_NETTO = "Cena razem netto";
    private static final String CENA_RAZEM_ZAKUP = "Cena razem zakup";
    private static final String ZYSK = "Zysk";
    private static final String DODAC_DO_OFERTY = "Dodać do oferty ?";

    private List<String> categories = Arrays.asList("tasma kalenicowa", "wspornik", "tasma do obrobki", "listwa", "kosz", "klamra do mocowania kosza", "tasma samorozprezna",
            "grzebien", "kratka", "pas", "klamra do gasiora", "spinka", "spinka cieta", "lawa mniejsza", "lawa wieksza", "stopien", "plotek mniejszy", "plotek wiekszy",
            "membrana", "laczenie membran", "reparacyjna", "blacha", "cegla", "podbitka");

    private EntityInputDataTiles dataTilesRepo = (EntityInputDataTiles) VaadinSession.getCurrent().getSession().getAttribute("tilesInputFromRepo");
    private Set<EntityAccesories> set = (Set<EntityAccesories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");
    private List<Binder<EntityAccesories>> binders = new ArrayList<>();
    private RadioButtonGroup<String> checkboxGroup = new RadioButtonGroup<>();

    @Autowired
    public AccesoriesViewOld(AccesoriesRepository accesoriesRepository) {
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);

        if (set == null) {
            set = new HashSet<>();
        }
        add(createCheckboxes());
        categories.forEach(category -> add(addSubLayout(category)));
        if (set != null) {
            read();
        }
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        VaadinSession.getCurrent().getSession().setAttribute("accesories", set);
        action.proceed();
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

    private FormLayout addSubLayout(String category) {
        FormLayout formLayout = new FormLayout();
        FormLayout.ResponsiveStep form = new FormLayout.ResponsiveStep("5px", 9);
        formLayout.setResponsiveSteps(form);

        Binder<EntityAccesories> binder = new Binder<>(EntityAccesories.class);

        TextArea name = new TextArea(NAZWA, category);
        NumberField pricePurchase = getTextArea(category, CENA_ZAKUPU);
        NumberField priceRetail = new NumberField(CENA_DETAL, category);
        NumberField numberField = new NumberField(ILOSC);
        numberField.setValue(value(category));
        NumberField allPriceRetail = getTextArea(category, CENA_RAZEM_NETTO);
        NumberField allPricePurchase = getTextArea(category, CENA_RAZEM_ZAKUP);
        NumberField profit = getTextArea(category, ZYSK);
        Checkbox checkbox = new Checkbox(DODAC_DO_OFERTY);

        binder.bind(name, EntityAccesories::getName, EntityAccesories::setName);
        binder.bind(pricePurchase, EntityAccesories::getPurchasePrice, EntityAccesories::setPurchasePrice);
        binder.bind(priceRetail, EntityAccesories::getDetalPrice, EntityAccesories::setDetalPrice);
        binder.bind(numberField, EntityAccesories::getQuantity, EntityAccesories::setQuantity);
        binder.bind(allPriceRetail, EntityAccesories::getAllPriceRetail, EntityAccesories::setAllPriceRetail);
        binder.bind(allPricePurchase, EntityAccesories::getAllPricePurchase, EntityAccesories::setAllPricePurchase);
        binder.bind(profit, EntityAccesories::getProfit, EntityAccesories::setProfit);
        binder.bind(checkbox, EntityAccesories::isOffer, (entityAccesories, offer) -> entityAccesories.setOffer(checkbox.getValue()));

        binder.setStatusLabel(new Label(category));

        binders.add(binder);
        binder.setStatusLabel(new Label(category));

        checkbox(binder);

        List<EntityAccesories> allWithTheSameCategory = accesoriesRepository.findAllByCategoryEquals(category);
        ComboBox<EntityAccesories> comboBox = new ComboBox<>(WYBIERZ, allWithTheSameCategory);

        formLayout.add(comboBox, name, numberField, pricePurchase, priceRetail, allPriceRetail, allPricePurchase, profit, checkbox);
        comboBox.setItemLabelGenerator(EntityAccesories::getName);
        comboBoxValueChangeListener(comboBox, binder);
        return formLayout;
    }

    private void read() {
        for (EntityAccesories accesories : set) {
            Set<Binder<EntityAccesories>> toChange = binders.stream().filter(f -> f.getStatusLabel().get().getText().equals(accesories.getCategory())).collect(Collectors.toSet());
            for (Binder<EntityAccesories> binder : toChange) {
                binder.setBean(accesories);
                binder.setStatusLabel(new Label(accesories.getCategory()));
            }
        }
    }

    private void checkbox(Binder<EntityAccesories> binder) {
        checkboxGroup.addValueChangeListener(e -> {
            List<EntityAccesories> all = accesoriesRepository.findAll();

            Set<EntityAccesories> newValue = all.stream().filter(f -> f.getOption().equals(e.getValue())).collect(Collectors.toSet());
            Set<EntityAccesories> oldValue = all.stream().filter(f -> f.getOption().equals(e.getOldValue())).collect(Collectors.toSet());
            Set<EntityAccesories> other = oldValue.stream().filter(f -> f.getCategory().equals(binder.getStatusLabel().get().getText())).collect(Collectors.toSet());
            Set<EntityAccesories> toChange = newValue.stream().filter(f -> f.getCategory().equals(binder.getStatusLabel().get().getText())).collect(Collectors.toSet());
            for (EntityAccesories accesories : other) {
                binder.setBean(null);
                binder.setStatusLabel(new Label(accesories.getCategory()));
                Set<EntityAccesories> toRemove = new HashSet<>();
                for (EntityAccesories accesoriesToRemove : set) {
                    if (accesories.getName().equals(accesoriesToRemove.getName()) && accesories.getOption().equals(accesoriesToRemove.getOption())) {
                        toRemove.add(accesoriesToRemove);
                    }
                }
                set.removeAll(toRemove);
            }
            for (EntityAccesories accesories : toChange) {
                accesories.setQuantity(value(accesories.getCategory()));
                accesories.setAllPricePurchase(accesories.getQuantity() * accesories.getPurchasePrice());
                accesories.setAllPriceRetail(accesories.getQuantity() * accesories.getDetalPrice());
                accesories.setProfit(accesories.getAllPriceRetail() - accesories.getAllPricePurchase());
                binder.setBean(accesories);
                binder.setStatusLabel(new Label(accesories.getCategory()));
                set.add(binder.getBean());
            }
        });
    }

    private FormLayout createCheckboxes() {
        FormLayout formLayout = new FormLayout();
        checkboxGroup.setItems("PODSTAWOWY", "PREMIUM", "LUX");
        formLayout.add(checkboxGroup);
        return formLayout;
    }

    private NumberField getTextArea(String category, String label) {
        NumberField textArea = new NumberField(label, category);
        textArea.setReadOnly(true);
        return textArea;
    }

    private void comboBoxValueChangeListener(ComboBox<EntityAccesories> comboBox, Binder<EntityAccesories> binder) {
        comboBox.addValueChangeListener(event -> {
            EntityAccesories value = event.getValue();
            value.setQuantity(value(value.getCategory()));
            value.setAllPricePurchase(value.getQuantity() * value.getPurchasePrice());
            value.setAllPriceRetail(value.getQuantity() * value.getDetalPrice());
            value.setProfit(value.getAllPriceRetail() - value.getAllPricePurchase());
            Set<EntityAccesories> collect = set.stream().filter(e -> e.getCategory().equals(value.getCategory())).collect(Collectors.toSet());
            collect.forEach(e -> set.remove(e));
            binder.setBean(value);
            set.add(binder.getBean());
        });
    }
}