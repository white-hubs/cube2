package com.guhecloud.cube.srv.gen.safety.management.service;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.AppSafetyWorkSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyMarketStatisticsCardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyStatisticsCardVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyWorkStatusVO;

/**
 * @author wql
 * @describe 安全管理
 * @date 2023/9/7 19:53
 */
public interface SafetyManageService {
    SafetyWorkStatusVO getSafetyWorkStatus(BaseQuery query);

    SafetyStatisticsCardVO getSafetyCardStatistics(BaseQuery query);

    SafetyMarketStatisticsCardVO getSafetyMarketCardStatistics(BaseQuery cardQuery);

    AppSafetyWorkSituationVO getAppSafetyWorkSituation(BaseQuery query);
}
