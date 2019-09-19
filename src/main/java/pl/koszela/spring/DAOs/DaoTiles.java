package pl.koszela.spring.DAOs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.EntityTiles;
import pl.koszela.spring.repositories.TilesRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

@Service
public class DaoTiles implements Dao {

    private final TilesRepository tilesRepository;

    @Autowired
    public DaoTiles(TilesRepository tilesRepository) {
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
    }

    @Override
    public final void save(String filePath) {
        String line = "";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                EntityTiles entityTiles = new EntityTiles();

                entityTiles.setPriceListName(data[0]);
                entityTiles.setType(data[1]);
                entityTiles.setName(data[2]);
                entityTiles.setUnitRetailPrice(new BigDecimal(data[3]));
                entityTiles.setProfit(Integer.valueOf(data[4]));
                entityTiles.setBasicDiscount(Integer.valueOf(data[5]));
                entityTiles.setSupplierDiscount(Integer.valueOf(data[6]));
                entityTiles.setAdditionalDiscount(Integer.valueOf(data[7]));
                entityTiles.setSkontoDiscount(Integer.valueOf(data[8]));

                tilesRepository.save(entityTiles);
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
