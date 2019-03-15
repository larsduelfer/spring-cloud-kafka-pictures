package info.novatec.spring.showcase.user.service;

import info.novatec.spring.showcase.common.Event;
import info.novatec.spring.showcase.user.configuration.messaging.TopicConfiguration;
import info.novatec.spring.showcase.user.model.User;
import info.novatec.spring.showcase.user.service.assembler.UserEventAssembler;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

@Service
class UserMessagingService {

  @Autowired private TopicConfiguration topicConfiguration;

  @Autowired private KafkaTemplate<String, Event> kafkaTemplate;

  @Autowired private UserEventAssembler userEventAssembler;

  void userCreated(User user) {
    sendEvent(userEventAssembler.toCreatedEvent(user));
  }

  void userUpdated(User user) {
    sendEvent(userEventAssembler.toUpdatedEvent(user));
  }

  private void sendEvent(Event eventResource) {
    kafkaTemplate.send(
        new ProducerRecord<>(
            topicConfiguration.getTopicForChannel("user"),
            getPartition(eventResource),
            eventResource.getIdentifier().toString(),
            eventResource,
            getHeaders(eventResource)));
  }

  private Collection<Header> getHeaders(Event eventResource) {
    return Collections.singletonList(
        new RecordHeader(
            "schemaVersion",
            ByteBuffer.allocate(4).putInt(eventResource.getSchemaVersion()).array()));
  }

  private int getPartition(Event eventResource) {
    return Utils.toPositive(
            Utils.murmur2(
                eventResource.getIdentifier().toString().getBytes(StandardCharsets.UTF_8)))
        % topicConfiguration.getPartitionsForChannel("user");
  }
}
