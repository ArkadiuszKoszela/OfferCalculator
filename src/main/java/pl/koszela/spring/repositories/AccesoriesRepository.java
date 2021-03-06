package pl.koszela.spring.repositories;

import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.main.Accessories;

@Repository(value = "repo_accesories")
public interface AccesoriesRepository extends BaseRepository<Accessories> {
}