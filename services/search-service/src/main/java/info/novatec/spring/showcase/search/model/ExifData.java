package info.novatec.spring.showcase.search.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ExifData {

  // Jpeg Directory
  private Long imageHeight;
  private Long imageWidth;

  // Exif IFD0 Directory
  private String artist;
  private String copyright;
  private Date datetime;
  private String make;
  private String model;
  private String orientation;

  // Exif Sub IFD Directory
  private String aperture;
  private String bodySerialNumber;
  private String cameraOwner;
  private String colorSpace;
  private Date datetimeDigitized;
  private Date datetimeOriginal;
  private String exifImageHeight;
  private String exifImageWidth;
  private String exposureBias;
  private String exposureProgram;
  private String exposureTime;
  private String flash;
  private String fNumber;
  private String focalLength;
  private Long iso;
  private String lensModel;
  private String lensSerialNumber;
  private String lensSpecification;
  private String meteringMode;
  private String shutterSpeed;
  private String whiteBalanceMode;
}
