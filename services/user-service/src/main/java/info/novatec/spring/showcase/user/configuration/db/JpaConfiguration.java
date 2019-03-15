package info.novatec.spring.showcase.user.configuration.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.persistence.EntityManagerFactory;

@Configuration
public class JpaConfiguration {

  @Primary
  @Bean
  public JpaTransactionManager transactionManager(EntityManagerFactory em) {
    return new JpaTransactionManager(em);
  }
}
