package pl.koszela.spring.views;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.InputData;
import pl.koszela.spring.entities.gutter.EntityGutter;
import pl.koszela.spring.entities.tiles.CategoryTiles;
import pl.koszela.spring.entities.tiles.Tiles;
import pl.koszela.spring.repositories.GutterRepository;
import pl.koszela.spring.repositories.TilesRepository;
import pl.koszela.spring.service.NameNumberFields;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Route(value = IncludeDataView.INCLUDE_DATA, layout = MainView.class)
public class IncludeDataView extends VerticalLayout implements BeforeLeaveObserver {

    static final String INCLUDE_DATA = "includeData";

    private TilesRepository tilesRepository;
    private GutterRepository gutterRepository;

    private List<NumberField> listOfNumberFields = new ArrayList<>();
    private Set<Tiles> set = (Set<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("tiles");
    private List<EntityGutter> list = (List<EntityGutter>) VaadinSession.getCurrent().getSession().getAttribute("gutter");
    private List<InputData> setInput = (List<InputData>) VaadinSession.getCurrent().getSession().getAttribute("inputData");


    @Autowired
    public IncludeDataView(TilesRepository tilesRepository, GutterRepository gutterRepository) {
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
        this.gutterRepository = Objects.requireNonNull(gutterRepository);
        add(createNewSubLayout());
    }

    private VerticalLayout createNewSubLayout() {
        if (setInput == null || setInput.size() == 0) {
            setInput = new ArrayList<>();
            for (NameNumberFields name : NameNumberFields.values()) {
                setInput.add(new InputData(name.toString(), 5d));
            }
        }
        VerticalLayout verticalLayout = new VerticalLayout();
        FormLayout tilesLayout = getFormLayout(6);
        FormLayout gutterLayout1 = getFormLayout(12);
        FormLayout gutterLayout2 = getFormLayout(5);
        CategoryTiles[] values = CategoryTiles.values();
        int i = 0;
        for (InputData inputData : setInput) {
            if (i <= 18) {
                tilesLayout.add(createNumberField(inputData, values[i].toString()));
            } else if (i > 18 && i < 55) {
                gutterLayout1.add(createNumberField(inputData, ""));
            } else {
                gutterLayout2.add(createNumberField(inputData, ""));
            }
            i++;
        }
        verticalLayout.addAndExpand(new Span("Dachówki"), tilesLayout, new Span("Rynny"), gutterLayout1, gutterLayout2);
        return verticalLayout;
    }

    private FormLayout getFormLayout(int intColumn) {
        FormLayout formLayout = new FormLayout();
        FormLayout.ResponsiveStep responsiveStep = new FormLayout.ResponsiveStep("2px", intColumn);
        formLayout.setResponsiveSteps(responsiveStep);
        return formLayout;
    }

    private NumberField createNumberField(InputData inputData, String category) {
        NumberField numberField = new NumberField();
        numberField.setValue(inputData.getValue());
        numberField.setLabel(inputData.getName());
        numberField.setPattern(category);
        numberField.setMin(0);
        numberField.setMax(500);
        numberField.setAutoselect(true);
        numberField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        numberField.addValueChangeListener(e -> {
            for (InputData inputData1 : setInput) {
                if (inputData1.getName().equals(numberField.getLabel()) && inputData1.getValue().equals(e.getOldValue())) {
                    inputData1.setValue(e.getValue());
                }
            }
        });
        addValueChangeListener();
        listOfNumberFields.add(numberField);
        return numberField;
    }

    private void addValueChangeListener() {
        Set<NumberField> gutter3mb = listOfNumberFields.stream().filter(e -> StringUtils.containsIgnoreCase(e.getLabel(), ("rynna 3mb"))).collect(Collectors.toSet());
        Set<NumberField> gutter4mb = listOfNumberFields.stream().filter(e -> StringUtils.containsIgnoreCase(e.getLabel(), ("rynna 4mb"))).collect(Collectors.toSet());
        Set<NumberField> odcinki = listOfNumberFields.stream().filter(e -> StringUtils.containsIgnoreCase(e.getLabel(), ("odcinek"))).collect(Collectors.toSet());

        gutter3mb.forEach(gutter3mbField -> {
            odcinki.forEach(odcinekField -> {
                gutter4mb.forEach(gutter4mbField -> {
                    if (przedlastChar(gutter3mbField.getLabel()) == (przedlastChar(odcinekField.getLabel())) && przedlastChar(gutter3mbField.getLabel()) == przedlastChar(gutter4mbField.getLabel())
                            && lastChar(gutter3mbField.getLabel()) == lastChar(odcinekField.getLabel()) && lastChar(gutter3mbField.getLabel()) == lastChar(gutter4mbField.getLabel())) {
                        gutter3mbField.addValueChangeListener(f -> {
                            odcinekField.setValue(f.getValue() * 3 + 4 * gutter4mbField.getValue());
                        });
                        gutter4mbField.addValueChangeListener(z -> {
                            odcinekField.setValue(z.getValue() * 4 + 3 * gutter3mbField.getValue());
                        });
                    }
                });
            });
        });
    }

    private char lastChar(String a) {
        return a.charAt(a.length() - 1);
    }

    private char przedlastChar(String a) {
        return a.charAt(a.length() - 2);
    }

    private Set<Tiles> listTiles() {
        if (set != null) {
            for (Tiles tiles : set) {
                listOfNumberFields.forEach(numberField -> {
                    if (numberField.getPattern().equals(tiles.getName())) {
                        tiles.setQuantity(numberField.getValue());
                    }
                });
            }
        } else {
            List<Tiles> allFromRepo = tilesRepository.findAll();
            set = new HashSet<>(allFromRepo);
            for (Tiles tiles : set) {
                listOfNumberFields.forEach(numberField -> {
                    if (numberField.getPattern().equals(tiles.getName())) {
                        tiles.setQuantity(numberField.getValue());
                        clearTiles(tiles);
                    }
                });
            }
        }
        return set;
    }

    private List<EntityGutter> listWithQuantityGutter() {
        Set<InputData> all3Mb = setInput.stream().filter(e -> e.getName().contains("Rynna 3mb")).collect(Collectors.toSet());
        Set<InputData> all4Mb = setInput.stream().filter(e -> e.getName().contains("Rynna 4mb")).collect(Collectors.toSet());
        Double gutter3mb = all3Mb.stream().map(InputData::getValue).reduce(Double::sum).get();
        Double gutter4mb = all4Mb.stream().map(InputData::getValue).reduce(Double::sum).get();
        if (list == null) {
            list = gutterRepository.findAll();
        }
        for (EntityGutter gutter : list) {
            if (gutter.getName().equals("rynna 3mb")) {
                gutter.setQuantity(gutter3mb);
            } else if (gutter.getName().equals("rynna 4mb")) {
                gutter.setQuantity(gutter4mb);
            }else{
                gutter.setQuantity(1.1d);
            }
            listOfNumberFields.forEach(numberField -> {
                if (StringUtils.containsIgnoreCase(gutter.getName(), numberField.getLabel())) {
                    gutter.setQuantity(numberField.getValue());
                }
            });

        }
        return list;
    }

    private void clearTiles(Tiles tiles) {
        tiles.setTotalProfit(new BigDecimal(0));
        tiles.setDiscount(0);
        tiles.setTotalPrice(new BigDecimal(0));
        tiles.setAllpriceAfterDiscount(0d);
        tiles.setAllpricePurchase(0d);
        tiles.setAllprofit(0d);
        tiles.setOption(false);
        tiles.setMain(false);
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        VaadinSession.getCurrent().getSession().setAttribute("gutter", listWithQuantityGutter());
        VaadinSession.getCurrent().getSession().setAttribute("tiles", listTiles());
        VaadinSession.getCurrent().getSession().setAttribute("inputData", setInput);
    }
}