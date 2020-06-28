package com.gaea.single.bridge.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * lobo服务端请求路径
 *
 * @author cludy
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoboPathConst {
  /** 广告列表 */
  public static final String BANNER_LIST = "/mapper/advert/getBannerList";
  /** 用户栏目列表 */
  public static final String USER_COLUMN_LIST = "/mapper/menu/getHomeList";
  /** 用户列表 */
  public static final String USER_LIST = "/mapper/list/getHomeData";
  /** 获取用户资料 */
  public static final String USER_PROFILE = "/user/userCenter/ProfileVo340";
  /** 举报主播 */
  public static final String REPORT_ANCHOR = "/postsales/complaintUser/insertComplaintProfile.do";
  /** 拉黑主播 */
  public static final String PULL_BACK_ANCHOR = "/relationship/blackCenter/black.do";
  /** 获取举报选项 */
  public static final String REPORT_SELECT_ITEMS = "/postsales/complaint/getComplaintConfig.do";
  /** 获取打赏礼物列表 */
  public static final String GRATUITY_GIFT_LIST = "/gratuity/gratuity/findAllGratuity";
  /** 获取账户余额 */
  public static final String ACCOUNT_BALANCE = "/user/userCenter/getUserMoney.do";
  /** 获取用户信息 */
  public static final String USER_INFO = "/user/userCenter/getUserInfo.do";
  /** 获取黑名单列表 */
  public static final String BLACK_LIST = "/relationship/blackCenter/getBlacks.do";
  /** 移除黑名单用户 */
  public static final String REMOVE_BLACK_USER = "/relationship/blackCenter/black.do";
  /** 用户登录 */
  public static final String USER_LOGIN = "/user/userLogin/login";
  /** 一键登录 */
  public static final String ONE_LOGIN = "/user/oneLogin/login";
  /** 获取用户相册 */
  public static final String USER_ALBUM = "/user/userCenter/getAlbum";
  /** 获取用户完善信息情况 */
  public static final String PERFECT_INFO = "/user/userCenter/getIsPerfect.do";
  /** 编辑用户资料 */
  public static final String EDIT_USER_INFO = "/user/userCenter/editData.do";
  /** 发送验证码 */
  public static final String SEND_SMS_CODE = "/user/userLogin/sendSmsCode";
  /** 检查版本 */
  public static final String CHECK_VERSION = "/basic/version/checkVersion";
  /** 修改用户头像 */
  public static final String UPLOAD_USER_PORTRAIT = "/user/userCenter/uploadPortrait.do";
  /** 获取用户头像 */
  public static final String GET_USER_PORTRAIT = "/user/userCenter/getPortrait.do";
  /** 上传相册图片 */
  public static final String UPLOAD_ALBUM_IMG = "/user/userCenter/uploadPhotos.do";
  /** 删除相册图片 */
  public static final String DELETE_ALBUM_IMG = "/user/userCenter/deletePhotos.do";
  /** 主播认证 */
  public static final String ANCHOR_AUTH =
      "/zuul/user/sellerCenter/uploadVideo.do?videoNumber=%s&key=-----";
  /** 分享奖励排行 */
  public static final String SHARE_RANKING = "/order/orderShare/findShareRanking";
  /** 分销信息 */
  public static final String SHARE_INFO = "/user/shareCenter/findShareInfo.do";
  /** 我的收益 */
  public static final String INCOME = "/user/sellerCenter/getMyIncome.do";
  /** 创建媒体订单 */
  public static final String CREATE_MEDIA_ORDER = "/order/createOrder.do";
  /** 验证媒体订单 */
  public static final String VALIDATE_MEDIA_ORDER = "/order/validOrder.do";
  /** 支付宝账号解绑 */
  public static final String UNBIND_ALIPAY_ACCOUNT = "/user/alipayCenter/unbindAlipay.do";
  /** 支付宝账号解绑 */
  public static final String BIND_ALIPAY_ACCOUNT = "/user/alipayCenter/bindAlipay.do";
  /** 获取支付绑定授权签名 */
  public static final String ALIPAY_AUTH_SIGN = "/user/alipayCenter/getLoginSign.do";
  /** 获取支付方式列表 */
  public static final String PAY_WAYS = "/basic/payEnable/findByType.do";
  /** 获取支付金额选项 */
  public static final String PAY_AMOUNT_OPTIONS = "/basic/recharge/findMoneyList.do";
  /** 获取支付宝支付签名 */
  public static final String ALIPAY_PAY_SIGN = "/finance/sign/getAlipaySignAll.do";
  /** 分享交易记录 */
  public static final String SHARE_INVITE_RECORD = "/user/shareCenter/findTotalInvite.do";
  /** 分享奖励记录 */
  public static final String SHARE_REWARD_RECORD = "/order/orderShare/findOrderShares.do";
  /** 分销提现 */
  public static final String SHARE_WITHDRAW = "/user/shareCenter/withdraw.do";
  /** 收益提现 */
  public static final String INCOME_WITHDRAW = "/user/sellerCenter/withdraw.do";
  /** 最后一笔订单的通话时长 */
  public static final String LAST_ORDER_DURATION_TIME = "/order/getDurationByUserId.do";
  /** 开始订单 */
  public static final String START_ORDER = "/order/startOrder.do";
  /** 视频结束信息 */
  public static final String VIDEO_END_INFO = "/order/getVideoEndInfo.do";
  /** 手动绑定支付宝账号 */
  public static final String MANUAL_BIND_ALIPAY_ACCOUNT = "/user/alipayCenter/manualBindAlipay.do";
  /** 获取主播随机标签列表 */
  public static final String ANCHOR_RANDOM_LABELS =
      "/user/userLabelCenter/getRandomOutUserLabels.do";
  /** 添加主播标签 */
  public static final String ADD_ANCHOR_LABEL = "/user/userLabelCenter/addUserLabel.do";
  /** 保存用户IOS PushKit的设备信息 */
  public static final String SAVE_IOS_PUSH_KIT_ACCOUNT = "/push/device/saveIOSPushKitAccount.do";
  /** 查询android应用审核状态 */
  public static final String CHECK_ANDROID_AUDIT_STATUS = "/basic/version/queryAuditSatus";
  /** 查询ios应用审核状态 */
  public static final String CHECK_IOS_AUDIT_STATUS = "/basic/version/queryAuditAndPaySatus";
  /** 上传用户地址 */
  public static final String UPLOAD_USER_ADDRESS = "/user/userLogin/uploadAddress.do";
  /** 获取订单列表 */
  public static final String GET_ORDER_LIST = "/order/order/getUserAll.do";
  /** 获取微信支付签名 */
  public static final String WECHAT_PAY_SIGN = "/finance/sign/getWechatSign.do";
  /** 绑定邀请码 */
  public static final String BIND_INVITE_CODE = "/user/shareCenter/saveShare.do";
  /** 主播置顶 */
  public static final String SET_ANCHOR_TOP = "/user/userCenter/setTop.do";
  /** 获取用户等级信息 */
  public static final String GET_USER_GRADE = "/user/userCenter/getGradeDetail.do";
  /** 获取主播价格列表 */
  public static final String GET_ANCHOR_PRICES = "/user/userCenter/getPrices.do";
  /** 设置主播单价 */
  public static final String SET_ANCHOR_PRICE = "/user/userCenter/updatePrice.do";
  /** 添加关注 */
  public static final String FOLLOW = "/relationship/followCenter/follow.do";
  /** 取消关注 */
  public static final String UNFOLLOW = "/relationship/followCenter/follow.do";
  /** 获取关注列表 */
  public static final String GET_FOLLOW_LIST = "/relationship/followCenter/getFollows.do";
  /** 获取粉丝列表 */
  public static final String GET_FANS_LIST = "/relationship/followCenter/getFollows.do";
  /** 获取视频列表 */
  public static final String GET_VIDEO_LIST = "/vod/videoCenter/queryVodByUserId";
  /** 删除视频 */
  public static final String DELETE_VIDEO = "/vod/videoCenter/deleteVod.do";
  /** 获取上传视频签名 */
  public static final String GET_UPLOAD_VIDEO_SIGN = "/vod/videoCenter/getUploadSignature.do";
  /** 上传视频 */
  public static final String UPLOAD_VIDEO = "/vod/videoCenter/uploadVideo.do";
  /** 视频点赞 */
  public static final String PRAISE_VIDEO = "/vod/videoCenter/praiseVod.do";
  /** 搜索用户 */
  public static final String SEARCH_USER = "/user/searchUser";
  /** 手机号绑定 */
  public static final String BIND_PHONE = "/user/userLogin/bindPhone.do";
  /** 弹窗广告 */
  public static final String GET_POPUP_ADVERT = "/mapper/advert/getPopupAdvert";
  /** 获取vip支付配置 */
  public static final String GET_VIP_CONFIG = "/user/userCenter/getBaseVipConfig";
  /** 获取vip剩余时间（毫秒） */
  public static final String GET_VIP_LAST_TIME = "/user/userCenter/getVipTime.do";
  /** 获取小羊商城支付表单 */
  public static final String GET_LEGEND_SHOP_PAY_FORM = "/finance/legend-shop/form/pay.do";
  /** 在线视频秀列表 */
  public static final String ONLINE_VIDEO_SHOW_LIST = "/vod/videoCenter/queryVideoShowNew";
  /** 推荐视频秀列表 */
  public static final String RECOMMEND_VIDEO_SHOW_LIST = "/vod/videoCenter/queryVideoShow";
  /** 谁看过我（全部） */
  public static final String VIEW_RECORD_LIST = "/user/getUserOverHistory.do";
  /** 获取加入公会认证信息 */
  public static final String USER_GUILD_AUTH_INFO = "/partnerCompany/getAuthInfo.do";
  /** 加入或拒绝加入公会 */
  public static final String UPDATE_GUILD_AUTH_INFO = "/partnerCompany/updateAuthInfo.do";
  /** 设置用户备注 */
  public static final String SET_USER_REMARK = "/user/saveUserRemarksInfo.do";
  /** 获取用户备注列表 */
  public static final String GET_USER_REMARK_LIST = "/user/getUserRemarksInfo.do";
  /** 获取排名菜单列表 */
  public static final String GET_RANK_MENU_LIST = "/mapper/rank/single/menu.net";
  /** 获取排行榜列表 */
  public static final String GET_RANK_LIST = "/mapper/rank/single/list.net";
}
