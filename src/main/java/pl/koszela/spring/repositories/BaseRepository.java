package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.koszela.spring.entities.main.BaseEntity;

public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {
}