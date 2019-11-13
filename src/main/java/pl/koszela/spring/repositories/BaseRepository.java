package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.Accesories;
import pl.koszela.spring.entities.BaseEntity;

import java.util.List;

public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {
}