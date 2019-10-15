package pl.koszela.spring.views;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextFieldVariant;
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

    private EntityInputDataTiles dataTilesRepo = (EntityInputDataTiles) VaadinSession.getCurrent().getSession().getAttribute("tilesInputFromRepo");
    private Set<EntityResultAccesories> setFromRepo = (Set<EntityResultAccesories>) VaadinSession.getCurrent().getSession().getAttribute("accesories");

    @Autowired
    public AccesoriesView(AccesoriesRepository accesoriesRepository) {
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);

        if (setFromRepo == null) {
            setFromRepo = new HashSet<>();
        }
        add(addSubLayout("tasma kalenicowa"), addSubLayout("wspornik"), addSubLayout("tasma do obrobki"), addSubLayout("listwa"),
                addSubLayout("kosz"), addSubLayout("klamra do mocowania kosza"), addSubLayout("tasma samorozprezna"),
                addSubLayout("grzebien"), addSubLayout("kratka"), addSubLayout("pas"), addSubLayout("klamra do gasiora"),
                addSubLayout("spinka"), addSubLayout("spinka cieta"), addSubLayout("lawa mniejsza"), addSubLayout("lawa wieksza"),
                addSubLayout("stopien"), addSubLayout("plotek mniejszy"), addSubLayout("plotek wiekszy"), addSubLayout("membrana"),
                addSubLayout("laczenie membran"), addSubLayout("reparacyjna"), addSubLayout("blacha"), addSubLayout("cegla"),
                addSubLayout("podbitka"));
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        VaadinSession.getCurrent().getSession().setAttribute("accesories", setFromRepo);
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

        TextArea name = getTextArea(category, "Nazwa");
        TextArea pricePurchase = getTextArea(category, "Cena zakupu");
        TextArea priceRetail = new TextArea("Cena detal", category);
        TextArea allPriceRetail = getTextArea(category, "Cena razem netto");
        TextArea allPricePurchase = getTextArea(category, "Cena razem zakup");
        TextArea profit = getTextArea(category, "Zysk");
        NumberField numberField = new NumberField("Ilość");
        numberField.setValue(value(category));
        numberField.setReadOnly(true);
        Checkbox checkbox = new Checkbox("Dodać do oferty?");

        List<EntityAccesories> allWithTheSameCategory = accesoriesRepository.findAllByCategoryEquals(category);
        ComboBox<EntityAccesories> comboBox = new ComboBox<>("Wybierz", allWithTheSameCategory);

        priceRetail.addValueChangeListener(e -> {
            allPriceRetail.setValue(String.valueOf(numberField.getValue() * Double.parseDouble(pricePurchase.getValue())));
            allPricePurchase.setValue(String.valueOf(numberField.getValue() * Double.parseDouble(priceRetail.getValue())));
            profit.setValue(String.valueOf(Double.parseDouble(allPricePurchase.getValue()) - Double.parseDouble(allPriceRetail.getValue())));
        });

        formLayout.add(comboBox, name, numberField, pricePurchase, priceRetail, allPriceRetail, allPricePurchase, profit, checkbox);

        if (setFromRepo != null) {
            Set<EntityResultAccesories> accesoriesWithTheSameCategory = setFromRepo.stream().filter(e -> e.getCategory().equals(category)).collect(Collectors.toSet());
            for (EntityResultAccesories resultAccesories : accesoriesWithTheSameCategory) {
                name.setValue(resultAccesories.getName());
                pricePurchase.setValue(String.valueOf(resultAccesories.getPricePurchase()));
                priceRetail.setValue(String.valueOf(resultAccesories.getPriceRetail()));
                allPriceRetail.setValue(String.valueOf(resultAccesories.getAllPriceRetail()));
                allPricePurchase.setValue(String.valueOf(resultAccesories.getAllPricePurchase()));
                profit.setValue(String.valueOf(resultAccesories.getProfit()));
            }
        }
        comboBox.setItemLabelGenerator(EntityAccesories::getName);

        comboBoxValueChangeListener(name, numberField, pricePurchase, priceRetail, allPriceRetail, allPricePurchase, profit, comboBox);
        return formLayout;
    }

    private TextArea getTextArea(String category, String label) {
        TextArea allPriceRetail = new TextArea(label, category);
        allPriceRetail.setReadOnly(true);
        return allPriceRetail;
    }

    private void comboBoxValueChangeListener(TextArea name, NumberField numberField23, TextArea pricePurchase, TextArea priceRetail, TextArea allPriceRetail, TextArea allPricePurchase, TextArea profit, ComboBox<EntityAccesories> comboBox) {
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
                    .build();

            if (setFromRepo.isEmpty()) {
                setFromRepo.add(resultAccesories);
            }
            for (EntityResultAccesories accesoriestoChange : setFromRepo) {
                if (accesoriestoChange.getCategory().equals(resultAccesories.getCategory())) {
                    accesoriestoChange.setName(name.getValue());
                    accesoriestoChange.setQuantity(numberField23.getValue());
                    accesoriestoChange.setPricePurchase(Double.valueOf(pricePurchase.getValue()));
                    accesoriestoChange.setPriceRetail(Double.valueOf(priceRetail.getValue()));
                    accesoriestoChange.setAllPriceRetail(Double.valueOf(allPriceRetail.getValue()));
                    accesoriestoChange.setAllPricePurchase(Double.valueOf(allPricePurchase.getValue()));
                    accesoriestoChange.setProfit(Double.valueOf(profit.getValue()));
                } else if (!accesoriestoChange.getCategory().equals(resultAccesories.getCategory()) && !setFromRepo.contains(resultAccesories)) {
                    boolean exist = setFromRepo.stream().anyMatch(c -> c.getCategory().equals(resultAccesories.getCategory()));
                    if (!exist) {
                        setFromRepo.add(resultAccesories);
                    }
                }
            }
        });
    }
}