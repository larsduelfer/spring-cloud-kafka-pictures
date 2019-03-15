package info.novatec.spring.showcase.storage.util;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Component;

@Component
public class GridFsTemplateExtension {

  private MongoDbFactory dbFactory;

  public GridFsTemplateExtension(MongoDbFactory dbFactory) {
    this.dbFactory = dbFactory;
  }

  public GridFsResource getResource(GridFSFile file) {
    return file != null
        ? new GridFsResource(file, getGridFs().openDownloadStream(file.getId()))
        : null;
  }

  private GridFSBucket getGridFs() {
    return GridFSBuckets.create(dbFactory.getDb());
  }
}
