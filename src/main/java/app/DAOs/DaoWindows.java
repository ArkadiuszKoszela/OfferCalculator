package app.DAOs;

import app.entities.EntityKolnierz;
import app.repositories.KolnierzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

@Service
public class DaoWindows implements Dao {

    private final KolnierzRepository kolnierzRepository;

    @Autowired
    public DaoWindows(KolnierzRepository kolnierzRepository) {
        this.kolnierzRepository = Objects.requireNonNull(kolnierzRepository);
    }

    @Override
    public final void save(String filePath) {
        String line = "";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath));
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
            System.out.println("Coś nie tak - save EntityWinodws in WindowsDao");
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
