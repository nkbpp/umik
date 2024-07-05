package ru.pfr.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class SSHConnection {

    private final static String S_PATH_FILE_PRIVATE_KEY;
    private final static String S_PATH_FILE_KNOWN_HOSTS;
    private final static String S_PASS_PHRASE = "db2admin";
    private final static int LOCAl_PORT = 5432;
    private final static int REMOTE_PORT = 5432;
    private final static int SSH_REMOTE_PORT = 22;
    private final static String SSH_USER = "root";
    private final static String SSH_REMOTE_SERVER = "myapp-mydomain.rhcloud.com";
    private final static String MYSQL_REMOTE_SERVER = "10.41.232.104";

    private Session session; //представляет каждый сеанс ssh

    static {
        try {
            S_PATH_FILE_PRIVATE_KEY = new ClassPathResource("id_rsa_ped").getURL().getPath();
            S_PATH_FILE_KNOWN_HOSTS = new ClassPathResource("known_hosts").getURL().getPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void closeSSH ()
    {
        session.disconnect();
    }

    public SSHConnection () throws Throwable
    {
        JSch jsch = null;

        jsch = new JSch();

        jsch.setKnownHosts(S_PATH_FILE_KNOWN_HOSTS);
        jsch.addIdentity(S_PATH_FILE_PRIVATE_KEY, S_PASS_PHRASE.getBytes());

        session = jsch.getSession(SSH_USER, SSH_REMOTE_SERVER, SSH_REMOTE_PORT);
        session.connect(); //ssh connection established!

        //by security policy, you must connect through a fowarded port
        session.setPortForwardingL(LOCAl_PORT, MYSQL_REMOTE_SERVER, REMOTE_PORT);
    }
}
