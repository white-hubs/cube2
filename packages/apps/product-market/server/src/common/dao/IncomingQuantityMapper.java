package com.guhecloud.cube.srv.gen.product.market.server.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.IncomingQuantity;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsIncomingStatParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.CommonStatVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：农产品来货量 Repository接口
 *
 * @author Appet
 * @date 2023-05-10 15:08:36
 */
@Repository
public interface IncomingQuantityMapper extends BaseMapper<IncomingQuantity> {

    /**
     * 来货量按时间统计
     *
     * @param param
     *
     * @return
     */
    List<CommonStatVO> incomingQuantityStatByTime(@Param("param") GoodsIncomingStatParam param);
}