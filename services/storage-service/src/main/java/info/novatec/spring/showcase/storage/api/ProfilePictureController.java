package info.novatec.spring.showcase.storage.api;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class ProfilePictureController {

  @GetMapping("/{id}/binaries")
  ResponseEntity<?> findOne(@PathVariable("id") UUID id) {
    ClassPathResource classPathResource = new ClassPathResource("profile-picture.svg");

    try (InputStream inputStream = classPathResource.getInputStream()) {
      byte[] bytes = IOUtils.toByteArray(inputStream);
      MultiValueMap<String, String> headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_TYPE, "image/svg+xml");
      return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    } catch (IOException e) {
      throw new IllegalArgumentException("File from database cannot be read", e);
    }
  }
}
