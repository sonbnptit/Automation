package com.tcb.auto.subprocess.ssh;

import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.GlobalVariable;
import com.tcb.auto.subprocess.ssh.CustomSort.SortBy;

import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.client.subsystem.sftp.SftpClient;
import org.apache.sshd.client.subsystem.sftp.SftpClient.CloseableHandle;
import org.apache.sshd.client.subsystem.sftp.SftpClient.DirEntry;
import org.apache.sshd.client.subsystem.sftp.SftpClient.OpenMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * This class for search files PIN in a folder, get PIN content by domain name
 * and first name
 * 
 * @author nhanha
 * @since JDK1.8
 * @version 1.0
 */
public class SFTPClient extends SSHClient {
	// final static Logger log = LogManager.getLogger(SFTPClient.class);

	public SFTPClient(String host, int port, String username, String password) {
		super(host, port, username, password);
	}

	public SFTPClient(String connKey) {
		super(connKey);
	}

	/**
	 *
	 * @param dirPath PIN folder
	 * @param filter  narrow file list
	 * @param sortBy  sort by DATE or NAME (DESC)
	 * @return
	 * @throws IOException
	 */
	public List<String> getListFile(String dirPath, FileFilter filter, SortBy sortBy) throws IOException {
		SftpClient sftpClient = getSftpClient();
		if (sftpClient == null)
			return null;

		Iterable<DirEntry> listDir = sftpClient.readDir(dirPath);
		List<DirEntry> entryList = new ArrayList<DirEntry>();
		List<String> fileList = new ArrayList<String>();
		try {
			if (!listDir.iterator().hasNext())
				return fileList;
			for (DirEntry dirEntry : listDir) {
				if (dirEntry.getFilename().equals(".") || dirEntry.getFilename().equals("..")) {
					continue;
				}
				if (filter != null && !filter.checkFilter(dirEntry)) {
					continue;
				}
				entryList.add(dirEntry);
			}
			entryList.sort((new CustomSort(sortBy)));
			// copy to file list
			for (DirEntry dirEntry : entryList) {
				fileList.add(dirPath + "/" + dirEntry.getFilename());
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			// log.warn(e.getMessage());
		}
		return fileList;
	}

	public String getFileContent(String filePath) throws IOException {
		SftpClient sftpClient = getSftpClient();
		if (sftpClient == null)
			return null;

		CloseableHandle fileHandle = sftpClient.open(filePath, OpenMode.Read);
		StringBuilder builder = new StringBuilder();
		int readCount = 0;
		int offset = 0;
		do {
			byte[] buff = new byte[SftpClient.IO_BUFFER_SIZE];
			readCount = sftpClient.read(fileHandle, offset, buff);
			if (readCount == 0)
				break;
			builder.append(new String(buff, 0, readCount, StandardCharsets.UTF_8));
			offset += readCount;
			if (readCount < buff.length)
				break;
		} while (readCount > 0);
		fileHandle.close();
		return builder.toString();
	}

	public boolean putTextFile(String filePath, String content, Charset charset) throws IOException {
		SftpClient sftpClient = getSftpClient();
		if (sftpClient == null)
			return false;

		byte[] fBytes = content.getBytes(charset);
		CloseableHandle fileHandle = sftpClient.open(filePath, OpenMode.Create, OpenMode.Write, OpenMode.Truncate);
		sftpClient.write(fileHandle, 0, fBytes);
		sftpClient.close(fileHandle);

		return true;
	}

	public boolean putTextFile(String filePath, String content) throws IOException {
		return putTextFile(filePath, content, StandardCharsets.UTF_8);
	}

	/**
	 *
	 * @param dirPath
	 * @param         searchDomain: search by Domain
	 * @param         searchFirst: search by first name
	 * @param         searchLast: search by last name
	 * @return
	 */
	public String getCustomerPIN(String dirPath, String searchDomain, String searchFirst, String searchLast,
			int pinMinAgo) {
		try {
			List<String> pinList = new LinkedList<>();
			Date startDate = Date.from(Instant.now().minus(pinMinAgo, ChronoUnit.MINUTES));
			Date endDate = new Date();
			FileFilter filter = new FileFilter().setDateCondition(startDate, endDate);
			List<String> listFile = getListFile(dirPath, filter, SortBy.NAME_DESC);
			for (String fName : listFile) {
				String content = getFileContent(fName);
				String pin = getPINContent(content, searchDomain, searchFirst, searchLast);
				if (pin != null && !pin.isEmpty()) {
					pinList.add(pin);
				}
			}
			return String.join(";", pinList);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			// log.warn(e.getMessage());
			return null;
		}
	}

	private String getPINContent(String content, String searchDomain, String searchFName, String searchLName) {
		if (content == null || content.trim().isEmpty())
			return null;
		String REGEX_BEGIN_END = "# BEGIN DATA\\s*|# END DATA\\s*";
		content = content.replaceAll(REGEX_BEGIN_END, "");
		String[] dataCol = content.split("\\|");
		if (dataCol.length < 7)
			return null;
		String firstName = dataCol[0];
		String lastName = dataCol[2];
		String domain = dataCol[4];
		String pin = dataCol[6];

		// check domain
		if (!Commons.isBlankOrEmpty(searchDomain)
				&& !searchDomain.trim().toLowerCase().equals(domain.trim().toLowerCase()))
			return null; // => not match domain

		// check first name
		if (!Commons.isBlankOrEmpty(searchFName)
				&& !searchFName.trim().toLowerCase().equals(firstName.trim().toLowerCase()))
			return null; // => not match first name

		// check last name
		if (!Commons.isBlankOrEmpty(searchLName)
				&& !searchLName.trim().toLowerCase().equals(lastName.trim().toLowerCase()))
			return null; // => not match last name

		return pin.trim();

	}

	/**
	 * Chay Job bang cau lenh tRun Cac tham so TAFJ_HOME va JAVA_HOME cua server T24
	 * duoc cau hinh tai /<fodler_du_an>/configs
	 * 
	 * @param service: ten service vd SET.SERVICE.TCB BNK/AI.USER.PROCESS
	 * @param action: hanh dong START/STOP/AUTO
	 * @author anhptn14
	 * @return
	 */
	public boolean tRunService(String service, String action) {
		try {
			this.getShellChannel();
			ClientSession session = this.getSshSession();
			OutputStream stdout = new ByteArrayOutputStream();
			Properties properties = GlobalVariable.listConfigEnv;
			String TAFJ_HOME = properties.getProperty(String.format("%s.TAFJ.HOME", "T24"));
			String JAVA_HOME = properties.getProperty(String.format("%s.JAVA.HOME", "T24"));
			session.executeRemoteCommand(
					"export TAFJ_HOME=" + TAFJ_HOME + ";" + "export JAVA_HOME=" + JAVA_HOME + ";" + "sh " + TAFJ_HOME
							+ "/bin/tRun " + service + " " + action.toUpperCase(),
					stdout, System.err, StandardCharsets.US_ASCII);
			String outStr = stdout.toString();
			System.out.println(outStr);
			if (outStr.contains(action.toUpperCase()))
				return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}