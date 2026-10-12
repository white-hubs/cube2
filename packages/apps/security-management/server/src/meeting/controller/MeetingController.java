package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingListParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.service.MeetingService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingAttendedTrendVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingPageVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingPeriodDistribution;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 会见管理
 */
@Slf4j
@RestController
@RequestMapping("/meeting")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeetingController {
    private final MeetingService meetingService;

    /**
     * 会见分页
     */
    @GetMapping("/page")
    public Pagination<MeetingPageVO> getPage(MeetingPageParam param) {
        return meetingService.getPage(param);
    }

    /**
     * 会见详情
     */
    @GetMapping("/detail")
    public MeetingVO selectById(@RequestParam(value = "id") String id) {
        return meetingService.selectById(Long.parseLong(id));
    }

    /**
     * 会见趋势图 展示每天进行的会见有多少记录数
     */
    @GetMapping("/attended/trend")
    public List<MeetingAttendedTrendVO> getAttendedTrend(MeetingListParam param) {
        // 根据参数类型设置查询参数的时间范围
        var now = LocalDateTime.now();
        switch (param.getType()) {
            case "TODAY":
                param.setPeriod(new Period(DateUtils.beginOfDay(now), DateUtils.endOfDay(now)));
                break;
            case "THIS_WEEK":
                param.setPeriod(new Period(DateUtils.beginOfWeek(now), DateUtils.endOfWeek(now)));
                break;
            case "THIS_MONTH":
                param.setPeriod(new Period(DateUtils.beginOfMonth(now), DateUtils.endOfMonth(now)));
                break;
            case "THIS_YEAR":
                param.setPeriod(new Period(DateUtils.beginOfYear(now), DateUtils.endOfYear(now)));
                break;
            default:
                param.setPeriod(new Period(DateUtils.beginOfDay(now), DateUtils.endOfDay(now)));
        }

        // 获取时间范围内所有会见时间
        List<MeetingVO> list = meetingService.getList(param);

        // 使用Map来统计每个时间单位的预约记录次数
        Map<String, Long> timeUnitRecordCountMap = new HashMap<>();

        for (MeetingVO meetingVO : list) {
            String format = switch (param.getType()) {
                // 格式化为小时 HH
                case "TODAY" -> "HH";
                // 格式化为 MM-dd
                case "THIS_WEEK" -> "MM-dd";
                // 格式化为 MM-dd
                case "THIS_MONTH" -> "MM-dd";
                // 格式化为月份 MM
                case "THIS_YEAR" -> "MM";
                default -> "MM-dd";
            };
            String key = DateUtils.format(meetingVO.getMeetingStartTime(), format);
            // 如果map中没有该时间单位的信息，则初始化记录数为0
            timeUnitRecordCountMap.putIfAbsent(key, 0L);

            // 更新该时间单位的记录数
            timeUnitRecordCountMap.put(key, timeUnitRecordCountMap.get(key) + 1);
        }

        // 生成完整的日期列表
        List<String> completeDates = generateCompleteDates(param.getType());

        // 将完整的日期列表与统计数据合并
        List<MeetingAttendedTrendVO> trendList = completeDates.stream()
                .map(date -> {
                    long recordCount = timeUnitRecordCountMap.getOrDefault(date, 0L);
                    return MeetingAttendedTrendVO.builder()
                            .date(date)
                            .recordCount(recordCount)
                            .build();
                })
                .collect(Collectors.toList());

        // 按时间单位排序
        return trendList.stream()
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .collect(Collectors.toList());
    }

    private List<String> generateCompleteDates(String type) {
        List<String> dates = new ArrayList<>();
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("MM-dd");
        DateTimeFormatter sdfMonth = DateTimeFormatter.ofPattern("MM");
        LocalDate today = LocalDate.now();

        switch (type) {
            case "TODAY":
                // 只有今天
                dates.add(sdf.format(LocalDate.now()));
                break;
            case "THIS_WEEK":
                // 当前周的所有日期
                LocalDate weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                LocalDate weekEnd = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

                while (!weekStart.isAfter(weekEnd)) {
                    dates.add(sdf.format(weekStart));
                    weekStart = weekStart.plusDays(1);
                }
                break;
            case "THIS_MONTH":
                // 当前月的所有日期
                LocalDate monthStart = today.with(TemporalAdjusters.firstDayOfMonth());
                LocalDate monthEnd = today.with(TemporalAdjusters.lastDayOfMonth());

                while (!monthStart.isAfter(monthEnd)) {
                    dates.add(sdf.format(monthStart));
                    monthStart = monthStart.plusDays(1);
                }
                break;
            case "THIS_YEAR":
                // 当前年的所有月份
                for (int month = 1; month <= 12; month++) {
                    dates.add(sdfMonth.format(LocalDate.of(today.getYear(), month, 1)));
                }
                break;
            default:
                // 默认只返回今天
                dates.add(sdf.format(LocalDate.now()));
        }

        return dates;
    }

    /**
     * 会见时段分布 以时间纬度来统计每个小时的会见情况
     */
    @GetMapping("/period/distribution")
    public List<MeetingPeriodDistribution> getPeriodDistribution(MeetingListParam param) {
        // 根据参数类型设置查询参数的时间范围
        var now = LocalDateTime.now();
        switch (param.getType()) {
            case "TODAY":
                param.setPeriod(new Period(DateUtils.beginOfDay(now), DateUtils.endOfDay(now)));
                break;
            case "THIS_WEEK":
                param.setPeriod(new Period(DateUtils.beginOfWeek(now), DateUtils.endOfWeek(now)));
                break;
            case "THIS_MONTH":
                param.setPeriod(new Period(DateUtils.beginOfMonth(now), DateUtils.endOfMonth(now)));
                break;
            case "THIS_YEAR":
                param.setPeriod(new Period(DateUtils.beginOfYear(now), DateUtils.endOfYear(now)));
                break;
            default:
                param.setPeriod(new Period(DateUtils.beginOfDay(now), DateUtils.endOfDay(now)));
        }

        // 获取时间范围内所有会见时间
        List<MeetingVO> list = meetingService.getList(param);

        // 使用Map来统计每个小时的会议记录数
        Map<String, Long> hourRecordCountMap = new HashMap<>();

        for (MeetingVO meetingVO : list) {
            LocalDateTime meetingTime = meetingVO.getMeetingStartTime();
            String key = meetingTime.format(DateTimeFormatter.ofPattern("HH:mm")).substring(0, 3) + "00";

            // 如果map中没有该小时的信息，则初始化记录数为0
            hourRecordCountMap.putIfAbsent(key, 0L);

            // 更新该小时的记录数
            hourRecordCountMap.put(key, hourRecordCountMap.get(key) + 1);
        }

        // 生成完整的小时列表
        List<String> completeHours = generateCompleteHours();

        // 将完整的小时列表与统计数据合并
        List<MeetingPeriodDistribution> distributionList = completeHours.stream()
                .map(hour -> {
                    long recordCount = hourRecordCountMap.getOrDefault(hour, 0L);
                    return MeetingPeriodDistribution.builder()
                            .date(hour)
                            .recordCount(recordCount)
                            .build();
                })
                .collect(Collectors.toList());

        // 按小时排序
        return distributionList.stream()
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .collect(Collectors.toList());
    }

    private List<String> generateCompleteHours() {
        List<String> hours = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            hours.add(String.format("%02d:00", hour));
        }
        return hours;
    }

}
