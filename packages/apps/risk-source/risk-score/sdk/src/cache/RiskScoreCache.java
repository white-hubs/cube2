package com.guhecloud.cube.srv.gen.riskscore.sdk.cache;

import com.guhecloud.cube.srv.gen.riskscore.sdk.cache.dto.SafetyRiskScoreDTO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.cache.dto.WeightAllotDTO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.constant.SafetyRiskConstant;
import com.guhecloud.cube.srv.gen.riskscore.sdk.enumeration.RiskIndexTypeEnum;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.RiskIndexVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RiskScoreCache {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void setRiskScoreConfigs(List<SafetyRiskScoreDTO> safetyRiskScoreDTOS) {
        Map<String, List<SafetyRiskScoreDTO>> safetyRiskScoreTypeMaps = safetyRiskScoreDTOS.stream()
                .collect(Collectors.groupingBy(safetyRiskScoreDTO -> safetyRiskScoreDTO.getIndexType() + "-" +
                                                                     safetyRiskScoreDTO.getTenant()));
        //分数区间配置存入Redis
        this.redisTemplate.opsForHash().putAll(SafetyRiskConstant.SAFETY_RISK_SCORE_CONFIG, safetyRiskScoreTypeMaps);
    }

    public List<SafetyRiskScoreDTO> getRiskScoreConfigs(RiskIndexTypeEnum riskIndexType) {
        Object o = this.redisTemplate.opsForHash().get(SafetyRiskConstant.SAFETY_RISK_SCORE_CONFIG,
                                                       riskIndexType.name() + "-" + SessionContextHolder.getRequester().getTenant());
        if (Objects.isNull(o)) {
            return Collections.emptyList();
        }
        return (List<SafetyRiskScoreDTO>) o;
    }

    public void setWeightAllotConfigs(List<WeightAllotDTO> weightAllotConfigs) {
        Map<String, List<WeightAllotDTO>> weightAllotTypeMaps = weightAllotConfigs.stream()
                .collect(Collectors.groupingBy(weightAllotDTO -> weightAllotDTO.getIndexType() + "-" + weightAllotDTO.getTenant()));
        this.redisTemplate.opsForHash().putAll(SafetyRiskConstant.SAFETY_WEIGHT_ALLOW_CONFIG, weightAllotTypeMaps);
    }

    public List<WeightAllotDTO> getWeightAllotConfigs(RiskIndexTypeEnum riskIndexType) {
        Object o = this.redisTemplate.opsForHash().get(SafetyRiskConstant.SAFETY_WEIGHT_ALLOW_CONFIG,
                                                       riskIndexType.name() + "-" + SessionContextHolder.getRequester().getTenant());
        if (Objects.isNull(o)) {
            return Collections.emptyList();
        }
        return (List<WeightAllotDTO>) o;
    }

    public void settingRiskScoreInfo(RiskIndexTypeEnum riskIndexType, RiskIndexVO riskIndexVO) {
        LocalDateTime lastWeek = DateUtils.lastWeek();
        LocalDateTime beginOfWeek = DateUtils.beginOfWeek(lastWeek);
        String lastWeekDate = DateUtils.formatDate(beginOfWeek);
        this.redisTemplate.opsForHash().put(riskIndexType.name(), riskIndexVO.getTenant() + "@" + lastWeekDate, riskIndexVO);
    }
}
