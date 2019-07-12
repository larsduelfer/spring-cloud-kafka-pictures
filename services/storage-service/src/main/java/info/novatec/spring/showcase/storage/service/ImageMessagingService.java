package info.novatec.spring.showcase.storage.service;

import info.novatec.spring.showcase.model.Image;
import info.novatec.spring.showcase.storage.configuration.messaging.TopicConfiguration;
import info.novatec.spring.showcase.storage.service.assembler.ImageEventAssembler;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
class ImageMessagingService {

  @Autowired private TopicConfiguration topicConfiguration;

  @Autowired private KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;

  @Autowired private ImageEventAssembler imageEventAssembler;

  void imageUploaded(Image image, String title) {
    sendEvent(image.getImageId().toString(), imageEventAssembler.toUploadedEvent(image, title));
  }

  void imageDeleted(Image image) {
    sendEvent(image.getImageId().toString(), imageEventAssembler.toDeletedEvent(image));
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
