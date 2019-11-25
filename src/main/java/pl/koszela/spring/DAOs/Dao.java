package pl.koszela.spring.DAOs;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.koszela.spring.entities.main.BaseEntity;
import pl.koszela.spring.repositories.main.BaseRepository;
import pl.koszela.spring.service.HasLogger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static pl.koszela.spring.service.CalculatePrices.calculatePurchasePrice;

@Service
public class Dao  implements HasLogger {
//    private Logger logger = Logger.getLogger(Dao.class);

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
            getLogger().debug(type.getName() + " cannot be imported");
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                    getLogger().info("success - import " + type.getName());
                } catch (IOException f) {
                    f.printStackTrace();
                }
            }
        }
    }

//    @Transactional("mainTransactionManager")
    public <T extends BaseEntity> void deleteAllFromRepo(BaseRepository<T> baseRepository) {
        baseRepository.deleteAll();
        getLogger().info("deleted all from " + BaseRepository.class.getName());
    }

    private String string (String[] strings, int i){
        if(i < strings.length) {
            return strings[i];
        }else {
            return "";
        }
    }
}