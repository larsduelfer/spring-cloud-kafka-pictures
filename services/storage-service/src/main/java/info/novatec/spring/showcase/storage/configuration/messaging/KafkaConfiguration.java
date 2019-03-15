package info.novatec.spring.showcase.storage.configuration.messaging;

import info.novatec.spring.showcase.common.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.config.client.RetryProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

@Profile("kafka")
@Configuration
public class KafkaConfiguration {

  @Autowired private KafkaProperties kafkaProperties;

  @Bean
  @ConfigurationProperties("kafka.retry")
  public RetryProperties retryProperties() {
    return new RetryProperties();
  }

  /**
   * Read referenced articles about transaction behaviour of kafka and spring.
   *
   * <p>https://stackoverflow.com/questions/47354521/transaction-synchronization-in-spring-kafka
   * https://www.javaworld.com/article/2077963/open-source-tools/distributed-transactions-in-spring--with-and-without-xa.html
   * https://docs.spring.io/spring-kafka/reference/html/_reference.html#transactions
   * https://github.com/spring-projects/spring-kafka/issues/580
   *
   * @return kafkaTransactionManager
   */
  @Bean
  public KafkaTransactionManager kafkaTransactionManager() {
    KafkaTransactionManager<?, ?> kafkaTransactionManager =
        new KafkaTransactionManager<>(kafkaProducerFactory());
    kafkaTransactionManager.setTransactionSynchronization(
        AbstractPlatformTransactionManager.SYNCHRONIZATION_ON_ACTUAL_TRANSACTION);
    return kafkaTransactionManager;
  }

  /*
  There are three different transaction managers beans defined in the application context:
  MongoTransactionManager, KafkaTransactionManager and ChainedTransactionManager.
  One bean has to be defined as the primary bean to use, when no explicit transaction manager
  is specified in the @Transactional annotation. It has to have the name transactionManager.
  As it is expected that all database changes have to send events, the chained transaction
  manager is defined as the primary bean to ensure that all transaction managers are
  called.
  https://stackoverflow.com/questions/47354521/transaction-synchronization-in-spring-kafka
   */
  @Bean
  public ChainedTransactionManager chainedTransactionManager(
      MongoTransactionManager mongoTransactionManager,
      KafkaTransactionManager kafkaTransactionManager) {
    return new ChainedTransactionManager(kafkaTransactionManager, mongoTransactionManager);
  }

  @Bean
  public ConsumerFactory<Object, Object> kafkaConsumerFactory() {
    return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties());
  }

  @Bean
  public ProducerFactory<?, ?> kafkaProducerFactory() {
    DefaultKafkaProducerFactory<String, Event> producerFactory =
        new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
    producerFactory.setTransactionIdPrefix(kafkaProperties.getProducer().getTransactionIdPrefix());
    return producerFactory;
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
      RetryProperties retryProperties) {
    ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setRetryTemplate(retryTemplate(retryProperties));
    factory.setConsumerFactory(kafkaConsumerFactory());
    factory.setErrorHandler(new SeekToCurrentErrorHandler(null, -1));
    factory.setRecoveryCallback(new DefaultRecoveryCallback());
    factory.getContainerProperties().setMissingTopicsFatal(false);
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    factory.setConcurrency(kafkaProperties.getListener().getConcurrency());
    return factory;
  }

  private RetryTemplate retryTemplate(RetryProperties retryProperties) {
    RetryTemplate retryTemplate = new RetryTemplate();

    ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
    backOffPolicy.setInitialInterval(retryProperties.getInitialInterval());
    backOffPolicy.setMultiplier(retryProperties.getMultiplier());
    backOffPolicy.setMaxInterval(retryProperties.getMaxInterval());
    retryTemplate.setBackOffPolicy(backOffPolicy);
    retryTemplate.setListeners(new RetryListener[] {new DefaultRetryListener()});

    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
    retryPolicy.setMaxAttempts(retryProperties.getMaxAttempts());
    retryTemplate.setRetryPolicy(retryPolicy);

    return retryTemplate;
  }
}
