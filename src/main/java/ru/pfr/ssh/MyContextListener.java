package ru.pfr.ssh;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pfr.Application;
import ru.pfr.model.umikbd.Logi;
import ru.pfr.service.bdumik.LogiService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Date;

public class MyContextListener implements ServletContextListener {

    @Autowired
    LogiService logiService;

    private static final Logger logger = LogManager.getLogger(Application.class);

    private SSHConnection conexionssh;

    public MyContextListener()
    {
        super();
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)
    {
        logiService.save(new Logi(
                new Date(),
                "system",
                "SSH Создание соединения"));
        logger.info("SSH Создание соединения");
        try
        {
            conexionssh = new SSHConnection();
            logiService.save(new Logi(
                    new Date(),
                    "system",
                    "SSH соединение успешно!"));
            logger.info("SSH соединение успешно!");
        }
        catch (Throwable e)
        {
            logiService.save(new Logi(
                    new Date(),
                    "system",
                    "ОШИБКА ПОДКЛЮЧЕНИЯ К SSH"));
            logger.info("ОШИБКА ПОДКЛЮЧЕНИЯ К SSH");
            e.printStackTrace(); // error connecting SSH server
        }
    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)
    {
        logiService.save(new Logi(
                new Date(),
                "system",
                "SSH соединение разрушено"));
        logger.info("SSH соединение разрушено");

        conexionssh.closeSSH(); // disconnect
    }
}
