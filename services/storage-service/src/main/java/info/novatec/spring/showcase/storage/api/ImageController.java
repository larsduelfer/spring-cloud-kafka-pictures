package info.novatec.spring.showcase.storage.api;

import info.novatec.spring.showcase.model.ImageResolution;
import info.novatec.spring.showcase.storage.service.ImageService;
import info.novatec.spring.showcase.storage.util.FileUtilities;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ImageController {

  @Autowired private ImageService imageService;

  @PostMapping(
      path = "/binaries",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseEntity<?> save(
      @RequestParam("file") MultipartFile file, @RequestParam(required = false) String title) {
    Assert.isTrue(MediaType.IMAGE_JPEG_VALUE.equals(file.getContentType()), "Invalid content type");
    try {
      FileUtilities.validateImageFile(file.getBytes());
      imageService.save(
          file.getBytes(), file.getName(), file.getContentType(), StringUtils.trimToNull(title));
      return ResponseEntity.ok().build();
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid image provided", e);
    }
  }

  @DeleteMapping("/{id}/binaries")
  ResponseEntity<?> delete(@PathVariable("id") UUID id) {
    imageService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}/binaries/{resolution}")
  ResponseEntity<?> findOne(
      @PathVariable("id") UUID id, @PathVariable("resolution") ImageResolution resolution) {
    GridFsResource file = imageService.findOne(id, resolution);
    if (file == null) {
      file = imageService.findOne(id, ImageResolution.RAW);
      if (file == null) {
        return ResponseEntity.notFound().build();
      }
    }

    try (InputStream inputStream = file.getInputStream()) {
      byte[] bytes = IOUtils.toByteArray(inputStream);
      MultiValueMap<String, String> headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_TYPE, file.getContentType());
      return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    } catch (IOException e) {
      throw new IllegalArgumentException("File from database cannot be read", e);
    }
  }
}
