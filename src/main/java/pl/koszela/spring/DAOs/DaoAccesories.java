package pl.koszela.spring.DAOs;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.Accesories;
import pl.koszela.spring.repositories.AccesoriesRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import static pl.koszela.spring.service.CalculatePrices.calculatePurchasePrice;

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
                accesories.setType(data[2]);
                accesories.setName(data[3]);
                accesories.setUnitDetalPrice(Double.valueOf(data[4]));
                accesories.setDiscount(0);
                accesories.setBasicDiscount(67);
                accesories.setAdditionalDiscount(0);
                accesories.setPromotionDiscount(0);
                accesories.setSkontoDiscount(0);
                accesories.setMargin(Integer.valueOf(data[5]));
                accesories.setUnitPurchasePrice(calculatePurchasePrice(accesories));

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
}