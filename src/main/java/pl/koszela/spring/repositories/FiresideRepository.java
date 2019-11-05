package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.Fireside;

@Repository(value = "repo_fire_side")
public interface FiresideRepository extends JpaRepository<Fireside, Long> {
}