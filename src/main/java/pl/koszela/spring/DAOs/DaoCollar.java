package pl.koszela.spring.DAOs;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.Collar;
import pl.koszela.spring.entities.Windows;
import pl.koszela.spring.repositories.CollarRepository;
import pl.koszela.spring.repositories.WindowsRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import static pl.koszela.spring.service.CalculatePrices.calculatePurchasePrice;

@Service
public class DaoCollar implements Dao {
    private final static Logger logger = Logger.getLogger(DaoCollar.class);

    private final CollarRepository collarRepository;
    private NameFromURL nameFromURL = new NameFromURL();

    @Autowired
    public DaoCollar(CollarRepository collarRepository) {
        this.collarRepository = Objects.requireNonNull(collarRepository);
    }

    @Override
    public final void readAndSaveToORM(String filePath) {
        String line = "";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath/*, StandardCharsets.UTF_8*/));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                Collar collar = new Collar();

                collar.setName(data[1]);
                collar.setSize(data[2]);
                collar.setUnitDetalPrice(Double.valueOf(data[3]));
                collar.setManufacturer(nameFromURL.getName(filePath));
                collar.setQuantity(0d);
                collar.setDiscount(0);
                collar.setBasicDiscount(30);
                collar.setAdditionalDiscount(0);
                collar.setPromotionDiscount(0);
                collar.setSkontoDiscount(0);
                collar.setUnitPurchasePrice(calculatePurchasePrice(collar));
                collarRepository.save(collar);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug("collar cannot be imported");
        } finally {
            if (br != null) {
                try {
                    br.close();
                    logger.info("succes - import collar " + nameFromURL.getName(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}