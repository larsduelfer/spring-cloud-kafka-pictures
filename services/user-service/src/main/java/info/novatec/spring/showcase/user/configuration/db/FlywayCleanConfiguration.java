package info.novatec.spring.showcase.user.configuration.db;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("db-clean")
public class FlywayCleanConfiguration {

  @Bean
  public FlywayMigrationStrategy cleanMigrationStrategy() {
    return flyway -> {
      flyway.clean();
      flyway.migrate();
    };
  }
}
