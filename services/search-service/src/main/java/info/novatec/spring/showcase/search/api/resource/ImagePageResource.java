package info.novatec.spring.showcase.search.api.resource;

import org.springframework.hateoas.PagedResources;

import java.util.Collection;

public class ImagePageResource extends PagedResource<ImageResource> {

  public ImagePageResource(
      Collection<ImageResource> imageResources, PagedResources.PageMetadata pageMetadata) {
    super(imageResources, pageMetadata);
  }
}
