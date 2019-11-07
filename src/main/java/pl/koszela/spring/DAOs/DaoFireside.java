package pl.koszela.spring.DAOs;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.Fireside;
import pl.koszela.spring.repositories.FiresideRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
public class DaoFireside implements Dao {
    private final static Logger logger = Logger.getLogger(DaoFireside.class);

    private final FiresideRepository firesideRepository;
    private NameFromURL nameFromURL = new NameFromURL();

    @Autowired
    public DaoFireside(FiresideRepository firesideRepository) {
        this.firesideRepository = Objects.requireNonNull(firesideRepository);
    }

    @Override
    public final void readAndSaveToORM(String filePath) {
        String line = "";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath/*, StandardCharsets.UTF_8*/));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                Fireside fireSide = new Fireside();

                fireSide.setName(data[1]);
                fireSide.setUnitDetalPrice(Double.valueOf(data[2]));
                fireSide.setCategory(data[3]);
                fireSide.setManufacturer(nameFromURL.getName(filePath));
                fireSide.setQuantity(0d);
                fireSide.setDiscount(0);
                fireSide.setUnitPurchasePrice(BigDecimal.valueOf(fireSide.getUnitDetalPrice() * 0.7).setScale(2, RoundingMode.HALF_UP).doubleValue());
                firesideRepository.save(fireSide);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug("fire side cannot be imported");
        } finally {
            if (br != null) {
                try {
                    br.close();
                    logger.info("succes - import fire side " + nameFromURL.getName(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}