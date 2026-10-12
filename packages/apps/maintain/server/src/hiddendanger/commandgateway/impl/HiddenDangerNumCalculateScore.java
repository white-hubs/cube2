package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.impl;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerNewService;
import com.guhecloud.cube.srv.gen.riskscore.sdk.cache.RiskScoreCache;
import com.guhecloud.cube.srv.gen.riskscore.sdk.cache.dto.SafetyRiskScoreDTO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.cache.dto.WeightAllotDTO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.enumeration.RiskIndexTypeEnum;
import com.guhecloud.cube.srv.gen.riskscore.sdk.service.CalculateScoreService;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.hiddendanger.HiddenDangerRiskIndexVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author wql
 * @describe 隐患风险指数计算
 * @date 2023/11/16 18:08
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerNumCalculateScore implements CalculateScoreService<HiddenDangerRiskIndexVO> {

    private final HiddenDangerNewService hiddenDangerNewService;
    private final RiskScoreCache riskScoreCache;
    private final TenantClient tenantClient;

    @Override
    public RiskIndexTypeEnum calculateIndexType() {
        return RiskIndexTypeEnum.HIDDEN_DANGER_NUMBER;
    }

    @Override
    public HiddenDangerRiskIndexVO calculateRiskScore() {
        HiddenDangerRiskIndexVO build = null;
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<Boolean> hiddenRiskIndex = hiddenDangerNewService.getCountHiddenRiskIndex(null);
        if (ObjectUtils.isNotEmpty(hiddenRiskIndex)) {
            List<SafetyRiskScoreDTO> riskScoreConfigs = riskScoreCache.getRiskScoreConfigs(RiskIndexTypeEnum.HIDDEN_DANGER_NUMBER);
            List<WeightAllotDTO> weightAllotConfigs = riskScoreCache.getWeightAllotConfigs(RiskIndexTypeEnum.HIDDEN_DANGER_NUMBER);
            TenantDTO byCode = tenantClient.getByCode(tenant);
            if (ObjectUtils.isNotEmpty(byCode) && !byCode.getAreaCovered().equals(BigDecimal.ZERO)
                && ObjectUtils.isNotEmpty(weightAllotConfigs) && weightAllotConfigs.get(0).getWeight() != null) {
                double score = BigDecimal.valueOf((long) hiddenRiskIndex.size() * weightAllotConfigs.get(0).getWeight())
                        .divide(byCode.getAreaCovered().multiply(BigDecimal.valueOf(100)), 4, RoundingMode.HALF_UP).doubleValue();
                SafetyRiskScoreDTO scoreConfig = riskScoreConfigs.stream()
                        .filter(safetyRiskScoreDTO -> safetyRiskScoreDTO.getSectionStart() <= score
                                                      && safetyRiskScoreDTO.getSectionEnd() > score
                               ).findFirst().orElse(null);
                if (ObjectUtils.isNotEmpty(scoreConfig)) {
                    assert scoreConfig != null;
                    int index = BigDecimal.valueOf(score - scoreConfig.getSectionStart())
                            .multiply(BigDecimal.valueOf(scoreConfig.getSlope()))
                            .add(BigDecimal.valueOf(scoreConfig.getScoreStart())).intValue();
                    build = HiddenDangerRiskIndexVO.builder().totalNum(hiddenRiskIndex.size()).riskIndex(index).tenant(tenant).build();
                }
            }
        }
        build = build == null ? HiddenDangerRiskIndexVO.builder().totalNum(hiddenRiskIndex.size()).riskIndex(0).tenant(tenant).build()
                              : build;
        riskScoreCache.settingRiskScoreInfo(RiskIndexTypeEnum.HIDDEN_DANGER_NUMBER, build);
        log.info("隐患数量风险指数计算结果：{}", JSON.toJSONString(build));
        return build;
    }
}
