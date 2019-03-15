package info.novatec.spring.showcase.user.configuration.api;

import info.novatec.spring.showcase.user.model.User;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.util.Assert;

import static org.apache.commons.lang3.StringUtils.wrap;
import static org.springframework.util.StringUtils.trimLeadingCharacter;
import static org.springframework.util.StringUtils.trimTrailingCharacter;

public class Version {

  private Long version;

  private Version(Long version) {
    Assert.notNull(version, "Version must not be null");
    this.version = version;
  }

  public static Version of(Long version) {
    return new Version(version);
  }

  public static Version of(String version) {
    Assert.hasLength(version, "Version must not be empty");
    String trimmedVersion = trimLeadingCharacter(trimTrailingCharacter(version, '"'), '"');
    Assert.hasLength(trimmedVersion, "Version must not be empty");
    return new Version(Long.valueOf(trimmedVersion));
  }

  public static Version of(User user) {
    Assert.notNull(user, "User must not be null");
    return new Version(user.getVersion());
  }

  public void verify(User user) {
    Assert.notNull(user, "User must not be null");

    if (!this.version.equals(of(user).version)) {
      throw new OptimisticLockingFailureException("Entity outdated");
    }
  }

  @Override
  public String toString() {
    return version == null ? "" : wrap(version.toString(), '"');
  }
}
