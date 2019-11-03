package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.Gutter;

@Repository(value = "repo_gutter")
public interface GutterRepository extends JpaRepository<Gutter, Long> {
}