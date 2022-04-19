package com.tcb.auto.subprocess.t24.telnet;

import com.tcb.auto.utils.Constants;
import com.tcb.auto.utils.GlobalVariable;
import org.apache.commons.net.telnet.TelnetClient;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.util.Properties;

public class T24TelnetClient implements Runnable {
    static TelnetClient telnetClient = new TelnetClient();
    static InputStream in;
    static PrintStream out;
    static StringBuffer stringBuffer = new StringBuffer();

    private String hostname;
    private String username;
    private String password;

    public T24TelnetClient(String hostname, String username, String password) {
        this.hostname = hostname;
        this.username = username;
        this.password = password;
    }

    public static T24TelnetClient getDefaultClient(){
        Properties variables = GlobalVariable.listConfigEnv;
        T24TelnetClient client = new T24TelnetClient(variables.getProperty(Constants.T24_JQL_SERVER), variables.getProperty(Constants.T24_JQL_AUTH_UID), variables.getProperty(Constants.T24_JQL_AUTH_PWD));
        client.hostname = variables.getProperty(Constants.T24_JQL_SERVER);
        client.username = variables.getProperty(Constants.T24_JQL_AUTH_UID);
        client.password = variables.getProperty(Constants.T24_JQL_AUTH_PWD);

        return client;
    }

    public String inputContractNo(String hostname, String username, String password, String command, String key) {
        try {
            /*
             * open connection
             */
            telnetClient.connect(hostname);
            in = null;
            in = telnetClient.getInputStream();
            out = new PrintStream(telnetClient.getOutputStream());

            /*
             * start a thread to read
             */
            Thread reader = new Thread(new T24TelnetClient(this.hostname, this.username, this.password));
            reader.start();

            /*
             * Auto login, wait until the prompt %
             */
            while (true) {
                if (stringBuffer.toString().endsWith("login: ")) {
                    out.println(username);
                    out.flush();
                    break;
                }
            }
            while (true) {
                if (stringBuffer.toString().endsWith("Password: ")) {
                    out.println(password);
                    out.flush();
                    break;
                }
            }
            while (true) {
                if (stringBuffer.toString().endsWith("%")) {
                    out.println("");
                    out.flush();
                    break;
                }
            }

            /*
             * Wait until the prompt tcb r14bau ? appears, then send the command
             */
            while (true) {
                if (stringBuffer.toString().endsWith("tcb " + username + " ?")) {
                    out.println(command);
                    out.flush();
                    break;
                }
            }
            while (true) {
                if (stringBuffer.toString().replace("\r\n", "").endsWith("CHAY:")) {
                    out.println(key);
                    out.flush();
                    break;
                }
            }

            /*
             * constantly check if timeout > 5 seconds then auto send a blank
             * package to refresh Input Stream. Wait until the prompt
             * "tcb r14bau ?" appears again
             */
            long start = System.currentTimeMillis();
            while (true) {
                long current = System.currentTimeMillis();
                if ((current - start) > 5000) {
                    if (!stringBuffer.toString().endsWith("tcb " + username + " ?")) {
                        out.println("");
                        out.flush();
                    } else {
                        break;
                    }
                    start = current;
                }
            }

            /*
             * Close connection, stop reader thread
             */
            reader.interrupt();
            in.close();
            out.close();

            /*
             * Return response
             */
            String response = stringBuffer.toString().trim();
            // clear stringBuffer
            stringBuffer.delete(0, stringBuffer.length());
            // clear unnecessary text
            response = response
                    .replaceAll(Constants.TEST_RESPONE_FIRST, "\n")
                    .trim();
            response = response.replace("[2J[H" + command, "")
                    .replace("tcb " + username + " ?" + command, "tcb " + username + " ?").trim();
            response = response.replace(Constants.TEST_RESPONE_CONTROL, "\n");
            response = response.replaceAll("\n\\s*\n", "\n").trim();
            String nameRespone = "tcb " + username + " ?";
            int fidx = response.indexOf(nameRespone);
            int lidx = response.lastIndexOf(nameRespone);
            response = response.substring(fidx + nameRespone.length(), lidx).trim();

            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param command
     * @param inputMsg
     * @param key
     * @param timeoutSecond: in second
     * @return
     */
    public String runT24Job(String command, String inputMsg, String key, int timeoutSecond) {
        try {
            long start = System.currentTimeMillis();
            long end = start + timeoutSecond * 1000;
            /*
             * open connection
             */
            telnetClient.connect(hostname);
            in = null;
            in = telnetClient.getInputStream();
            out = new PrintStream(telnetClient.getOutputStream());

            /*
             * start a thread to read
             */
            Thread reader = new Thread(new T24TelnetClient(this.hostname, this.username, this.password));
            reader.start();

            /*
             * Auto login, wait until the prompt %
             */
            while (true) {
                if (stringBuffer.toString().endsWith("login: ")) {
                    out.println(username);
                    out.flush();
                    break;
                }
            }
            while (true) {
                if (stringBuffer.toString().endsWith("Password: ")) {
                    out.println(password);
                    out.flush();
                    break;
                }
            }
            while (true) {
                if (stringBuffer.toString().endsWith("%")) {
                    out.println("");
                    out.flush();
                    break;
                }
            }

            /*
             * Wait until the prompt tcb r14bau ? appears, then send the command
             */

            while (true) {
                if (stringBuffer.toString().endsWith("tcb " + username + " ?")) {
                    System.out.println("Run command: " + command);
                    out.println(command);
                    out.flush();
                    Thread.sleep(1000);
                    break;
                }
            }

            if(inputMsg != null && !inputMsg.isEmpty() && !inputMsg.equals(Constants.NULL_TEXT_INDICATOR)) {
                //Wait Input Message appear
                System.out.println("Wait input message: " + inputMsg);
                while (true) {
                    if(timeoutSecond > 0 && end < System.currentTimeMillis()){
                        System.out.println("Timeout when running job");
                        return Constants.NULL_TEXT_INDICATOR;
                    }
                    Thread.sleep(200);
                    if (stringBuffer.toString().replace("\r\n", "").endsWith(inputMsg)) {
                        System.out.println("Input key: " + key);
                        out.println(key);
                        out.flush();
                        break;
                    }
                }
            }

            /*
             * check stringBuffer every 1s & wait job done
             */

            System.out.println("Wait job done...");
            while (true) {
                if(timeoutSecond > 0 && end < System.currentTimeMillis()){
                    System.out.println("Timeout when running job");
                    return Constants.NULL_TEXT_INDICATOR;
                }
                if (!stringBuffer.toString().endsWith("tcb " + username + " ?")) {
                    out.println("");
                    out.flush();
                } else {
                    break;
                }
                Thread.sleep(1000);
            }

            /*
             * Close connection, stop reader thread
             */
            reader.interrupt();
            in.close();
            out.close();

            /*
             * Return response
             */
            String response = stringBuffer.toString().trim();
            // clear stringBuffer
            stringBuffer.delete(0, stringBuffer.length());
            // clear unnecessary text
            response = response
                    .replaceAll(Constants.TEST_RESPONE_FIRST, "\n")
                    .trim();
            response = response.replace("[2J[H" + command, "")
                    .replace("tcb " + username + " ?" + command, "tcb " + username + " ?").trim();
            response = response.replace(Constants.TEST_RESPONE_CONTROL, "\n");
            response = response.replaceAll("\n\\s*\n", "\n").trim();
            String nameRespone = "tcb " + username + " ?";
            int fidx = response.indexOf(nameRespone);
            int lidx = response.lastIndexOf(nameRespone);
            response = response.substring(fidx + nameRespone.length(), lidx).trim();
            response = response.replaceAll("tcb \\S+ \\?[\\\r\\\n]*", "");
            return response;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return Constants.NULL_TEXT_INDICATOR;
    }

    @Override
    public void run() {
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            do {
                read = in.read(buff);
                if (read > 0) {
                    // System.out.print(new String(buff, 0, read));
                    stringBuffer.append(new String(buff, 0, read));
                }
            } while (read >= 0);
        } catch (InterruptedIOException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reader thread's run method, constantly receives data from remote server
     * and update to a StringBuffer
     */

}
