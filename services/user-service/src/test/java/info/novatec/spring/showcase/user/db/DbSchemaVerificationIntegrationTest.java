package info.novatec.spring.showcase.user.db;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.MySQL57Dialect;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.Resource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Driver;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("jpa")
@DataJpaTest
@TestPropertySource(properties = {"spring.flyway.enabled=false"})
public class DbSchemaVerificationIntegrationTest {

  private final SchemaExporter schemaExporter = new SchemaExporter();

  @Value("classpath:db/schema-h2.sql")
  private Resource schemaH2;

  @Value("classpath:db/schema-mysql.sql")
  private Resource schemaMysql;

  @Autowired private LocalContainerEntityManagerFactoryBean bean;

  @Test
  public void verifyH2SchemaUpToDate() throws IOException {
    verifySchema(schemaH2, H2Dialect.class, org.h2.Driver.class);
  }

  @Test
  public void verifyMysqlSchemaUpToDate() throws IOException {
    verifySchema(schemaMysql, MySQL57Dialect.class, com.mysql.jdbc.Driver.class);
  }

  private void verifySchema(
      Resource referenceFile, Class<? extends Dialect> dialect, Class<? extends Driver> driver)
      throws IOException {
    File tempFile =
        File.createTempFile("Smartsite-schema-verification", referenceFile.getFilename());
    tempFile.deleteOnExit();

    schemaExporter.exportSchema(bean, dialect, driver, tempFile.getAbsolutePath());

    String generatedSchema = FileUtils.readFileToString(tempFile, StandardCharsets.UTF_8);
    generatedSchema = StringUtils.replace(generatedSchema, "\r\n", "\n");
    String referenceSchema =
        FileUtils.readFileToString(referenceFile.getFile(), StandardCharsets.UTF_8);
    referenceSchema = StringUtils.replace(referenceSchema, "\r\n", "\n");

    assertThat(generatedSchema).isEqualTo(referenceSchema);
  }
}
