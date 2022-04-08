package com.tcb.auto.subprocess.ssh;

import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.GlobalVariable;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelExec;
import org.apache.sshd.client.channel.ChannelShell;
import org.apache.sshd.client.config.hosts.HostConfigEntryResolver;
import org.apache.sshd.client.keyverifier.AcceptAllServerKeyVerifier;
import org.apache.sshd.client.scp.ScpClient;
import org.apache.sshd.client.scp.ScpClientCreator;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.client.subsystem.sftp.SftpClient;
import org.apache.sshd.client.subsystem.sftp.SftpClientFactory;
import org.apache.sshd.common.keyprovider.KeyPairProvider;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * This class do Login to Server via SSH, get shell channel (for send command) and get SFTP client (for read folder & file)
 * @author nhanha
 * @since JDK1.8
 * @version 1.0
 */
public class SSHClient {

	//final static Logger log = LogManager.getLogger(SSHClient.class);
	
	private String host;
	private int port;
	private String username;
	private String password;
	
	private SshClient sshClient;
	private ClientSession sshSession;
	
	private ChannelShell shellChannel;
	private SftpClient sftpClient;
	private ScpClient scpClient;

	private ChannelExec execChanel;

	/**
	 * Login and get ssh session
	 * @param host Server ip
	 * @param port ssh port (default 22)
	 * @param username login username
	 * @param password login password
	 */
	public SSHClient(String host, int port, String username, String password){
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	public SSHClient(String connKey){
		Properties properties = GlobalVariable.listConfigEnv;
		if (properties == null) {
			properties = GlobalVariable.loadEnvConfigs();
		}
		this.host = properties.getProperty(String.format("%s.HOST", connKey));
		this.port = Integer.valueOf(properties.getProperty(String.format("%s.PORT", connKey)));
		this.username = properties.getProperty(String.format("%s.USERNAME", connKey));
		this.password = properties.getProperty(String.format("%s.PASSWORD", connKey));
	}
	
	public boolean login() throws IOException {
		sshClient = SshClient.setUpDefaultClient();
		sshClient.setServerKeyVerifier(AcceptAllServerKeyVerifier.INSTANCE);
		sshClient.setHostConfigEntryResolver(HostConfigEntryResolver.EMPTY);
//		sshClient.setKeyPairProvider();
		sshClient.setKeyPairProvider(KeyPairProvider.EMPTY_KEYPAIR_PROVIDER);
		sshSession = null;

		int retries = 0;

		while (retries < SshClient.DEFAULT_AUTH_TIMEOUT) {
			try {
				sshClient.start();
				
				sshSession = sshClient.connect(username, host, port).verify(SshClient.DEFAULT_AUTH_TIMEOUT, TimeUnit.SECONDS).getSession();
				sshSession.addPasswordIdentity(password);

				sshSession.auth().verify(SshClient.DEFAULT_AUTH_TIMEOUT, TimeUnit.SECONDS);
				// Connected
				return true;
			} catch (IOException ex) {
				if (++retries < SshClient.DEFAULT_AUTH_TIMEOUT) {
					Commons.waitAction(1000);
				} else {
					throw ex;
				}
			}
		}
		return false;
	}

	/**
	 * Init shell channel
	 * @throws IOException
	 */
	private void initChannelClient() throws IOException{
		if(sshSession == null || !sshSession.isAuthenticated()){
			login();
		}
		if(shellChannel == null || !shellChannel.isOpen()){
			//init new channel
			shellChannel = sshSession.createShellChannel();
			shellChannel.open();
		}
	}

	/**
	 * Init Sftp client
	 * @throws IOException
	 */
	private void initSftpClient() throws IOException{
		if(sshSession == null || !sshSession.isAuthenticated()){
			login();
		}

		if(sftpClient == null || !sftpClient.isOpen()){
			//init new sftpClient
			sftpClient = SftpClientFactory.instance().createSftpClient(sshSession);
		}
	}

	/**
	 * Init Scp client
	 * @throws IOException
	 */
	private void initScpClient() throws IOException{
		if(sshSession == null || !sshSession.isAuthenticated()){
			login();
		}

		if(scpClient == null){
			//init new scpClient
			scpClient = ScpClientCreator.instance().createScpClient(sshSession);
		}
	}

	private void initExecChannel(String command) throws IOException{
		if(sshSession == null || !sshSession.isAuthenticated()){
			login();
		}
		if(execChanel == null || !execChanel.isOpen()){
			//init new channel
			execChanel = sshSession.createExecChannel(command);
			//execChanel.open();
		}
	}

	public ClientSession getSshSession() {
		return sshSession;
	}

	public ChannelShell getShellChannel() throws IOException {
		initChannelClient();
		return shellChannel;
	}

	public SftpClient getSftpClient() throws IOException {
		initSftpClient();
		return sftpClient;
	}

	public ScpClient getScpClient() throws IOException {
		initScpClient();
		return scpClient;
	}

	public ChannelExec getExecChannel(String command) throws IOException {
		initExecChannel(command);
		return execChanel;
	}

	/**
	 * Close all channel and ssh session
	 */
	public void close() {
		try {
			if (shellChannel != null && shellChannel.isOpen()) {
				shellChannel.close();
			}
			if (sftpClient != null && sftpClient.isOpen()) {
				sftpClient.close();
			}
			sshSession.close();
			sshClient.stop();
		} catch (IOException e) {
			shellChannel = null;
			sftpClient = null;
			sshSession = null;
			sshClient = null;

			System.err.println(e.getMessage());
			//log.warn(e.getMessage());
		}
	}
}
