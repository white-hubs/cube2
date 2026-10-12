package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingApplyListParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param.MeetingApplyPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.service.MeetingApplyService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingApplyAppointmentTrendVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingApplyPageVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.vo.MeetingApplyVO;
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
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 会见管理 INTERVIEW
 */
@Slf4j
@RestController
@RequestMapping("/meeting/apply")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeetingApplyController {
    private final MeetingApplyService meetingApplyService;

    /**
     * 会见申请分页
     */
    @GetMapping("/page")
    public Pagination<MeetingApplyPageVO> getPage(MeetingApplyPageParam param) {
        return meetingApplyService.getPage(param);
    }

    /**
     * 会见申请详情
     */
    @GetMapping("/detail")
    public MeetingApplyVO selectById(@RequestParam(value = "id") String id) {
        return meetingApplyService.selectById(Long.parseLong(id));
    }

    /**
     * 会见预约时间分布 展示每天预约会见的有多少记录数
     */
    @GetMapping("/appointment/trend")
    public List<MeetingApplyAppointmentTrendVO> getAppointmentTrend(MeetingApplyListParam param) {
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

        // 获取时间范围内所有会见预约时间
        List<MeetingApplyVO> list = meetingApplyService.getList(param);

        // 使用Map来统计每个日期的预约记录次数 (补充每周、每月的点数)
        Map<String, Long> dateAttendedCountMap = new HashMap<>();

        for (MeetingApplyVO meetingApplyVO : list) {
            // 将日期格式化为 MM-DD
            String formattedDate = DateUtils.format(meetingApplyVO.getMeetingStartTime(), "MM-dd");

            // 如果map中没有该日期的信息，则初始化当日预约记录数为0
            dateAttendedCountMap.putIfAbsent(formattedDate, 0L);

            // 更新该日期的记录数
            dateAttendedCountMap.put(formattedDate, dateAttendedCountMap.get(formattedDate) + 1);
        }

        // 生成完整的日期列表
        List<String> completeDates = generateCompleteDates(param.getType());

        // 将完整的日期列表与统计数据合并
        List<MeetingApplyAppointmentTrendVO> trendList = completeDates.stream()
                .map(date -> {
                    long recordCount = dateAttendedCountMap.getOrDefault(date, 0L);
                    return MeetingApplyAppointmentTrendVO.builder()
                            .date(date)
                            .recordCount(recordCount)
                            .build();
                })
                .collect(Collectors.toList());

        // 按日期排序
        return trendList.stream()
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .collect(Collectors.toList());
    }

    private List<String> generateCompleteDates(String type) {
        List<String> dates = new ArrayList<>();
        var fmt = "MM-dd";
        LocalDate today = LocalDate.now();

        switch (type) {
            case "TODAY":
                // 只有今天
                dates.add(DateUtils.format(LocalDateTime.now(), fmt));
                break;
            case "THIS_WEEK":
                // 当前周的所有日期
                LocalDate weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                LocalDate weekEnd = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

                while (!weekStart.isAfter(weekEnd)) {
                    dates.add(DateUtils.format(weekStart.atStartOfDay(), fmt));
                    weekStart = weekStart.plusDays(1);
                }
                break;
            case "THIS_MONTH":
                // 当前月的所有日期
                LocalDate monthStart = today.with(TemporalAdjusters.firstDayOfMonth());
                LocalDate monthEnd = today.with(TemporalAdjusters.lastDayOfMonth());

                while (!monthStart.isAfter(monthEnd)) {
                    dates.add(DateUtils.format(monthStart.atStartOfDay(), fmt));
                    monthStart = monthStart.plusDays(1);
                }
                break;
            case "THIS_YEAR":
                // 当前年的所有月份
                for (int month = 1; month <= 12; month++) {
                    dates.add(String.format("%02d-01", month));
                }
                break;
            default:
                // 默认只返回今天
                dates.add(DateUtils.format(LocalDateTime.now(), fmt));
        }

        return dates;
    }
}
