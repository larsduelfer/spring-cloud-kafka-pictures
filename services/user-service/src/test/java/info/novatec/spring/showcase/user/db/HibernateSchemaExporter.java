package info.novatec.spring.showcase.user.db;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.MySQL57Dialect;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@IfProfileValue(name = "spring.profiles.active", value="schema-export")
@ActiveProfiles("jpa")
@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties = {"spring.flyway.enabled=false"})
public class HibernateSchemaExporter {

  private final SchemaExporter schemaExporter = new SchemaExporter();

  @Autowired private LocalContainerEntityManagerFactoryBean bean;

  @Test
  public void exportH2Schema() {
    schemaExporter.exportSchema(
        bean, H2Dialect.class, org.h2.Driver.class, "src/main/resources/db/schema-h2.sql");
  }

  @Test
  public void exportMySqlSchema() {
    schemaExporter.exportSchema(
        bean,
        MySQL57Dialect.class,
        com.mysql.jdbc.Driver.class,
        "src/main/resources/db/schema-mysql.sql");
  }
}
