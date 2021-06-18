package ru.pfr.ssh;


import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SSHConnection {
    private final static String S_PATH_FILE_PRIVATE_KEY = "C:\\id_rsa_ped"; //windows absolut path of our ssh private key locally saved
    private final static String S_PATH_FILE_KNOWN_HOSTS = "C:\\known_hosts";
    private final static String S_PASS_PHRASE = "db2admin";
    private final static int LOCAl_PORT = 5432;
    private final static int REMOTE_PORT = 5432;
    private final static int SSH_REMOTE_PORT = 22;
    private final static String SSH_USER = "root";
    private final static String SSH_REMOTE_SERVER = "myapp-mydomain.rhcloud.com";
    private final static String MYSQL_REMOTE_SERVER = "10.41.232.104";

    private Session sesion; //представляет каждый сеанс ssh

    public void closeSSH ()
    {
        sesion.disconnect();
    }

    public SSHConnection () throws Throwable
    {

        JSch jsch = null;

        jsch = new JSch();

        jsch.setKnownHosts(S_PATH_FILE_KNOWN_HOSTS);
        jsch.addIdentity(S_PATH_FILE_PRIVATE_KEY, S_PASS_PHRASE.getBytes());

        sesion = jsch.getSession(SSH_USER, SSH_REMOTE_SERVER, SSH_REMOTE_PORT);
        sesion.connect(); //ssh connection established!

        //by security policy, you must connect through a fowarded port
        sesion.setPortForwardingL(LOCAl_PORT, MYSQL_REMOTE_SERVER, REMOTE_PORT);

    }
}
