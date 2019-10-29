package pl.koszela.spring.DAOs;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.EntityKolnierz;
import pl.koszela.spring.repositories.KolnierzRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class DaoKolnierz implements Dao {
    private final static Logger logger = Logger.getLogger(DaoKolnierz.class);

    private final KolnierzRepository kolnierzRepository;

    @Autowired
    public DaoKolnierz(KolnierzRepository kolnierzRepository) {
        this.kolnierzRepository = Objects.requireNonNull(kolnierzRepository);
    }

    @Override
    public final void save(String filePath, String priceListName) {
        String line = "";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath/*, StandardCharsets.UTF_8*/));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                EntityKolnierz entityKolnierz = new EntityKolnierz();

                entityKolnierz.setName(data[0]);
                entityKolnierz.setUnitRetailPrice(new BigDecimal(data[1]));
                entityKolnierz.setDiscount(Double.valueOf(data[2]));

                kolnierzRepository.save(entityKolnierz);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug("coś nie tak - nie zaimportowano kolnierz");
        } finally {
            if (br != null) {
                try {
                    br.close();
                    logger.info("succes - import kolnierz");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
