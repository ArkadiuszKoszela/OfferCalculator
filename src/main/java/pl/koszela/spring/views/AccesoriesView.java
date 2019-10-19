package pl.koszela.spring.views;

import com.vaadin.flow.component.HasValue;
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
    private Set<EntityAccesories> set = (Set<EntityAccesories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");

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
        save();
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

    private Set<NumberField> textAreaList = new HashSet<>();
    private Set<TextArea> textAreaSet = new HashSet<>();

    private FormLayout addSubLayout(String category) {
        FormLayout formLayout = new FormLayout();
        FormLayout.ResponsiveStep form = new FormLayout.ResponsiveStep("5px", 9);
        formLayout.setResponsiveSteps(form);

        TextArea name = new TextArea(NAZWA, category);
        textAreaSet.add(name);
//        textAreaList.add(name);
        NumberField pricePurchase = getTextArea(category, CENA_ZAKUPU);
        textAreaList.add(pricePurchase);
        NumberField priceRetail = new NumberField(CENA_DETAL, category);
        textAreaList.add(priceRetail);
        VaadinSession.getCurrent().getSession().setAttribute(category + CENA_DETAL, priceRetail);
        NumberField allPriceRetail = getTextArea(category, CENA_RAZEM_NETTO);
        textAreaList.add(allPriceRetail);
        NumberField allPricePurchase = getTextArea(category, CENA_RAZEM_ZAKUP);
        textAreaList.add(allPricePurchase);
        NumberField profit = getTextArea(category, ZYSK);
        textAreaList.add(profit);
        NumberField numberField = new NumberField(ILOSC);
        numberField.setValue(value(category));
        numberField.setReadOnly(true);
        VaadinSession.getCurrent().getSession().setAttribute(category + ILOSC, numberField);
        Checkbox checkbox = new Checkbox(DODAC_DO_OFERTY);
        VaadinSession.getCurrent().getSession().setAttribute(category + DODAC_DO_OFERTY, checkbox);

        List<EntityAccesories> allWithTheSameCategory = accesoriesRepository.findAllByCategoryEquals(category);
        ComboBox<EntityAccesories> comboBox = new ComboBox<>(WYBIERZ, allWithTheSameCategory);
        VaadinSession.getCurrent().getSession().setAttribute(category + WYBIERZ, comboBox);

//        priceRetail.addValueChangeListener(e -> {
//            allPriceRetail.setValue(numberField.getValue() * pricePurchase.getValue());
//            allPricePurchase.setValue(numberField.getValue() * priceRetail.getValue());
//            profit.setValue(allPricePurchase.getValue() - allPriceRetail.getValue());
//        });

        formLayout.add(comboBox, name, numberField, pricePurchase, priceRetail, allPriceRetail, allPricePurchase, profit, checkbox);

        if (set != null) {
            Set<EntityAccesories> accesoriesWithTheSameCategory = set.stream().filter(e -> e.getCategory().equals(category)).collect(Collectors.toSet());
            for (EntityAccesories resultAccesories : accesoriesWithTheSameCategory) {
                name.setValue(resultAccesories.getName());
                pricePurchase.setValue(resultAccesories.getPurchasePrice());
                priceRetail.setValue(resultAccesories.getDetalPrice());
                allPriceRetail.setValue(resultAccesories.getAllPriceRetail());
                allPricePurchase.setValue(resultAccesories.getAllPricePurchase());
                profit.setValue(resultAccesories.getProfit());
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

            List<EntityAccesories> collect = all.stream().filter(f -> f.getOption().equals(e.getValue())).collect(Collectors.toList());
            set = new HashSet<>(collect);
            set = set.stream().filter(f -> f.getOption().equals(e.getValue())).collect(Collectors.toSet());
            for (NumberField textArea1 : textAreaList) {
                textArea1.clear();
            }
            textAreaSet.forEach(HasValue::clear);
            for (EntityAccesories accesories : set) {
                for (NumberField numberField : textAreaList) {
                    for (TextArea textArea : textAreaSet) {
                        if (accesories.getCategory().equals(numberField.getPlaceholder()) && accesories.getCategory().equals(textArea.getPlaceholder())) {
                            textArea.setValue(accesories.getName());
                            switch (numberField.getLabel()) {
//                            case NAZWA:
//                                textArea.setValue(accesories.getName());
//                                break;
                                case CENA_ZAKUPU:
                                    numberField.setValue(accesories.getPurchasePrice());
                                    break;
                                case CENA_DETAL:
                                    numberField.setValue(accesories.getDetalPrice());
                                    break;
                            }
                        }
                    }
                }
            }
        });
        formLayout.add(checkboxGroup);
        return formLayout;
    }

    private NumberField getTextArea(String category, String label) {
        NumberField textArea = new NumberField(label, category);
        textArea.setReadOnly(true);
        VaadinSession.getCurrent().getSession().setAttribute(category + label, textArea);
        return textArea;
    }

    private void comboBoxValueChangeListener(TextArea name, NumberField numberField23, NumberField pricePurchase, NumberField priceRetail, NumberField allPriceRetail, NumberField allPricePurchase, NumberField profit, ComboBox<EntityAccesories> comboBox, Checkbox checkbox) {
        comboBox.addValueChangeListener(event -> {
            EntityAccesories value = event.getValue();

            name.setValue(value.getName());
            pricePurchase.setValue(value.getPurchasePrice());
            priceRetail.setValue(BigDecimal.valueOf((value.getPurchasePrice() * value.getMargin() / 100) + value.getPurchasePrice()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            allPriceRetail.setValue(BigDecimal.valueOf(numberField23.getValue() * pricePurchase.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            allPricePurchase.setValue(BigDecimal.valueOf(numberField23.getValue() * priceRetail.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            profit.setValue((BigDecimal.valueOf(allPricePurchase.getValue()).subtract(BigDecimal.valueOf(allPriceRetail.getValue())).setScale(2, RoundingMode.HALF_UP).doubleValue()));

//            EntityResultAccesories resultAccesories = EntityResultAccesories.builder()
//                    .name(name.getValue())
//                    .quantity(numberField23.getValue())
//                    .pricePurchase(Double.valueOf(pricePurchase.getValue()))
//                    .priceRetail(Double.valueOf(priceRetail.getValue()))
//                    .allPriceRetail(Double.valueOf(allPriceRetail.getValue()))
//                    .allPricePurchase(Double.valueOf(allPricePurchase.getValue()))
//                    .profit(Double.valueOf(profit.getValue()))
//                    .category(value.getCategory())
//                    .offer(checkbox.getValue())
//                    .build();

            if (set.isEmpty()) {
                set.add(value);
            }
            for (EntityAccesories accesoriestoChange : set) {
                if (accesoriestoChange.getCategory().equals(value.getCategory())) {
                    accesoriestoChange.setName(name.getValue());
                    accesoriestoChange.setQuantity(numberField23.getValue());
                    accesoriestoChange.setPurchasePrice(pricePurchase.getValue());
                    accesoriestoChange.setDetalPrice(priceRetail.getValue());
                    accesoriestoChange.setAllPriceRetail(allPriceRetail.getValue());
                    accesoriestoChange.setAllPricePurchase(allPricePurchase.getValue());
                    accesoriestoChange.setProfit(profit.getValue());
                    accesoriestoChange.setOffer(checkbox.getValue());
                } else if (!accesoriestoChange.getCategory().equals(value.getCategory()) && !set.contains(value)) {
                    boolean exist = set.stream().anyMatch(c -> c.getCategory().equals(value.getCategory()));
                    if (!exist) {
                        set.add(value);
                    }
                }
            }
        });
    }

    private void save() {
        for (NumberField numberField : textAreaList) {
            for (EntityAccesories accesories : set) {
                for (TextArea textArea : textAreaSet) {
                    if (accesories.getCategory().equals(numberField.getPlaceholder()) && accesories.getCategory().equals(textArea.getPlaceholder())) {
                        accesories.setName(textArea.getValue());
                        switch (numberField.getLabel()) {
//                        case NAZWA:
//                            accesories.setName(textArea.getValue());
//                            break;
                            case CENA_ZAKUPU:
                                accesories.setPurchasePrice(numberField.getValue());
                                break;
                            case CENA_DETAL:
                                accesories.setDetalPrice(numberField.getValue());
                                break;
                            case CENA_RAZEM_NETTO:
                                accesories.setAllPriceRetail(numberField.getValue());
                                break;
                            case CENA_RAZEM_ZAKUP:
                                accesories.setAllPricePurchase(numberField.getValue());
                                break;
                            case ZYSK:
                                accesories.setProfit(numberField.getValue());
                                break;
                        }
                    }
                }
            }
        }
    }
}