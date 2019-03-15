package info.novatec.spring.showcase.search.api.resource.assembler;

import info.novatec.spring.showcase.search.api.resource.ImagePageResource;
import info.novatec.spring.showcase.search.api.resource.ImageResource;
import info.novatec.spring.showcase.search.model.Image;
import info.novatec.spring.showcase.search.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImagePageResourceAssembler {

  @Autowired private ImageResourceAssembler imageResourceAssembler;

  public ImagePageResource assemble(Page<Image> images, List<User> users) {
    List<ImageResource> imageResources =
        images.stream()
            .map(image -> imageResourceAssembler.assemble(image, users))
            .collect(Collectors.toList());
    return new ImagePageResource(
        imageResources,
        new PagedResources.PageMetadata(
            images.getSize(),
            images.getNumber(),
            images.getTotalElements(),
            images.getTotalPages()));
  }
}
