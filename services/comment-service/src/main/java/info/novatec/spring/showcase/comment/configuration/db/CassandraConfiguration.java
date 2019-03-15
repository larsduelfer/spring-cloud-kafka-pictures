package info.novatec.spring.showcase.comment.configuration.db;

import info.novatec.spring.showcase.comment.CommentServiceApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableCassandraRepositories(basePackageClasses = CommentServiceApplication.class)
public class CassandraConfiguration extends AbstractCassandraConfiguration {

  @Autowired private CassandraProperties cassandraProperties;

  @Override
  protected String getKeyspaceName() {
    return cassandraProperties.getKeyspaceName();
  }

  @Override
  protected boolean getMetricsEnabled() {
    return false;
  }

  @Override
  public String[] getEntityBasePackages() {
    return new String[] {CommentServiceApplication.class.getPackage().getName()};
  }

  @Override
  public SchemaAction getSchemaAction() {
    return SchemaAction.valueOf(cassandraProperties.getSchemaAction());
  }

  @Override
  protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {

    return Collections.singletonList(
        CreateKeyspaceSpecification.createKeyspace(cassandraProperties.getKeyspaceName())
            .with(KeyspaceOption.DURABLE_WRITES, true)
            .withSimpleReplication()
            .ifNotExists());
  }
}
