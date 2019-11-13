package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.koszela.spring.entities.Tiles;

@Transactional
@Repository(value = "tiles_repo")
public interface TilesRepository /*extends JpaRepository<Tiles, Long> */ extends BaseRepository<Tiles>{
}