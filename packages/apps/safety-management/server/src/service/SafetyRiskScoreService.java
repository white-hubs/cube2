package com.guhecloud.cube.srv.gen.safety.management.service;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.RiskScoreQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.RiskScoreVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * @author wql
 * @describe 分值区间
 * @date 2023/10/12 15:56
 */
public interface SafetyRiskScoreService {
    void saveRiskScore(RiskScoreVO riskScoreVO);

    void deleteRiskScore(Long id);

    RiskScoreVO riskScoreDetail(Long id);

    Pagination<RiskScoreVO> riskScoreList(RiskScoreQuery query);

    RiskScoreVO getRiskScoreConfig(double alarmNum, String riskIndexType);

}
