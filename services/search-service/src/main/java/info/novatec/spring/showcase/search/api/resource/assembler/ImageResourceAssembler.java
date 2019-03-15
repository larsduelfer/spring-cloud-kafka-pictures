package info.novatec.spring.showcase.search.api.resource.assembler;

import info.novatec.spring.showcase.model.ImageResolution;
import info.novatec.spring.showcase.search.api.ImageController;
import info.novatec.spring.showcase.search.api.resource.ImageResource;
import info.novatec.spring.showcase.search.configuration.security.SecurityContextHelper;
import info.novatec.spring.showcase.search.model.Image;
import info.novatec.spring.showcase.search.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.BasicLinkBuilder;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ImageResourceAssembler {

  @Autowired private UserResourceAssembler userResourceAssembler;

  public ImageResource assemble(Image image, List<User> users) {
    ImageResource imageResource = new ImageResource();
    imageResource.setExifData(image.getExifData());
    imageResource.setIdentifier(image.getIdentifier());
    imageResource.setTitle(image.getTitle() == null ? "<no title>" : image.getTitle());
    imageResource.embed("user", userResourceAssembler.assemble(users, image.getUserIdentifier()));
    imageResource.add(
        getImageLink(image.getIdentifier(), ImageResolution.MP_1, "imageSmall"),
        getImageLink(image.getIdentifier(), ImageResolution.MP_6, "imageMedium"),
        getImageLink(image.getIdentifier(), ImageResolution.RAW, "imageRaw"));

    User user = SecurityContextHelper.getInstance().getCurrentUserIfLoggedIn();
    if (user != null) {
      // Add link to add comments
      imageResource.add(
          BasicLinkBuilder.linkToCurrentMapping()
              .slash("/images/" + image.getIdentifier() + "/comments")
              .withRel("addComment"));

      // Add link to update title
      if (user.getIdentifier().equals(image.getUserIdentifier())) {
        imageResource.add(
            ControllerLinkBuilder.linkTo(
                    ControllerLinkBuilder.methodOn(ImageController.class)
                        .updateTitle(image.getIdentifier(), null))
                .withRel("updateTitle"));
      }
    }

    return imageResource;
  }

  private Link getImageLink(UUID imageId, ImageResolution resolution, String rel) {
    return BasicLinkBuilder.linkToCurrentMapping()
        .slash("/images/" + imageId + "/binaries/" + resolution)
        .withRel(rel);
  }
}
