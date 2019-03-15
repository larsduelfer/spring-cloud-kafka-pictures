package info.novatec.spring.showcase.resize.configuration.messaging;

import lombok.Data;

@Data
public class TopicParameter {

  private String topic;
  private Integer partitions;
}
