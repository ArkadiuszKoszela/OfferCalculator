package pl.koszela.spring.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.koszela.spring.entities.EntityUser;

@Transactional
@Repository(value = "repo_users")
public interface UsersRepo extends CrudRepository<EntityUser, Long> {

    EntityUser findUsersEntityByNameAndSurnameEquals(String name, String surname);
    EntityUser findEntityUserByNameAndSurnameContains(String name, String surname);

}
