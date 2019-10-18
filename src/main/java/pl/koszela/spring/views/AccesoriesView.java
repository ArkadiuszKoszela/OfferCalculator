package pl.koszela.spring.views;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.EntityAccesories;
import pl.koszela.spring.entities.EntityInputDataTiles;
import pl.koszela.spring.entities.EntityResultAccesories;
import pl.koszela.spring.repositories.AccesoriesRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Route(value = AccesoriesView.SELECT_ACCESORIES, layout = MainView.class)
public class AccesoriesView extends VerticalLayout implements BeforeLeaveObserver {

    static final String SELECT_ACCESORIES = "accesories";
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
    private Set<EntityResultAccesories> set = (Set<EntityResultAccesories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");

    @Autowired
    public AccesoriesView(AccesoriesRepository accesoriesRepository) {
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);

        if (set == null) {
            set = new HashSet<>();
        }
        add(createCheckboxes());
        categories.forEach(category -> add(addSubLayout(category)));
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        VaadinSession.getCurrent().getSession().setAttribute("accesories", set);
        save();
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

        TextArea name = getTextArea(category, NAZWA);
        TextArea pricePurchase = getTextArea(category, CENA_ZAKUPU);
        TextArea priceRetail = new TextArea(CENA_DETAL, category);
        VaadinSession.getCurrent().getSession().setAttribute(category + CENA_DETAL, priceRetail);
        TextArea allPriceRetail = getTextArea(category, CENA_RAZEM_NETTO);
        TextArea allPricePurchase = getTextArea(category, CENA_RAZEM_ZAKUP);
        TextArea profit = getTextArea(category, ZYSK);
        NumberField numberField = new NumberField(ILOSC);
        numberField.setValue(value(category));
        numberField.setReadOnly(true);
        VaadinSession.getCurrent().getSession().setAttribute(category + ILOSC, numberField);
        Checkbox checkbox = new Checkbox(DODAC_DO_OFERTY);
        VaadinSession.getCurrent().getSession().setAttribute(category + DODAC_DO_OFERTY, checkbox);

        List<EntityAccesories> allWithTheSameCategory = accesoriesRepository.findAllByCategoryEquals(category);
        ComboBox<EntityAccesories> comboBox = new ComboBox<>(WYBIERZ, allWithTheSameCategory);
        VaadinSession.getCurrent().getSession().setAttribute(category + WYBIERZ, comboBox);

        priceRetail.addValueChangeListener(e -> {
            allPriceRetail.setValue(String.valueOf(numberField.getValue() * Double.parseDouble(pricePurchase.getValue())));
            allPricePurchase.setValue(String.valueOf(numberField.getValue() * Double.parseDouble(priceRetail.getValue())));
            profit.setValue(String.valueOf(Double.parseDouble(allPricePurchase.getValue()) - Double.parseDouble(allPriceRetail.getValue())));
        });

        formLayout.add(comboBox, name, numberField, pricePurchase, priceRetail, allPriceRetail, allPricePurchase, profit, checkbox);

        if (set != null) {
            Set<EntityResultAccesories> accesoriesWithTheSameCategory = set.stream().filter(e -> e.getCategory().equals(category)).collect(Collectors.toSet());
            for (EntityResultAccesories resultAccesories : accesoriesWithTheSameCategory) {
                name.setValue(resultAccesories.getName());
                pricePurchase.setValue(String.valueOf(resultAccesories.getPricePurchase()));
                priceRetail.setValue(String.valueOf(resultAccesories.getPriceRetail()));
                allPriceRetail.setValue(String.valueOf(resultAccesories.getAllPriceRetail()));
                allPricePurchase.setValue(String.valueOf(resultAccesories.getAllPricePurchase()));
                profit.setValue(String.valueOf(resultAccesories.getProfit()));
                checkbox.setValue(resultAccesories.isOffer());
            }
        }
        comboBox.setItemLabelGenerator(EntityAccesories::getName);

        comboBoxValueChangeListener(name, numberField, pricePurchase, priceRetail, allPriceRetail, allPricePurchase, profit, comboBox, checkbox);
        return formLayout;
    }

    private RadioButtonGroup<String> checkboxGroup = new RadioButtonGroup<>();

    private FormLayout createCheckboxes() {
        FormLayout formLayout = new FormLayout();
        checkboxGroup.setItems("PODSTAWOWY", "PREMIUM", "LUX");
        checkboxGroup.addValueChangeListener(e -> {
            List<EntityAccesories> all = accesoriesRepository.findAll();
//            Optional<EntityAccesories> collect = all.stream().filter(f -> f.getOption().equals(e.getValue())).findFirst();
            List<EntityAccesories> collect = all.stream().filter(f -> f.getOption().equals(e.getValue())).collect(Collectors.toList());
            for(EntityAccesories accesories: collect) {
                TextField textField = (TextField) VaadinSession.getCurrent().getSession().getAttribute(accesories.getCategory() + NAZWA);
                textField.setValue(accesories.getName());
            }
        });
        formLayout.add(checkboxGroup);
        return formLayout;
    }

    private TextArea getTextArea(String category, String label) {
        TextArea textArea = new TextArea(label, category);
        textArea.setReadOnly(true);
        VaadinSession.getCurrent().getSession().setAttribute(category + label, textArea);
        return textArea;
    }

    private void comboBoxValueChangeListener(TextArea name, NumberField numberField23, TextArea pricePurchase, TextArea priceRetail, TextArea allPriceRetail, TextArea allPricePurchase, TextArea profit, ComboBox<EntityAccesories> comboBox, Checkbox checkbox) {
        comboBox.addValueChangeListener(event -> {
            EntityAccesories value = event.getValue();


            name.setValue(value.getName());
            pricePurchase.setValue(String.valueOf(value.getPurchasePrice()));
            priceRetail.setValue(BigDecimal.valueOf((value.getPurchasePrice() * value.getMargin() / 100) + value.getPurchasePrice()).setScale(2, RoundingMode.HALF_UP).toString());
            allPriceRetail.setValue(BigDecimal.valueOf(numberField23.getValue() * Double.parseDouble(pricePurchase.getValue())).setScale(2, RoundingMode.HALF_UP).toString());
            allPricePurchase.setValue(BigDecimal.valueOf(numberField23.getValue() * Double.parseDouble(priceRetail.getValue())).setScale(2, RoundingMode.HALF_UP).toString());
            profit.setValue(BigDecimal.valueOf(Double.parseDouble(allPricePurchase.getValue()) - Double.parseDouble(allPriceRetail.getValue())).setScale(2, RoundingMode.HALF_UP).toString());

            EntityResultAccesories resultAccesories = EntityResultAccesories.builder()
                    .name(name.getValue())
                    .quantity(numberField23.getValue())
                    .pricePurchase(Double.valueOf(pricePurchase.getValue()))
                    .priceRetail(Double.valueOf(priceRetail.getValue()))
                    .allPriceRetail(Double.valueOf(allPriceRetail.getValue()))
                    .allPricePurchase(Double.valueOf(allPricePurchase.getValue()))
                    .profit(Double.valueOf(profit.getValue()))
                    .category(value.getCategory())
                    .offer(checkbox.getValue())
                    .build();

            if (set.isEmpty()) {
                set.add(resultAccesories);
            }
            for (EntityResultAccesories accesoriestoChange : set) {
                if (accesoriestoChange.getCategory().equals(resultAccesories.getCategory())) {
                    accesoriestoChange.setName(name.getValue());
                    accesoriestoChange.setQuantity(numberField23.getValue());
                    accesoriestoChange.setPricePurchase(Double.valueOf(pricePurchase.getValue()));
                    accesoriestoChange.setPriceRetail(Double.valueOf(priceRetail.getValue()));
                    accesoriestoChange.setAllPriceRetail(Double.valueOf(allPriceRetail.getValue()));
                    accesoriestoChange.setAllPricePurchase(Double.valueOf(allPricePurchase.getValue()));
                    accesoriestoChange.setProfit(Double.valueOf(profit.getValue()));
                    accesoriestoChange.setOffer(checkbox.getValue());
                } else if (!accesoriestoChange.getCategory().equals(resultAccesories.getCategory()) && !set.contains(resultAccesories)) {
                    boolean exist = set.stream().anyMatch(c -> c.getCategory().equals(resultAccesories.getCategory()));
                    if (!exist) {
                        set.add(resultAccesories);
                    }
                }
            }
        });
    }

    private void save() {
        for (String category : categories) {
            TextArea name = (TextArea) VaadinSession.getCurrent().getSession().getAttribute(category + NAZWA);
            TextArea pricePurchase = (TextArea) VaadinSession.getCurrent().getSession().getAttribute(category + CENA_ZAKUPU);
            TextArea priceRetail = (TextArea) VaadinSession.getCurrent().getSession().getAttribute(category + CENA_DETAL);
            TextArea allPriceRetail = (TextArea) VaadinSession.getCurrent().getSession().getAttribute(category + CENA_RAZEM_NETTO);
            TextArea allPricePurchase = (TextArea) VaadinSession.getCurrent().getSession().getAttribute(category + CENA_RAZEM_ZAKUP);
            TextArea profit = (TextArea) VaadinSession.getCurrent().getSession().getAttribute(category + ZYSK);
            NumberField numberField = (NumberField) VaadinSession.getCurrent().getSession().getAttribute(category + ILOSC);
            Checkbox checkbox = (Checkbox) VaadinSession.getCurrent().getSession().getAttribute(category + DODAC_DO_OFERTY);
            ComboBox comboBox = (ComboBox) VaadinSession.getCurrent().getSession().getAttribute(category + WYBIERZ);
            set.forEach(e -> {
                if (e.getCategory().equals(category)) {
                    e.setName(name.getValue());
                    e.setPricePurchase(Double.valueOf(pricePurchase.getValue()));
                    e.setPriceRetail(Double.valueOf(priceRetail.getValue()));
                    e.setAllPriceRetail(Double.valueOf(allPriceRetail.getValue()));
                    e.setAllPricePurchase(Double.valueOf(allPricePurchase.getValue()));
                    e.setProfit(Double.valueOf(profit.getValue()));
                    e.setQuantity(numberField.getValue());
                    e.setOffer(checkbox.getValue());
                }
            });
        }
    }
}