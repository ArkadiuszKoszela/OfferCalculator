package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.EntityPersonalData;
import pl.koszela.spring.entities.EntityUser;

import java.util.List;
import java.util.Optional;

@Repository(value = "repo_users")
public interface UsersRepo extends JpaRepository<EntityUser, Long> {
    List<EntityUser> findEntityUserByEntityPersonalDataIsNotNull();

    EntityUser findEntityUserByEntityPersonalDataNameAndEntityPersonalDataSurname(String name, String surname);

    Optional<EntityUser> findEntityUserByEntityPersonalDataEquals(EntityPersonalData entityPersonalData);

    void deleteById(Long id);
}