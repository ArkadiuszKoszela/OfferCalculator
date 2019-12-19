package pl.koszela.spring.service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.server.VaadinService;
import org.apache.commons.lang3.StringUtils;
import pl.koszela.spring.entities.main.Accessories;
import pl.koszela.spring.entities.main.BaseEntity;
import pl.koszela.spring.repositories.BaseRepository;


import javax.servlet.http.Cookie;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static pl.koszela.spring.service.CalculatePrices.calculateDetalPrice;

public interface PriceListInterface<T extends BaseEntity> {

    default Grid<T> createGrid(Grid<T> grid, Binder<T> binder, List<T> list, BaseRepository<T> repository) {
//        TextField filter = new TextField();


        Grid.Column<T> nameColumn = grid.addColumn(T::getName).setHeader("Nazwa");
        Grid.Column<T> priceColumn = grid.addColumn(T::getUnitDetalPrice).setHeader("Cena detal");
        Grid.Column<T> basicDiscountColumn = grid.addColumn(T::getBasicDiscount).setHeader("Podstawowy rabat");
        Grid.Column<T> promotionDiscountColumn = grid.addColumn(T::getPromotionDiscount).setHeader("Promocja");
        Grid.Column<T> additionalDiscountColumn = grid.addColumn(T::getAdditionalDiscount).setHeader("Dodatkowy rabat");
        Grid.Column<T> skontoDiscountColumn = grid.addColumn(T::getSkontoDiscount).setHeader("Skonto");
        Grid.Column<T> priceFromRepoColumn = grid.addColumn(T::getUnitPurchasePrice).setHeader("Cena zakupu(z ręki)");
        Grid.Column<T> date = grid.addColumn(T::getDate).setHeader("Data zmiany");
        Grid.Column<T> fileUrl = grid.addColumn(T::getUrlToDownloadFile).setHeader("Cennik");

        binder = new Binder<>();
        grid.getEditor().setBinder(binder);

//        HeaderRow filterRow = grid.appendHeaderRow();
//        filter.addValueChangeListener(event -> listDataProvider.addFilter(
//                baseEntity -> StringUtils.containsIgnoreCase(baseEntity.getName(), filter.getValue())
//        ));

//        filter.setValueChangeMode(ValueChangeMode.EAGER);
//        filterRow.getCell(nameColumn).setComponent(filter);
//        filter.setSizeFull();
//        filter.setPlaceholder("Filter");

//        TextField basicDiscount = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), T::getBasicDiscount, T::setBasicDiscount);
//        itemClickListener(grid, basicDiscount);
//        basicDiscountColumn.setEditorComponent(basicDiscount);
//
//        TextField promotionDiscount = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), T::getPromotionDiscount, T::setPromotionDiscount);
//        itemClickListener(grid, promotionDiscount);
//        promotionDiscountColumn.setEditorComponent(promotionDiscount);
//
//        TextField additionalDiscount = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), T::getAdditionalDiscount, T::setAdditionalDiscount);
//        itemClickListener(grid, additionalDiscount);
//        additionalDiscountColumn.setEditorComponent(additionalDiscount);
//
//        TextField skontoDiscount = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), T::getSkontoDiscount, T::setSkontoDiscount);
//        itemClickListener(grid, skontoDiscount);
//        skontoDiscountColumn.setEditorComponent(skontoDiscount);

        closeListener(grid, binder);
        settingsGrid(grid);
        return grid;
    }

    default Button createButtonToDiscounts(Button button, List<T> list, Dialog dialog, Grid<T> grid, BaseRepository<T> baseRepository) {
        return button = new Button("Rabaty", event -> {
            ComboBox<String> comboBox = new ComboBox<>("Producent");
            Set<String> manufacturerSet = list.stream().map(BaseEntity::getManufacturer).collect(Collectors.toSet());
            comboBox.setItems(manufacturerSet);
            comboBox.setClearButtonVisible(true);
            comboBox.addValueChangeListener(event1 -> {
                NumberField numberField = new NumberField("Podstawowy rabat");
                NumberField numberField1 = new NumberField("Dodatkowy rabat");
                NumberField numberField2 = new NumberField("Promocja");
                NumberField numberField3 = new NumberField("Skonto");
                NumberField numberField4 = new NumberField("Podstawowy rabat");
                NumberField numberField5 = new NumberField("Dodatkowy rabat");
                NumberField numberField6 = new NumberField("Promocja");
                NumberField numberField7 = new NumberField("Skonto");
                Optional<T> main = list.stream().filter(e -> e.getManufacturer().equals(event1.getValue()) && e.getName().equals("DACHOWKA_PODSTAWOWA")).findFirst();
                if (main.isPresent()) {
                    numberField.setValue(main.get().getBasicDiscount().doubleValue());
                    numberField1.setValue(main.get().getAdditionalDiscount().doubleValue());
                    numberField2.setValue(main.get().getPromotionDiscount().doubleValue());
                    numberField3.setValue(main.get().getSkontoDiscount().doubleValue());
                }
                List<T> additionalsList = list.stream().filter(e -> e.getManufacturer().equals(event1.getValue()) && !e.getName().equals("DACHOWKA_PODSTAWOWA")).collect(Collectors.toList());
                Optional<T> additional = Optional.ofNullable(additionalsList.get(0));
                if (additional.isPresent()) {
                    numberField4.setValue(additional.get().getBasicDiscount().doubleValue());
                    numberField5.setValue(additional.get().getAdditionalDiscount().doubleValue());
                    numberField6.setValue(additional.get().getPromotionDiscount().doubleValue());
                    numberField7.setValue(additional.get().getSkontoDiscount().doubleValue());
                }
                Button button1 = new Button("Zapisz", event2 -> {
                    for (T entity : list) {
                        if (entity.getManufacturer().equals(event1.getValue())) {
                            if (entity.getName().equals("DACHOWKA_PODSTAWOWA")) {
                                entity.setBasicDiscount(numberField.getValue().intValue());
                                entity.setAdditionalDiscount(numberField1.getValue().intValue());
                                entity.setPromotionDiscount(numberField2.getValue().intValue());
                                entity.setSkontoDiscount(numberField3.getValue().intValue());
                            } else {
                                entity.setBasicDiscount(numberField4.getValue().intValue());
                                entity.setAdditionalDiscount(numberField5.getValue().intValue());
                                entity.setPromotionDiscount(numberField6.getValue().intValue());
                                entity.setSkontoDiscount(numberField7.getValue().intValue());
                            }
                            baseRepository.deleteById(entity.getId());
                            baseRepository.save(entity);
                        }
                    }
                    dialog.close();
                    dialog.removeAll();
                    grid.getDataProvider().refreshAll();
                });
                FormLayout formLayout = new FormLayout();
                FormLayout.ResponsiveStep responsiveStep = new FormLayout.ResponsiveStep("5px", 4);
                formLayout.setResponsiveSteps(responsiveStep);
                formLayout.add(numberField, numberField1, numberField2, numberField3, numberField4, numberField5, numberField6, numberField7, button1);
                dialog.add(formLayout);
            });
            dialog.add(comboBox);
            dialog.open();
        });
    }

    default Button saveToRepo(Grid<T> grid, List<T> fromRepo, List<T> actuallyList, BaseRepository<T> repository) {
        return new Button("Zapisz do bazy", event -> {

            for (T old : fromRepo) {
                for (T tiles : actuallyList) {
                    if (old.getManufacturer().equals(tiles.getManufacturer()) && old.getName().equals(tiles.getName())) {
                        if (!old.getBasicDiscount().equals(tiles.getBasicDiscount())
                                || !old.getAdditionalDiscount().equals(tiles.getAdditionalDiscount())
                                || !old.getPromotionDiscount().equals(tiles.getPromotionDiscount())
                                || !old.getSkontoDiscount().equals(tiles.getSkontoDiscount())) {
                            LocalDateTime dateTime = LocalDateTime.now();
                            DateTimeFormatter myDateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                            tiles.setDate(dateTime.format(myDateFormat));
                        }
                    }
                }
            }
            repository.saveAll(new HashSet<>(actuallyList));
            NotificationInterface.notificationOpen("Zmodyfikowano cenniki dn.    " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), NotificationVariant.LUMO_SUCCESS);
            grid.getDataProvider().refreshAll();
        });
    }

    default TextField bindTextFieldToInteger(Binder<T> binder, StringToIntegerConverter stringToIntegerConverter, ValueProvider<T, Integer> valueProvider, Setter<T, Integer> setter) {
        TextField textField = new TextField();
        binder.forField(textField)
                .withConverter(stringToIntegerConverter)
                .bind(valueProvider, setter);
        return textField;
    }

    default void settingsGrid(Grid<T> grid) {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.getColumns().forEach(e -> e.setAutoWidth(true));
        grid.setMinHeight("900px");
    }

    default void itemClickListener(Grid<T> grid, TextField textField) {
        textField.getElement()
                .addEventListener("keydown",
                        event -> grid.getEditor().cancel())
                .setFilter("event.key === 'Enter'");
        grid.addItemDoubleClickListener(event -> {
            grid.getEditor().editItem(event.getItem());
            textField.focus();
        });
    }

    default void closeListener(Grid<T> grid, Binder<T> binder) {
        grid.getEditor().addCloseListener(event -> {
            if (binder.getBean() != null) {
                BaseEntity bean = binder.getBean();
                if (bean.getDiscount() == null || bean.getQuantity() == null) {
                    bean.setDiscount(0);
                    bean.setQuantity(0d);
                }
                if (bean.getDiscount() > 30) {
                    bean.setDiscount(30);
                    NotificationInterface.notificationOpen("Maksymalny rabat to 30 %", NotificationVariant.LUMO_ERROR);
                }
                bean.setUnitDetalPrice(0d);
                bean.setUnitDetalPrice(calculateDetalPrice(bean));
                bean.setUnitDetalPrice(BigDecimal.valueOf(bean.getUnitDetalPrice() * (100 - bean.getDiscount()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                bean.setAllpriceAfterDiscount(BigDecimal.valueOf(bean.getUnitDetalPrice() * bean.getQuantity()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                bean.setAllpricePurchase(BigDecimal.valueOf(bean.getUnitPurchasePrice() * bean.getQuantity()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                bean.setAllprofit(BigDecimal.valueOf(bean.getAllpriceAfterDiscount() - bean.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
            grid.getDataProvider().refreshAll();
        });
    }
}
