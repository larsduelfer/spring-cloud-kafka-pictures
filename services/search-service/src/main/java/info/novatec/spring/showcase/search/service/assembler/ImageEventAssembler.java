package info.novatec.spring.showcase.search.service.assembler;

import info.novatec.spring.showcase.image.message.v1.resource.ImageMetadataUpdatedEvent;
import info.novatec.spring.showcase.search.model.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageEventAssembler {

  public ImageMetadataUpdatedEvent toMetadataUpdatedEvent(Image image) {
    return new ImageMetadataUpdatedEvent(
        image.getLastModifiedDate(),
        image.getIdentifier(),
        image.getUserIdentifier(),
        image.getTitle());
  }
}
