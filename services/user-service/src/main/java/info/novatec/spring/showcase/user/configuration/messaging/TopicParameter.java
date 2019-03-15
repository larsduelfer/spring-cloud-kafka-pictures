package info.novatec.spring.showcase.user.configuration.messaging;

import lombok.Data;

@Data
public class TopicParameter {

  private String topic;
  private Integer partitions;
}
