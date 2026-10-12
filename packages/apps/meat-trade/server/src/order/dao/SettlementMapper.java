package com.guhecloud.cube.biz.meattrade.server.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Settlement;
import org.springframework.stereotype.Repository;

@Repository("meattrade-settlementMapper")
public interface SettlementMapper extends BaseMapper<Settlement> {

}
