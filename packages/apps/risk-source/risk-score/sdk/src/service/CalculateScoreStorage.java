package com.guhecloud.cube.srv.gen.riskscore.sdk.service;

import com.guhecloud.cube.srv.gen.riskscore.sdk.cache.RiskScoreCache;
import com.guhecloud.cube.srv.gen.riskscore.sdk.enumeration.RiskIndexTypeEnum;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.RiskIndexVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CalculateScoreStorage {

    /**
     * 分数存储
     *
     * @param calculateScoreService
     */
    public static void calculateRiskIndexScore(CalculateScoreService calculateScoreService) {
        //获取指标类型
        RiskIndexTypeEnum riskIndexType = calculateScoreService.calculateIndexType();
        //获取分数
        RiskIndexVO riskIndexVO = calculateScoreService.calculateRiskScore();
        //存入Redis
        RiskScoreCache riskScoreCache = ApplicationContextHolder.getBean(RiskScoreCache.class);
        riskScoreCache.settingRiskScoreInfo(riskIndexType, riskIndexVO);
    }
}
