package com.guhecloud.cube.biz.meattrade.server.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Order;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Payment;
import com.guhecloud.cube.biz.meattrade.server.order.param.OrderPayParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 订单支付业务操作接口
 *
 * @author Woody
 * @date 2021/9/15
 */
public interface PaymentService extends IService<Payment> {

    Payment getByOrderId(Long orderId, Integer tenant);

    List<Payment> listByOrderIds(Collection<? extends Serializable> orderIds, Integer tenant);

    /**
     * 订单支付并返回是否成功
     *
     * @param id       支付主键
     * @param targetId 转出用户
     * @param order    订单实例
     * @param param    支付参数
     *
     * @return true/false
     */
    boolean payment(Long id, Long targetId, Order order, OrderPayParam param);
}
