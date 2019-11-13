package pl.koszela.spring.repositories;

import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.Accesories;

@Repository(value = "repo_accesories")
public interface AccesoriesRepository extends BaseRepository<Accesories> {
}