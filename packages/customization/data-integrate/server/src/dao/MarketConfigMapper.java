package com.guhecloud.cube.base.dataintegrate.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.dataintegrate.server.entity.MarketConfig;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：采集市场配置 Repository接口
 *
 * @author Appet
 * @date 2023-09-09 15:08:36
 */
@Repository
public interface MarketConfigMapper extends BaseMapper<MarketConfig> {

    @InterceptorIgnore(tenantLine = "true")
    @Select("select * from market_config where deleted != 0")
    List<MarketConfig> getInvalidMarketConfig();
}