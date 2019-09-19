package pl.koszela.spring.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.EntityResultTiles;

import java.util.List;

@Repository("result_tiles")
public interface ResultTilesRepository extends CrudRepository<EntityResultTiles, Long> {

    List<EntityResultTiles> findAllByPriceListName(String priceList);
}
