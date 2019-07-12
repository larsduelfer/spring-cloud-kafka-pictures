package info.novatec.spring.showcase.comment.api;

import info.novatec.spring.showcase.comment.api.assembler.UserAssembler;
import info.novatec.spring.showcase.comment.service.UserService;
import info.novatec.spring.showcase.user.message.UserCreatedEventAvro;
import info.novatec.spring.showcase.user.message.UserUpdatedEventAvro;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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
  public void listenToUserEvents(
      Acknowledgment ack, ConsumerRecord<String, SpecificRecordBase> event) {
    if (event.value() instanceof UserCreatedEventAvro) {
      userService.save(userAssembler.assemble((UserCreatedEventAvro) event.value()));
    } else if (event.value() instanceof UserUpdatedEventAvro) {
      userService.update(userAssembler.assemble((UserUpdatedEventAvro) event.value()));
    } else {
      log.debug("Skip message of type: " + event.getClass());
    }
    ack.acknowledge();
  }
}
