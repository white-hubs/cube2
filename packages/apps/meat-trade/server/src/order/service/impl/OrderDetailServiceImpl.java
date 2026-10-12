package com.guhecloud.cube.biz.meattrade.server.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.order.dao.OrderDetailMapper;
import com.guhecloud.cube.biz.meattrade.server.order.entity.OrderDetail;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderDetailService;
import com.guhecloud.cube.biz.meattrade.server.order.vo.OrderGoodsCategoryVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service("meattrade-orderDetailService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

    @Override
    public OrderDetail getByPorkArrangeDetail(@NonNull Long porkArrangeDetailId, @NonNull Integer tenant) {
        return this.baseMapper.selectOne(Wrappers.<OrderDetail>lambdaQuery()
                                                 .eq(OrderDetail::getPorkArrangeDetailId, porkArrangeDetailId)
                                                 .eq(OrderDetail::getTenant, tenant));
    }

    @Override
    public List<OrderDetail> listUnCanceledByPorkArrangeDetails(@NonNull Collection<? extends Serializable> porkArrangeDetailIds,
                                                                @NonNull Integer tenant
                                                               ) {
        return this.baseMapper.listUnCanceledByPorkArrangeDetails(porkArrangeDetailIds, tenant);
    }

    @Override
    public List<OrderDetail> getByOrderId(@NonNull Serializable orderId) {
        return this.baseMapper.selectList(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, orderId));
    }

    @Override
    public List<OrderDetail> listByOrderId(@NonNull Serializable orderId, @NonNull Integer tenant) {
        return this.baseMapper.selectList(Wrappers.<OrderDetail>lambdaQuery()
                                                  .eq(OrderDetail::getOrderId, orderId)
                                                  .eq(OrderDetail::getTenant, tenant));
    }

    @Override
    public List<OrderDetail> listByOrderIds(@NonNull Collection<? extends Serializable> orderIds, @NonNull Integer tenant) {
        return CollectionUtils.isEmpty(orderIds) ? Collections.emptyList() :
               this.lambdaQuery().in(OrderDetail::getOrderId, orderIds).eq(OrderDetail::getTenant, tenant).list();
    }

    @Override
    public List<OrderGoodsCategoryVO> listOrderGoodsCategoryByOrderIds(@NonNull Collection<? extends Serializable> orderIds,
                                                                       @NonNull Integer tenant
                                                                      ) {
        return CollectionUtils.isEmpty(orderIds) ? Collections.emptyList() :
               this.baseMapper.listOrderGoodsCategoryByOrderIds(orderIds, tenant);
    }

    @Override
    public List<OrderDetail> getActiveOrderDetail(@NonNull Collection<? extends Serializable> porkArrangeDetailIds, Integer tenant) {
        return CollectionUtils.isEmpty(porkArrangeDetailIds) ? Collections.emptyList() :
               this.lambdaQuery()
                       .eq(OrderDetail::getChecked, 1)
                       .eq(OrderDetail::getTenant, tenant)
                       .in(OrderDetail::getPorkArrangeDetailId, porkArrangeDetailIds)
                       .list();
    }
}
