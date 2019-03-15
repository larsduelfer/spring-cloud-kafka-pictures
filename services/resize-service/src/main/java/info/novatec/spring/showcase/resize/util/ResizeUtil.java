package info.novatec.spring.showcase.resize.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import info.novatec.spring.showcase.image.message.v1.resource.ExifData;
import info.novatec.spring.showcase.model.ImageResolution;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.util.Assert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImagingOpException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

@Slf4j
public final class ResizeUtil {

  private static final String INVALID_IMAGE_PROVIDED = "Invalid image provided";

  private static final String NO_EXIFDATA_AVAILABLE = "No exif data available";

  private static final String NO_IMAGE_PROVIDED = "No image provided";

  private static final String NO_RESOLUTION_AVAILABLE = "No resolution available";

  private ResizeUtil() {}

  public static ExifData readExifData(byte[] image) throws InvalidImageException {
    Assert.notNull(image, NO_IMAGE_PROVIDED);
    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(image)) {
      Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

      // Get directories from image
      JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
      ExifIFD0Directory exifIFD0Directory =
          metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
      ExifSubIFDDirectory exifSubIFDDirectory =
          metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

      if (jpegDirectory == null) {
        throw new InvalidImageException(INVALID_IMAGE_PROVIDED);
      } else {
        ExifData exifData = new ExifData();

        // Jpeg Directory
        exifData.setImageHeight(getLongValue(jpegDirectory, JpegDirectory.TAG_IMAGE_HEIGHT));
        exifData.setImageWidth(getLongValue(jpegDirectory, JpegDirectory.TAG_IMAGE_WIDTH));

        // Exif IFD0 Directory
        if (exifIFD0Directory != null) {
          exifData.setArtist(getStringValue(exifIFD0Directory, ExifIFD0Directory.TAG_ARTIST));
          exifData.setCopyright(getStringValue(exifIFD0Directory, ExifIFD0Directory.TAG_COPYRIGHT));
          exifData.setDatetime(getDateValue(exifIFD0Directory, ExifIFD0Directory.TAG_DATETIME));
          exifData.setMake(getStringValue(exifIFD0Directory, ExifIFD0Directory.TAG_MAKE));
          exifData.setModel(getStringValue(exifIFD0Directory, ExifIFD0Directory.TAG_MODEL));
          exifData.setOrientation(
              getStringValue(exifIFD0Directory, ExifIFD0Directory.TAG_ORIENTATION));
        }

        // Exif Sub IFD Directory
        if (exifSubIFDDirectory != null) {
          exifData.setAperture(
              getStringValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_APERTURE));
          exifData.setBodySerialNumber(
              getStringValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_BODY_SERIAL_NUMBER));
          exifData.setDatetimeDigitized(
              getDateValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED));
          exifData.setCameraOwner(
              getStringValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_CAMERA_OWNER_NAME));
          exifData.setColorSpace(
              getStringValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_COLOR_SPACE));
          exifData.setDatetimeOriginal(
              getDateValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
          exifData.setExifImageHeight(
              getStringValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT));
          exifData.setExifImageWidth(
              getStringValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH));
          exifData.setExposureBias(
              getStringValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_EXPOSURE_BIAS));
          exifData.setExposureProgram(
              getStringValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_EXPOSURE_PROGRAM));
          exifData.setExposureTime(
              getStringValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_EXPOSURE_TIME));
          exifData.setFlash(getStringValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_FLASH));
          exifData.setFocalLength(
              getStringValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_FOCAL_LENGTH));
          exifData.setLensModel(
              getStringValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_LENS_MODEL));
          exifData.setLensSerialNumber(
              getStringValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_LENS_SERIAL_NUMBER));
          exifData.setLensSpecification(
              getStringValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_LENS_SPECIFICATION));
          exifData.setFNumber(getStringValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_FNUMBER));
          exifData.setIso(
              getLongValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_ISO_EQUIVALENT));
          exifData.setMeteringMode(
              getStringValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_METERING_MODE));
          exifData.setShutterSpeed(
              getStringValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_SHUTTER_SPEED));
          exifData.setWhiteBalanceMode(
              getStringValue(exifSubIFDDirectory, ExifSubIFDDirectory.TAG_WHITE_BALANCE_MODE));
        }

        return exifData;
      }
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    } catch (ImageProcessingException ex) {
      throw new InvalidImageException(INVALID_IMAGE_PROVIDED, ex);
    }
  }

  private static Long getLongValue(Directory directory, int tag) {
    try {
      return directory.getLong(tag);
    } catch (MetadataException e) {
      log.debug("Couldn't read tag: {}", tag, e);
      return null;
    }
  }

  private static String getStringValue(Directory directory, int tag) {
    return directory.getDescription(tag);
  }

  private static Date getDateValue(Directory directory, int tag) {
    return directory.getDate(tag);
  }

  /**
   * Resizes image to expected resolution. Aspect ratio is kept.
   *
   * @param image the image to resize
   * @param expectedResolution expected resolution.
   * @return the resized image and the corresponding exif data
   */
  public static byte[] resizeImage(
      byte[] image, ExifData exifData, ImageResolution expectedResolution)
      throws InvalidImageException {
    Assert.notNull(image, NO_IMAGE_PROVIDED);
    Assert.notNull(exifData, NO_EXIFDATA_AVAILABLE);
    Assert.notNull(expectedResolution, NO_RESOLUTION_AVAILABLE);

    // Calculate size of resized image.
    Tuple<Long, Long> imageSize =
        calculateImageSizeByResolution(
            exifData.getImageWidth(),
            exifData.getImageHeight(),
            expectedResolution.getMaxResolution());

    if (expectedResolution == ImageResolution.RAW
        || exifData.getImageWidth() * exifData.getImageHeight()
            < expectedResolution.getMaxResolution()) {
      return image;
    }

    // Load and resize image
    try (ByteArrayInputStream originalImageInputStream = new ByteArrayInputStream(image);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      BufferedImage bufferedImage = ImageIO.read(originalImageInputStream);
      BufferedImage resizedImageBuffer =
          resizeImage(
              bufferedImage,
              Scalr.Method.QUALITY,
              Scalr.Mode.FIT_EXACT,
              imageSize.getFirst().intValue(),
              imageSize.getSecond().intValue(),
              Scalr.OP_ANTIALIAS);
      ImageIO.write(resizedImageBuffer, "jpg", outputStream);
      byte[] resizedImage = outputStream.toByteArray();

      return ExifDataCopyUtil.copyExifData(
          image,
          resizedImage,
          (short) (long) imageSize.getFirst(),
          (short) (long) imageSize.getSecond());
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  /**
   * Calculates image size based on expected maximum resolution. If the resolution is higher than
   * the specified maximum resolution, then recalculated size is returned. Aspect ratio is
   * considered / kept in size calculation.
   *
   * @param width original width, value != null && value > 0
   * @param height original height, value != null && value > 0
   * @param maxResolution expected resolution (width*height) of resized image, value > 0
   * @return tuple of size values containing width and height.
   */
  private static Tuple<Long, Long> calculateImageSizeByResolution(
      Long width, Long height, Long maxResolution) {
    Assert.notNull(width, "Invalid image provided");
    Assert.notNull(height, "Invalid image provided");
    Assert.isTrue(width > 0, "Image width must be greater than 0");
    Assert.isTrue(height > 0, "Image height must be greater than 0");
    if (width * height <= maxResolution) {
      return Tuple.of(width, height);
    }
    double aspectRatio = (width * 1.0) / (height * 1.0);
    Long newHeight = Math.round(Math.floor(Math.sqrt(maxResolution / aspectRatio)));
    Long newWidth = Math.round(Math.floor(aspectRatio * newHeight));
    return Tuple.of(newWidth, newHeight);
  }

  /**
   * Resizes the given image.
   *
   * @param bufferedImage the buffered image to scale
   * @param scalingMethod the resize method
   * @param resizeMode the resize mode
   * @param targetWidth target width of image
   * @param targetHeight target height of image
   * @param ops options to use for resizing
   * @return the resized image
   */
  private static BufferedImage resizeImage(
      BufferedImage bufferedImage,
      Scalr.Method scalingMethod,
      Scalr.Mode resizeMode,
      int targetWidth,
      int targetHeight,
      BufferedImageOp... ops)
      throws InvalidImageException {
    try {
      return Scalr.resize(bufferedImage, scalingMethod, resizeMode, targetWidth, targetHeight, ops);
    } catch (ImagingOpException e) {
      throw new InvalidImageException(INVALID_IMAGE_PROVIDED, e);
    }
  }
}
