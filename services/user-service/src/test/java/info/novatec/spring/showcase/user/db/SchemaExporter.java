package info.novatec.spring.showcase.user.db;

import org.apache.commons.lang.WordUtils;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.relational.QualifiedNameImpl;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Constraint;
import org.hibernate.mapping.Table;
import org.hibernate.mapping.UniqueKey;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.spi.PersistenceUnitInfo;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Driver;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.fail;

/** Database schema exporter. */
class SchemaExporter {

  /**
   * Exports database schema to file for the given dialect and database driver.
   *
   * @param factoryBean {@link LocalContainerEntityManagerFactoryBean} where to take hibernate
   *     information / object references from.
   * @param dialect the dialect to export the schema for
   * @param driver the driver to use for schema export
   * @param outputPath the path where to write the export, relative to project root directory
   */
  void exportSchema(
      LocalContainerEntityManagerFactoryBean factoryBean,
      Class<? extends Dialect> dialect,
      Class<? extends Driver> driver,
      String outputPath) {

    try {

      // Specify properties how to export
      Map<String, Object> jpaPropertyMap = factoryBean.getJpaPropertyMap();
      jpaPropertyMap.put("hibernate.hbm2ddl.auto", "create");
      jpaPropertyMap.put("hibernate.show_sql", "false");
      jpaPropertyMap.put("hibernate.format_sql", "true");
      jpaPropertyMap.put("hibernate.dialect", dialect.getName());
      jpaPropertyMap.put("datasource.driverClassName", driver.getName());

      // Get hibernate persistence provider
      HibernatePersistenceProvider persistenceProvider =
          (HibernatePersistenceProvider) factoryBean.getPersistenceProvider();

      // Get class method of entity manager factory builder using
      // reflection api in hibernate persistence provider
      Method methodEntityManagerFactoryBuilder =
          HibernatePersistenceProvider.class.getDeclaredMethod(
              "getEntityManagerFactoryBuilder", PersistenceUnitInfo.class, Map.class);
      methodEntityManagerFactoryBuilder.setAccessible(true);

      // Get persistence info from factory bean
      PersistenceUnitInfo persistenceUnitInfo = factoryBean.getPersistenceUnitInfo();

      // Get entity manager factory builder from persistence provider
      EntityManagerFactoryBuilderImpl entityManagerFactoryBuilder =
          (EntityManagerFactoryBuilderImpl)
              methodEntityManagerFactoryBuilder.invoke(
                  persistenceProvider, persistenceUnitInfo, jpaPropertyMap);

      // Close output file. Hibernate 5.2.0 appends to it automatically.
      new PrintWriter(outputPath).close();

      // Get metadata implementor
      Method methodMetadataImplementor =
          EntityManagerFactoryBuilderImpl.class.getDeclaredMethod("metadata");
      methodMetadataImplementor.setAccessible(true);
      MetadataImplementor metadataImplementor =
          (MetadataImplementor) methodMetadataImplementor.invoke(entityManagerFactoryBuilder);

      // Export schema
      SchemaExport schemaExport = new SchemaExport();
      schemaExport.setFormat(true);
      schemaExport.setDelimiter(";");
      schemaExport.setOutputFile(outputPath);
      schemaExport.execute(
          EnumSet.of(TargetType.SCRIPT), SchemaExport.Action.CREATE, metadataImplementor);

      // Add indices for foreign keys
      exportIndicesForForeignKeys(metadataImplementor, outputPath);
    } catch (IOException
        | NoSuchMethodException
        | IllegalAccessException
        | InvocationTargetException e) {
      fail("Database schema export failed", e);
    }
  }

  /**
   * Export indices for foreign keys defined in the database schema.
   *
   * @param metadataImplementor meta data implementor where to take information from what to export
   * @param outputPath the export file where to export
   * @throws IOException if file to append cannot be found / opened
   */
  private void exportIndicesForForeignKeys(
      MetadataImplementor metadataImplementor, String outputPath) throws IOException {
    List<String> statements = new ArrayList<>();

    metadataImplementor
        .getDatabase()
        .getNamespaces()
        .forEach(
            namespace ->
                namespace
                    .getTables()
                    .forEach(
                        table ->
                            table
                                .getForeignKeys()
                                .values()
                                .forEach(
                                    foreignKey -> {
                                      Iterable<UniqueKey> iterable = table::getUniqueKeyIterator;

                                      // Collect all unique key columns to skip them if the unique
                                      // keys are foreign keys, too.
                                      // This is required because some databases don't allow
                                      // duplicate indices
                                      List<Column> uniqueKeyColumns =
                                          StreamSupport.stream(iterable.spliterator(), false)
                                              .map(Constraint::getColumns)
                                              .filter(columns -> columns.size() == 1)
                                              .flatMap(List::stream)
                                              .collect(Collectors.toList());

                                      // Remove unique key columns
                                      List<Column> columns = foreignKey.getColumns();
                                      columns.removeAll(uniqueKeyColumns);

                                      // Skip column if it's already indexed
                                      if (columns.isEmpty()) {
                                        return;
                                      }

                                      // Create index name
                                      String columnName =
                                          columns.stream()
                                              .map(column -> shorten(column.getName()))
                                              .collect(Collectors.joining());
                                      String indexName =
                                          "IX_" + shorten(table.getName()) + "_" + columnName;

                                      // Create index statement
                                      statements.add(
                                          createIndexStatement(
                                              table, columns, indexName, metadataImplementor));
                                      statements.add("\n\n");
                                    })));

    if (!statements.isEmpty()) {
      // If statements are not empty then add a new line to separate index
      // statements from previous statements
      statements.add(0, "\n");

      // Write index statements to file
      try (FileWriter fileWriter = new FileWriter(outputPath, true)) {
        fileWriter.write(String.join("", statements));
      }
    }
  }

  /**
   * Returns a shortened version of the given name.
   *
   * @param value the name to shorten
   * @return the shortened name
   */
  private String shorten(String value) {
    String inputString = value;
    // If column ends with _id then shorten it
    boolean withId = inputString.endsWith("_id");
    if (withId) {
      inputString = inputString.substring(0, inputString.length() - 3);
    }

    if (inputString.contains("_")) {
      // If name is compound of multiple parts then shorten each part
      String[] parts = inputString.split("_");
      inputString =
          Stream.of(parts)
              .map(
                  part ->
                      WordUtils.capitalize(
                          part.toLowerCase(Locale.US).substring(0, Math.min(3, part.length()))))
              .collect(Collectors.joining());
    } else {
      // Else if name is not compound then shorten the name directly
      String rawString = inputString;
      inputString = inputString.substring(0, Math.min(3, inputString.length()));
      if (rawString.length() > inputString.length() + 3) {
        inputString += rawString.substring(rawString.length() - 3);
      } else if (rawString.length() > inputString.length() + 2) {
        inputString += rawString.substring(rawString.length() - 2);
      } else if (rawString.length() > inputString.length() + 1) {
        inputString += rawString.substring(rawString.length() - 1);
      }
    }
    return WordUtils.capitalize(inputString);
  }

  /**
   * Create index sql statement. Uses code from {@link
   * org.hibernate.tool.schema.internal.StandardIndexExporter}.
   *
   * @param table the table to create an index for
   * @param indexColumns the columns to create index for
   * @param indexName the index name
   * @param metadata metadata to verify and qualify object names
   * @return the index statement
   */
  private String createIndexStatement(
      Table table, List<Column> indexColumns, String indexName, Metadata metadata) {
    JdbcEnvironment jdbcEnvironment = metadata.getDatabase().getJdbcEnvironment();
    Dialect dialect = metadata.getDatabase().getDialect();
    String tableName =
        jdbcEnvironment
            .getQualifiedObjectNameFormatter()
            .format(table.getQualifiedTableName(), dialect);

    String indexNameForCreation;
    if (dialect.qualifyIndexName()) {
      indexNameForCreation =
          jdbcEnvironment
              .getQualifiedObjectNameFormatter()
              .format(
                  new QualifiedNameImpl(
                      table.getQualifiedTableName().getCatalogName(),
                      table.getQualifiedTableName().getSchemaName(),
                      jdbcEnvironment.getIdentifierHelper().toIdentifier(indexName)),
                  jdbcEnvironment.getDialect());
    } else {
      indexNameForCreation = indexName;
    }
    String statement =
        indexColumns.stream()
            .map(column -> column.getQuotedName(dialect))
            .collect(Collectors.joining(", "));
    return "create index " + indexNameForCreation + " on " + tableName + " (" + statement + ");";
  }
}
