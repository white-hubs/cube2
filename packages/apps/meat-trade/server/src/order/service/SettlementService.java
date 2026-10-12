package com.guhecloud.cube.biz.meattrade.server.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Order;
import com.guhecloud.cube.biz.meattrade.server.order.entity.OrderDetail;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Settlement;
import com.guhecloud.cube.biz.meattrade.server.order.param.SettlementQueryParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface SettlementService extends IService<Settlement> {

    /**
     * 根据订单主键新增结算信息
     *
     * @param order        订单详情
     * @param orderDetails 订单详情信息
     */
    void saveByOrderAndDetails(Order order, List<OrderDetail> orderDetails);

    /**
     * 完成结算并返回是否成功
     *
     * @param settlement 结算信息
     * @param supplier   批发商信息
     * @param password   支付密码
     *
     * @return true/false
     */
    boolean complete(Settlement settlement, SupplierDTO supplier, String password);

    /**
     * 市场完成结算并返回是否成功
     *
     * @param settlements 结算信息集合
     * @param supplier    卖家信息
     * @param password    支付密码
     *
     * @return true/false
     */
    boolean batchComplete(List<Settlement> settlements, SupplierDTO supplier, String password);

    Settlement getByOrderCode(String orderCode);

    List<Settlement> listByIds(Collection<? extends Serializable> ids);

    Page<Settlement> pageSettlement(SettlementQueryParam queryParam);

}
