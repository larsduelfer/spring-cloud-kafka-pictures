package info.novatec.spring.showcase.resize.service;

import info.novatec.spring.showcase.model.Image;
import info.novatec.spring.showcase.resize.configuration.messaging.TopicConfiguration;
import info.novatec.spring.showcase.resize.model.ExifData;
import info.novatec.spring.showcase.resize.service.assembler.ImageEventAssembler;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
class ImageMessagingService {

  @Autowired private TopicConfiguration topicConfiguration;

  @Autowired private KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;

  @Autowired private ImageEventAssembler imageEventAssembler;

  void imageInvalid(UUID imageId, UUID userId) {
    sendEvent(imageId.toString(), imageEventAssembler.toInvalidEvent(imageId, userId));
  }

  void imageResized(Image image, ExifData exifData) {
    sendEvent(
        image.getImageId().toString(),
        imageEventAssembler.toExifDataExtractedEvent(image, exifData));
  }

  private void sendEvent(String key, SpecificRecordBase value) {
    kafkaTemplate.send(
        new ProducerRecord<>(
            topicConfiguration.getTopicForChannel("image"), getPartition(key), key, value));
  }

  private int getPartition(String key) {
    return Utils.toPositive(Utils.murmur2(key.getBytes(StandardCharsets.UTF_8)))
        % topicConfiguration.getPartitionsForChannel("image");
  }
}
