package info.novatec.spring.showcase.user.configuration.db;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.UUID;

@Converter(autoApply = true)
public final class UuidConverter implements AttributeConverter<UUID, String> {

  @Override
  public String convertToDatabaseColumn(UUID attribute) {
    if (attribute == null) {
      return null;
    }
    return attribute.toString();
  }

  @Override
  public UUID convertToEntityAttribute(String dbData) {
    if (StringUtils.isEmpty(dbData)) {
      return null;
    }
    return UUID.fromString(dbData);
  }
}
