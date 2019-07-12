package info.novatec.spring.showcase.resize.service.assembler;

import info.novatec.spring.showcase.image.message.ExifDataAvro;
import info.novatec.spring.showcase.image.message.ImageExifDataExtractedEventAvro;
import info.novatec.spring.showcase.image.message.ImageInvalidEventAvro;
import info.novatec.spring.showcase.model.Image;
import info.novatec.spring.showcase.resize.model.ExifData;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class ImageEventAssembler {

  public ImageExifDataExtractedEventAvro toExifDataExtractedEvent(Image image, ExifData exifData) {
    return ImageExifDataExtractedEventAvro.newBuilder()
        .setDate(image.getCreatedDate().getTime())
        .setIdentifier(image.getImageId().toString())
        .setUserIdentifier(image.getUserId().toString())
        .setExifDataBuilder(
            ExifDataAvro.newBuilder()
                .setAperture(exifData.getAperture())
                .setArtist(exifData.getArtist())
                .setBodySerialNumber(exifData.getBodySerialNumber())
                .setCameraOwner(exifData.getCameraOwner())
                .setColorSpace(exifData.getColorSpace())
                .setCopyright(exifData.getCopyright())
                .setDatetime(exifData.getDatetime().getTime())
                .setDatetimeDigitized(exifData.getDatetimeDigitized().getTime())
                .setDatetimeOriginal(exifData.getDatetimeOriginal().getTime())
                .setExifImageHeight(exifData.getExifImageHeight())
                .setExifImageWidth(exifData.getExifImageWidth())
                .setExposureBias(exifData.getExposureBias())
                .setExposureProgram(exifData.getExposureProgram())
                .setExposureTime(exifData.getExposureTime())
                .setFlash(exifData.getFlash())
                .setFNumber(exifData.getFNumber())
                .setFocalLength(exifData.getFocalLength())
                .setImageHeight(exifData.getImageHeight())
                .setImageWidth(exifData.getImageWidth())
                .setIso(exifData.getIso())
                .setLensModel(exifData.getLensModel())
                .setLensSerialNumber(exifData.getLensSerialNumber())
                .setLensSpecification(exifData.getLensSpecification())
                .setMake(exifData.getMake())
                .setMeteringMode(exifData.getMeteringMode())
                .setModel(exifData.getModel())
                .setOrientation(exifData.getOrientation())
                .setShutterSpeed(exifData.getShutterSpeed())
                .setWhiteBalanceMode(exifData.getWhiteBalanceMode()))
        .build();
  }

  public ImageInvalidEventAvro toInvalidEvent(UUID imageId, UUID userId) {
    return ImageInvalidEventAvro.newBuilder()
        .setDate(new Date().getTime())
        .setIdentifier(imageId.toString())
        .setUserIdentifier(userId.toString())
        .build();
  }
}
