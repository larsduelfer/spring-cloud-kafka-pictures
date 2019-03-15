package info.novatec.spring.showcase.comment.configuration.messaging;

import lombok.Data;

@Data
public class TopicParameter {

  private String topic;
  private Integer partitions;
}
