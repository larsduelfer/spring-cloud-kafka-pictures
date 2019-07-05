package info.novatec.spring.showcase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.TimeZone;

/**
 * Eureka is currently not Java 9 compatible.
 *
 * <p>If exception "java.lang.TypeNotPresentException: Type javax.xml.bind.JAXBContext not present"
 * is shown at startup - switch to Java 8.
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication {

  public static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    SpringApplication.run(EurekaApplication.class, args);
  }

  /**
   * Spring security enables csrf protection which hinders eureka clients to connect (403) to eureka
   * server. As a workaround as described here
   * (https://github.com/spring-cloud/spring-cloud-netflix/issues/2754) the csrf protection will be
   * deactivated.
   */
  @EnableWebSecurity
  @Configuration
  static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable().authorizeRequests().anyRequest().authenticated().and().httpBasic();
    }
  }
}
