package info.novatec.spring.showcase.storage.api;

import info.novatec.spring.showcase.storage.api.assembler.UserAssembler;
import info.novatec.spring.showcase.storage.service.UserService;
import info.novatec.spring.showcase.user.message.UserCreatedEventAvro;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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
  public void listenToUserEvents(
      Acknowledgment ack, ConsumerRecord<String, SpecificRecordBase> event) {
    if (event.value() instanceof UserCreatedEventAvro) {
      userService.upsert(userAssembler.assemble((UserCreatedEventAvro) event.value()));
      ack.acknowledge();
    } else {
      log.debug("Skip message of type: " + event.getClass());
      ack.acknowledge();
    }
  }
}
