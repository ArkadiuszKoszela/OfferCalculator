package app.repositories;

import app.entities.EntityInputDataTiles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("input_data_tiles")
public interface InputDataTilesRepository extends CrudRepository<EntityInputDataTiles, Long> {

}
