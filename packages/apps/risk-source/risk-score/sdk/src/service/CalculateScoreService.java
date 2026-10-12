package com.guhecloud.cube.srv.gen.riskscore.sdk.service;

import com.guhecloud.cube.srv.gen.riskscore.sdk.enumeration.RiskIndexTypeEnum;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.RiskIndexVO;

public interface CalculateScoreService<T extends RiskIndexVO> {

    RiskIndexTypeEnum calculateIndexType();

    T calculateRiskScore();
}
