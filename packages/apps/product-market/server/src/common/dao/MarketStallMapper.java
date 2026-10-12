package com.guhecloud.cube.srv.gen.product.market.server.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketStall;
import com.guhecloud.cube.srv.gen.product.market.server.common.param.MarketStallQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.MarketStallGroupByBossVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：档位管理 Repository接口
 *
 * @author Appet
 * @date 2023-05-10 15:08:36
 */
@Repository
public interface MarketStallMapper extends BaseMapper<MarketStall> {

    Page<MarketStallGroupByBossVO> getStallGroupByBoss(Page page, @Param("param") MarketStallQueryParam param);

    void insertOrUpdateStallBatch(List<MarketStall> dataList);
}