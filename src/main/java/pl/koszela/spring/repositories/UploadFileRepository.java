package pl.koszela.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.koszela.spring.entities.main.BaseEntity;
import pl.koszela.spring.entities.main.UploadFile;

@Repository(value = "upload_file_repo")
public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
}