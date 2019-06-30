package info.novatec.spring.showcase.resize.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/** Utility to copy exif data from one image to another. */
@Slf4j
public final class ExifDataCopyUtil {

  private ExifDataCopyUtil() {}

  /**
   * Reads exif data from image.
   *
   * @param image the image to read exif data from
   * @return exif data or null
   */
  static TiffOutputSet readExifData(byte[] image) {
    if (image == null) {
      return null;
    }
    try {
      ImageMetadata metaData = Imaging.getMetadata(image);
      if (metaData instanceof JpegImageMetadata) {
        return ((JpegImageMetadata) metaData).getExif().getOutputSet();
      }
      return null;
    } catch (ImageReadException | ImageWriteException | IOException ex) {
      log.info("Exif data couldn't be read from file", ex);
      return null;
    }
  }

  /**
   * Writes exif data to image.
   *
   * @param image the image to write exif data to
   * @param exifData the exif data to write or null if nothing to write
   * @param width the width of the image where to write exif data into
   * @param height the height of the image where to write exif data into
   * @return the image (with exif data if set - or the image without exif data if exif data is null)
   */
  static byte[] writeExifDataToImage(
      byte[] image, TiffOutputSet exifData, short width, short height) {
    if (image == null || exifData == null) {
      return image;
    }

    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      // Update image size
      exifData.getExifDirectory().removeField(ExifTagConstants.EXIF_TAG_EXIF_IMAGE_WIDTH);
      exifData.getExifDirectory().removeField(ExifTagConstants.EXIF_TAG_EXIF_IMAGE_LENGTH);
      exifData.getExifDirectory().add(ExifTagConstants.EXIF_TAG_EXIF_IMAGE_WIDTH, width);
      exifData.getExifDirectory().add(ExifTagConstants.EXIF_TAG_EXIF_IMAGE_LENGTH, height);

      // Write exif data to image
      new ExifRewriter().updateExifMetadataLossless(image, outputStream, exifData);
      return outputStream.toByteArray();
    } catch (ImageReadException | ImageWriteException | IOException ex) {
      log.info("Exif data couldn't be written to file", ex);
      return image;
    }
  }

  /**
   * Copies exif data from one image to another.
   *
   * @param from the image where to load exif data to copy from
   * @param to the image where to write exif data into
   * @param width the width of the image where to write exif data into
   * @param height the height of the image where to write exif data into
   * @return the image
   */
  public static byte[] copyExifData(byte[] from, byte[] to, short width, short height) {
    TiffOutputSet exifData = readExifData(from);
    return writeExifDataToImage(to, exifData, width, height);
  }
}
