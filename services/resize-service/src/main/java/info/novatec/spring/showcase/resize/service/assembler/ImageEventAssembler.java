package info.novatec.spring.showcase.resize.service.assembler;

import info.novatec.spring.showcase.image.message.v1.resource.ExifData;
import info.novatec.spring.showcase.image.message.v1.resource.ImageInvalidEvent;
import info.novatec.spring.showcase.image.message.v1.resource.ImageResizedEvent;
import info.novatec.spring.showcase.model.Image;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class ImageEventAssembler {

  public ImageResizedEvent toResizedEvent(Image image, ExifData exifData) {
    return new ImageResizedEvent(
        image.getCreatedDate(), image.getImageId(), image.getUserId(), exifData);
  }

  public ImageInvalidEvent toInvalidEvent(UUID imageId, UUID userId) {
    return new ImageInvalidEvent(new Date(), imageId, userId);
  }
}
