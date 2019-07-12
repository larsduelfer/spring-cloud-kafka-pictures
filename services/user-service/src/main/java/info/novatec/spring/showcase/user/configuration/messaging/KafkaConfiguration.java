package info.novatec.spring.showcase.user.configuration.messaging;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import java.util.Map;

@Profile("kafka")
@Configuration
public class KafkaConfiguration {

  @Autowired private KafkaProperties kafkaProperties;

  @Bean
  public KafkaTransactionManager<?, ?> kafkaTransactionManager() {
    KafkaTransactionManager<?, ?> kafkaTransactionManager =
        new KafkaTransactionManager<>(kafkaProducerFactory());
    kafkaTransactionManager.setTransactionSynchronization(
        AbstractPlatformTransactionManager.SYNCHRONIZATION_ON_ACTUAL_TRANSACTION);
    return kafkaTransactionManager;
  }

  @Bean
  public ChainedTransactionManager chainedTransactionManager(
      JpaTransactionManager jpaTransactionManager,
      KafkaTransactionManager kafkaTransactionManager) {
    return new ChainedTransactionManager(kafkaTransactionManager, jpaTransactionManager);
  }

  @Bean
  public ProducerFactory<?, ?> kafkaProducerFactory() {
    Map<String, Object> producerProperties = kafkaProperties.buildProducerProperties();
    producerProperties.put(
        ProducerConfig.CLIENT_ID_CONFIG, kafkaProperties.getProperties().get("clientId.app"));
    DefaultKafkaProducerFactory<String, SpecificRecordBase> producerFactory =
        new DefaultKafkaProducerFactory<>(producerProperties);
    producerFactory.setTransactionIdPrefix(kafkaProperties.getProducer().getTransactionIdPrefix());
    return producerFactory;
  }
}
