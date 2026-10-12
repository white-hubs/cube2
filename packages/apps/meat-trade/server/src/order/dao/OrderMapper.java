package com.guhecloud.cube.biz.meattrade.server.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Order;
import com.guhecloud.cube.biz.meattrade.server.order.vo.OrderDateWeightVO;
import com.guhecloud.cube.biz.meattrade.server.order.vo.OrderTradeStatisticsVO;
import com.guhecloud.cube.biz.meattrade.server.order.vo.SupplierOrderTradeStatisticsVO;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lzy
 * @date 2021/12/8
 */
@Repository("meattrade-orderMapper")
public interface OrderMapper extends BaseMapper<Order> {

    List<OrderDateWeightVO> selectOrderDateGroupWeights(@Param("param") StatQueryParam queryParam);

    List<SupplierOrderTradeStatisticsVO> selectSupplierOrderTradeStatistics(Page page, @Param("param") StatQueryParam queryParam);

    OrderTradeStatisticsVO selectOrderTradeStatistics(@Param("param") StatQueryParam queryParam);
}
