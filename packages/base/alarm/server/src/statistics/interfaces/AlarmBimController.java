package com.guhecloud.cube.base.alarm.server.statistics.interfaces;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.alarm.server.common.facade.device.AlarmBimStatisticsQueryConvert;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.AlarmRecordQueryService;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query.AlarmRecordQueryParam;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordPageVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmTrendStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmTrendStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.service.AlarmStatisticsService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmBimController {

    private final AlarmStatisticsService alarmStatisticsService;

    private final AlarmRecordQueryService alarmRecordQueryService;

    private final AlarmBimStatisticsQueryConvert alarmBimStatisticsQueryConvert;

    /**
     * 报警统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-bim/statistics")
    public AlarmStatisticsVO getAlarmStatistics(@Valid AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam alarmStatisticsQueryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        List<Long> ids = alarmStatisticsQueryParam.getDeviceIds();
        if (CollectionUtils.isEmpty(ids)) {
            return AlarmStatisticsVO
                    .builder()
                    .alarmNum(0L)
                    .build();
        }
        LocalDateTime dateTime = DateUtils.endOfDay(DateUtils.now());
        LocalDateTime startTime = DateUtils.beginOfDay(DateUtils.offset(dateTime, ChronoUnit.DAYS, -6));
        AlarmStatisticsQueryParam queryParam = ObjectUtils.copying(param, AlarmStatisticsQueryParam.class);
        queryParam.setPeriod(new Period(startTime, dateTime));
        queryParam.setDeviceIds(ids);
        return AlarmStatisticsVO
                .builder()
                .alarmNum(this.alarmStatisticsService.getAlarmTotal(queryParam))
                .alarmStatusStatisticsVOS(this.alarmStatisticsService.getAlarmStatusStatistics(queryParam))
                .alarmResultStatisticsVOS(this.alarmStatisticsService.getAlarmResultStatistics(queryParam))
                .build();
    }

    /**
     * 报警记录列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-bim/records")
    public Pagination<AlarmRecordPageVO> getAlarmRecordPage(@Valid AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam alarmStatisticsQueryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        List<Long> ids = alarmStatisticsQueryParam.getDeviceIds();
        if (CollectionUtils.isEmpty(ids)) {
            return Pagination.empty();
        }
        AlarmRecordQueryParam queryParam = new AlarmRecordQueryParam();
        queryParam.setPeriod(param.getPeriod());
        queryParam.setDeviceIds(ids);
        Page<AlarmRecordPageVO> alarmRecordPage = this.alarmRecordQueryService.getPage(queryParam);
        if (CollectionUtils.isEmpty(alarmRecordPage.getRecords())) {
            return Pagination.empty();
        }
        alarmRecordPage.getRecords().stream().forEach(alarmRecordPageVO -> {
            alarmRecordPageVO.setAlarmStatusName(alarmRecordPageVO.getAlarmStatus().getDescription());
        });
        return PagingUtils.convert(alarmRecordPage);
    }

    /**
     * 近七天报警点趋势图
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-bim/statistics/seven-day-trend")
    public List<AlarmTrendStatisticsVO> getAlarmConfirmedLastSevenDayTrendStatistics(@Valid AlarmBimStatisticsQueryParam param) {
        AlarmStatisticsQueryParam alarmStatisticsQueryParam = this.alarmBimStatisticsQueryConvert.convertAlarmStatisticsQueryParam(param);
        List<Long> ids = alarmStatisticsQueryParam.getDeviceIds();
        LocalDateTime dateTime = DateUtils.endOfDay(DateUtils.now());
        LocalDateTime startTime = DateUtils.beginOfDay(DateUtils.offset(dateTime, ChronoUnit.DAYS, -6));
        if (CollectionUtils.isEmpty(ids)) {
            List<AlarmTrendStatisticsVO> alarmTrendStatisticsVOS = new ArrayList<>();
            for (LocalDateTime start = startTime;
                 start.isBefore(dateTime) || start.isEqual(dateTime); start = DateUtils.offset(start, ChronoUnit.DAYS, 1)) {
                String date = DateUtils.format(start, "yyyy-MM-dd");
                alarmTrendStatisticsVOS.add(new AlarmTrendStatisticsVO(0L, date));
            }
            return alarmTrendStatisticsVOS;
        }
        AlarmTrendStatisticsQueryParam queryParam = new AlarmTrendStatisticsQueryParam();
        queryParam.setPeriod(new Period(startTime, dateTime));
        queryParam.setDeviceIds(ids);
        queryParam.setType("DAY");
        return this.alarmStatisticsService.getAlarmTrendStatistics(queryParam);
    }
}
