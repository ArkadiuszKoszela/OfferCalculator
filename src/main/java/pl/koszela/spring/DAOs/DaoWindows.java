package pl.koszela.spring.DAOs;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.Windows;
import pl.koszela.spring.repositories.WindowsRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import static pl.koszela.spring.service.CalculatePrices.calculatePurchasePrice;

@Service
public class DaoWindows implements Dao {
    private final static Logger logger = Logger.getLogger(DaoWindows.class);

    private final WindowsRepository windowsRepository;
    private NameFromURL nameFromURL = new NameFromURL();

    @Autowired
    public DaoWindows(WindowsRepository windowsRepository) {
        this.windowsRepository = Objects.requireNonNull(windowsRepository);
    }

    @Override
    public final void readAndSaveToORM(String filePath) {
        String line = "";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath/*, StandardCharsets.UTF_8*/));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                Windows windows = new Windows();

                windows.setName(data[1]);
                windows.setSize(data[2]);
                windows.setUnitDetalPrice(Double.valueOf(data[3]));
                windows.setManufacturer(nameFromURL.getName(filePath));
                windows.setQuantity(0d);
                windows.setDiscount(0);
                windows.setBasicDiscount(36);
                windows.setAdditionalDiscount(0);
                windows.setPromotionDiscount(0);
                windows.setSkontoDiscount(0);
                windows.setUnitPurchasePrice(calculatePurchasePrice(windows));
                windowsRepository.save(windows);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug("windows cannot be imported");
        } finally {
            if (br != null) {
                try {
                    br.close();
                    logger.info("succes - import windows " + nameFromURL.getName(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}