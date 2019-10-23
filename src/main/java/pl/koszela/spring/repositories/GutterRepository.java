package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.EntityAccesories;
import pl.koszela.spring.entities.EntityGutter;

import java.util.List;

@Repository(value = "repo_gutter")
public interface GutterRepository extends JpaRepository<EntityGutter, Long> {
}