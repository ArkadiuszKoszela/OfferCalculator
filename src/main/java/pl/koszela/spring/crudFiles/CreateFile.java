package pl.koszela.spring.crudFiles;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.koszela.spring.service.HasLogger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static pl.koszela.spring.crudFiles.ConfigToFtpServer.*;

@Service
public class CreateFile implements HasLogger {

    public void createFile(MultipartFile file, RedirectAttributes redirectAttributes, String nameFolder, HttpServletResponse response) {
        FTPClient ftpClient = null;

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
                String nameFolderWithoutSpaces = nameFolder.replaceAll("\\s+", "");
                String dirToCreate = "public_html/" + nameFolderWithoutSpaces;
                ftpClient.makeDirectory(dirToCreate);
                showServerReply(ftpClient);
                Cookie cookie = new Cookie("nameFolder", nameFolderWithoutSpaces);
                cookie.setPath("/");
                response.addCookie(cookie);
                getLogger().info("Successfully created directory: " + dirToCreate);

                String fileNameWithoutSpaces = Objects.requireNonNull(file.getOriginalFilename()).replaceAll("\\s+", "");

                ftpClient.storeFile(dirToCreate + "/" + fileNameWithoutSpaces, file.getInputStream());
                showServerReply(ftpClient);
                ftpClient.logout();
                ftpClient.disconnect();
                redirectAttributes.addAttribute("message",
                        "You successfully uploaded " + fileNameWithoutSpaces + "!");
                Cookie cookie1 = new Cookie("fileName", fileNameWithoutSpaces);
                cookie1.setPath("/");
                response.addCookie(cookie1);
            }
        } catch (Exception e) {
            String fileNameWithoutSpaces = Objects.requireNonNull(file.getOriginalFilename()).replaceAll("\\s+", "");
            redirectAttributes.addAttribute("message",
                    "Could not upload " + fileNameWithoutSpaces + "!");
        }
    }
}
