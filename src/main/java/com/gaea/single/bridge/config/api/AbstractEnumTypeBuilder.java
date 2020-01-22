package com.gaea.single.bridge.config.api;

import com.gaea.single.bridge.enums.Metadata;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableValues;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractEnumTypeBuilder {
  protected boolean isMetadata(Class<?> type) {
    return Metadata.class.isAssignableFrom(type);
  }

  protected AllowableValues getAllowableValues(Class<?> metadata) {
    try {
      Enum[] enums = (Enum[]) metadata.getMethod("values").invoke(null);

      List<String> values =
          Arrays.stream(enums)
              .map(
                  value -> {
                    try {
                      return value.name() + "(" + metadata.getMethod("getDesc").invoke(value) + ")";
                    } catch (Exception e) {
                      throw new RuntimeException("");
                    }
                  })
              .collect(Collectors.toList());
      return new AllowableListValues(values, "LIST");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
