package pl.koszela.spring.DAOs;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.AccesoriesWindows;
import pl.koszela.spring.entities.Collar;
import pl.koszela.spring.repositories.AccesoriesWindowsRepository;
import pl.koszela.spring.repositories.CollarRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import static pl.koszela.spring.service.CalculatePrices.calculatePurchasePrice;

@Service
public class DaoAccesoriesWindows implements Dao {
    private final static Logger logger = Logger.getLogger(DaoAccesoriesWindows.class);

    private final AccesoriesWindowsRepository accesoriesWindowsRepository;
    private NameFromURL nameFromURL = new NameFromURL();

    @Autowired
    public DaoAccesoriesWindows(AccesoriesWindowsRepository accesoriesWindowsRepository) {
        this.accesoriesWindowsRepository = Objects.requireNonNull(accesoriesWindowsRepository);
    }

    @Override
    public final void readAndSaveToORM(String filePath) {
        String line = "";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath/*, StandardCharsets.UTF_8*/));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                AccesoriesWindows accesoriesWindows = new AccesoriesWindows();

                accesoriesWindows.setName(data[1]);
                accesoriesWindows.setSize(data[2]);
                accesoriesWindows.setUnitDetalPrice(Double.valueOf(data[3]));
                accesoriesWindows.setManufacturer(nameFromURL.getName(filePath));
                accesoriesWindows.setQuantity(0d);
                accesoriesWindows.setDiscount(0);
                accesoriesWindows.setBasicDiscount(30);
                accesoriesWindows.setAdditionalDiscount(0);
                accesoriesWindows.setPromotionDiscount(0);
                accesoriesWindows.setSkontoDiscount(0);
                accesoriesWindows.setUnitPurchasePrice(calculatePurchasePrice(accesoriesWindows));
                accesoriesWindowsRepository.save(accesoriesWindows);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug("accesories from windows cannot be imported");
        } finally {
            if (br != null) {
                try {
                    br.close();
                    logger.info("succes - import accesories from windows " + nameFromURL.getName(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}