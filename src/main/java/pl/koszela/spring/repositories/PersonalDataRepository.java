package pl.koszela.spring.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.main.PersonalData;

import java.util.Optional;

@Repository(value = "repo_personal_data")
public interface PersonalDataRepository extends CrudRepository<PersonalData, Long> {
    Optional<PersonalData> findEntityPersonalDataByNameAndSurnameEquals(String name, String surname);
}