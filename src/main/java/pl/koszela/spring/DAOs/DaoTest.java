package pl.koszela.spring.DAOs;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.BaseEntity;
import pl.koszela.spring.repositories.BaseRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static pl.koszela.spring.service.CalculatePrices.calculatePurchasePrice;

@Service
public class DaoTest {
    private Logger logger = Logger.getLogger(DaoTest.class);

    private NameFromURL nameFromURL = new NameFromURL();

    public <T extends BaseEntity> void readAndSaveToORMTest(String filePath, Class<T> type, T entity, BaseRepository<T> repository) {
        String line = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                entity = type.getDeclaredConstructor().newInstance();

                entity.setName(string(data, 1));
                entity.setUnitDetalPrice(Double.valueOf(string(data, 2)));
                entity.setCategory(string(data, 3));
                entity.setSize(string(data, 4));
                entity.setType(string(data, 9));
                entity.setQuantity(0d);
                entity.setDiscount(0);
                entity.setBasicDiscount(67);
                entity.setAdditionalDiscount(0);
                entity.setPromotionDiscount(0);
                entity.setSkontoDiscount(0);
                entity.setManufacturer(nameFromURL.getName(filePath));
                entity.setUnitPurchasePrice(calculatePurchasePrice(entity));

                repository.save(entity);
            }
        } catch (IOException f) {
            f.printStackTrace();
            logger.debug(type.getName() + " cannot be imported");
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                    logger.info("succes - import " + type.getName());
                } catch (IOException f) {
                    f.printStackTrace();
                }
            }
        }
    }

    private String string (String[] strings, int i){
        if(i < strings.length) {
            return strings[i];
        }else {
            return "";
        }
    }
}