package ru.pfr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.pfr.model.umikbd.Logi;
import ru.pfr.service.bdumik.LogiService;


import java.util.Date;

@SpringBootApplication
public class Application implements ApplicationRunner {
    @Autowired
    LogiService logiService;

    private static final Logger logger = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        /*        logger.debug("Debugging log");*/
        logiService.save(new Logi(
                new Date(),
                "system",
                "ПРИЛОЖЕНИЕ ЗАПУЩЕНО!"));
        logger.info("LOG START!!!");


/*        logger.warn("Hey, This is a warning!");
        logger.error("Oops! We have an Error. OK");
        logger.fatal("Damn! Fatal error. Please fix me.");*/
    }
}
