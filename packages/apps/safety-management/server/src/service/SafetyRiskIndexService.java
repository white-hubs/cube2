package com.guhecloud.cube.srv.gen.safety.management.service;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.RiskIndexQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.RiskIndexDetailVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.RiskIndexVO;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyRiskIndexPO;

import java.util.List;

/**
 * @author wql
 * @describe 安全风险指数
 * @date 2023/10/12 15:57
 */
public interface SafetyRiskIndexService {
    List<RiskIndexVO> riskIndexList(RiskIndexQuery query);

    List<RiskIndexVO> riskIndexPeriodList(RiskIndexQuery query);

    RiskIndexDetailVO riskIndexDetail(RiskIndexQuery query);

    List<SafetyRiskIndexPO> riskIndexGenerate(RiskIndexQuery query);
}
