package info.novatec.spring.showcase.search.api;

import info.novatec.spring.showcase.search.api.resource.assembler.ImagePageResourceAssembler;
import info.novatec.spring.showcase.search.api.resource.assembler.ImageResourceAssembler;
import info.novatec.spring.showcase.search.model.Image;
import info.novatec.spring.showcase.search.model.User;
import info.novatec.spring.showcase.search.service.ImageService;
import info.novatec.spring.showcase.search.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping
public class ImageController {

  @Autowired private ImageService imageService;

  @Autowired private UserService userService;

  @Autowired private ImageResourceAssembler imageResourceAssembler;

  @Autowired private ImagePageResourceAssembler imagePageResourceAssembler;

  @PatchMapping(path = "/images/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<?> updateTitle(
      @PathVariable("id") UUID imageId, @RequestParam String title) {
    Image image = imageService.updateTitle(imageId, StringUtils.trimToNull(title));
    List<Image> images = Collections.singletonList(image);
    List<User> users = findUsers(images);
    return ResponseEntity.ok(imageResourceAssembler.assemble(image, users));
  }

  @GetMapping(path = "/images/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseEntity<?> findOne(@PathVariable("id") UUID identifier) {
    Image image = imageService.findOne(identifier);
    if (image == null) {
      return ResponseEntity.noContent().build();
    }
    List<User> users = findUsers(Collections.singletonList(image));
    return ResponseEntity.ok(imageResourceAssembler.assemble(image, users));
  }

  @GetMapping(path = "/images/search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseEntity<?> search(@RequestParam(value = "term") String term, Pageable pageable) {
    Page<Image> images = imageService.findImages(term, pageable);
    List<User> users = findUsers(images);
    return ResponseEntity.ok(imagePageResourceAssembler.assemble(images, users));
  }

  @GetMapping(path = "/users/{id}/images", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseEntity<?> findUserImages(@PathVariable("id") UUID userIdentifier, Pageable pageable) {
    Page<Image> images = imageService.findUserImages(userIdentifier, pageable);
    List<User> users = findUsers(images);
    return ResponseEntity.ok(imagePageResourceAssembler.assemble(images, users));
  }

  private List<User> findUsers(Iterable<Image> images) {
    List<UUID> userIdentifiers =
        StreamSupport.stream(images.spliterator(), false)
            .map(Image::getUserIdentifier)
            .collect(Collectors.toList());
    if (userIdentifiers.isEmpty()) {
      return Collections.emptyList();
    }
    return userService.findAllByUserIdentifiers(userIdentifiers);
  }
}
