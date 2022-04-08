package com.tcb.auto.subprocess.ssh;

import com.tcb.auto.subprocess.ssh.CustomSort.SortBy;
import com.tcb.auto.utils.Commons;
import org.apache.sshd.client.scp.ScpClient;
import org.apache.sshd.client.subsystem.sftp.SftpClient;
import org.apache.sshd.client.subsystem.sftp.SftpClient.CloseableHandle;
import org.apache.sshd.client.subsystem.sftp.SftpClient.DirEntry;
import org.apache.sshd.client.subsystem.sftp.SftpClient.OpenMode;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * This class for search files PIN in a folder, get PIN content by domain name and first name
 * @author nhanha
 * @since JDK1.8
 * @version 1.0
 */
public class SCPClient extends SSHClient {
	//final static Logger log = LogManager.getLogger(SFTPClient.class);

	public SCPClient(String host, int port, String username, String password) {
		super(host, port, username, password);
	}

	public SCPClient(String connKey) {
		super(connKey);
	}

	public boolean downloadFile(String serverPath, String localPath) throws IOException {
		ScpClient scpClient = getScpClient();
		if(scpClient == null) return false;

		scpClient.download(serverPath, localPath);
		return true;
	}

	public boolean uploadFile(String localPath, String serverPath) throws IOException {
		ScpClient scpClient = getScpClient();
		if(scpClient == null) return false;

		scpClient.upload(localPath, serverPath);
		return true;
	}

	public String getTextFileContent(String serverPath, Charset charset) throws IOException {
		ScpClient scpClient = getScpClient();
		if(scpClient == null) return null;
		byte[] fBytes = scpClient.downloadBytes(serverPath);
		return new String(fBytes, charset);
	}

	public String getTextFileContent(String serverPath) throws IOException {
		return getTextFileContent(serverPath, StandardCharsets.UTF_8);
	}
}