package com.gaea.single.bridge.config;

import lombok.Getter;
import org.platform.config.core.CheckException;
import org.platform.config.core.data.DataPool;
import org.platform.config.core.data.set.PropertyData;
import org.platform.config.core.kernel.ConfigPool;
import org.platform.config.core.kernel.IConfig;
import org.platform.config.core.kernel.set.CFile;
import org.platform.config.core.kernel.set.SingleConfigSet;

import java.util.Properties;

@Getter
public class DictionaryProperties implements IConfig {
  private String loboHost;

  public DictionaryProperties(Properties properties) {
    this.loboHost = properties.getProperty("lobo.host");
  }

  public static DictionaryProperties get() {
    return ConfigPool.open(Set.class).get();
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
