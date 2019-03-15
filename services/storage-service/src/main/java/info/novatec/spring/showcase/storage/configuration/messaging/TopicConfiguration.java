package info.novatec.spring.showcase.storage.configuration.messaging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class TopicConfiguration {

  private Map<String, TopicParameter> channels = new HashMap<>();

  public String getTopicForChannel(String channel) {
    return channels.get(channel).getTopic();
  }

  public Integer getPartitionsForChannel(String channel) {
    return channels.get(channel).getPartitions();
  }
}
