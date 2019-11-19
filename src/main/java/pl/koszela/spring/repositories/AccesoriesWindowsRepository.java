package pl.koszela.spring.repositories;

import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.AccessoriesWindows;

@Repository(value = "repo_accesories_windows")
public interface AccesoriesWindowsRepository /*extends JpaRepository<AccesoriesWindows, Long>*/ extends BaseRepository<AccessoriesWindows> {
}