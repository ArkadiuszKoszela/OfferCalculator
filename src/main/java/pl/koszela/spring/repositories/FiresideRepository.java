package pl.koszela.spring.repositories;

import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.main.Fireside;

@Repository(value = "repo_fire_side")
public interface FiresideRepository extends BaseRepository<Fireside> {
}