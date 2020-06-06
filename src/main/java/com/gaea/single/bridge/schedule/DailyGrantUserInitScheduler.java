package com.gaea.single.bridge.schedule;

import com.gaea.single.bridge.core.manager.GreetUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

//@Component
public class DailyGrantUserInitScheduler {
  @Autowired private GreetUserManager greetUserManager;

  /** 发送昨日统计报告邮件 */
  @Scheduled(cron = "0 0 0 * * ?")
  public void sendDailyStatisticMail() {
    LocalDate date = LocalDate.now().minusDays(1);
  }
}
