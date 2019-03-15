package info.novatec.spring.showcase.comment.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@RequiredArgsConstructor
@Table
public class Image {

  @PrimaryKey @NonNull @NotNull private UUID identifier;

  @NonNull @NotNull private UUID userIdentifier;
}
