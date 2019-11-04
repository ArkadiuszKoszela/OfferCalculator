package pl.koszela.spring.service;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.List;

public interface GridInteraface {
    TreeGrid createGrid();

    void itemClickListener(TextField textField);

    void closeListener();

    TreeData addItems(List list);

    TextField editField(StringToIntegerConverter stringToIntegerConverter, StringToDoubleConverter stringToDoubleConverter);

    ComponentRenderer createComponent();

    default void addEnterEvent(TreeGrid treeGrid, TextField textField){
        textField.getElement()
                .addEventListener("keydown",
                        event -> treeGrid.getEditor().cancel())
                .setFilter("event.key === 'Enter'");
    }
}