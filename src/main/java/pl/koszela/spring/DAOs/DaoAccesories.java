package pl.koszela.spring.DAOs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.EntityAccesories;
import pl.koszela.spring.repositories.AccesoriesRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class DaoAccesories implements Dao {

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
            br = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                EntityAccesories entityAccesories = new EntityAccesories();

                entityAccesories.setName(data[0]);
                entityAccesories.setPurchasePrice(new BigDecimal(data[1]));
                entityAccesories.setMargin(Integer.valueOf(data[2]));
                entityAccesories.setFirstMultiplier(Double.valueOf(data[3]));
                entityAccesories.setSecondMultiplier(Double.valueOf(data[4]));

                accesoriesRepository.save(entityAccesories);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Coś nie tak - save AkcesroiaEntity in AkcesoriaDao");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
