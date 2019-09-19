package pl.koszela.spring.DAOs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.EntityWindows;
import pl.koszela.spring.repositories.WindowsRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

@Service
public class DaoKolnierz implements Dao {

    private final WindowsRepository windowsRepository;

    @Autowired
    public DaoKolnierz(WindowsRepository windowsRepository) {
        this.windowsRepository = Objects.requireNonNull(windowsRepository);
    }

    @Override
    public final void save(String filePath) {
        String line = "";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                EntityWindows entityWindows = new EntityWindows();

                entityWindows.setName(data[0]);
                entityWindows.setUnitRetailPrice(new BigDecimal(data[1]));
                entityWindows.setDiscount(Double.valueOf(data[2]));

                windowsRepository.save(entityWindows);
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
