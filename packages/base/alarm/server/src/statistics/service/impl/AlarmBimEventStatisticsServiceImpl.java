package com.guhecloud.cube.base.alarm.server.statistics.service.impl;

import com.guhecloud.cube.base.alarm.server.common.facade.device.AlarmBimStatisticsQueryConvert;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordMapper;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.service.AlarmBimEventStatisticsService;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsEventGroupVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsHandlerVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmBimEventStatisticsServiceImpl implements AlarmBimEventStatisticsService {

    private final AlarmRecordMapper alarmRecordMapper;

    private final AlarmBimStatisticsQueryConvert alarmBimStatisticsQueryConvert;

    @Override
    public List<AlarmBimStatisticsHandlerVO> getEventTypeHandlerListStatistics(AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam queryParam = alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        if ((Objects.nonNull(param.getSceneType()) || !CollectionUtils.isEmpty(param.getDeviceTags())) &&
            CollectionUtils.isEmpty(queryParam.getDeviceIds())) {
            return Collections.emptyList();
        }
        return this.alarmRecordMapper.selectEventTypeHandlerListStatistics(queryParam);
    }

    @Override
    public List<AlarmBimStatisticsHandlerVO> getEventTypeListStatistics(AlarmStatisticsQueryParam param) {
        return this.alarmRecordMapper.selectEventTypeListStatistics(param);
    }

    @Override
    public List<AlarmBimStatisticsEventGroupVO> getEventTypeGroupTotalStatistics(AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam queryParam = alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        if ((Objects.nonNull(param.getSceneType()) || !CollectionUtils.isEmpty(param.getDeviceTags())) &&
            CollectionUtils.isEmpty(queryParam.getDeviceIds())) {
            return Collections.emptyList();
        }
        return this.alarmRecordMapper.selectEventTypeGroupTotalStatistics(queryParam);
    }
}
