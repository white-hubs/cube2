package com.guhecloud.cube.biz.meattrade.server.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.meattrade.server.order.entity.SettlementConfig;
import org.springframework.stereotype.Repository;

@Repository("meattrade-settlementConfigMapper")
public interface SettlementConfigMapper extends BaseMapper<SettlementConfig> {
}
