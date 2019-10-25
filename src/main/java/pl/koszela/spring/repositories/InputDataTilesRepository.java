package pl.koszela.spring.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.tiles.EntityInputDataTiles;

@Repository("input_data_tiles")
public interface InputDataTilesRepository extends CrudRepository<EntityInputDataTiles, Long> {

}
