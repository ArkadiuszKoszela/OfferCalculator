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
                List<Enums> list = Arrays.asList(Enums.values());
                Set<Tiles> set = new HashSet<>();
                List<String> stringList = Arrays.asList(data);
                List<String> doUsuniecia = new ArrayList<>();
                doUsuniecia.add(stringList.get(0));
                List<String> poprawna = stringList.stream().filter(e -> !doUsuniecia.contains(e)).collect(Collectors.toList());
                for (int i = 0; i < list.size(); i++) {
                    Tiles tiles = new Tiles();
                    tiles.setName(list.get(i).name());
                    tiles.setPrice(new BigDecimal(poprawna.get(i)));
                    tiles.setPriceListName(priceListName);
                    set.add(tiles);
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
