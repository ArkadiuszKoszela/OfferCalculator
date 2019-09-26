package pl.koszela.spring.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.EntityInputDataAccesories;

@Repository("input_data_accesories")
public interface InputDataAccesoriesRespository extends CrudRepository<EntityInputDataAccesories, Long> {
}