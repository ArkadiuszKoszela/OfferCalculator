package pl.koszela.spring.DAOs;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.LightningProtectionSystem;
import pl.koszela.spring.repositories.LightningProtectionSystemRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import static pl.koszela.spring.service.CalculatePrices.calculatePurchasePrice;


@Service
public class DaoLigtningProtectionSystem implements Dao {
    private final static Logger logger = Logger.getLogger(DaoLigtningProtectionSystem.class);

    private final LightningProtectionSystemRepository lightningProtectionSystemRepository;

    @Autowired
    public DaoLigtningProtectionSystem(LightningProtectionSystemRepository lightningProtectionSystemRepository) {
        this.lightningProtectionSystemRepository = Objects.requireNonNull(lightningProtectionSystemRepository);
    }

    @Override
    public final void readAndSaveToORM(String filePath) {
        String line = "";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath/*, StandardCharsets.UTF_8*/));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                LightningProtectionSystem ligtningProtectionSystem = new LightningProtectionSystem();

                ligtningProtectionSystem.setName(data[1]);
                ligtningProtectionSystem.setCategory(data[3]);
                ligtningProtectionSystem.setUnitDetalPrice(Double.valueOf(data[2]));
                ligtningProtectionSystem.setQuantity(0d);
                ligtningProtectionSystem.setDiscount(0);
                ligtningProtectionSystem.setBasicDiscount(30);
                ligtningProtectionSystem.setAdditionalDiscount(0);
                ligtningProtectionSystem.setPromotionDiscount(0);
                ligtningProtectionSystem.setSkontoDiscount(0);
                ligtningProtectionSystem.setUnitPurchasePrice(calculatePurchasePrice(ligtningProtectionSystem));
                lightningProtectionSystemRepository.save(ligtningProtectionSystem);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug("ligtning protection system cannot be imported");
        } finally {
            if (br != null) {
                try {
                    br.close();
                    logger.info("succes - import ligtning protection system");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}