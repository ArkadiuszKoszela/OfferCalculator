package pl.koszela.spring.beforeRun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.koszela.spring.entities.EntityResultTiles;
import pl.koszela.spring.repositories.ResultTilesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class BeforeRunApp {

    private ResultTilesRepository resultTilesRepository;

    private List<EntityResultTiles> resultTiles;

    @Autowired
    public BeforeRunApp(ResultTilesRepository resultTilesRepository) {
        this.resultTilesRepository = Objects.requireNonNull(resultTilesRepository);
    }



    @EventListener(ApplicationReadyEvent.class)
    public List<EntityResultTiles> createFields() {
        resultTiles = new ArrayList<>();
        resultTiles.add(EntityResultTiles.builder()
                .name("Dachówka podstawowa").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Dachówka skrajna lewa").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Dachówka skrajna prawa").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Dachówka dwufalowa").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Dachówka wentylacyjna").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Komplet kominka wentylacyjnego").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Gąsior podstawowy").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Gąsior początkowy kalenica prosta").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Gąsior końcowy kalenica prosta").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Płytka początkowa").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Płytka końcowa").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Trójnik").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Gąsior zaokrąglony").build());
        /*resultTiles.add(EntityResultTiles.builder()
                .name("Trójnik").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Gąsior z podwójną mufą").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Dachówka skrajna 3/4 lewa ").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Dachówka skrajna 3/4 prawa ").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Dachówka skrajna 5/4 lewa ").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Dachówka skrajna 5/4 prawa ").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Dachówka skrajna 1/2 lewa ").build());
        resultTiles.add(EntityResultTiles.builder()
                .name("Dachówka skrajna 1/2 prawa ").build());*/
        resultTilesRepository.saveAll(resultTiles);
        return resultTiles;
    }

}