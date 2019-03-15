package info.novatec.spring.showcase.storage.configuration.db;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
public class MongoDbConfiguration extends AbstractMongoConfiguration {

  @Autowired private Environment environment;

  @Override
  public MongoClient mongoClient() {
    return mongoClientFactory()
        .createMongoClient(MongoClientOptions.builder().sslEnabled(false).build());
  }

  @Override
  protected String getDatabaseName() {
    return "storage-service";
  }

  @Bean
  public MongoProperties mongoProperties() {
    return new MongoProperties();
  }

  @Bean
  public MongoClientFactory mongoClientFactory() {
    return new MongoClientFactory(mongoProperties(), environment);
  }

  @Primary
  @Bean
  public MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
    return new MongoTransactionManager(dbFactory);
  }

  @Bean
  public GridFsTemplate gridFsTemplate(
      MongoDbFactory mongoDbFactory, MongoConverter mongoConverter) {
    return new GridFsTemplate(mongoDbFactory, mongoConverter);
  }
}
