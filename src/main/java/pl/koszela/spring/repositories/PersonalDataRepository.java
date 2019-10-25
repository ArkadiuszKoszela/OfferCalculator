package pl.koszela.spring.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.personalData.EntityPersonalData;

import java.util.Optional;

@Repository
public interface PersonalDataRepository extends CrudRepository<EntityPersonalData, Long> {
    EntityPersonalData findUsersEntityByNameAndSurnameEquals(String name, String surname);

    Optional<EntityPersonalData> findEntityPersonalDataByNameAndSurnameAndAdressEquals(String name, String surname, String adress);

    Optional<EntityPersonalData> findEntityPersonalDataByNameAndSurnameEquals(String name, String surname);
}