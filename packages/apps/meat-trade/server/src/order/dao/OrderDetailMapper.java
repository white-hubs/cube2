package com.guhecloud.cube.biz.meattrade.server.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.meattrade.server.order.entity.OrderDetail;
import com.guhecloud.cube.biz.meattrade.server.order.vo.OrderGoodsCategoryVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Repository("meattrade-orderDetailMapper")
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

    List<OrderGoodsCategoryVO> listOrderGoodsCategoryByOrderIds(@Param("orderIds") Collection<? extends Serializable> orderIds,
                                                                @Param("tenant") Integer tenant
                                                               );

    List<OrderDetail> listUnCanceledByPorkArrangeDetails(
            @Param("porkArrangeDetailIds") Collection<? extends Serializable> porkArrangeDetailIds, @Param("tenant") Integer tenant);
}
