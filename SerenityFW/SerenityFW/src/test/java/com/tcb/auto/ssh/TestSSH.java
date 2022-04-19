package com.tcb.auto.ssh;

import com.tcb.auto.subprocess.ssh.SCPClient;
import com.tcb.auto.subprocess.ssh.SFTPClient;
import org.junit.Test;

import java.io.IOException;

public class TestSSH {
    //@Test
    public void TestPutFile() throws IOException {
        String filePath = "/home/localpwm2/message.txt";
        String fileContent = "ALTEK INTERNATIONAL FZE\n" +
                "PO BOX 128907 OFF 202\n" +
                "MSA BLDG A OLD STRAND\n" +
                "LONDON";

        SFTPClient sftpClient = new SFTPClient("10.101.4.5", 22, "localpwm2", "P@ssw0rd123");
        boolean result = sftpClient.putTextFile(filePath, fileContent);
        System.out.println("Result: " + result);
    }

    //@Test
    public void TestUploadFile() throws IOException {
        String serverPath = "/home/localpwm2/message.txt";
        String localPath = "D:\\Auto\\message_server.txt";
        SCPClient scpClient = new SCPClient("10.101.4.5", 22, "localpwm2", "P@ssw0rd123");
        boolean result = scpClient.uploadFile(localPath, serverPath);
        System.out.println("Result: " + result);
    }

    //@Test
    public void TestDownloadFile() throws IOException {
        String serverPath = "/home/localpwm2/message.txt";
        String localPath = "D:\\Auto\\message_server.txt";
        SCPClient scpClient = new SCPClient("10.101.4.5", 22, "localpwm2", "P@ssw0rd123");
        boolean result = scpClient.downloadFile(serverPath, localPath);
        System.out.println("Result: " + result);
    }

    //@Test
    public void TestGetFileContent() throws IOException {
        String serverPath = "/home/localpwm2/message.txt";
        SCPClient scpClient = new SCPClient("10.101.4.5", 22, "localpwm2", "P@ssw0rd123");

        String content = scpClient.getTextFileContent(serverPath);
        System.out.println("content: " + content);
    }
}
