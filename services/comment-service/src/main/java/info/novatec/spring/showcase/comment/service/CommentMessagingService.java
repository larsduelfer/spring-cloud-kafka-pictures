package info.novatec.spring.showcase.comment.service;

import info.novatec.spring.showcase.comment.configuration.messaging.TopicConfiguration;
import info.novatec.spring.showcase.comment.model.Comment;
import info.novatec.spring.showcase.comment.service.assembler.CommentEventAssembler;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
class CommentMessagingService {

  @Autowired private TopicConfiguration topicConfiguration;

  @Autowired private KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;

  @Autowired private CommentEventAssembler commentEventAssembler;

  void commentAdded(Comment comment) {
    sendEvent(
        comment.getIdentifier().toString(), commentEventAssembler.toCommentAddedEvent(comment));
  }

  private void sendEvent(String key, SpecificRecordBase event) {
    kafkaTemplate.send(
        new ProducerRecord<>(
            topicConfiguration.getTopicForChannel("comment"), getPartition(key), key, event));
  }

  private int getPartition(String key) {
    return Utils.toPositive(Utils.murmur2(key.getBytes(StandardCharsets.UTF_8)))
        % topicConfiguration.getPartitionsForChannel("comment");
  }
}
