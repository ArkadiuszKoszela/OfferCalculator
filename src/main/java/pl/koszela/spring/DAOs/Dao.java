package pl.koszela.spring.DAOs;

import org.springframework.stereotype.Service;

@Service
public interface Dao {
    void readAndSaveToORM(String filePath);
}
