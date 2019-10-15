package pl.koszela.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.Category;
import pl.koszela.spring.entities.Tiles;
import pl.koszela.spring.repositories.TilesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AvailablePriceList {

    private TilesRepository tilesRepository;

    @Autowired
    public AvailablePriceList(TilesRepository tilesRepository) {
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
    }

    public List<String> getAvailablePriceList() {
        List<Tiles> allTilesFromRepository = tilesRepository.findByNameEquals(Category.DACHOWKA_PODSTAWOWA.toString());
        List<String> allTiles = new ArrayList<>();
        allTilesFromRepository.forEach(e -> allTiles
                .add(e.getPriceListName()));
        return allTiles;
    }
}