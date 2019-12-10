package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.main.PersonalData;
import pl.koszela.spring.entities.main.User;

import java.util.List;
import java.util.Optional;

@Repository(value = "repo_users")
public interface UsersRepo extends JpaRepository<User, Long> {
    List<User> findByPersonalDataIsNotNull();

    Optional<User> findUserByPersonalDataEquals(PersonalData personalData);

    void deleteById(Long id);
}