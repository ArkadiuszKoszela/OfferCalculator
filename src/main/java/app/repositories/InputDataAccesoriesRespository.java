package app.repositories;

import app.entities.EntityInputDataAccesories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("input_data_accesories")
public interface InputDataAccesoriesRespository extends CrudRepository<EntityInputDataAccesories, Long> {
}
