package info.novatec.spring.showcase.comment.api;

import info.novatec.spring.showcase.comment.api.assembler.UserAssembler;
import info.novatec.spring.showcase.comment.service.UserService;
import info.novatec.spring.showcase.common.Event;
import info.novatec.spring.showcase.user.message.v1.resource.UserCreatedEvent;
import info.novatec.spring.showcase.user.message.v1.resource.UserUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class UserMessagingController {

  @Value("${spring.kafka.clientId}")
  private String kafkaClientPrefix;

  @Autowired private UserAssembler userAssembler;

  @Autowired private UserService userService;

  @KafkaListener(
      topics = "#{topicConfiguration.getTopicForChannel('user')}",
      clientIdPrefix = "${spring.kafka.properties.clientId.app}.user")
  public void listenToUserEvents(Acknowledgment ack, Event event) {
    if (event instanceof UserCreatedEvent) {
      UserCreatedEvent userCreatedEvent = (UserCreatedEvent) event;
      if (userCreatedEvent.getSchemaVersion() == UserCreatedEvent.SCHEMA_VERSION) {
        userService.save(userAssembler.assemble(userCreatedEvent));
        ack.acknowledge();
      } else {
        throwUnsupportedSchemaVersionError(event);
      }
    } else if (event instanceof UserUpdatedEvent) {
      UserUpdatedEvent userUpdatedEvent = (UserUpdatedEvent) event;
      if (userUpdatedEvent.getSchemaVersion() == UserCreatedEvent.SCHEMA_VERSION) {
        userService.update(userAssembler.assemble(userUpdatedEvent));
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
