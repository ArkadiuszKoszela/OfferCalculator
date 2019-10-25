package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.accesories.EntityAccesories;
import pl.koszela.spring.entities.gutter.InputGutterData;

import java.util.List;

@Repository(value = "repo_inputGutterData")
public interface InputGutterDataRepository extends JpaRepository<InputGutterData, Long> {
}