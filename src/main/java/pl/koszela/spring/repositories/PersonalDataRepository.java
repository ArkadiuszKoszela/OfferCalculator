package pl.koszela.spring.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.koszela.spring.entities.EntityPersonalData;

public interface PersonalDataRepository extends CrudRepository<EntityPersonalData, Long> {

        EntityPersonalData findUsersEntityByNameAndSurnameEquals(String name, String surname);
}
