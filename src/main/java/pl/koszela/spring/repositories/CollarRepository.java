package pl.koszela.spring.repositories;

import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.main.Collar;

@Repository(value = "repo_collar")
public interface CollarRepository extends BaseRepository<Collar> {
}