package pl.koszela.spring.crudFiles;

import com.vaadin.flow.component.notification.NotificationVariant;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;
import pl.koszela.spring.entities.main.UploadFile;
import pl.koszela.spring.repositories.UploadFileRepository;
import pl.koszela.spring.service.HasLogger;
import pl.koszela.spring.service.NotificationInterface;

import java.util.Set;

import static pl.koszela.spring.crudFiles.ConfigToFtpServer.*;
import static pl.koszela.spring.views.priceLists.AccessoriesPriceListView.*;

@Service
public class DeleteFile implements HasLogger {

    public boolean deleteFile(UploadFile uploadFile, String remotePath, Set<UploadFile> uploadFiles, UploadFileRepository uploadFileRepository) {
        FTPClient ftpClient = null;
        String pathToDelete = remotePath + "/" + uploadFile.getNameFolder();

        try {
            ftpClient = new FTPClient();
            ftpClient.connect(FTP_ADDRESS, PORT);
            showServerReply(ftpClient);

            boolean success = ftpClient.login(USERNAME, PASSWORD);
            showServerReply(ftpClient);
            if (!success) {
                getLogger().warn("Could not login to the server");
            }
            if (success) {
                ftpClient.enterLocalPassiveMode(); // important!
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.deleteFile(pathToDelete + "/" + uploadFile.getNameFile());
                showServerReply(ftpClient);
                ftpClient.removeDirectory(pathToDelete);
                showServerReply(ftpClient);
                getLogger().info("File '" + pathToDelete + "/" + uploadFile.getNameFile() + "' deleted...");
                showServerReply(ftpClient);
                if (ftpClient.getReplyCode() == 250) {
                    uploadFiles.removeIf(uploadFile1 -> (uploadFile1.equals(uploadFile)));
                    uploadFileRepository.deleteById(uploadFile.getId());
                    return true;
                }
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
