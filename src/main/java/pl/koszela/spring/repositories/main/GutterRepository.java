package pl.koszela.spring.repositories.main;

import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.main.Gutter;

@Repository(value = "repo_gutter")
public interface GutterRepository /*extends JpaRepository<Gutter, Long>*/ extends BaseRepository<Gutter> {
}