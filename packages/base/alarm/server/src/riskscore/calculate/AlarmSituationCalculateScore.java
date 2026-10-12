package com.guhecloud.cube.base.alarm.server.riskscore.calculate;

import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordMapper;
import com.guhecloud.cube.base.alarm.server.riskscore.convert.AlarmRiskIndexConvert;
import com.guhecloud.cube.base.alarm.server.riskscore.dto.AlarmRiskIndexDTO;
import com.guhecloud.cube.base.device.center.DeviceCenterClient;
import com.guhecloud.cube.srv.gen.riskscore.sdk.cache.RiskScoreCache;
import com.guhecloud.cube.srv.gen.riskscore.sdk.enumeration.RiskIndexItemEnum;
import com.guhecloud.cube.srv.gen.riskscore.sdk.enumeration.RiskIndexTypeEnum;
import com.guhecloud.cube.srv.gen.riskscore.sdk.service.CalculateScoreService;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.alarm.AlarmLevelRiskIndexVO;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.alarm.AlarmSituationRiskIndexVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmSituationCalculateScore implements CalculateScoreService<AlarmSituationRiskIndexVO> {

    private final RiskScoreCache riskScoreCache;

    private final DeviceCenterClient deviceCenterClient;

    private final AlarmRecordMapper alarmRecordMapper;

    @Override
    public RiskIndexTypeEnum calculateIndexType() {
        return RiskIndexTypeEnum.ALARM_NUMBER;
    }

    @Override
    public AlarmSituationRiskIndexVO calculateRiskScore() {
        var lastWeek = DateUtils.lastWeek();
        var beginOfWeek = DateUtils.beginOfWeek(lastWeek);
        var endOfWeek = DateUtils.endOfWeek(lastWeek);
        var riskScoreConfigs = riskScoreCache.getRiskScoreConfigs(RiskIndexTypeEnum.ALARM_NUMBER);
        var weightAllotConfigs = riskScoreCache.getWeightAllotConfigs(RiskIndexTypeEnum.ALARM_NUMBER);
        var alarmRiskIndexDTOS = alarmRecordMapper.selectRiskIndexAlarmRecord(new Period(beginOfWeek, endOfWeek));
        var score = 0;
        var deviceCount = this.deviceCenterClient.getAllDevice().size();
        if (CollectionUtils.isEmpty(alarmRiskIndexDTOS) || 0 >= deviceCount) {
            return AlarmSituationRiskIndexVO.builder()
                    .riskIndex(score)
                    .tenant(SessionContextHolder.getRequester().getTenant())
                    .build();
        }
        List<AlarmLevelRiskIndexVO> alarmLevelRiskIndexDTOS = new ArrayList<>();
        //报警数量
        Map<Integer, List<AlarmRiskIndexDTO>> alarmLevelRiskIndexVOMap = alarmRiskIndexDTOS.stream()
                .collect(Collectors.groupingBy(AlarmRiskIndexDTO::getAlarmLevel));
        var riskIndexItemEnums = RiskIndexItemEnum.values();
        double totalAlarmNum = 0;
        for (RiskIndexItemEnum riskIndexItemEnum : riskIndexItemEnums) {
            List<AlarmRiskIndexDTO> alarmRiskIndexVOS = new ArrayList<>();
            var alarmLevels = AlarmRiskIndexConvert.getAlarmIndexItem(riskIndexItemEnum);
            for (Integer alarmLevel : alarmLevels) {
                if (alarmLevelRiskIndexVOMap.containsKey(alarmLevel)) {
                    alarmRiskIndexVOS.addAll(alarmLevelRiskIndexVOMap.get(alarmLevel));
                }
            }
            if (!CollectionUtils.isEmpty(alarmRiskIndexVOS)) {
                var deviceNum = new BigDecimal(deviceCount * 100);
                var weightAllot = weightAllotConfigs.stream()
                        .filter(weightAllotDTO -> riskIndexItemEnum.name()
                                .equals(weightAllotDTO.getIndexItem()))
                        .findAny().get();
                totalAlarmNum += new BigDecimal(alarmRiskIndexVOS.size() * weightAllot.getWeight())
                        .divide(deviceNum, 4, RoundingMode.HALF_UP).doubleValue();
            }
            alarmLevelRiskIndexDTOS.add(AlarmLevelRiskIndexVO.builder()
                                                .totalNum(alarmRiskIndexVOS.size())
                                                .alarmLevel(riskIndexItemEnum.name())
                                                .riskIndex(0)
                                                .build());
        }
        var alarmNum = totalAlarmNum;
        var scoreConfig = riskScoreConfigs.stream()
                .filter(safetyRiskScoreDTO ->
                                safetyRiskScoreDTO.getSectionStart() <= alarmNum
                                && safetyRiskScoreDTO.getSectionEnd() > alarmNum
                       ).findFirst().orElse(null);
        if (ObjectUtils.isNotNull(scoreConfig)) {
            //计算报警数量得分
            var b = totalAlarmNum - scoreConfig.getSectionStart();
            score = BigDecimal.valueOf(scoreConfig.getSlope()).multiply(new BigDecimal(Double.toString(b))).intValue() +
                    scoreConfig.getScoreStart();
        }
        log.info("报警数量得分:{}", score);
        return AlarmSituationRiskIndexVO.builder()
                .riskIndex(score)
                .tenant(SessionContextHolder.getRequester().getTenant())
                .alarmLevelIndexDetails(alarmLevelRiskIndexDTOS)
                .build();
    }
}
