package com.guhecloud.cube.base.alarm.server.statistics.service.impl;

import com.guhecloud.cube.base.alarm.server.common.facade.device.AlarmBimStatisticsQueryConvert;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordMapper;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.service.AlarmBimResultStatisticsService;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimHandlerResultStatisticsVO;
import com.guhecloud.cube.base.alarm.server.util.DateUnitUtils;
import com.guhecloud.cube.base.alarm.server.util.MathUtils;
import com.guhecloud.platform.base.model.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmBimResultStatisticsServiceImpl implements AlarmBimResultStatisticsService {

    private final AlarmRecordMapper alarmRecordMapper;

    private final AlarmBimStatisticsQueryConvert alarmBimStatisticsQueryConvert;

    @Override
    public AlarmBimHandlerResultStatisticsVO getAlarmHandlerResultStatistics(AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam queryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        if ((Objects.nonNull(param.getSceneType()) || !CollectionUtils.isEmpty(param.getDeviceTags())) &&
            CollectionUtils.isEmpty(queryParam.getDeviceIds())) {
            return new AlarmBimHandlerResultStatisticsVO(0L, 0f, 0L, 0f, 0L, 0f);
        }
        Long alarmNum = this.alarmRecordMapper.selectAlarmTotal(queryParam);
        //本期处理结果
        AlarmBimHandlerResultStatisticsVO alarmBimCurrentHandlerResultStatistics = this.alarmRecordMapper
                .selectAlarmBimHandlerResultStatistics(queryParam);
        //上期处理结果
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod());
        queryParam.setPeriod(previousPeriod);
        AlarmBimHandlerResultStatisticsVO alarmBimLastHandlerResultStatistics = this.alarmRecordMapper
                .selectAlarmBimHandlerResultStatistics(queryParam);
        //上期报警总数
        Long lastAlarmNum = this.alarmRecordMapper.selectAlarmTotal(queryParam);
        //本期的处理结果数量
        Long actualNum = alarmBimCurrentHandlerResultStatistics.getActualNum();
        Long impracticalNum = alarmBimCurrentHandlerResultStatistics.getImpracticalNum();
        //上期的处理结果数量
        Long lastActualNum = alarmBimLastHandlerResultStatistics.getActualNum();
        Long lastImpracticalNum = alarmBimLastHandlerResultStatistics.getImpracticalNum();
        return AlarmBimHandlerResultStatisticsVO.builder()
                .totalNum(alarmNum)
                .ratio(MathUtils.calculateMonthOnMonthRatio(actualNum, lastAlarmNum))
                .actualNum(actualNum)
                .actualRatio(MathUtils.calculateMonthOnMonthRatio(actualNum, lastActualNum))//实报环比
                .impracticalNum(impracticalNum)
                .impracticalRatio(MathUtils.calculateMonthOnMonthRatio(impracticalNum, lastImpracticalNum))//误报环比
                .build();
    }

}
