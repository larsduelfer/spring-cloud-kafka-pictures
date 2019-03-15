package info.novatec.spring.showcase.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

import java.util.TimeZone;

@EnableConfigServer
@SpringBootApplication
public class ConfigApplication {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigApplication.class);

  public static void main(String[] args) {
    LOGGER.info("Application directory path: " + System.getProperty("user.dir"));
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    SpringApplication.run(ConfigApplication.class, args);
  }
}
