package com.guhecloud.cube.srv.gen.safety.management.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketBaseVO;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyRiskIndexPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wql
 * @describe 安全风险指数
 * @date 2023/10/12 15:51
 */
@Mapper
public interface SafetyRiskIndexMapper extends BaseMapper<SafetyRiskIndexPO> {
    List<MarketBaseVO> getMarketList(@Param("marketTenant") Integer marketTenant);
}
