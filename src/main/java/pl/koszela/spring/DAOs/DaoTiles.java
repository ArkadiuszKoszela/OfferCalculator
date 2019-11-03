package pl.koszela.spring.DAOs;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.Tiles;
import pl.koszela.spring.repositories.TilesRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class DaoTiles implements Dao {
    private final static Logger logger = Logger.getLogger(DaoTiles.class);

    private final TilesRepository tilesRepository;
    private NameFromURL nameFromURL = new NameFromURL();

    @Autowired
    public DaoTiles(TilesRepository tilesRepository) {
        this.tilesRepository = Objects.requireNonNull(tilesRepository);
    }

    @Override
    public final void readAndSaveToORM(String filePath) {
        String line;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath/*, StandardCharsets.UTF_8*/));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                Tiles tiles = new Tiles();
                tiles.setName(data[1]);
                tiles.setDiscount(0);
                tiles.setUnitDetalPrice(Double.parseDouble(data[2]));
                tiles.setUnitPurchasePrice(Double.valueOf(data[3]));
                tiles.setBasicDiscount(Integer.valueOf(data[4]));
                tiles.setAdditionalDiscount(Integer.valueOf(data[5]));
                tiles.setPromotionDiscount(Integer.valueOf(data[6]));
                tiles.setSkontoDiscount(Integer.valueOf(data[7]));
                tiles.setPriceListName(nameFromURL.getName(filePath));
                tiles.setUnitPurchasePrice(calculatePurchasePrice(tiles));
                tilesRepository.save(tiles);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug("tiles cannot be imported");
        } finally {
            if (br != null) {
                try {
                    br.close();
                    logger.info("succes - import tiles " + nameFromURL.getName(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Double calculatePurchasePrice(Tiles tiles){
        BigDecimal constance = new BigDecimal(100);
        BigDecimal pricePurchase = BigDecimal.valueOf(tiles.getUnitDetalPrice());
        BigDecimal firstDiscount = (constance.subtract(new BigDecimal(tiles.getBasicDiscount()))).divide(constance, 2, RoundingMode.HALF_UP);
        BigDecimal secondDiscount = (constance.subtract(new BigDecimal(tiles.getPromotionDiscount()))).divide(constance, 2, RoundingMode.HALF_UP);
        BigDecimal thirdDiscount = (constance.subtract(new BigDecimal(tiles.getAdditionalDiscount()))).divide(constance, 2, RoundingMode.HALF_UP);
        BigDecimal fourthDiscount = (constance.subtract(new BigDecimal(tiles.getSkontoDiscount()))).divide(constance, 2, RoundingMode.HALF_UP);
        BigDecimal result = pricePurchase.multiply(firstDiscount).multiply(secondDiscount).multiply(thirdDiscount).multiply(fourthDiscount).setScale(2, RoundingMode.HALF_UP);
        return result.doubleValue();
    }
}