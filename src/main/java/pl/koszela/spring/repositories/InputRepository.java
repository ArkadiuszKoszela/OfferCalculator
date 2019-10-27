package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.InputData;
import pl.koszela.spring.entities.accesories.EntityAccesories;

import java.util.List;

@Repository(value = "repo_input")
public interface InputRepository extends JpaRepository<InputData, Long> {
}