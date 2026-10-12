package com.guhecloud.cube.srv.gen.safety.management.service;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.UseAirSafetyStatisticsCardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.UseAirSafetyStatisticsMarketCardVO;

/**
 * @author wql
 * @describe 用气安全管理
 * @date 2023/9/8 14:34
 */
public interface UseAirSafetyManageService {

    UseAirSafetyStatisticsCardVO getStatisticalCard(String applicationCode);

    UseAirSafetyStatisticsMarketCardVO getStatisticalMarketCard(BaseQuery baseQuery);
}
