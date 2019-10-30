package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.accesories.EntityAccesories;

import java.util.List;

@Repository(value = "repo_accesories")
public interface AccesoriesRepository extends JpaRepository<EntityAccesories, Long> {
}