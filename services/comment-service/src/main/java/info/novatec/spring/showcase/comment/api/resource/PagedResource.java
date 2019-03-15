package info.novatec.spring.showcase.comment.api.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
class PagedResource<T> extends ResourceSupport {

  private Collection<T> content;

  private PagedResources.PageMetadata metadata;
}
