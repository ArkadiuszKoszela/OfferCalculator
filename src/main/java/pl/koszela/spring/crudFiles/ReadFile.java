package pl.koszela.spring.crudFiles;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.main.UploadFile;
import pl.koszela.spring.repositories.UploadFileRepository;
import pl.koszela.spring.service.HasLogger;

import java.io.IOException;
import java.util.Set;

import static pl.koszela.spring.crudFiles.ConfigToFtpServer.*;
import static pl.koszela.spring.views.priceLists.AccessoriesPriceListView.*;

@Service
public class ReadFile implements HasLogger {

    public void listFolder(String remotePath, Set<UploadFile> uploadFiles, UploadFileRepository uploadFileRepository) throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setDefaultPort(21);
        ftpClient.connect(FTP_ADDRESS, PORT);
        boolean success = ftpClient.login(USERNAME, PASSWORD);
        FTPFile[] directories = ftpClient.listDirectories(remotePath);

        ftpClient.logout();
        ftpClient.disconnect();

        uploadFileRepository.deleteAll();
        if (uploadFileRepository.count() == 0) {
            System.out.println("Usunięto wszystko");
        }

        for (FTPFile directory : directories) {
            UploadFile uploadFile = new UploadFile();
            if (!directory.getName().equals(".") && !directory.getName().equals("..")) {
                if (directory.isDirectory()) {
                    getLogger().info("Directory->: " + directory.getName());
                    uploadFile.setNameFolder(directory.getName());
                    String[] files = getFileName(directory, remotePath);
                    if (files != null) {
                        if (files.length == 2) {
                            getLogger().info("File not found");
                            uploadFile.setNameFile("File not found");
                            uploadFiles.add(uploadFile);
                        } else {
                            for (String file : files) {
                                if (!file.equals(".") && !file.equals("..")) {
                                    getLogger().info("File From        ->: " + file);
                                    uploadFile.setNameFile(file);
                                    uploadFiles.add(uploadFile);
                                }
                            }
                        }
                    }
                }
            }
        }
        getLogger().info("Finish import");
    }

    private static String[] getFileName(FTPFile directories, String remotePath) throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setDefaultPort(21);
        ftpClient.connect(FTP_ADDRESS, PORT);
        boolean success = ftpClient.login(USERNAME, PASSWORD);
        String[] files = ftpClient.listNames(remotePath + "/" + directories.getName());
        ftpClient.logout();
        ftpClient.disconnect();
        return files;
    }
}
