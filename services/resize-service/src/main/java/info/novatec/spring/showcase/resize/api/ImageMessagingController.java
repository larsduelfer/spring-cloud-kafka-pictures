package info.novatec.spring.showcase.resize.api;

import info.novatec.spring.showcase.common.Event;
import info.novatec.spring.showcase.image.message.v1.resource.ImageUploadedEvent;
import info.novatec.spring.showcase.resize.service.ResizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ImageMessagingController {

  @Autowired private ResizeService resizeService;

  @KafkaListener(topics = "#{topicConfiguration.getTopicForChannel('image')}")
  public void listenToImageEvents(Acknowledgment ack, Event event) {
    if (event instanceof ImageUploadedEvent) {
      ImageUploadedEvent imageUploadedEvent = (ImageUploadedEvent) event;
      if (imageUploadedEvent.getSchemaVersion() == ImageUploadedEvent.SCHEMA_VERSION) {
        resizeService.resize(imageUploadedEvent.getIdentifier());
        ack.acknowledge();
      } else {
        throw new IllegalArgumentException(
            event.getClass().getName()
                + "'s schema version "
                + event.getSchemaVersion()
                + " is not supported");
      }
    } else {
      log.debug("Skip message of type: " + event.getClass());
      ack.acknowledge();
    }
  }
}
