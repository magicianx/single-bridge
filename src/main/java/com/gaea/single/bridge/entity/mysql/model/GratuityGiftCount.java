package com.gaea.single.bridge.entity.mysql.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 主播被打赏礼物数量统计
 *
 * @author cludy
 */
@Getter
@Setter
public class GratuityGiftCount {
  /** 礼物id */
  private Long giftId;
  /** 礼物名称 */
  private String giftName;
  /** 礼物图标链接 */
  private String giftIconUrl;
  /** 礼物数量 */
  private Integer giftNum;
}
