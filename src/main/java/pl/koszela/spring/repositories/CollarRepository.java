package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.Collar;

@Repository(value = "repo_collar")
public interface CollarRepository extends JpaRepository<Collar, Long> {
}