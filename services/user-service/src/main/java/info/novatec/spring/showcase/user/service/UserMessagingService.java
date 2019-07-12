package info.novatec.spring.showcase.user.service;

import info.novatec.spring.showcase.user.configuration.messaging.TopicConfiguration;
import info.novatec.spring.showcase.user.model.User;
import info.novatec.spring.showcase.user.service.assembler.UserEventAssembler;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
class UserMessagingService {

  @Autowired private TopicConfiguration topicConfiguration;

  @Autowired private KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;

  @Autowired private UserEventAssembler userEventAssembler;

  void userCreated(User user) {
    sendEvent(user.getIdentifier().toString(), userEventAssembler.toCreatedEvent(user));
  }

  void userUpdated(User user) {
    sendEvent(user.getIdentifier().toString(), userEventAssembler.toUpdatedEvent(user));
  }

  private void sendEvent(String key, SpecificRecordBase value) {
    kafkaTemplate.send(
        new ProducerRecord<>(
            topicConfiguration.getTopicForChannel("user"), getPartition(key), key, value));
  }

  private int getPartition(String key) {
    return Utils.toPositive(Utils.murmur2(key.getBytes(StandardCharsets.UTF_8)))
        % topicConfiguration.getPartitionsForChannel("user");
  }
}
