package info.novatec.spring.showcase.resize.service;

import com.mongodb.Block;
import com.mongodb.client.gridfs.model.GridFSFile;
import info.novatec.spring.showcase.model.Image;
import info.novatec.spring.showcase.model.ImageResolution;
import info.novatec.spring.showcase.resize.util.GridFsTemplateExtension;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
class ImageService {

  @Autowired private GridFsTemplate gridFsTemplate;

  @Autowired private GridFsTemplateExtension gridFsTemplateExtension;

  @Autowired private MongoOperations mongoOperations;

  @Transactional(readOnly = true)
  Image extractMetadata(GridFSFile gridFSFile) {
    Document metadata = gridFSFile.getMetadata();
    Assert.notNull(metadata, "Metadata not found");
    return mongoOperations.getConverter().read(Image.class, metadata);
  }

  @Transactional
  void delete(UUID imageId) {
    gridFsTemplate.delete(Query.query(Criteria.where("metadata.imageId").is(imageId)));
  }

  @Transactional
  void saveImage(byte[] file, Image image, String contentType) {
    try (InputStream inputStream = new ByteArrayInputStream(file)) {
      gridFsTemplate.store(inputStream, image.getFileName(), contentType, image);
    } catch (IOException e) {
      throw new IllegalArgumentException("Image could't be stored", e);
    }
  }

  @Transactional(readOnly = true)
  Map<ImageResolution, GridFSFile> findImageSizes(UUID imageId) {
    Map<ImageResolution, GridFSFile> images = new HashMap<>();
    gridFsTemplate
        .find(Query.query(Criteria.where("metadata.imageId").is(imageId)))
        .forEach(
            (Block<? super GridFSFile>)
                gridFSFile -> {
                  Document metadata = gridFSFile.getMetadata();
                  Assert.notNull(metadata, "Metadata is null");
                  ImageResolution resolution =
                      ImageResolution.valueOf((String) metadata.get("imageResolution"));
                  images.put(resolution, gridFSFile);
                });
    return images;
  }

  @Transactional(readOnly = true)
  GridFsResource loadResource(GridFSFile gridFSFile) {
    return gridFsTemplateExtension.getResource(gridFSFile);
  }
}
