package info.novatec.spring.showcase.resize.service;

import info.novatec.spring.showcase.common.Event;
import info.novatec.spring.showcase.image.message.v1.resource.ExifData;
import info.novatec.spring.showcase.model.Image;
import info.novatec.spring.showcase.resize.configuration.messaging.TopicConfiguration;
import info.novatec.spring.showcase.resize.service.assembler.ImageEventAssembler;
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
import java.util.UUID;

@Service
class ImageMessagingService {

  @Autowired private TopicConfiguration topicConfiguration;

  @Autowired private KafkaTemplate<String, Event> kafkaTemplate;

  @Autowired private ImageEventAssembler imageEventAssembler;

  void imageInvalid(UUID imageId, UUID userId) {
    sendEvent(imageEventAssembler.toInvalidEvent(imageId, userId));
  }

  void imageResized(Image image, ExifData exifData) {
    sendEvent(imageEventAssembler.toResizedEvent(image, exifData));
  }

  private void sendEvent(Event event) {
    kafkaTemplate.send(
        new ProducerRecord<>(
            topicConfiguration.getTopicForChannel("image"),
            getPartition(event),
            event.getIdentifier().toString(),
            event,
            getHeaders(event)));
  }

  private Collection<Header> getHeaders(Event event) {
    return Collections.singletonList(
        new RecordHeader(
            "schemaVersion", ByteBuffer.allocate(4).putInt(event.getSchemaVersion()).array()));
  }

  private int getPartition(Event event) {
    return Utils.toPositive(
            Utils.murmur2(event.getIdentifier().toString().getBytes(StandardCharsets.UTF_8)))
        % topicConfiguration.getPartitionsForChannel("image");
  }
}
