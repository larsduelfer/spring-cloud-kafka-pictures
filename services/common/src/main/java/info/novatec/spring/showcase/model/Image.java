package info.novatec.spring.showcase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Image {

  private Date createdDate;

  private UUID imageId;

  private ImageResolution imageResolution;

  private UUID userId;

  private String fileName;

  private int fileSize;
}
