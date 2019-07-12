package info.novatec.spring.showcase.search.api.assembler;

import info.novatec.spring.showcase.image.message.ExifDataAvro;
import info.novatec.spring.showcase.image.message.ImageExifDataExtractedEventAvro;
import info.novatec.spring.showcase.image.message.ImageUploadedEventAvro;
import info.novatec.spring.showcase.search.model.ExifData;
import info.novatec.spring.showcase.search.model.Image;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class ImageAssembler {

  public Image assemble(ImageUploadedEventAvro imageUploadedEvent) {
    return new Image(
        new Date(imageUploadedEvent.getDate()),
        new Date(imageUploadedEvent.getDate()),
        UUID.fromString(imageUploadedEvent.getIdentifier()),
        UUID.fromString(imageUploadedEvent.getUserIdentifier()),
        imageUploadedEvent.getTitle(),
        null);
  }

  public Image assemble(ImageExifDataExtractedEventAvro exifDataExtractedEvent) {
    ExifDataAvro eventExifData = exifDataExtractedEvent.getExifData();

    ExifData exifData = new ExifData();
    exifData.setAperture(eventExifData.getAperture());
    exifData.setArtist(eventExifData.getArtist());
    exifData.setBodySerialNumber(eventExifData.getBodySerialNumber());
    exifData.setCameraOwner(eventExifData.getCameraOwner());
    exifData.setColorSpace(eventExifData.getColorSpace());
    exifData.setCopyright(eventExifData.getCopyright());
    exifData.setDatetime(
        eventExifData.getDatetime() == null ? null : new Date(eventExifData.getDatetime()));
    exifData.setDatetimeDigitized(
        eventExifData.getDatetimeDigitized() == null
            ? null
            : new Date(eventExifData.getDatetimeDigitized()));
    exifData.setDatetimeOriginal(
        eventExifData.getDatetimeOriginal() == null
            ? null
            : new Date(eventExifData.getDatetimeOriginal()));
    exifData.setExifImageHeight(eventExifData.getExifImageHeight());
    exifData.setExifImageWidth(eventExifData.getExifImageWidth());
    exifData.setExposureBias(eventExifData.getExposureBias());
    exifData.setExposureProgram(eventExifData.getExposureProgram());
    exifData.setExposureTime(eventExifData.getExposureTime());
    exifData.setFlash(eventExifData.getFlash());
    exifData.setFNumber(eventExifData.getFNumber());
    exifData.setFocalLength(eventExifData.getFocalLength());
    exifData.setImageHeight(eventExifData.getImageHeight());
    exifData.setImageWidth(eventExifData.getImageWidth());
    exifData.setIso(eventExifData.getIso());
    exifData.setLensModel(eventExifData.getLensModel());
    exifData.setLensSerialNumber(eventExifData.getLensSerialNumber());
    exifData.setLensSpecification(eventExifData.getLensSpecification());
    exifData.setMake(eventExifData.getMake());
    exifData.setMeteringMode(eventExifData.getMeteringMode());
    exifData.setModel(eventExifData.getModel());
    exifData.setOrientation(eventExifData.getOrientation());
    exifData.setShutterSpeed(eventExifData.getShutterSpeed());
    exifData.setWhiteBalanceMode(eventExifData.getWhiteBalanceMode());

    return new Image(
        new Date(exifDataExtractedEvent.getDate()),
        new Date(exifDataExtractedEvent.getDate()),
        UUID.fromString(exifDataExtractedEvent.getIdentifier()),
        UUID.fromString(exifDataExtractedEvent.getUserIdentifier()),
        null,
        exifData);
  }
}
