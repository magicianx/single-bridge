package com.gaea.single.bridge.core.lobo;

import java.util.HashMap;
import java.util.Map;

public class LoboCode {
  private static Map<Integer, String> SUCCESS_CODE;
  private static Map<Integer, String> ERROR_CODE;

  /**
   * 是否为错误编码
   *
   * @param code lobo编码
   * @return boolean
   */
  public static boolean isErrorCode(int code) {
    return ERROR_CODE.get(code) != null;
  }

  /**
   * 获取成功消息
   *
   * @param code 成功编码
   * @return 成功消息
   */
  public static String getSuccessMessage(int code) {
    return SUCCESS_CODE.get(code);
  }

  /**
   * 获取错误消息
   *
   * @param code 错误编码
   * @return 错误消息
   */
  public static String getErrorMessage(int code) {
    return ERROR_CODE.get(code);
  }

  static {
    SUCCESS_CODE =
        new HashMap<Integer, String>() {
          {
            put(1001, "登录成功");
            put(1002, "搜索成功");
            put(1003, "获取成功");
            put(1004, "上传成功");
            put(1005, "上传成功,待审核");
            put(1006, "删除成功");
            put(1007, "绑定成功");
            put(1008, "解绑成功");
            put(1009, "提现成功");
            put(1010, "上传地理位置成功");
            put(1011, "提交成功");
            put(1012, "修改成功");
            put(1013, "设置封面成功");
            put(1014, "成功删除非封面照片");
            put(1015, "设置成功");
            put(1016, "操作成功");
            put(1017, "置顶成功");
            put(2100, "保存成功");
            put(1200, "关注成功");
            put(1201, "取消关注成功");
            put(1100, "创建订单成功");
            put(1101, "接收方校验订单成功");
            put(1102, "获取用户账单列表成功");
            put(1103, "获取鉴黄token成功");
            put(1203, "拉黑成功");
            put(1204, "取消拉黑成功");
            put(1300, "创建打赏订单成功");
            put(1105, "魅力值兑换钻石成功");
            put(1400, "请求匹配服务成功");
            put(1500, "创建IM订单成功");
            put(1602, "获取打赏列表成功");
            put(1603, "获取投诉列表成功");
            put(1701, "Socket返回成功");
            put(1600, "发送成功");
            put(1900, "用户反馈成功");
            put(2001, "登陆成功");
            put(2002, "操作成功");
            put(2101, "小视频设置封面成功");
            put(2102, "删除小视频成功");
            put(2201, "获取视频内投诉列表成功");
            put(2202, "投诉成功");
            put(2301, "获取帖子详情成功");
            put(2302, "获取回复成功");
            put(2304, "点赞成功");
            put(2305, "回复成功");
            put(2308, "获取标签成功");
            put(2309, "获取列表成功");
            ;
          }
        };

    ERROR_CODE =
        new HashMap<Integer, String>() {
          {
            put(0, "服务器忙，请稍后再试");
            put(2, "校验失败");
            put(10013, "参数缺失");
            put(10015, "包头无效");
            put(10016, "session过期，重新登录");
            put(10001, "三方登录，获取不到用户id");
            put(10002, "三方登录，获取不到用户userName");
            put(10003, "三方登录，获取不到用户photo");
            put(10004, "请输入手机号");
            put(10005, "请输入验证码");
            put(10006, "验证码错误");
            put(10007, "请输入密码");
            put(10008, "该用户尚未注册或已被注销");
            put(10009, "您输入的密码不正确");
            put(10010, "由于您的违规操作，现已将您的账号做拉黑处理，如有问题可添加聊播官方微信-liaosaoGL进行申诉");
            put(10011, "由于您的违规操作，现已将您的账号做冻结处理，如有问题可添加聊播官方微信-liaosaoGL进行申诉（带解冻时间）");
            put(10012, "云信处理失败");
            put(10028, "第三方用户不存在");
            put(10053, "用户被拉黑，不允许登录");
            put(10057, "不能填写比你后注册的人的邀请码");
            put(10014, "手机号无效");
            put(1601, "如果用户选的密码登录，但是没有注册过，就返回1601，表示提示“，自动跳转注册页”");
            put(-1, "跳转到密码登录界面");
            put(10033, "手机号已存在，不能重复绑定");
            put(10017, "获取失败");
            put(10018, "你上传的头像不合法,请重新上传");
            put(10019, "上传失败");
            put(10021, "要删除的照片不存在");
            put(10022, "年龄小于18岁");
            put(10024, "没有任何修改");
            put(10026, "不能包含敏感词汇");
            put(10027, "昵称已经被占用,换一个吧");
            put(10038, "性别或年龄已经修改过了,不能再修改");
            put(10029, "无效的邀请码");
            put(10030, "绑定失败");
            put(10031, "你已经填写过邀请码，不能再次填写");
            put(10032, "不能填写自己的邀请码");
            put(10034, "账户余额大于10元,不能转为主播！");
            put(10035, "您已经认证过了,无需再次认证");
            put(10036, "很抱歉您不符合播主认证的条件,不可再发起认证");
            put(10037, "7天之内不能重复提交");
            put(10039, "没有完善资料，没有改过性别／年龄／昵称");
            put(10040, "该支付宝账号已被冻结，不能进行绑定");
            put(10041, "该用户已经绑定了支付宝，不能再绑定");
            put(10043, "解绑失败");
            put(10044, "手机号尚未注册");
            put(10046, "提现金额不能小于等于0");
            put(10047, "金额必须到达100元才能提现");
            put(10049, "提现金额超过账户余额");
            put(10048, "用户没有绑定支付宝");
            put(10050, "提现失败");
            put(10052, "用户被冻结，不允许操作");
            put(10051, "只有每周的周x可以提现");
            put(10054, "请输入搜索内容");
            put(10058, "当日的提现次数已达上限");
            put(10059, "用户系统等级不能解锁该价格");
            put(10061, "修改失败");
            put(10062, "不能删除封面照片");
            put(10063, "删除失败");
            put(10064, "照片已被删除，不能设为封面");
            put(10065, "今日置顶次数已经用完");
            put(10066, "设置置顶失败");
            put(10067, "B端用户不能设置隐身");
            put(10068, "C端用户不能设置置顶");
            put(10069, "请求参数错误");
            put(10070, "无设置排行榜隐身权限");
            put(10071, "C端用户无查看用户报表数据权限");
            put(12000, "关注失败");
            put(12001, "取消关注失败");
            put(12003, "对方在你的黑名单中，不能关注");
            put(12002, "拉黑／取消拉黑失败");
            put(12004, "对方已经在你的黑名单中了，不能重复操作");
            put(12005, "你已经关注了对方（不用再点关注了，没卵用）");
            put(11001, "服务器繁忙，请联系管理员");
            put(11002, "创建订单参数错误");
            put(11003, "拨打方余额不足（是指买家拨打卖家）");
            put(11004, "对方余额不足（是指卖家拨打买家）");
            put(11005, "自己在对方黑名单中");
            put(11006, "接收方校验订单失败");
            put(11007, "对方在你的黑名单中");
            put(11008, "获取用户账单列表失败");
            put(11009, "拨打方被冻结");
            put(11010, "接听方被冻结");
            put(11011, "只能向播主发起通话");
            put(11012, "只能向粉丝发起通话");
            put(11013, "对方设置了勿扰");
            put(11014, "对方忙绿");
            put(11015, "获取鉴黄token失败");
            put(11017, "魅力值兑换钻石数量不够");
            put(11018, "魅力值兑换钻石参数错误");
            put(11019, "收益兑换钻石异常");
            put(13001, "创建打赏订单失败");
            put(13002, "参数缺失");
            put(13003, "打赏socket的session过期");
            put(13004, "买家余额不足");
            put(13005, "买家余额不足(打赏后不够聊天3分钟)");
            put(14001, "请求匹配服务参数错误");
            put(14002, "获取首页栏目失败");
            put(14003, "获取排行榜栏目失败");
            put(14004, "获取首页数据失败");
            put(14005, "获取排行榜数据失败");
            put(14006, "获取闪屏数据失败");
            put(14007, "请求参数缺失");
            put(14008, "请求弹窗广告失败");
            put(14009, "请求banner失败");
            put(14010, "后台未配置数据");
            put(15001, "创建IM订单失败");
            put(15002, "买方余额不足");
            put(15003, "发送方被冻结");
            put(15004, "对方在自己的黑名单中");
            put(15005, "自己在对方的黑名单");
            put(16001, "密码登录时，输入手机号码点击下一步时，如果是新用户，弹出提示，“尚未注册，自动跳转注册页”，页面跳转到输入验证码界面，并且自动发送验证码");
            put(16002, "24小时内获取短信验证码超过上限值");
            put(16003, "发送短信失败");
            put(16004, "获取打赏列表失败");
            put(16005, "获取投诉列表失败");
            put(1702, "用户被冻结");
            put(1703, "用户解冻");
            put(1704, "用户头像审核未通过");
            put(1705, "用户头像审核通过");
            put(1706, "视频认证未通过");
            put(1707, "昵称审核未通过");
            put(1708, "昵称审核通过");
            put(1709, "简介审核未通过");
            put(1710, "简介审核通过");
            put(1711, "后台设置免打扰");
            put(1712, "订单投诉驳回或受理");
            put(1713, "订单投诉已退款");
            put(1714, "系统公告");
            put(17001, "Socket服务器繁忙");
            put(17002, "Socket参数错误");
            put(17003, "Socke未登录");
            put(17004, "Socke对方账户被冻结");
            put(17005, "Socke自己在对方黑名单");
            put(17006, "Socke余额不足");
            put(19001, "用户反馈失败");
            put(19002, "用户反馈参数缺失");
            put(20001, "登陆失败");
            put(20002, "参数缺失");
            put(20003, "用户编号不存在");
            put(20004, "密码错误");
            put(20005, "用户被禁用");
            put(20006, "操作失败");
            put(20007, "登陆失效");
            put(20008, "无权限");
            put(20009, "查询失败");
            put(20010, "推荐栏目查询失败");
            put(20011, "卖家栏目不能添加卖家自定义用户");
            put(20012, "买家栏目不能添加买家自定义用户");
            put(20013, "活动等级处只能添加B端用户，不能添加C端用户");
            put(20014, "不是B端用户");
            put(20015, "数据可能已经被删除，请刷新界面");
            put(20016, "B端标签只能添加B端用户");
            put(20017, "C端标签只能添加C端用户");
            put(21000, "存在待审核的小视频，请等待审核之后再上传");
            put(21001, "小视频保存失败");
            put(21002, "小视频设置封面失败");
            put(21003, "删除小视频失败");
            put(21004, "上传小视频不成超过99个");
            put(21005, "封面小视频不能被删除");
            put(22001, "获取视频内投诉列表失败");
            put(22002, "订单还没开始，无法投诉");
            put(22003, "该订单已经投诉过了，不能再次投诉");
            put(22004, "投诉正在处理，无法再次投诉");
            put(22005, "投诉失败");
            put(23005, "点赞参数失败");
            put(23006, "重复点赞");
            put(23010, "帖子不存在");
            put(23011, "帖子不允许回复");
            put(23012, "回复字数过长");
            put(23013, "帖子回复参数错误");
            put(23008, "获取标签失败");
            put(23009, "获取列表失败");
            put(23014, "被禁言禁止在社区发帖");
            put(2305, "没有被禁言 允许发帖子");
            put(23001, "你还不是特权身份");
            put(23002, "亲密值打赏次数用完了");
          }
        };
  }
}
