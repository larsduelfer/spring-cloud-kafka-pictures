package info.novatec.spring.showcase.storage.util;

import com.drew.imaging.FileType;
import com.drew.imaging.FileTypeDetector;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public final class FileUtilities {

  private FileUtilities() {}

  /**
   * Validates that the given file contains a jpeg image.
   *
   * @param image the image as byte array
   */
  public static void validateImageFile(byte[] image) {
    try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(image);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(byteArrayInputStream)) {
      FileType fileType = FileTypeDetector.detectFileType(bufferedInputStream);
      if (FileType.Jpeg != fileType) {
        throw new IllegalStateException("Invalid image provided");
      }
    } catch (IOException ex) {
      throw new IllegalStateException("Invalid image provided", ex);
    }
  }
}
