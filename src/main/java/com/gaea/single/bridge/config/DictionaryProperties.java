package com.gaea.single.bridge.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.platform.config.core.CheckException;
import org.platform.config.core.data.DataPool;
import org.platform.config.core.data.set.PropertyData;
import org.platform.config.core.kernel.ConfigPool;
import org.platform.config.core.kernel.IConfig;
import org.platform.config.core.kernel.set.CFile;
import org.platform.config.core.kernel.set.SingleConfigSet;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

@Getter
@Slf4j
@AllArgsConstructor
public class DictionaryProperties implements IConfig {
  public static DictionaryProperties get() {
    return ConfigPool.open(Set.class).get();
  }

  private Lobo lobo;
  private Share share;

  public DictionaryProperties(Properties properties) {
    this.lobo = getProperty(Lobo.class, "lobo", properties);
    this.share = getProperty(Share.class, "share", properties);
  }

  private <R> R getProperty(Class<R> cls, String prefix, Properties properties) {
    try {
      R instance = cls.newInstance();
      for (Field field : cls.getDeclaredFields()) {
        String fieldName = field.getName();
        Method setMethod =
            cls.getMethod(
                "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1),
                String.class);
        String value = properties.getProperty(prefix + "." + fieldName);
        Object arg = value;
        if (Integer.class.isAssignableFrom(field.getType())) {
          arg = Integer.valueOf(value);
        }
        setMethod.invoke(instance, arg);
      }

      return instance;
    } catch (Exception ex) {
      log.error(String.format("构建配置属性对象 %s 失败", prefix), ex);
      throw new RuntimeException(ex);
    }
  }

  @Getter
  @Setter
  public static class Share {
    private String url;
    private String title;
    private String content;
  }

  @Getter
  @Setter
  public static class Lobo {
    private String host;
    private String iosAuditHost;
    private String appId;
    private String userSecretaryId;
    private String anchorSecretaryId;
  }

  public static class Set extends SingleConfigSet<DictionaryProperties> {
    private static final String PATH = "dictionary.properties";

    public Set() throws Exception {
      super(new CFile(PATH));
    }

    @Override
    protected void check(DictionaryProperties config) throws CheckException {}

    @Override
    protected DictionaryProperties load() throws Exception {
      PropertyData data = DataPool.get(PATH);
      return new DictionaryProperties(data.data());
    }
  }
}
