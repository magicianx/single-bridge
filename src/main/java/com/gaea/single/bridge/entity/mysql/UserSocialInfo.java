package com.gaea.single.bridge.entity.mysql;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class UserSocialInfo {

  @Id private Long userId;

  private String nickName;

  private String gradeIds;

  private String intruduction;

  private String portrait;

  private String smallPortrait;

  private String photoAlbum;

  private Integer isVideoAudit;

  private LocalDateTime videoAuditTime;

  private Integer complaintCount;

  private Integer complaintedCount;

  private Integer isBindBankCard; // 是否绑定银行卡，1：未绑定；2：已绑定

  private Integer pornCount; // 被查涉黄次数

  private String inviteCode;

  private LocalDateTime createTime;

  private LocalDateTime lastModifyTime;

  private Integer price; // 视频单价

  private Integer audioPrice; // 语音单价

  private String coverPhotoAlbum; // 封面相册

  private Integer acceptOnlineRemind; // 接受主播上线提醒 1.是 2.否

  private Integer contractMarker; // 签约标识 1.已签约 2.未签约
}
