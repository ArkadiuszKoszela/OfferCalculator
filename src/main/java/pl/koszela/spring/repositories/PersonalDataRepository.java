package pl.koszela.spring.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.EntityPersonalData;

import java.util.Optional;

@Repository
public interface PersonalDataRepository extends CrudRepository<EntityPersonalData, Long> {
    Optional<EntityPersonalData> findEntityPersonalDataByNameAndSurnameEquals(String name, String surname);
}