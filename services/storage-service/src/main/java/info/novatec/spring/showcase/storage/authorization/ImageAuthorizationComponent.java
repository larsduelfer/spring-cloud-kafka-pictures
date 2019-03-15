package info.novatec.spring.showcase.storage.authorization;

import com.mongodb.client.gridfs.model.GridFSFile;
import info.novatec.spring.showcase.model.ImageResolution;
import info.novatec.spring.showcase.storage.configuration.security.SecurityContextHelper;
import info.novatec.spring.showcase.storage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ImageAuthorizationComponent {

  @Autowired private GridFsTemplate gridFsTemplate;

  public boolean hasDeletePermission(UUID imageId) {
    User currentUser = SecurityContextHelper.getInstance().getCurrentUser();

    GridFSFile file =
        gridFsTemplate.findOne(
            Query.query(
                Criteria.where("metadata.imageId")
                    .is(imageId)
                    .and("metadata.imageResolution")
                    .is(ImageResolution.RAW)));

    if (file == null || file.getMetadata() == null) {
      return false;
    }

    UUID userId = (UUID) file.getMetadata().get("userId");
    return currentUser.getIdentifier().equals(userId);
  }
}
