package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.Accesories;

@Repository(value = "repo_accesories")
public interface AccesoriesRepository extends JpaRepository<Accesories, Long> {
}