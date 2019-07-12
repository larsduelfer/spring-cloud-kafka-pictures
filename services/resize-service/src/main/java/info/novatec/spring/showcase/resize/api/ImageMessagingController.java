package info.novatec.spring.showcase.resize.api;

import info.novatec.spring.showcase.image.message.ImageUploadedEventAvro;
import info.novatec.spring.showcase.resize.service.ResizeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@Controller
public class ImageMessagingController {

  @Autowired private ResizeService resizeService;

  @KafkaListener(topics = "#{topicConfiguration.getTopicForChannel('image')}")
  public void listenToImageEvents(
      Acknowledgment ack, ConsumerRecord<String, SpecificRecordBase> event) {
    if (event.value() instanceof ImageUploadedEventAvro) {
      resizeService.resize(
          UUID.fromString(((ImageUploadedEventAvro) event.value()).getIdentifier()));
      ack.acknowledge();
    } else {
      log.debug("Skip message of type: " + event.getClass());
      ack.acknowledge();
    }
  }
}
