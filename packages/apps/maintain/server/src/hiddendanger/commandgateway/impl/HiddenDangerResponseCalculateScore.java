package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.impl;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerNewService;
import com.guhecloud.cube.srv.gen.riskscore.sdk.cache.RiskScoreCache;
import com.guhecloud.cube.srv.gen.riskscore.sdk.cache.dto.SafetyRiskScoreDTO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.cache.dto.WeightAllotDTO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.enumeration.RiskIndexTypeEnum;
import com.guhecloud.cube.srv.gen.riskscore.sdk.service.CalculateScoreService;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.hiddendanger.HiddenDangerUnConfirmedRiskIndexVO;
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
 * @describe 隐患响应情况风险指数计算
 * @date 2023/11/17 9:16
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerResponseCalculateScore implements CalculateScoreService<HiddenDangerUnConfirmedRiskIndexVO> {

    private final HiddenDangerNewService hiddenDangerNewService;
    private final RiskScoreCache riskScoreCache;

    @Override
    public RiskIndexTypeEnum calculateIndexType() {
        return RiskIndexTypeEnum.HIDDEN_DANGER_RESPONSE;
    }

    @Override
    public HiddenDangerUnConfirmedRiskIndexVO calculateRiskScore() {
        HiddenDangerUnConfirmedRiskIndexVO build = null;
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<Boolean> hiddenRiskIndex = hiddenDangerNewService.getCountHiddenRiskIndex(null);
        if (ObjectUtils.isNotEmpty(hiddenRiskIndex)) {
            List<SafetyRiskScoreDTO> riskScoreConfigs = riskScoreCache.getRiskScoreConfigs(RiskIndexTypeEnum.HIDDEN_DANGER_RESPONSE);
            List<WeightAllotDTO> weightAllotConfigs = riskScoreCache.getWeightAllotConfigs(RiskIndexTypeEnum.HIDDEN_DANGER_RESPONSE);
            long unResponseNum = hiddenRiskIndex.stream().filter(s -> !s).count();
            if (ObjectUtils.isNotEmpty(weightAllotConfigs) && weightAllotConfigs.get(0).getWeight() != null) {
                double score = BigDecimal.valueOf(unResponseNum * weightAllotConfigs.get(0).getWeight())
                        .divide(BigDecimal.valueOf(hiddenRiskIndex.size() * 100L), 4, RoundingMode.HALF_UP).doubleValue();
                SafetyRiskScoreDTO scoreConfig = riskScoreConfigs.stream()
                        .filter(safetyRiskScoreDTO -> safetyRiskScoreDTO.getSectionStart() <= score
                                                      && safetyRiskScoreDTO.getSectionEnd() > score
                               ).findFirst().orElse(null);
                if (ObjectUtils.isNotEmpty(scoreConfig)) {
                    assert scoreConfig != null;
                    int index = BigDecimal.valueOf(score - scoreConfig.getSectionStart())
                            .multiply(BigDecimal.valueOf(scoreConfig.getSlope()))
                            .add(BigDecimal.valueOf(scoreConfig.getScoreStart())).intValue();
                    build = HiddenDangerUnConfirmedRiskIndexVO.builder().totalNum(hiddenRiskIndex.size()).riskIndex(index).tenant(tenant)
                            .build();
                }
            }
        }
        build = build == null ? HiddenDangerUnConfirmedRiskIndexVO.builder().totalNum(hiddenRiskIndex.size()).riskIndex(0).tenant(tenant)
                .build() : build;
        riskScoreCache.settingRiskScoreInfo(RiskIndexTypeEnum.HIDDEN_DANGER_RESPONSE, build);
        log.info("隐患单响应风险指数计算结果:{}", JSON.toJSONString(build));
        return build;
    }
}
