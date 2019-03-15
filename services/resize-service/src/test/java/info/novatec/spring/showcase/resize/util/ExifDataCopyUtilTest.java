package info.novatec.spring.showcase.resize.util;

import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.imgscalr.Scalr;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/** Test to verify expected behavior of {@link ExifDataCopyUtil}. */
public class ExifDataCopyUtilTest {

  private final ClassPathResource sampleImageResource =
      new ClassPathResource("images/canon-sample-image.jpg");

  /**
   * Verifies that {@link ExifDataCopyUtil#readExifData(byte[])} works as expected if provided image
   * is null.
   */
  @Test
  public void verifyReadExifDataImageNull() {
    TiffOutputSet tiffOutputSet = ExifDataCopyUtil.readExifData(null);
    Assertions.assertThat(tiffOutputSet).isNull();
  }

  /**
   * Verifies that {@link ExifDataCopyUtil#readExifData(byte[])} works as expected if invalid image
   * is provided.
   */
  @Test
  public void verifyReadExifDataInvalidImage() {
    byte[] image = new byte[0];
    TiffOutputSet tiffOutputSet = ExifDataCopyUtil.readExifData(image);
    Assertions.assertThat(tiffOutputSet).isNull();
  }

  /**
   * Verifies that {@link ExifDataCopyUtil#readExifData(byte[])} works as expected if valid image
   * with exif data is provided.
   *
   * @throws IOException if image cannot be read
   */
  @Test
  public void verifyReadExifData() throws IOException {
    byte[] image = FileUtils.readFileToByteArray(sampleImageResource.getFile());
    TiffOutputSet tiffOutputSet = ExifDataCopyUtil.readExifData(image);
    Assertions.assertThat(tiffOutputSet).isNotNull();
    Assertions.assertThat(tiffOutputSet.getDirectories().size()).isEqualTo(5);
    List<Integer> directorySizes = new ArrayList<>(Arrays.asList(10, 37, 2, 0, 5));
    tiffOutputSet
        .getDirectories()
        .forEach(
            directory -> {
              int index = directorySizes.indexOf(directory.getFields().size());
              assertThat(index).isNotEqualTo(-1);
              directorySizes.remove(index);
            });
  }

  /**
   * Verifies that {@link ExifDataCopyUtil#readExifData(byte[])} works as expected.
   *
   * @throws IOException if image cannot be read or written
   */
  @Test
  public void verifyWriteExifDataToImage() throws IOException {
    BufferedImage bufferedImage = ImageIO.read(sampleImageResource.getFile());
    BufferedImage resizedBufferedImage =
        Scalr.resize(
            bufferedImage,
            Scalr.Method.QUALITY,
            Scalr.Mode.FIT_EXACT,
            3000,
            2000,
            Scalr.OP_ANTIALIAS);
    byte[] resizedImage;
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      ImageIO.write(resizedBufferedImage, "jpg", outputStream);
      resizedImage = outputStream.toByteArray();
    }
    byte[] image = FileUtils.readFileToByteArray(sampleImageResource.getFile());
    TiffOutputSet tiffOutputSet = ExifDataCopyUtil.readExifData(image);
    byte[] writtenImage =
        ExifDataCopyUtil.writeExifDataToImage(
            resizedImage, tiffOutputSet, (short) 3000, (short) 2000);
    TiffOutputSet writtenOutputSet = ExifDataCopyUtil.readExifData(writtenImage);

    Assertions.assertThat(writtenOutputSet).isNotNull();
    Assertions.assertThat(writtenOutputSet.getDirectories().size()).isEqualTo(5);
    List<Integer> directorySizes = new ArrayList<>(Arrays.asList(10, 37, 2, 0, 5));
    writtenOutputSet
        .getDirectories()
        .forEach(
            directory -> {
              int index = directorySizes.indexOf(directory.getFields().size());
              assertThat(index).isNotEqualTo(-1);
              directorySizes.remove(index);
            });
  }

  /**
   * Verifies that {@link ExifDataCopyUtil#copyExifData(byte[], byte[], short, short)} works as
   * expected.
   *
   * @throws IOException if image cannot be read or written
   */
  @Test
  public void verifyCopyExifData() throws IOException {
    BufferedImage bufferedImage = ImageIO.read(sampleImageResource.getFile());
    BufferedImage resizedBufferedImage =
        Scalr.resize(
            bufferedImage,
            Scalr.Method.QUALITY,
            Scalr.Mode.FIT_EXACT,
            3000,
            2000,
            Scalr.OP_ANTIALIAS);
    byte[] resizedImage;
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      ImageIO.write(resizedBufferedImage, "jpg", outputStream);
      resizedImage = outputStream.toByteArray();
    }
    byte[] image = FileUtils.readFileToByteArray(sampleImageResource.getFile());
    byte[] writtenImage =
        ExifDataCopyUtil.copyExifData(image, resizedImage, (short) 3000, (short) 2000);
    TiffOutputSet writtenOutputSet = ExifDataCopyUtil.readExifData(writtenImage);

    Assertions.assertThat(writtenOutputSet).isNotNull();
    Assertions.assertThat(writtenOutputSet.getDirectories().size()).isEqualTo(5);
    List<Integer> directorySizes = new ArrayList<>(Arrays.asList(10, 37, 2, 0, 5));
    writtenOutputSet
        .getDirectories()
        .forEach(
            directory -> {
              int index = directorySizes.indexOf(directory.getFields().size());
              assertThat(index).isNotEqualTo(-1);
              directorySizes.remove(index);
            });
  }
}
