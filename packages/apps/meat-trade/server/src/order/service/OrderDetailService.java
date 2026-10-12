package com.guhecloud.cube.biz.meattrade.server.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.order.entity.OrderDetail;
import com.guhecloud.cube.biz.meattrade.server.order.vo.OrderGoodsCategoryVO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface OrderDetailService extends IService<OrderDetail> {

    OrderDetail getByPorkArrangeDetail(Long porkArrangeDetailId, Integer tenant);

    List<OrderDetail> listUnCanceledByPorkArrangeDetails(Collection<? extends Serializable> porkArrangeDetailIds, Integer tenant);

    List<OrderDetail> getByOrderId(Serializable orderId);

    List<OrderDetail> listByOrderId(Serializable orderId, Integer tenant);

    List<OrderDetail> listByOrderIds(Collection<? extends Serializable> orderIds, Integer tenant);

    List<OrderGoodsCategoryVO> listOrderGoodsCategoryByOrderIds(Collection<? extends Serializable> orderIds, Integer tenant);

    List<OrderDetail> getActiveOrderDetail(Collection<? extends Serializable> porkArrangeDetailIds, Integer tenant);

}
