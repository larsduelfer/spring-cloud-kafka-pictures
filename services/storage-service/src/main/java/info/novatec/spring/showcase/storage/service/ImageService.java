package info.novatec.spring.showcase.storage.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import info.novatec.spring.showcase.model.Image;
import info.novatec.spring.showcase.model.ImageResolution;
import info.novatec.spring.showcase.storage.configuration.security.SecurityContextHelper;
import info.novatec.spring.showcase.storage.model.User;
import info.novatec.spring.showcase.storage.util.GridFsTemplateExtension;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.IdGenerator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Service
public class ImageService {

  @Autowired private IdGenerator idGenerator;

  @Autowired private MongoOperations mongoOperations;

  @Autowired private GridFsTemplate gridFsTemplate;

  @Autowired private GridFsTemplateExtension gridFsTemplateExtension;

  @Autowired private ImageMessagingService imageMessagingService;

  @PreAuthorize("isAuthenticated()")
  @Transactional(transactionManager = "chainedTransactionManager")
  public void save(byte[] file, String fileName, String contentType, String title) {
    User user = SecurityContextHelper.getInstance().getCurrentUser();

    Image image = new Image();
    image.setCreatedDate(new Date());
    image.setFileName(fileName);
    image.setFileSize(file.length);
    image.setImageId(idGenerator.generateId());
    image.setImageResolution(ImageResolution.RAW);
    image.setUserId(user.getIdentifier());

    try (InputStream inputStream = new ByteArrayInputStream(file)) {
      gridFsTemplate.store(inputStream, fileName, contentType, image);
      imageMessagingService.imageUploaded(image, title);
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid image provided", e);
    }
  }

  @Transactional(transactionManager = "chainedTransactionManager")
  @PreAuthorize("@imageAuthorizationComponent.hasDeletePermission(#imageId)")
  public void delete(UUID imageId) {
    Document imageDocument = findMetadata(imageId).getMetadata();
    Assert.notNull(imageDocument, "Image document must not be null");
    Image image = mongoOperations.getConverter().read(Image.class, imageDocument);
    gridFsTemplate.delete(Query.query(Criteria.where("metadata.imageId").is(imageId)));
    imageMessagingService.imageDeleted(image);
  }

  @Transactional(readOnly = true)
  public GridFsResource findOne(UUID imageId, ImageResolution resolution) {
    GridFSFile gridFSFile =
        gridFsTemplate.findOne(
            Query.query(
                Criteria.where("metadata.imageId")
                    .is(imageId)
                    .and("metadata.imageResolution")
                    .is(resolution)));
    if (gridFSFile == null) {
      return null;
    }
    return gridFsTemplateExtension.getResource(gridFSFile);
  }

  private GridFSFile findMetadata(UUID imageId) {
    return gridFsTemplate.findOne(
        Query.query(
            Criteria.where("metadata.imageId")
                .is(imageId)
                .and("metadata.imageResolution")
                .is(ImageResolution.RAW)));
  }
}
