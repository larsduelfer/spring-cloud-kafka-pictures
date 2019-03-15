package info.novatec.spring.showcase.comment.model;

import lombok.*;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

//https://www.datastax.com/dev/blog/the-most-important-thing-to-know-in-cassandra-data-modeling-the-primary-key
@Data
@EqualsAndHashCode
@NoArgsConstructor
@RequiredArgsConstructor
@Table
public class Comment {

  @PrimaryKeyColumn(name = "commentidentifier", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
  @NonNull
  @NotNull
  private UUID identifier;

  @PrimaryKeyColumn(name = "imageidentifier", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
  @NonNull @NotNull private UUID imageIdentifier;

  @NonNull @NotNull private UUID userIdentifier;

  @NonNull @NotNull private String comment;

  @PrimaryKeyColumn(
      name = "creationdate",
      ordinal = 1,
      type = PrimaryKeyType.CLUSTERED,
      ordering = Ordering.DESCENDING)
  @NonNull
  @NotNull
  private Date date;
}
