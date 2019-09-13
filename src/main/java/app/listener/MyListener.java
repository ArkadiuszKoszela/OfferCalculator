package app.listener;

import app.entities.EntityResultTiles;
import app.repositories.InputData;
import app.repositories.ResultTiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyListener {

    @Autowired
    private ResultTiles resultTiles;

    @Autowired
    private InputData inputData;

    private List<EntityResultTiles> listaDoTabeliGrid;


    @EventListener(ApplicationReadyEvent.class)
    public List<EntityResultTiles> createListaDoTabeliGrid() {
        listaDoTabeliGrid = new ArrayList<>();
        listaDoTabeliGrid.add(new EntityResultTiles("Dachówka podstawowa", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Dachówka skrajna lewa", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Dachówka skrajna prawa", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Dachówka dwufalowa", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Dachówka wentylacyjna", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Komplet kominka wentylacyjnego", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Gąsior podstawowy", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Gąsior początkowy kalenica prosta", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Gąsior końcowy kalenica prosta", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Płytka początkowa", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Płytka końcowa", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Trójnik", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Gąsior zaokrąglony", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Trójnik", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Gąsior z podwójną mufą", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Dachówka skrajna 3/4 lewa ", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Dachówka skrajna 3/4 prawa ", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Dachówka skrajna 5/4 lewa ", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Dachówka skrajna 5/4 prawa ", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Dachówka skrajna 1/2 lewa ", "0", "0", "0", "0"));
        listaDoTabeliGrid.add(new EntityResultTiles("Dachówka skrajna 1/2 prawa ", "0", "0", "0", "0"));
        resultTiles.saveAll(listaDoTabeliGrid);
        return listaDoTabeliGrid;
    }

}