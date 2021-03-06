package com.gaea.single.bridge.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.platform.config.core.CheckException;
import org.platform.config.core.data.DataPool;
import org.platform.config.core.data.set.PropertyData;
import org.platform.config.core.kernel.ConfigPool;
import org.platform.config.core.kernel.IConfig;
import org.platform.config.core.kernel.set.CFile;
import org.platform.config.core.kernel.set.SingleConfigSet;
import org.springframework.integration.util.PoolItemNotAvailableException;

import java.io.PipedReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
  private AppStoreAudit appStoreAudit;
  private DrainagePackage drainagePackage;
  private User user;
  private GreetMessage greetMessage;
  private Pay pay;
  private Ranking ranking;
  private FirstRecharge firstRecharge;

  public DictionaryProperties(Properties properties) {
    this.lobo = getProperty(Lobo.class, "lobo", properties);
    this.share = getProperty(Share.class, "share", properties);
    this.appStoreAudit = getProperty(AppStoreAudit.class, "appStoreAudit", properties);
    this.drainagePackage = getProperty(DrainagePackage.class, "drainagePackage", properties);
    this.user = getProperty(User.class, "user", properties);
    this.greetMessage = getProperty(GreetMessage.class, "greetMessage", properties);
    this.pay = getProperty(Pay.class, "pay", properties);
    this.ranking = getProperty(Ranking.class, "ranking", properties);
    this.firstRecharge = getProperty(FirstRecharge.class, "firstRecharge", properties);
  }

  private <R> R getProperty(Class<R> cls, String prefix, Properties properties) {
    try {
      R instance = cls.newInstance();
      for (Field field : cls.getDeclaredFields()) {
        String fieldName = field.getName();
        Class<?> argType = String.class;

        if (List.class.isAssignableFrom(field.getType())) {
          argType = List.class;
        } else if (Long.class.isAssignableFrom(field.getType())) {
          argType = Long.class;
        } else if (Integer.class.isAssignableFrom(field.getType())) {
          argType = Integer.class;
        }

        Method setMethod =
            cls.getMethod(
                "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), argType);
        String value = properties.getProperty(prefix + "." + fieldName);
        Object arg = value;

        if (argType == List.class) {
          arg = toSplitStrList(value);
          setMethod.invoke(instance, arg);
        } else if (value != null) {
          if (argType == Long.class) {
            arg = Long.valueOf(value);
          } else if (argType == Integer.class) {
            arg = Integer.valueOf(value);
          }
          setMethod.invoke(instance, arg);
        }
      }

      return instance;
    } catch (Exception ex) {
      log.error(String.format("构建配置属性对象 %s 失败", prefix), ex);
      throw new RuntimeException(ex);
    }
  }

  private List<String> toSplitStrList(String value) {
    if (StringUtils.isNotBlank(value)) {
      return Arrays.asList(value.split(","));
    }

    return Collections.emptyList();
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
  public static class Pay {
    private List<String> androidPayWays;
    private List<String> iosPayWays;
  }

  @Getter
  @Setter
  public static class AppStoreAudit {
    private Long userColumnId; // android审核展示的此栏目，ios目前没有用
    private String password;
    private List<String> phones;
  }

  @Getter
  @Setter
  public static class Lobo {
    private String host;
    private String iosAuditHost;
    private String appId;
    private String bossHost;
    private String userSecretaryId;
    private String anchorSecretaryId;
    private String imgHost;
    private Long systemLabelId;
  }

  @Getter
  @Setter
  public static class User {
    private Integer defaultMessageCount;
  }

  @Getter
  @Setter
  public static class DrainagePackage {
    private String downloadUrl;
    private String advertImgUrl;
  }

  @Getter
  @Setter
  public static class GreetMessage {
    /** 最多可拥有消息数量，包含系统和自定义 */
    private Integer maxMessageCount;
    /** 每次发送最多打招呼消息数量 */
    private Integer oneMaxSendGreetCount;
    /** 消息发送间隔秒数 */
    private Integer sendIntervalSecond;
    /** 最大接收打招呼次数 */
    private Integer maxReceiveGreetTimes;
    /** 注册多少天内为新用户 */
    private Integer newUserDays;
    /** 打开app多少秒未拨打视频为未呼叫用户 */
    private Integer uncalledUserSecond;
  }

  /** 排名配置 */
  @Getter
  @Setter
  public static class Ranking {
    /** 消费提示文案 */
    private String userTipText;
    /** 收入提示文案 */
    private String anchorTipText;
    /** 显示数量 */
    private Integer size;
  }

  /** 首充配置 */
  @Getter
  @Setter
  public static class FirstRecharge {
    /** 描述 */
    private String desc;
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
