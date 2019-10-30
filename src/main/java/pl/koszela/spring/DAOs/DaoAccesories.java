package pl.koszela.spring.DAOs;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.accesories.EntityAccesories;
import pl.koszela.spring.repositories.AccesoriesRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
    public final void save(String filePath, String priceListName) {
        String line = "";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath/*, StandardCharsets.UTF_8*/));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                EntityAccesories entityAccesories = new EntityAccesories();

                entityAccesories.setCategory(data[1]);
                entityAccesories.setOption(data[2]);
                entityAccesories.setName(data[3]);
                entityAccesories.setUnitPurchasePrice(Double.valueOf(data[4]));
                entityAccesories.setMargin(Integer.valueOf(data[5]));

                accesoriesRepository.save(entityAccesories);
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
