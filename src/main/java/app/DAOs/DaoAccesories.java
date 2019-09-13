package app.DAOs;

import app.entities.EntityAccesories;
import app.repositories.Accesories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

@Service
public class DaoAccesories implements Dao {

    private Accesories accesories;

    public DaoAccesories() {
    }

    @Autowired
    public DaoAccesories(Accesories accesories) {
        this.accesories = accesories;
    }

    @Override
    public final void save(String sciezkaDostepu) {
        String line = "";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(sciezkaDostepu));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                EntityAccesories entityAccesories = new EntityAccesories();

                entityAccesories.setName(data[0]);
                entityAccesories.setPurchasePrice(new BigDecimal(data[1]));
                entityAccesories.setMargin(Integer.valueOf(data[2]));
                entityAccesories.setFirstMultiplier(Double.valueOf(data[3]));
                entityAccesories.setSecondMultiplier(Double.valueOf(data[4]));

                accesories.save(entityAccesories);
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
