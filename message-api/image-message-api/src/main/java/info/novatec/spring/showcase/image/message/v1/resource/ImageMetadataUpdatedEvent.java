package info.novatec.spring.showcase.image.message.v1.resource;

import info.novatec.spring.showcase.common.Event;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageMetadataUpdatedEvent extends Event {

  public static final int SCHEMA_VERSION = 1;

  private UUID userIdentifier;

  private String title;

  public ImageMetadataUpdatedEvent(Date date, UUID identifier, UUID userIdentifier, String title) {
    super(SCHEMA_VERSION, identifier, date);
    this.userIdentifier = userIdentifier;
    this.title = title;
  }
}
