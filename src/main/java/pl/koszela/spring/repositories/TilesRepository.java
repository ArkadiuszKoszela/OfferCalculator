package pl.koszela.spring.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.Tiles;

import java.util.List;

@Repository(value = "tiles_repo")
public interface TilesRepository extends CrudRepository<Tiles, Long> {

    List<Tiles> findByNameEquals (String name);
    List<Tiles> findByPriceListNameEquals (String priceListName);
}
