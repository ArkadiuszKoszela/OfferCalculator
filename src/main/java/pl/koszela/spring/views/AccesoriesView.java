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
        TextArea name = new TextArea("Nazwa");
        name.setReadOnly(true);
        name.setPlaceholder(category);

        NumberField numberField23 = new NumberField("Ilość");
        numberField23.setValue(value(category));
        numberField23.setReadOnly(true);

        TextArea pricePurchase = new TextArea("Cena zakupu");
        pricePurchase.setReadOnly(true);
        pricePurchase.setPlaceholder(category);

        TextArea priceRetail = new TextArea("Cena detal");
        priceRetail.setPlaceholder(category);

        TextArea allPriceRetail = new TextArea("Cena razem netto");
        allPriceRetail.setReadOnly(true);
        allPriceRetail.setPlaceholder(category);

        TextArea allPricePurchase = new TextArea("Cena razem zakup");
        allPricePurchase.setReadOnly(true);
        allPricePurchase.setPlaceholder(category);

        TextArea profit = new TextArea("Zysk");
        profit.setReadOnly(true);
        profit.setPlaceholder(category);
        if (setFromRepo != null) {
            Set<EntityResultAccesories> collect = setFromRepo.stream().filter(e -> e.getCategory().equals(category)).collect(Collectors.toSet());
            for (EntityResultAccesories resultAccesories : collect) {
                name.setValue(resultAccesories.getName());
                pricePurchase.setValue(String.valueOf(resultAccesories.getPricePurchase()));
                priceRetail.setValue(String.valueOf(resultAccesories.getPriceRetail()));
                allPriceRetail.setValue(String.valueOf(resultAccesories.getAllPriceRetail()));
                allPricePurchase.setValue(String.valueOf(resultAccesories.getAllPricePurchase()));
                profit.setValue(String.valueOf(resultAccesories.getProfit()));
            }
        }

        priceRetail.addValueChangeListener(e -> {
            allPriceRetail.setValue(String.valueOf(numberField23.getValue() * Double.parseDouble(pricePurchase.getValue())));
            allPricePurchase.setValue(String.valueOf(numberField23.getValue() * Double.parseDouble(priceRetail.getValue())));
            profit.setValue(String.valueOf(Double.parseDouble(allPricePurchase.getValue()) - Double.parseDouble(allPriceRetail.getValue())));
        });
        Checkbox checkbox = new Checkbox("Dodać do oferty?");
        ComboBox<EntityAccesories> comboBox = new ComboBox<>("Wybierz");
        formLayout.add(comboBox, name, numberField23, pricePurchase, priceRetail, allPriceRetail, allPricePurchase, profit, checkbox);
        List<EntityAccesories> all = accesoriesRepository.findAllByCategoryEquals(category);

        comboBox.setItems(all);

        comboBox.setItemLabelGenerator(EntityAccesories::getName);

        comboBox.addValueChangeListener(event -> {
            if (event.getSource().isEmpty()) {
                comboBox.setValue(event.getValue());
            } else {
                EntityAccesories value = event.getValue();
                name.setValue(value.getName());
                pricePurchase.setValue(String.valueOf(value.getPurchasePrice()));
                double cenaDetal = (value.getPurchasePrice() * value.getMargin() / 100) + value.getPurchasePrice();
                BigDecimal bigDecimalPriceRetail = new BigDecimal(cenaDetal);
                priceRetail.setValue(String.valueOf(bigDecimalPriceRetail.setScale(2, RoundingMode.HALF_UP)));

                double val = numberField23.getValue() * Double.parseDouble(pricePurchase.getValue());
                BigDecimal bigDecimalallPriceRetail = new BigDecimal(val);
                allPriceRetail.setValue(String.valueOf(bigDecimalallPriceRetail.setScale(2, RoundingMode.HALF_UP)));

                double va = numberField23.getValue() * Double.parseDouble(priceRetail.getValue());
                BigDecimal bigDecimalAllPricePurchase = new BigDecimal(va);
                allPricePurchase.setValue(String.valueOf(bigDecimalAllPricePurchase.setScale(2, RoundingMode.HALF_UP)));

                double cena = Double.parseDouble(allPricePurchase.getValue()) - Double.parseDouble(allPriceRetail.getValue());
                BigDecimal bigDecimalProfit = new BigDecimal(cena);
                profit.setValue(String.valueOf(bigDecimalProfit.setScale(2, RoundingMode.HALF_UP)));

                EntityResultAccesories resultAccesories = new EntityResultAccesories();
                resultAccesories.setName(name.getValue());
                resultAccesories.setQuantity(numberField23.getValue());
                resultAccesories.setPricePurchase(Double.valueOf(pricePurchase.getValue()));
                resultAccesories.setPriceRetail(Double.valueOf(priceRetail.getValue()));
                resultAccesories.setAllPriceRetail(Double.valueOf(allPriceRetail.getValue()));
                resultAccesories.setAllPricePurchase(Double.valueOf(allPricePurchase.getValue()));
                resultAccesories.setProfit(Double.valueOf(profit.getValue()));
                resultAccesories.setCategory(value.getCategory());
                if (setFromRepo.isEmpty()) {
                    setFromRepo.add(resultAccesories);
                }
                for (EntityResultAccesories resultAccesories1 : setFromRepo) {
                    if (resultAccesories1.getCategory().equals(resultAccesories.getCategory())) {
                        resultAccesories1.setName(name.getValue());
                        resultAccesories1.setQuantity(numberField23.getValue());
                        resultAccesories1.setPricePurchase(Double.valueOf(pricePurchase.getValue()));
                        resultAccesories1.setPriceRetail(Double.valueOf(priceRetail.getValue()));
                        resultAccesories1.setAllPriceRetail(Double.valueOf(allPriceRetail.getValue()));
                        resultAccesories1.setAllPricePurchase(Double.valueOf(allPricePurchase.getValue()));
                        resultAccesories1.setProfit(Double.valueOf(profit.getValue()));
                    } else if (!resultAccesories1.getCategory().equals(resultAccesories.getCategory()) && !setFromRepo.contains(resultAccesories)) {
                        boolean exist = setFromRepo.stream().anyMatch(c -> c.getCategory().equals(resultAccesories.getCategory()));
                        if (!exist) {
                            setFromRepo.add(resultAccesories);
                        }
                    }
                }
            }
        });
        return formLayout;
    }
}