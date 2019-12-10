package pl.koszela.spring.crudFiles;

import org.apache.commons.net.ftp.FTPClient;

public class ConfigToFtpServer {

    static final String FTP_ADDRESS = "files.000webhost.com";
    static final int PORT = 21;
    static final String USERNAME = "begginerwebsite";
    static final String PASSWORD = "arek123!";
    public static final String REMOTE_PATH = "/public_html";

    public static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }
}
