package com.guhecloud.cube.biz.meattrade.server.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.order.dto.OrderCommissionDTO;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Order;
import com.guhecloud.cube.biz.meattrade.server.order.entity.OrderDetail;
import com.guhecloud.cube.biz.meattrade.server.order.param.OrderCommissionModifyParam;
import com.guhecloud.cube.biz.meattrade.server.order.param.OrderModifyParam;
import com.guhecloud.cube.biz.meattrade.server.order.param.OrderQueryParam;
import com.guhecloud.cube.biz.meattrade.server.order.vo.OrderDateWeightVO;
import com.guhecloud.cube.biz.meattrade.server.order.vo.OrderTradeStatisticsVO;
import com.guhecloud.cube.biz.meattrade.server.order.vo.SupplierOrderTradeStatisticsVO;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatQueryParam;

import java.io.Serializable;
import java.util.List;

public interface OrderService extends IService<Order> {

    Long addOrder(OrderModifyParam param);

    void removeOrder(Long id);

    void cancelOrder(Long id);

    void sendOrderMsg(Order order);

    Order getById(Serializable id);

    /**
     * 获取交易完成订单数据
     *
     * @param queryParam 查询参数
     *
     * @return 订单数据
     */
    List<Order> getDealOrder(StatQueryParam queryParam);

    /**
     * 白条交易统计
     *
     * @param queryParam
     *
     * @return
     */
    OrderTradeStatisticsVO getOrderTradeStatistics(StatQueryParam queryParam);

    List<Order> getDealOrderPage(StatQueryParam queryParam);

    List<SupplierOrderTradeStatisticsVO> getSupplierOrderTradeStatistics(StatQueryParam queryParam);

    List<Order> listOrder(OrderQueryParam queryParam);

    Page<Order> pageOrder(OrderQueryParam queryParam);

    Page<Order> pageMpSupplierOrder(OrderQueryParam queryParam);

    List<OrderDateWeightVO> orderDateGroupWeights(StatQueryParam queryParam);

    /**
     * 完成订单并返回是否成功
     *
     * @param id 订单主键
     *
     * @return true/false
     */
    boolean complete(Long id);

    List<OrderCommissionDTO> calculateCommissionRules(OrderCommissionModifyParam queryParam);

    /**
     * 根据订单生成食安溯源信息
     *
     * @param details
     * @param order
     */
    void saveSpotCheckResource(List<OrderDetail> details, Order order);
}
