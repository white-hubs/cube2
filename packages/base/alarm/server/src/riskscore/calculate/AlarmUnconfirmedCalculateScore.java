package com.guhecloud.cube.base.alarm.server.riskscore.calculate;

import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordMapper;
import com.guhecloud.cube.base.alarm.server.riskscore.convert.AlarmRiskIndexConvert;
import com.guhecloud.cube.base.alarm.server.riskscore.dto.AlarmRiskIndexDTO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.cache.RiskScoreCache;
import com.guhecloud.cube.srv.gen.riskscore.sdk.cache.dto.SafetyRiskScoreDTO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.enumeration.RiskIndexItemEnum;
import com.guhecloud.cube.srv.gen.riskscore.sdk.enumeration.RiskIndexTypeEnum;
import com.guhecloud.cube.srv.gen.riskscore.sdk.service.CalculateScoreService;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.alarm.AlarmLevelRiskIndexVO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.alarm.AlarmUnConfirmedRiskIndexVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmUnconfirmedCalculateScore implements CalculateScoreService<AlarmUnConfirmedRiskIndexVO> {

    private final RiskScoreCache riskScoreCache;

    private final AlarmRecordMapper alarmRecordMapper;

    @Override
    public RiskIndexTypeEnum calculateIndexType() {
        return RiskIndexTypeEnum.ALARM_RESPONSE;
    }

    @Override
    public AlarmUnConfirmedRiskIndexVO calculateRiskScore() {
        var lastWeek = DateUtils.lastWeek();
        var beginOfWeek = DateUtils.beginOfWeek(lastWeek);
        var endOfWeek = DateUtils.endOfWeek(lastWeek);
        var riskScoreConfigs = riskScoreCache.getRiskScoreConfigs(RiskIndexTypeEnum.ALARM_RESPONSE);
        var weightAllotConfigs = riskScoreCache.getWeightAllotConfigs(RiskIndexTypeEnum.ALARM_RESPONSE);
        var alarmRiskIndexDTOS = alarmRecordMapper.selectRiskIndexAlarmRecord(new Period(beginOfWeek, endOfWeek));
        //报警未响应数量指标总分
        var score = 0;
        if (CollectionUtils.isEmpty(alarmRiskIndexDTOS)) {
            return AlarmUnConfirmedRiskIndexVO.builder()
                    .riskIndex(score)
                    .tenant(SessionContextHolder.getRequester().getTenant())
                    .build();
        }
        List<AlarmLevelRiskIndexVO> alarmLevelRiskIndexDTOS = new ArrayList<>();
        Map<Integer, List<AlarmRiskIndexDTO>> alarmLevelRiskIndexVOMap = alarmRiskIndexDTOS.stream()
                .filter(vo -> AlarmStatusEnum.ALARM_UN_CONFIRMED
                        .name()
                        .equals(vo.getAlarmStatus()))
                .collect(Collectors.groupingBy(vo -> vo.getAlarmLevel()));
        var riskIndexItemEnums = RiskIndexItemEnum.values();
        double totalAlarmNum = 0;
        for (RiskIndexItemEnum riskIndexItemEnum : riskIndexItemEnums) {
            List<AlarmRiskIndexDTO> alarmRiskIndexVOS = new ArrayList<>();
            //获取对应指标项下的报警级别数量
            var alarmIndexItem = AlarmRiskIndexConvert.getAlarmIndexItem(riskIndexItemEnum);
            var levelAlarmCount = 0;
            for (Integer alarmLevel : alarmIndexItem) {
                if (alarmLevelRiskIndexVOMap.containsKey(alarmLevel)) {
                    alarmRiskIndexVOS.addAll(alarmLevelRiskIndexVOMap.get(alarmLevel));
                }
                levelAlarmCount += (int) alarmRiskIndexDTOS.stream().filter(alarmRiskIndexVO -> alarmLevel ==
                                                                                                alarmRiskIndexVO.getAlarmLevel())
                        .count();
            }
            var noResponse = 0;
            if (!CollectionUtils.isEmpty(alarmRiskIndexVOS)) {
                noResponse = alarmRiskIndexVOS.size();
                var weightAllot = weightAllotConfigs.stream()
                        .filter(weightAllotDTO -> riskIndexItemEnum.name()
                                .equals(weightAllotDTO.getIndexItem()))
                        .findFirst().get().getWeight();
                totalAlarmNum += new BigDecimal(weightAllot).multiply(new BigDecimal(noResponse)).divide(new BigDecimal(
                        levelAlarmCount * 100), 4, RoundingMode.HALF_UP).doubleValue();
            }
            alarmLevelRiskIndexDTOS.add(AlarmLevelRiskIndexVO.builder()
                                                .totalNum(noResponse)
                                                .alarmLevel(riskIndexItemEnum.name())
                                                .riskIndex(0)
                                                .build());
        }
        var alarmNum = totalAlarmNum;
        SafetyRiskScoreDTO responseConfig = riskScoreConfigs.stream()
                .filter(safetyRiskScoreDTO ->
                                safetyRiskScoreDTO.getSectionStart() <= alarmNum
                                && safetyRiskScoreDTO.getSectionEnd() > alarmNum
                       ).findFirst().orElse(null);
        if (Objects.nonNull(responseConfig)) {
            //计算报警响应得分
            var b = alarmNum - responseConfig.getSectionStart();
            score = BigDecimal.valueOf(responseConfig.getSlope()).multiply(new BigDecimal(Double.toString(b))).intValue() +
                    responseConfig.getScoreStart();
        }
        log.info("报警未响应得分:{}", score);
        return AlarmUnConfirmedRiskIndexVO.builder()
                .riskIndex(score)
                .tenant(SessionContextHolder.getRequester().getTenant())
                .alarmLevelIndexDetails(alarmLevelRiskIndexDTOS)
                .build();
    }
}
