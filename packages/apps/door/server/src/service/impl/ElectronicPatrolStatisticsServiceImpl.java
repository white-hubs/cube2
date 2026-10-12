package com.guhecloud.cube.biz.door.server.service.impl;

import com.guhecloud.cube.biz.door.server.constant.TimeDimensionConstant;
import com.guhecloud.cube.biz.door.server.entity.EventRecord;
import com.guhecloud.cube.biz.door.server.param.StatisticsParam;
import com.guhecloud.cube.biz.door.server.service.ElectronicPatrolStatisticsService;
import com.guhecloud.cube.biz.door.server.service.EventRecordService;
import com.guhecloud.cube.biz.door.server.vo.PatrolListStatisticsVO;
import com.guhecloud.cube.biz.door.server.vo.PatrolPersonLocationStatisticsVO;
import com.guhecloud.cube.biz.door.server.vo.TodayPatrolStatisticsVO;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 电子巡更-定制海康UE面板统计
 * @date 2024/3/15 14:51
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ElectronicPatrolStatisticsServiceImpl implements ElectronicPatrolStatisticsService {

    private final EventRecordService eventRecordService;

    @Override
    public TodayPatrolStatisticsVO todayPatrolStatistics(StatisticsParam param) {
        List<EventRecord> records = eventRecordService.getPatrolRecord(param);
        if (ObjectUtils.isNotEmpty(records)) {
            LocalDateTime startTime = records.getFirst().getEventTime();
            LocalDateTime endTime = records.getLast().getEventTime();
            return TodayPatrolStatisticsVO.builder()
                    .personNum(records.stream().map(EventRecord::getPersonId).distinct().count())
                    .pointNum(records.stream().map(EventRecord::getDoorIndexCode).distinct().count())
                    .elapsedTime(DateUtils.between(startTime, endTime, ChronoUnit.HOURS))
                    .build();
        }
        return TodayPatrolStatisticsVO.builder()
                .personNum(0L)
                .pointNum(0L)
                .elapsedTime(0L)
                .build();
    }

    @Override
    public List<PatrolListStatisticsVO> todayPatrolTimeFrame(StatisticsParam param) {
        List<EventRecord> records = eventRecordService.getPatrolRecord(param);
        records = ObjectUtils.isNull(records) ? new ArrayList<>() : records;
        List<PatrolListStatisticsVO> vos = new ArrayList<>();
        LocalDateTime begin = DateUtils.beginOfDay(DateUtils.now());
        int hour = begin.getHour();
        for (int i = 0; i < hour + 1; i++) {
            LocalDateTime finalBegin = begin;
            long count = records.stream().filter(record -> DateUtils.toTimestamp(record.getEventTime()) > DateUtils.toTimestamp(finalBegin)
                                                           &&
                                                           DateUtils.toTimestamp(record.getEventTime()) <
                                                           DateUtils.toTimestamp(DateUtils.offset(finalBegin, ChronoUnit.HOURS, 1)))
                    .count();
            vos.add(PatrolListStatisticsVO.builder()
                            .key(DateUtils.format(begin, "HH:mm"))
                            .value(count)
                            .build());
            begin = DateUtils.offset(begin, ChronoUnit.HOURS, 1);
        }
        return vos;
    }

    @Override
    public List<PatrolListStatisticsVO> PatrolPersonLocation(StatisticsParam param) {
        List<EventRecord> records = eventRecordService.getPatrolRecord(param);
        List<PatrolListStatisticsVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(records)) {
            Map<String, Long> collect = records.stream().collect(Collectors.groupingBy(EventRecord::getPersonName, Collectors.counting()));
            collect.forEach((key, value) -> {
                vos.add(PatrolListStatisticsVO.builder().key(key).value(value).build());
            });
        }
        List<PatrolListStatisticsVO> sortList = vos.stream().sorted(Comparator.comparing(PatrolListStatisticsVO::getValue).reversed())
                .collect(Collectors.toList());
        return sortList.size() > 10 ? sortList.subList(0, 10) : sortList;
    }

    @Override
    public List<PatrolPersonLocationStatisticsVO> PatrolPersonLocationNew(StatisticsParam param) {
        List<EventRecord> records = eventRecordService.getPatrolRecord(param);
        List<PatrolPersonLocationStatisticsVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(records)) {
            Map<String, List<EventRecord>> collect = records.stream().collect(Collectors.groupingBy(EventRecord::getPersonName));
            collect.forEach((key, value) -> {
                List<EventRecord> recordList = value.stream().sorted(Comparator.comparing(EventRecord::getEventTime).reversed())
                        .toList();
                vos.add(PatrolPersonLocationStatisticsVO.builder()
                                .location(recordList.getFirst().getDoorName())
                                .datetime(DateUtils.format(recordList.getFirst().getEventTime()))
                                .personName(key)
                                .build());
            });
        }
        return vos;
    }

    @Override
    public List<PatrolListStatisticsVO> PatrolLocationTop(StatisticsParam param) {
        List<EventRecord> records = eventRecordService.getPatrolRecord(param);
        List<PatrolListStatisticsVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(records)) {
            Map<String, Long> collect = records.stream().collect(Collectors.groupingBy(EventRecord::getDoorName, Collectors.counting()));
            collect.forEach((key, value) -> {
                vos.add(PatrolListStatisticsVO.builder().key(key).value(value).build());
            });
        }
        List<PatrolListStatisticsVO> sortList = vos.stream().sorted(Comparator.comparing(PatrolListStatisticsVO::getValue).reversed())
                .collect(Collectors.toList());
        return sortList.size() > 10 ? sortList.subList(0, 10) : sortList;
    }

    @Override
    public List<PatrolListStatisticsVO> PatrolLocationTrend(StatisticsParam param) {
        List<EventRecord> records = eventRecordService.getPatrolRecord(param);
        List<PatrolListStatisticsVO> vos = new ArrayList<>();
        switch (param.getType()) {
            case TimeDimensionConstant.WEEK: {
                vos = buildWeekTrend(records);
                break;
            }
            case TimeDimensionConstant.MONTH: {
                vos = buildMonthTrend(records);
                break;
            }
            case TimeDimensionConstant.YEAR:
                vos = buildYearTrend(records);
                break;
        }
        return vos;
    }

    private List<PatrolListStatisticsVO> buildYearTrend(List<EventRecord> records) {
        LocalDateTime now = DateUtils.now();
        LocalDateTime startDate = DateUtils.beginOfYear(now);
        int month = now.getMonthValue();
        List<PatrolListStatisticsVO> vos = new ArrayList<>();
        for (int i = 0; i < month + 1; i++) {
            LocalDateTime finalStartDate = startDate;
            long count = records.stream()
                    .filter(record -> DateUtils.toTimestamp(record.getEventTime()) > DateUtils.toTimestamp(finalStartDate)
                                      && DateUtils.toTimestamp(record.getEventTime()) <
                                         DateUtils.toTimestamp(DateUtils.offsetMonth(finalStartDate, 1))).count();
            vos.add(PatrolListStatisticsVO.builder()
                            .key(DateUtils.format(startDate, "yyyy-MM"))
                            .value(count)
                            .build());
            startDate = DateUtils.offset(startDate, ChronoUnit.MONTHS, 1);
        }
        return vos;
    }

    private List<PatrolListStatisticsVO> buildMonthTrend(List<EventRecord> records) {
        LocalDateTime now = DateUtils.now();
        LocalDateTime startDate = DateUtils.beginOfMonth(now);
        int dayOfMonth = now.getDayOfMonth();
        List<PatrolListStatisticsVO> vos = new ArrayList<>();
        for (int i = 0; i < dayOfMonth; i++) {
            startDate = countLocationNum(startDate, records, vos);
        }
        return vos;
    }

    private LocalDateTime countLocationNum(LocalDateTime startDate, List<EventRecord> records, List<PatrolListStatisticsVO> vos) {
        long count = records.stream().filter(record -> DateUtils.toTimestamp(record.getEventTime()) > DateUtils.toTimestamp(startDate)
                                                       && DateUtils.toTimestamp(record.getEventTime()) <
                                                          DateUtils.toTimestamp(DateUtils.offsetDay(startDate, 1)))
                .count();
        vos.add(PatrolListStatisticsVO.builder()
                        .key(DateUtils.format(startDate, "MM-dd"))
                        .value(count)
                        .build());
        return DateUtils.offsetDay(startDate, 1);
    }

    private List<PatrolListStatisticsVO> buildWeekTrend(List<EventRecord> records) {
        LocalDateTime now = DateUtils.now();
        LocalDateTime startDate = DateUtils.beginOfWeek(now);
        int dayOfWeek = now.getDayOfWeek().getValue();
        List<PatrolListStatisticsVO> vos = new ArrayList<>();
        for (int i = 0; i < dayOfWeek; i++) {
            startDate = countLocationNum(startDate, records, vos);
        }
        return vos;
    }
}
