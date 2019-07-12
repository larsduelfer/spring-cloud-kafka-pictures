package info.novatec.spring.showcase.search.api.resource;

import info.novatec.spring.showcase.search.model.ExifData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ImageResource extends HalResource {

  private UUID identifier;
  private String title;

  private ExifData exifData;
}
