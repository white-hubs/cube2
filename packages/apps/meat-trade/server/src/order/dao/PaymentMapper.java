package com.guhecloud.cube.biz.meattrade.server.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Payment;
import org.springframework.stereotype.Repository;

/**
 * 订单支付数据操作接口
 *
 * @author Woody
 * @date 2021/9/15
 */
@Repository("meattrade-paymentMapper")
public interface PaymentMapper extends BaseMapper<Payment> {
}
