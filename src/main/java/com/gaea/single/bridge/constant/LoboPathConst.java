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
  /** 获取账户余额 */
  String ACCOUNT_BALANCE = "/user/userCenter/getUserMoney.do";
  /** 获取用户信息 */
  String USER_INFO = "/user/userCenter/getUserInfo.do";
  /** 获取黑名单列表 */
  String BLACK_LIST = "/relationship/blackCenter/getBlacks.do";
  /** 移除黑名单用户 */
  String REMOVE_BLACK_USER = "/relationship/blackCenter/black.do";
  /** 用户登录 */
  String USER_LOGIN = "/user/userLogin/login";
  /** 一键登录 */
  String ONE_LOGIN = "/user/userLogin/login";
  /** 获取用户相册 */
  String USER_ALBUM = "/user/userCenter/getAlbum";
  /** 获取用户完善信息情况 */
  String PERFECT_INFO = "/user/userCenter/getIsPerfect.do";
  /** 编辑用户资料 */
  String EDIT_USER_INFO = "/user/userCenter/editData.do";
  /** 发送验证码 */
  String SEND_SMS_CODE = "/user/userLogin/sendSmsCode";
  /** 检查版本 */
  String CHECK_VERSION = "/basic/version/checkVersion";
  /** 修改用户头像 */
  String UPLOAD_USER_PORTRAIT = "/user/userCenter/uploadPortrait.do";
  /** 获取用户头像 */
  String GET_USER_PORTRAIT = "/user/userCenter/getPortrait.do";
  /** 上传相册图片 */
  String UPLOAD_ALBUM_IMG = "/user/userCenter/uploadPhotos.do";
  /** 删除相册图片 */
  String DELETE_ALBUM_IMG = "/user/userCenter/deletePhotos.do";
  /** 主播认证 */
  String ANCHOR_AUTH = "/zuul/user/sellerCenter/uploadVideo.do?videoNumber=%s&key=-----";
  /** 分享奖励排行 */
  String SHARE_RANKING = "/webapi/order/orderShare/findShareRanking";
  /** 分销信息 */
  String SHARE_INFO = "/webapi/user/shareCenter/findShareInfo.do";
  /** 我的收益 */
  String INCOME = "/webapi/user/sellerCenter/getMyIncome.do";
  /** 创建媒体订单 */
  String CREATE_MEDIA_ORDER = "/order/createOrder.do";
  /** 验证媒体订单 */
  String VALIDATE_MEDIA_ORDER = "/order/validOrder.do";
  /** 支付宝账号解绑 */
  String UNBIND_ALIPAY_ACCOUNT = "/user/alipayCenter/unbindAlipay.do";
  /** 支付宝账号解绑 */
  String BIND_ALIPAY_ACCOUNT = "/user/alipayCenter/bindAlipay.do";
  /** 获取支付绑定授权签名 */
  String ALIPAY_AUTH_SIGN = "/user/alipayCenter/getLoginSign.do";
  /** 获取支付方式列表 */
  String PAY_WAYS = "/basic/payEnable/findByType.do";
  /** 获取支付金额选项 */
  String PAY_AMOUNT_OPTIONS = "/basic/recharge/findMoneyList.do";
  /** 获取支付宝支付签名 */
  String ALIPAY_PAY_SIGN = "/finance/sign/getAlipaySignAll.do";
  /** 分享交易记录 */
  String SHARE_INVITE_RECORD = "/webapi/user/shareCenter/findTotalInvite.do";
  /** 分享奖励记录 */
  String SHARE_REWARD_RECORD = "/webapi/order/orderShare/findOrderShares.do";
  /** 分销提现 */
  String SHARE_WITHDRAW = "/webapi/user/shareCenter/withdraw.do";
  /** 收益提现 */
  String INCOME_WITHDRAW = "/webapi/user/sellerCenter/withdraw.do";
  /** 最后一笔订单的通话时长 */
  String LAST_ORDER_DURATION_TIME = "/order/getDurationByUserId.do";
}
