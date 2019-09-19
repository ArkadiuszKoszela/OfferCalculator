package pl.koszela.spring.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.EntityTiles;

import java.util.List;

@Repository(value = "repo_tiles")
public interface TilesRepository extends CrudRepository<EntityTiles, Long> {

    List<EntityTiles> findByPriceListNameAndType(String priceList, String type);

    List<EntityTiles> findByPriceListNameContains(String name);
}
