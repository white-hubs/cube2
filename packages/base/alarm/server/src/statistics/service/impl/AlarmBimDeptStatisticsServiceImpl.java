package com.guhecloud.cube.base.alarm.server.statistics.service.impl;

import com.guhecloud.cube.base.alarm.server.common.facade.device.AlarmBimStatisticsQueryConvert;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordMapper;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.service.AlarmBimDeptStatisticsService;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsTopChainVO;
import com.guhecloud.cube.base.alarm.server.util.DateUnitUtils;
import com.guhecloud.cube.base.alarm.server.util.MathUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmBimDeptStatisticsServiceImpl implements AlarmBimDeptStatisticsService {

    private final AlarmRecordMapper alarmRecordMapper;

    private final AlarmBimStatisticsQueryConvert alarmBimStatisticsQueryConvert;

    @Override
    public List<AlarmBimStatisticsTopChainVO> getDeptHandlerChainTopStatistics(AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        queryParam.setAlarmStatus(AlarmStatusEnum.ALARM_HANDLER.name());
        if ((Objects.nonNull(param.getSceneType()) || !CollectionUtils.isEmpty(param.getDeviceTags())) &&
            CollectionUtils.isEmpty(queryParam.getDeviceIds())) {
            return Collections.emptyList();
        }
        List<AlarmBimStatisticsTopChainVO> alarmBimStatisticsTopChainVOS = this.alarmRecordMapper.selectDeptChainTopStatistics(queryParam);
        if (CollectionUtils.isEmpty(alarmBimStatisticsTopChainVOS)) {
            return Collections.emptyList();
        }
        //排名的部门主键
        List<String> topDeptIds = alarmBimStatisticsTopChainVOS.stream()
                .map(alarmBimStatisticsTopChainVO -> alarmBimStatisticsTopChainVO.getId())
                .collect(Collectors.toList());
        queryParam.setPeriod(DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod()));
        queryParam.setDepartmentIds(topDeptIds);
        List<AlarmBimStatisticsTopChainVO> lastTopStatistics = this.alarmRecordMapper.selectDeptChainTopStatistics(queryParam);
        alarmBimStatisticsTopChainVOS.stream().forEach(alarmBimStatisticsTopChainVO -> {
            Optional<AlarmBimStatisticsTopChainVO> lastAlarmBimStatisticsTop = lastTopStatistics.stream()
                    .filter(lastTop -> alarmBimStatisticsTopChainVO
                            .getId()
                            .equals(lastTop.getId()))
                    .findFirst();
            long lastAlarmNum = 0L;
            if (lastAlarmBimStatisticsTop.isPresent()) {
                lastAlarmNum = lastAlarmBimStatisticsTop.get().getAlarmNum().longValue();
            }
            alarmBimStatisticsTopChainVO.setChainRatio(MathUtils.calculateMonthOnMonthRatio(alarmBimStatisticsTopChainVO
                                                                                                    .getAlarmNum()
                                                                                                    .longValue(), lastAlarmNum));
        });
        return alarmBimStatisticsTopChainVOS;
    }
}
