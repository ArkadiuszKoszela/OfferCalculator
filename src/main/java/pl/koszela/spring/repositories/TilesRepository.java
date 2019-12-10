package pl.koszela.spring.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.koszela.spring.entities.main.Tiles;

@Transactional
@Repository(value = "tiles_repo")
public interface TilesRepository extends BaseRepository<Tiles>{
}