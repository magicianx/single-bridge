package com.gaea.single.bridge.constant;

public interface LoboPathConst {
  /** 广告列表 */
  String BANNER_LIST = "/mapper/advert/getBannerList";
  /** 用户栏目列表 */
  String USER_COLUMN_LIST = "/mapper/menu/getHomeList";
  /** 用户列表 */
  String USER_LIST = "/mapper/list/getHomeData";
  /** 获取用户资料 */
  String USER_PROFILE = "/user/userCenter/ProfileVo340";
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
  String ONE_LOGIN = "/user/oneLogin/login";
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
  String SHARE_RANKING = "/order/orderShare/findShareRanking";
  /** 分销信息 */
  String SHARE_INFO = "/user/shareCenter/findShareInfo.do";
  /** 我的收益 */
  String INCOME = "/user/sellerCenter/getMyIncome.do";
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
  String SHARE_INVITE_RECORD = "/user/shareCenter/findTotalInvite.do";
  /** 分享奖励记录 */
  String SHARE_REWARD_RECORD = "/order/orderShare/findOrderShares.do";
  /** 分销提现 */
  String SHARE_WITHDRAW = "/user/shareCenter/withdraw.do";
  /** 收益提现 */
  String INCOME_WITHDRAW = "/user/sellerCenter/withdraw.do";
  /** 最后一笔订单的通话时长 */
  String LAST_ORDER_DURATION_TIME = "/order/getDurationByUserId.do";
  /** 开始订单 */
  String START_ORDER = "/order/startOrder.do";
  /** 视频结束信息 */
  String VIDEO_END_INFO = "/order/getVideoEndInfo.do";
  /** 手动绑定支付宝账号 */
  String MANUAL_BIND_ALIPAY_ACCOUNT = "/user/alipayCenter/manualBindAlipay.do";
  /** 获取主播随机标签列表 */
  String ANCHOR_RANDOM_LABELS = "/user/userLabelCenter/getRandomOutUserLabels.do";
  /** 添加主播标签 */
  String ADD_ANCHOR_LABEL = "/user/userLabelCenter/addUserLabel.do";
  /** 保存用户IOS PushKit的设备信息 */
  String SAVE_IOS_PUSH_KIT_ACCOUNT = "/push/device/saveIOSPushKitAccount.do";
  /** 查询android应用审核状态 */
  String CHECK_ANDROID_AUDIT_STATUS = "/basic/version/queryAuditSatus";
  /** 查询ios应用审核状态 */
  String CHECK_IOS_AUDIT_STATUS = "/basic/version/queryAuditAndPaySatus";
  /** 上传用户地址 */
  String UPLOAD_USER_ADDRESS = "/user/userLogin/uploadAddress.do";
  /** 获取订单列表 */
  String GET_ORDER_LIST = "/order/order/getUserAll.do";
  /** 获取微信支付签名 */
  String WECHAT_PAY_SIGN = "/finance/sign/getWechatSign.do";
  /** 绑定邀请码 */
  String BIND_INVITE_CODE = "/user/shareCenter/saveShare.do";
  /** 主播置顶 */
  String SET_ANCHOR_TOP = "/user/userCenter/setTop.do";
  /** 获取用户等级信息 */
  String GET_USER_GRADE = "/user/userCenter/getGradeDetail.do";
  /** 获取主播价格列表 */
  String GET_ANCHOR_PRICES = "/user/userCenter/getPrices.do";
  /** 设置主播单价 */
  String SET_ANCHOR_PRICE = "/user/userCenter/updatePrice.do";
  /** 添加关注 */
  String FOLLOW = "/relationship/followCenter/follow.do";
  /** 取消关注 */
  String UNFOLLOW = "/relationship/followCenter/follow.do";
  /** 获取关注列表 */
  String GET_FOLLOW_LIST = "/relationship/followCenter/getFollows.do";
  /** 获取粉丝列表 */
  String GET_FANS_LIST = "/relationship/followCenter/getFollows.do";
  /** 获取视频列表 */
  String GET_VIDEO_LIST = "/vod/videoCenter/queryVodByUserId";
  /** 删除视频 */
  String DELETE_VIDEO = "/vod/videoCenter/deleteVod.do";
  /** 获取上传视频签名 */
  String GET_UPLOAD_VIDEO_SIGN = "/vod/videoCenter/getUploadSignature.do";
  /** 上传视频 */
  String UPLOAD_VIDEO = "/vod/videoCenter/uploadVideo.do";
}
