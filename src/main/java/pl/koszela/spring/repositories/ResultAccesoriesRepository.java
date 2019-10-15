package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.EntityResultAccesories;

@Repository(value = "resultAccesories_repo")
public interface ResultAccesoriesRepository extends JpaRepository<EntityResultAccesories, Long> {
}