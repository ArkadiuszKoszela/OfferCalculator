package pl.koszela.spring.DAOs;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.Gutter;
import pl.koszela.spring.repositories.GutterRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

@Service
public class DaoGutter implements Dao {
    private final static Logger logger = Logger.getLogger(DaoGutter.class);

    private final GutterRepository gutterRepository;

    @Autowired
    public DaoGutter(GutterRepository gutterRepository) {
        this.gutterRepository = Objects.requireNonNull(gutterRepository);
    }

    private NameFromURL nameFromURL = new NameFromURL();

    @Override
    public final void readAndSaveToORM(String filePath) {
        String line = "";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath/*, StandardCharsets.UTF_8*/));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                Gutter gutter = new Gutter();

                gutter.setName(data[1]);
                gutter.setUnitDetalPrice(Double.valueOf(data[2]));
                gutter.setCategory(nameFromURL.getName(filePath));
                gutter.setDiscount(0);
                gutter.setUnitPurchasePrice(0d);

                gutterRepository.save(gutter);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug("gutters cannot be imported");
        } finally {
            if (br != null) {
                try {
                    br.close();
                    logger.info("succes - import gutters " + nameFromURL.getName(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}