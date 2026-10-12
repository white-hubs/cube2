package com.guhecloud.cube.base.alarm.server.statistics.service.impl;

import com.guhecloud.cube.base.alarm.server.common.facade.device.AlarmBimStatisticsQueryConvert;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordMapper;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.service.AlarmBimDeviceStatisticsService;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsActualVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsVO;
import com.guhecloud.cube.base.alarm.server.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmBimDeviceStatisticsServiceImpl implements AlarmBimDeviceStatisticsService {

    private final AlarmBimStatisticsQueryConvert alarmBimStatisticsQueryConvert;

    private final AlarmRecordMapper alarmRecordMapper;

    @Override
    public List<AlarmBimStatisticsActualVO> getDeviceTagActualRatioStatistics(AlarmBimStatisticsQueryParam param) {
        if (CollectionUtils.isEmpty(param.getDeviceTags())) {
            return Collections.emptyList();
        }
        List<AlarmBimStatisticsActualVO> alarmBimStatisticsActualVOS = new ArrayList<>(param.getDeviceTags().size());
        AlarmStatisticsQueryParam statisticsQueryParam = ObjectUtils.copying(param, AlarmStatisticsQueryParam.class);
        for (String deviceTag : param.getDeviceTags()) {
            List<Long> deviceIds = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsDeviceTag(deviceTag, null);
            AlarmBimStatisticsActualVO alarmBimStatisticsActualVO = AlarmBimStatisticsActualVO.builder()
                    .code(deviceTag)
                    .actualNum(0)
                    .handlerNum(0)
                    .ratio(0)
                    .build();
            //获取标签设备下的报警数据
            if (!CollectionUtils.isEmpty(deviceIds)) {
                statisticsQueryParam.setDeviceIds(deviceIds);
                AlarmBimStatisticsVO alarmBimStatisticsVO = this.alarmRecordMapper.selectAlarmBimStatistics(statisticsQueryParam);
                alarmBimStatisticsActualVO.setHandlerNum(alarmBimStatisticsVO.getAlarmHandlerNum().intValue());
                alarmBimStatisticsActualVO.setActualNum(alarmBimStatisticsVO.getAlarmActualNum().intValue());
                alarmBimStatisticsActualVO.setRatio(MathUtils.calculateProportion(alarmBimStatisticsVO.getAlarmActualNum(),
                                                                                  alarmBimStatisticsVO.getAlarmHandlerNum()));
            }
            alarmBimStatisticsActualVOS.add(alarmBimStatisticsActualVO);
        }
        return alarmBimStatisticsActualVOS;
    }
}
