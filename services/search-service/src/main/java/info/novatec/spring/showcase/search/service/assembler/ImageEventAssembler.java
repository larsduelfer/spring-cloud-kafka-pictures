package info.novatec.spring.showcase.search.service.assembler;

import info.novatec.spring.showcase.image.message.ImageMetadataUpdatedEventAvro;
import info.novatec.spring.showcase.search.model.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageEventAssembler {

  public ImageMetadataUpdatedEventAvro toMetadataUpdatedEvent(Image image) {
    return ImageMetadataUpdatedEventAvro.newBuilder()
        .setDate(image.getLastModifiedDate().getTime())
        .setIdentifier(image.getIdentifier().toString())
        .setUserIdentifier(image.getUserIdentifier().toString())
        .setTitle(image.getTitle())
        .build();
  }
}
