package com.guhecloud.cube.biz.meattrade.server.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.order.dao.PaymentMapper;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Order;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Payment;
import com.guhecloud.cube.biz.meattrade.server.order.param.OrderPayParam;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderService;
import com.guhecloud.cube.biz.meattrade.server.order.service.PaymentService;
import com.guhecloud.cube.biz.meattrade.server.wallet.enumeration.BusinessType;
import com.guhecloud.cube.biz.wallet.client.VirementClient;
import com.guhecloud.cube.biz.wallet.client.enumeration.ChannelType;
import com.guhecloud.cube.biz.wallet.client.param.AccountTransferParam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 订单支付业务操作实现
 *
 * @author Woody
 * @date 2021/9/15
 */
@Service("meattrade-paymentService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {

    private final OrderService orderService;

    private final VirementClient virementClient;

    @Override
    public Payment getByOrderId(@NonNull Long orderId, @NonNull Integer tenant) {
        return this.baseMapper.selectOne(Wrappers.<Payment>lambdaQuery().eq(Payment::getOrderId, orderId).eq(Payment::getTenant, tenant));
    }

    @Override
    public List<Payment> listByOrderIds(@NonNull Collection<? extends Serializable> orderIds, @NonNull Integer tenant) {
        return CollectionUtils.isEmpty(orderIds) ? Collections.emptyList() :
               this.lambdaQuery().in(Payment::getOrderId, orderIds).eq(Payment::getTenant, tenant).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean payment(@NonNull Long id, Long targetId, @NonNull Order order, @NonNull OrderPayParam param) {
        // 完成订单支付
        if (!this.orderService.complete(order.getId())) {
            return false;
        }
        // 保存支付记录
        Payment payment = Payment.builder().id(id).mode(ChannelType.BALANCE).orderId(order.getId())
                .amount(order.getAmount()).tenant(order.getTenant()).build();
        this.baseMapper.insert(payment);

        // 账户转账
        AccountTransferParam transfer = AccountTransferParam.builder().id(id).business(BusinessType.PORK_TRADE.name())
                .sourceUserId(order.getCustomerId()).targetUserId(targetId).amount(order.getAmount())
                .password(param.getPassword()).tenant(order.getTenant()).build();
        this.virementClient.transfer(transfer);
        return true;
    }
}
