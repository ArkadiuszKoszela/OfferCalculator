package pl.koszela.spring.repositories;

import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.main.Gutter;

@Repository(value = "repo_gutter")
public interface GutterRepository extends BaseRepository<Gutter> {
}