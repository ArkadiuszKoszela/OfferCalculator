package pl.koszela.spring.DAOs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.Enums;
import pl.koszela.spring.entities.Tiles;
import pl.koszela.spring.repositories.TilesRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DaoTiles implements Dao {

    private final TilesRepository tilesRepository;

    @Autowired
    public DaoTiles(TilesRepository tilesRepository) {
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
    }

    @Override
    public final void save(String filePath, String priceListName) {
        String line;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                List<Enums> tileCategory = Arrays.asList(Enums.values());
                List<String> stringsFromFile = Arrays.asList(data);
                List<String> toRemove = new ArrayList<>();
                toRemove.add(stringsFromFile.get(0));
                List<String> collectStringsFromFile = stringsFromFile.stream().filter(e -> !toRemove.contains(e)).collect(Collectors.toList());
                for (int i = 0; i < tileCategory.size(); i++) {
                    Tiles tiles = new Tiles();
                    tiles.setName(tileCategory.get(i).name());
                    tiles.setDiscount(0);
                    tiles.setPrice(new BigDecimal(collectStringsFromFile.get(i)));
                    tiles.setPriceListName(priceListName);
                    tilesRepository.save(tiles);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("GeneratePdfReport nie tak save EntityTiles in ProductDAo");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}