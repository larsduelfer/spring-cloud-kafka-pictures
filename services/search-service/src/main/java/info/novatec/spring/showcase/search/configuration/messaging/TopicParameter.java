package info.novatec.spring.showcase.search.configuration.messaging;

import lombok.Data;

@Data
public class TopicParameter {

  private String topic;
  private Integer partitions;
}
