package pl.koszela.spring.DAOs;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.Accesories;
import pl.koszela.spring.repositories.AccesoriesRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
public class DaoAccesories implements Dao {
    private final static Logger logger = Logger.getLogger(DaoAccesories.class);

    private final AccesoriesRepository accesoriesRepository;

    @Autowired
    public DaoAccesories(AccesoriesRepository accesoriesRepository) {
        this.accesoriesRepository = Objects.requireNonNull(accesoriesRepository);
    }

    @Override
    public final void readAndSaveToORM(String filePath) {
        String line = "";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath/*, StandardCharsets.UTF_8*/));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                Accesories accesories = new Accesories();

                accesories.setCategory(data[1]);
                accesories.setOption(data[2]);
                accesories.setName(data[3]);
                accesories.setUnitPurchasePrice(Double.valueOf(data[4]));
                accesories.setMargin(Integer.valueOf(data[5]));
                accesories.setUnitDetalPrice(calculatePurchasePrice(accesories));

                accesoriesRepository.save(accesories);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug("accesories cannot be imported");
        } finally {
            if (br != null) {
                try {
                    br.close();
                    logger.info("succes - import accesories");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Double calculatePurchasePrice(Accesories accesories) {
        BigDecimal constance = new BigDecimal(100);
        BigDecimal margin = BigDecimal.valueOf(accesories.getMargin());
        BigDecimal pricePurchase = BigDecimal.valueOf(accesories.getUnitPurchasePrice());
        BigDecimal priceDetal = pricePurchase.multiply(margin.divide(constance, 2, RoundingMode.HALF_UP)).add(pricePurchase).setScale(2, RoundingMode.HALF_UP);
        return priceDetal.doubleValue();
    }
}
