package info.novatec.spring.showcase.storage.api;

import info.novatec.spring.showcase.common.Event;
import info.novatec.spring.showcase.storage.api.assembler.UserAssembler;
import info.novatec.spring.showcase.storage.service.UserService;
import info.novatec.spring.showcase.user.message.v1.resource.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class UserMessagingController {

  @Autowired private UserAssembler userAssembler;

  @Autowired private UserService userService;

  @KafkaListener(topics = "#{topicConfiguration.getTopicForChannel('user')}")
  public void listenToUserEvents(Acknowledgment ack, Event event) {
    if (event instanceof UserCreatedEvent) {
      UserCreatedEvent userCreatedEvent = (UserCreatedEvent) event;
      if (userCreatedEvent.getSchemaVersion() == UserCreatedEvent.SCHEMA_VERSION) {
        userService.upsert(userAssembler.assemble(userCreatedEvent));
        ack.acknowledge();
      } else {
        throw new IllegalArgumentException(
            event.getClass().getName()
                + "'s schema version "
                + userCreatedEvent.getSchemaVersion()
                + " is not supported");
      }
    } else {
      log.debug("Skip message of type: " + event.getClass());
      ack.acknowledge();
    }
  }
}
