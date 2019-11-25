package pl.koszela.spring.repositories.main;

import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.main.AccessoriesWindows;

@Repository(value = "repo_accesories_windows")
public interface AccesoriesWindowsRepository /*extends JpaRepository<AccesoriesWindows, Long>*/ extends BaseRepository<AccessoriesWindows> {
}