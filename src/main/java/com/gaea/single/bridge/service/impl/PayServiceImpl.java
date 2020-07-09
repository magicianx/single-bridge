package com.gaea.single.bridge.service.impl;

import com.gaea.single.bridge.repository.mysql.OrderBuyPrivilegeRepository;
import com.gaea.single.bridge.repository.mysql.OrderRechargeDetailRepository;
import com.gaea.single.bridge.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 支付服务
 *
 * @author JunJYu
 * @date 2020/7/9 16:02
 */
@Service
public class PayServiceImpl implements PayService {

  /** 特权购买订单 */
  @Autowired private OrderBuyPrivilegeRepository orderBuyPrivilegeRepository;

  /** 订单充值详情 */
  @Autowired private OrderRechargeDetailRepository orderRechargeDetailRepository;

  @Override
  public Mono<Boolean> isFirstRecharge(Long userId) {
    return orderRechargeDetailRepository
        .buyCount(userId)
        .flatMap(
            rCount ->
                rCount > 0
                    ? Mono.just(false)
                    : orderBuyPrivilegeRepository.buyCount(userId).map(pCount -> pCount == 0));
  }
}
