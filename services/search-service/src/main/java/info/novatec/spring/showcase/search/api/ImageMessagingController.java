package info.novatec.spring.showcase.search.api;

import info.novatec.spring.showcase.common.Event;
import info.novatec.spring.showcase.image.message.v1.resource.ImageDeletedEvent;
import info.novatec.spring.showcase.image.message.v1.resource.ImageInvalidEvent;
import info.novatec.spring.showcase.image.message.v1.resource.ImageResizedEvent;
import info.novatec.spring.showcase.image.message.v1.resource.ImageUploadedEvent;
import info.novatec.spring.showcase.search.api.assembler.ImageAssembler;
import info.novatec.spring.showcase.search.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ImageMessagingController {

  @Value("${spring.kafka.clientId}")
  private String kafkaClientPrefix;

  @Autowired private ImageAssembler imageAssembler;

  @Autowired private ImageService imageService;

  @KafkaListener(
      topics = "#{topicConfiguration.getTopicForChannel('image')}",
      clientIdPrefix = "${spring.kafka.clientId}.image")
  public void listenToUserEvents(Acknowledgment ack, Event event) {
    if (event instanceof ImageUploadedEvent) {
      ImageUploadedEvent imageUploadedEvent = (ImageUploadedEvent) event;
      if (imageUploadedEvent.getSchemaVersion() == ImageUploadedEvent.SCHEMA_VERSION) {
        imageService.create(imageAssembler.assemble(imageUploadedEvent));
        ack.acknowledge();
      } else {
        throwUnsupportedSchemaVersionError(event);
      }
    } else if (event instanceof ImageResizedEvent) {
      ImageResizedEvent imageResizedEvent = (ImageResizedEvent) event;
      if (imageResizedEvent.getSchemaVersion() == ImageResizedEvent.SCHEMA_VERSION) {
        imageService.updateExifData(imageAssembler.assemble(imageResizedEvent));
        ack.acknowledge();
      } else {
        throwUnsupportedSchemaVersionError(event);
      }
    } else if (event instanceof ImageInvalidEvent) {
      ImageInvalidEvent imageInvalidEvent = (ImageInvalidEvent) event;
      if (imageInvalidEvent.getSchemaVersion() == ImageInvalidEvent.SCHEMA_VERSION) {
        imageService.deleteImage(imageInvalidEvent.getIdentifier());
        ack.acknowledge();
      } else {
        throwUnsupportedSchemaVersionError(event);
      }
    } else if (event instanceof ImageDeletedEvent) {
      ImageDeletedEvent imageDeletedEvent = (ImageDeletedEvent) event;
      if (imageDeletedEvent.getSchemaVersion() == ImageDeletedEvent.SCHEMA_VERSION) {
        imageService.deleteImage(imageDeletedEvent.getIdentifier());
        ack.acknowledge();
      } else {
        throwUnsupportedSchemaVersionError(event);
      }
    } else {
      log.debug("Skip message of type: " + event.getClass());
      ack.acknowledge();
    }
  }

  private void throwUnsupportedSchemaVersionError(Event event) {
    throw new IllegalArgumentException(
        event.getClass().getName()
            + "'s schema version "
            + event.getSchemaVersion()
            + " is not supported");
  }
}
