package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.gutter.EntityGutter;

@Repository(value = "repo_gutter")
public interface GutterRepository extends JpaRepository<EntityGutter, Long> {
}