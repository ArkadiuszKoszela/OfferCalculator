package app.repositories;

import app.entities.EntityTiles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "repo_tiles")
public interface Tiles extends CrudRepository<EntityTiles, Long> {

    List<EntityTiles> findByPriceListNameAndType(String nazwaCennika, String typ);

    List<EntityTiles> findByPriceListNameContains(String nazwa);





}
