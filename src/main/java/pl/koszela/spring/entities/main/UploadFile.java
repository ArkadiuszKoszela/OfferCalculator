package pl.koszela.spring.entities.main;

import javax.persistence.*;

@Entity
@Table(name = "upload_files")
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameFolder;
    private String nameFile;

    public UploadFile() {
    }

    public UploadFile(String nameFolder, String nameFile) {
        this.nameFolder = nameFolder;
        this.nameFile = nameFile;
    }

    public String getNameFolder() {
        return this.nameFolder;
    }

    public void setNameFolder(String nameFolder) {
        this.nameFolder = nameFolder.trim();
    }

    public String getNameFile() {
        return this.nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile.trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
