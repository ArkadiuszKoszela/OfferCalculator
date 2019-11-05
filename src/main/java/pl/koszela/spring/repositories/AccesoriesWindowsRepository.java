package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.AccesoriesWindows;
import pl.koszela.spring.entities.Collar;

@Repository(value = "repo_accesories_windows")
public interface AccesoriesWindowsRepository extends JpaRepository<AccesoriesWindows, Long> {
}