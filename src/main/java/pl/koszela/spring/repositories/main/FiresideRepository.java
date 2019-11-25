package pl.koszela.spring.repositories.main;

import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.main.Fireside;

@Repository(value = "repo_fire_side")
public interface FiresideRepository /*extends JpaRepository<Fireside, Long>*/ extends BaseRepository<Fireside> {
}