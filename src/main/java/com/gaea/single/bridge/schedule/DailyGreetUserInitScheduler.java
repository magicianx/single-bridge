package com.gaea.single.bridge.schedule;

import com.gaea.single.bridge.core.manager.GreetUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyGreetUserInitScheduler {
  @Autowired private GreetUserManager greetUserManager;

  /** 移除不再是新注册用户的 */
  @Scheduled(cron = "0 0 0 * * ?")
  public void send() {
    greetUserManager.removeOverDayNewUser();
  }
}
