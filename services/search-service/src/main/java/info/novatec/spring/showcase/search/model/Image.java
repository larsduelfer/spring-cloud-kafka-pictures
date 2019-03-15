package info.novatec.spring.showcase.search.model;

import info.novatec.spring.showcase.image.message.v1.resource.ExifData;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@RequiredArgsConstructor
@Document(
    indexName = ModelConstants.INDEX_IMAGE,
    type = ModelConstants.TYPE_IMAGE,
    shards = 1,
    replicas = 0,
    refreshInterval = "-1")
public class Image {

  @NonNull @NotNull
  private Date createdDate;

  @NonNull @NotNull
  private Date lastModifiedDate;

  @Id @NonNull @NotNull private UUID identifier;

  @NonNull @NotNull private UUID userIdentifier;

  private String title;

  @Field(type = FieldType.Nested)
  private ExifData exifData;
}
