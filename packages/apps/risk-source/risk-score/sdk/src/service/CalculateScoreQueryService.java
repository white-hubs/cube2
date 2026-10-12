package com.guhecloud.cube.srv.gen.riskscore.sdk.service;

import com.guhecloud.cube.srv.gen.riskscore.sdk.enumeration.RiskIndexTypeEnum;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.RiskIndexDetailVO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.RiskIndexVO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.alarm.AlarmSituationRiskIndexVO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.alarm.AlarmUnConfirmedRiskIndexVO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.hiddendanger.HiddenDangerRiskIndexVO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.hiddendanger.HiddenDangerUnConfirmedRiskIndexVO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.propertyfiling.PropertyFilingRiskIndexVO;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CalculateScoreQueryService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取安全分析分数
     *
     * @param beginWeek
     * @param tenant
     *
     * @return
     */
    public int getRiskIndexScore(LocalDateTime beginWeek, Integer tenant) {
        LocalDateTime beginOfWeek = DateUtils.beginOfWeek(beginWeek);
        int score = 0;
        for (RiskIndexTypeEnum riskIndexType : RiskIndexTypeEnum.values()) {
            Object o = redisTemplate.opsForHash().get(riskIndexType.name(), tenant + "@" + DateUtils.formatDate(beginOfWeek));
            if (Objects.nonNull(o)) {
                RiskIndexVO riskIndexVO = (RiskIndexVO) o;
                score += riskIndexVO.getRiskIndex();
            }
        }
        return score;
    }

    /**
     * 获取安全分析分数详情
     *
     * @param beginWeek
     * @param tenant
     *
     * @return
     */
    public RiskIndexDetailVO getRiskIndexDetail(LocalDateTime beginWeek, Integer tenant) {
        RiskIndexDetailVO riskIndexDetailVO = new RiskIndexDetailVO();
        int score = 0;
        LocalDateTime beginOfWeek = DateUtils.beginOfWeek(beginWeek);
        for (RiskIndexTypeEnum riskIndexType : RiskIndexTypeEnum.values()) {
            Object o = redisTemplate.opsForHash().get(riskIndexType.name(), tenant + "@" + DateUtils.formatDate(beginOfWeek));
            if (Objects.nonNull(o)) {
                RiskIndexVO riskIndexVO = (RiskIndexVO) o;
                score += riskIndexVO.getRiskIndex();
                this.getRiskIndexVO(riskIndexDetailVO, o, riskIndexType);
            }
        }
        riskIndexDetailVO.setRiskIndex(score);
        return riskIndexDetailVO;
    }

    private void getRiskIndexVO(RiskIndexDetailVO indexDetailVO, Object o, RiskIndexTypeEnum riskIndexTypeEnum) {
        switch (riskIndexTypeEnum) {
            case ALARM_NUMBER:
                indexDetailVO.setAlarmSituationRiskIndex((AlarmSituationRiskIndexVO) o);
                break;
            case ALARM_RESPONSE:
                indexDetailVO.setAlarmUnConfirmedRiskIndex((AlarmUnConfirmedRiskIndexVO) o);
                break;
            case PROPERTY_FILING:
                indexDetailVO.setPropertyFilingRiskIndex((PropertyFilingRiskIndexVO) o);
                break;
            case HIDDEN_DANGER_NUMBER:
                indexDetailVO.setHiddenDangerRiskIndex((HiddenDangerRiskIndexVO) o);
                break;
            case HIDDEN_DANGER_RESPONSE:
                indexDetailVO.setHiddenDangerUnConfirmedRiskIndex((HiddenDangerUnConfirmedRiskIndexVO) o);
        }
    }
}
