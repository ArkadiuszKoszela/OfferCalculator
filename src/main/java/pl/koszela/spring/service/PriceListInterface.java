package pl.koszela.spring.service;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.apache.commons.lang3.StringUtils;
import pl.koszela.spring.entities.Accessories;
import pl.koszela.spring.entities.BaseEntity;
import pl.koszela.spring.entities.Tiles;
import pl.koszela.spring.repositories.BaseRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;

public interface PriceListInterface<E extends BaseEntity> extends GridInteraface<E> {

    default Grid<E> createGrid(Grid<E> grid, Binder<E> binder, List<E> list) {
        TextField filter = new TextField();
        ListDataProvider<E> listDataProvider = new ListDataProvider<>(list);
        grid.setDataProvider(new ListDataProvider<>(list));

        Grid.Column<E> priceListNameColumn = grid.addColumn(E::getManufacturer).setHeader("Nazwa Cennika");
        Grid.Column<E> nameColumn = grid.addColumn(E::getName).setHeader("Nazwa");
        Grid.Column<E> priceColumn = grid.addColumn(E::getUnitDetalPrice).setHeader("Cena detal");
        Grid.Column<E> basicDiscountColumn = grid.addColumn(E::getBasicDiscount).setHeader("Podstawowy rabat");
        Grid.Column<E> promotionDiscountColumn = grid.addColumn(E::getPromotionDiscount).setHeader("Promocja");
        Grid.Column<E> additionalDiscountColumn = grid.addColumn(E::getAdditionalDiscount).setHeader("Dodatkowy rabat");
        Grid.Column<E> skontoDiscountColumn = grid.addColumn(E::getSkontoDiscount).setHeader("Skonto");
        Grid.Column<E> priceFromRepoColumn = grid.addColumn(E::getUnitPurchasePrice).setHeader("Cena zakupu(z ręki)");
        Grid.Column<E> date = grid.addColumn(E::getDate).setHeader("Data zmiany");

        binder = new Binder<>();
        grid.getEditor().setBinder(binder);

        HeaderRow filterRow = grid.appendHeaderRow();
        filter.addValueChangeListener(event -> listDataProvider.addFilter(
                tiles -> StringUtils.containsIgnoreCase(tiles.getName(), filter.getValue())
        ));

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(priceListNameColumn).setComponent(filter);
        filter.setSizeFull();
        filter.setPlaceholder("Filter");

        TextField basicDiscount = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), E::getBasicDiscount, E::setBasicDiscount);
        itemClickListener(grid, basicDiscount);
        basicDiscountColumn.setEditorComponent(basicDiscount);

        TextField promotionDiscount = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), E::getPromotionDiscount, E::setPromotionDiscount);
        itemClickListener(grid, promotionDiscount);
        promotionDiscountColumn.setEditorComponent(promotionDiscount);

        TextField additionalDiscount = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), E::getAdditionalDiscount, E::setAdditionalDiscount);
        itemClickListener(grid, additionalDiscount);
        additionalDiscountColumn.setEditorComponent(additionalDiscount);

        TextField skontoDiscount = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), E::getSkontoDiscount, E::setSkontoDiscount);
        itemClickListener(grid, skontoDiscount);
        skontoDiscountColumn.setEditorComponent(skontoDiscount);

        closeListener(grid, binder);
        settingsGrid(grid);
        return grid;
    }

    default Button saveToRepo(Grid<E> grid, List<E> fromRepo, List<E> actuallyList, BaseRepository<E> repository) {
        return new Button("Zapisz do bazy", event -> {

            for (E old : fromRepo) {
                for (E tiles : actuallyList) {
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
}
