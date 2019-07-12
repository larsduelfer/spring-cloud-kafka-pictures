package info.novatec.spring.showcase.resize.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import info.novatec.spring.showcase.model.Image;
import info.novatec.spring.showcase.model.ImageResolution;
import info.novatec.spring.showcase.resize.model.ExifData;
import info.novatec.spring.showcase.resize.util.InvalidImageException;
import info.novatec.spring.showcase.resize.util.ResizeUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResizeService {

  @Autowired private MongoOperations mongoOperations;

  @Autowired private ImageService imageService;

  @Autowired private ImageMessagingService imageMessagingService;

  @Transactional(transactionManager = "chainedTransactionManager")
  public void resize(UUID imageId) {
    Map<ImageResolution, GridFSFile> existingImageSizes = imageService.findImageSizes(imageId);
    GridFSFile rawDbFile = existingImageSizes.get(ImageResolution.RAW);

    // Skip resizing if the original image isn't in the database  or all image sizes already exist
    if (rawDbFile == null
        || existingImageSizes.keySet().containsAll(Arrays.asList(ImageResolution.values()))) {
      return;
    }

    // Load image binary from database and convert to byte array
    byte[] imageBinary = loadImageBinary(rawDbFile);

    // Extract metadata from database object
    Image metadata = imageService.extractMetadata(rawDbFile);

    // Extract content type
    Assert.notNull(rawDbFile.getMetadata(), "Content type is null");
    String contentType = (String) rawDbFile.getMetadata().get("_contentType");

    // Find missing image sizes
    Set<ImageResolution> missingImageSizes = getMissingImageSizes(existingImageSizes);

    try {
      ExifData exifData = ResizeUtil.readExifData(imageBinary);

      // Resize image to all required resolutions as specified in the image resolution enumeration
      for (ImageResolution size : missingImageSizes) {
        resizeAndSave(imageBinary, metadata, exifData, size, contentType);
      }

      // Send operation complete message with extracted EXIF data
      imageMessagingService.imageResized(metadata, exifData);
    } catch (InvalidImageException ex) {
      // Image is invalid - delete image and send notification to other services
      imageService.delete(metadata.getImageId());
      imageMessagingService.imageInvalid(metadata.getImageId(), metadata.getUserId());
    }
  }

  private Set<ImageResolution> getMissingImageSizes(
      Map<ImageResolution, GridFSFile> existingSizes) {
    Set<ImageResolution> allSizes =
        Arrays.stream(ImageResolution.values()).collect(Collectors.toSet());
    allSizes.remove(ImageResolution.RAW);
    allSizes.removeAll(existingSizes.keySet());
    return allSizes;
  }

  private byte[] loadImageBinary(GridFSFile fsFile) {
    GridFsResource gridFsResource = imageService.loadResource(fsFile);
    try {
      return IOUtils.toByteArray(gridFsResource.getInputStream());
    } catch (IOException ex) {
      throw new IllegalStateException("Image data couldn't be read", ex);
    }
  }

  private void resizeAndSave(
      byte[] imageBinary,
      Image metadata,
      ExifData exifData,
      ImageResolution resolution,
      String contentType)
      throws InvalidImageException {
    Assert.notNull(contentType, "Content type is null");
    byte[] resizedImage = ResizeUtil.resizeImage(imageBinary, exifData, resolution);
    imageService.saveImage(
        resizedImage,
        metadata.toBuilder().imageResolution(resolution).fileSize(resizedImage.length).build(),
        contentType);
  }
}
