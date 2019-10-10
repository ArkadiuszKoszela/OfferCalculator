package pl.koszela.spring.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.EntityPersonalData;

@Repository
public interface PersonalDataRepository extends CrudRepository<EntityPersonalData, Long> {
    EntityPersonalData findUsersEntityByNameAndSurnameEquals(String name, String surname);
}