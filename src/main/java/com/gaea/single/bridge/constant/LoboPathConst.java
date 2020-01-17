package com.gaea.single.bridge.constant;

public interface LoboPathConst {
  /** 广告列表 */
  String BANNER_LIST = "/mapper/advert/getBannerList";
  /** 主播栏目列表 */
  String ANCHOR_COLUMN_LIST = "/mapper/menu/getHomeList";
  /** 主播列表 */
  String ANCHOR_LIST = "/mapper/list/getHomeData";
  /** 获取主播资料 */
  String ANCHOR_PROFILE = "/user/userCenter/ProfileVo340";
  /** 举报主播 */
  String REPORT_ANCHOR = "/postsales/complaintUser/insertComplaintProfile.do";
  /** 拉黑主播 */
  String PULL_BACK_ANCHOR = "/relationship/blackCenter/black.do";
  /** 获取举报选项 */
  String REPORT_SELECT_ITEMS = "/postsales/complaint/getComplaintConfig.do";
  /** 获取打赏礼物列表 */
  String GRATUITY_GIFT_LIST = "/gratuity/gratuity/findAllGratuity";
}
