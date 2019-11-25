package pl.koszela.spring.repositories.main;

import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.main.Collar;

@Repository(value = "repo_collar")
public interface CollarRepository /*extends JpaRepository<Collar, Long>*/ extends BaseRepository<Collar> {
}