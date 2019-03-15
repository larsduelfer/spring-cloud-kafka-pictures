package info.novatec.spring.showcase.search.api.assembler;

import info.novatec.spring.showcase.image.message.v1.resource.ImageResizedEvent;
import info.novatec.spring.showcase.image.message.v1.resource.ImageUploadedEvent;
import info.novatec.spring.showcase.search.model.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageAssembler {

  public Image assemble(ImageUploadedEvent imageUploadedEvent) {
    Image image = new Image(
        imageUploadedEvent.getDate(),
        imageUploadedEvent.getDate(),
        imageUploadedEvent.getIdentifier(),
        imageUploadedEvent.getUserIdentifier());
    image.setTitle(imageUploadedEvent.getTitle());
    return image;
  }

  public Image assemble(ImageResizedEvent imageResizedEvent) {
    Image image = new Image();
    image.setLastModifiedDate(imageResizedEvent.getDate());
    image.setIdentifier(imageResizedEvent.getIdentifier());
    image.setUserIdentifier(imageResizedEvent.getUserIdentifier());
    image.setExifData(imageResizedEvent.getExifData());
    return image;
  }
}
