package com.guhecloud.cube.srv.gen.safety.management.service;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.FireSafetyStatisticsCardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.FireSafetyStatisticsMarketCardVO;

/**
 * @author wql
 * @describe 消防管理
 * @date 2023/9/8 14:51
 */
public interface FireSafetyManageService {
    FireSafetyStatisticsCardVO getStatisticalCard(String applicationCode);

    FireSafetyStatisticsMarketCardVO getStatisticalMarketCard(BaseQuery query);
}
