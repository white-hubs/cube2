package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.param.PersonRollCallListParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.param.PersonRollCallTaskPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.service.PersonRollCallTaskService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo.PersonRollCallAbnormalRankVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo.PersonRollCallAttendedTrendVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo.PersonRollCallTaskPageVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo.PersonRollCallTaskVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo.PersonRollCallTodayCountVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.subsystem.requester.sdk.client.PersonRollCallClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 人员点名任务记录
 */
@Slf4j
@RestController
@RequestMapping("/person/roll/call/task")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PersonRollCallTaskController {
    private final PersonRollCallTaskService personRollCallTaskService;
    private final PersonRollCallClient personRollCallClient;

    /**
     * 人员点名任务分页
     */
    @GetMapping("/page")
    public Pagination<PersonRollCallTaskPageVO> getPage(PersonRollCallTaskPageParam param) {
        return personRollCallTaskService.getPage(param);
    }

    /**
     * 获取 人员点名类别枚举
     */
    @GetMapping("/call-type")
    public List<Map<String, Object>> getCallTypeEnum() {
        return personRollCallClient.getCallTypeEnum();
    }

    /**
     * 今日人员清点 展示今天实到、应到人数
     */
    @GetMapping("/today/count")
    public PersonRollCallTodayCountVO getTodayCount(PersonRollCallListParam param) {
        var now = LocalDateTime.now();
        //点名开始时间在今日内的所有记录
        param.setCallTimePeriod(new Period(DateUtils.beginOfDay(now), DateUtils.endOfDay(now)));
        List<PersonRollCallTaskVO> list = personRollCallTaskService.getList(param);
        //应到=已到+未到+特殊
        // 初始化统计变量
        Long attendedCount = 0L; // 已点到人数
        Long unattendedCount = 0L; // 未点到人数
        Long specialCount = 0L; // 特殊人数

        // 遍历点名任务列表，统计已点到、未点到和特殊情况人数
        for (PersonRollCallTaskVO task : list) {
            attendedCount += task.getAttendedCount();
            unattendedCount += task.getUnattendedCount();
            specialCount += task.getSpecialCount();
        }

        // 计算应到人数
        Long totalExpected = attendedCount + unattendedCount + specialCount;
        PersonRollCallTodayCountVO vo = new PersonRollCallTodayCountVO();
        vo.setExpectedCount(totalExpected);
        vo.setUnattendedCount(unattendedCount);
        return vo;
    }

    /**
     * 今日异常监区排行榜
     */
    @GetMapping("/abnormal/rank")
    public List<PersonRollCallAbnormalRankVO> getAbnormalRank(PersonRollCallListParam param) {
        var now = LocalDateTime.now();
        // 设置查询参数的时间范围为今日
        param.setCallTimePeriod(new Period(DateUtils.beginOfDay(now), DateUtils.endOfDay(now)));

        // 获取今日内所有点名任务记录
        List<PersonRollCallTaskVO> tasks = personRollCallTaskService.getList(param);

        // 使用Map来统计每个监区的相关人数
        Map<String, Map<String, Long>> locationStatsMap = new HashMap<>();

        for (PersonRollCallTaskVO task : tasks) {
            // 如果map中没有该监区的信息，则初始化一个空的统计数据map
            if (!locationStatsMap.containsKey(task.getLocationName())) {
                locationStatsMap.put(task.getLocationName(), new HashMap<>());
                locationStatsMap.get(task.getLocationName()).put("expectedCount", 0L);
                locationStatsMap.get(task.getLocationName()).put("attendedCount", 0L);
                locationStatsMap.get(task.getLocationName()).put("unattendedCount", 0L);
                locationStatsMap.get(task.getLocationName()).put("specialCount", 0L);
            }

            // 计算应到人数
            long expectedCount = task.getAttendedCount() + task.getUnattendedCount() + task.getSpecialCount();
            // 更新该监区的统计数据
            locationStatsMap.get(task.getLocationName()).put("expectedCount",
                                                             locationStatsMap.get(task.getLocationName()).get("expectedCount") +
                                                             expectedCount);
            locationStatsMap.get(task.getLocationName()).put("attendedCount",
                                                             locationStatsMap.get(task.getLocationName()).get("attendedCount") +
                                                             task.getAttendedCount());
            locationStatsMap.get(task.getLocationName()).put("unattendedCount",
                                                             locationStatsMap.get(task.getLocationName()).get("unattendedCount") +
                                                             task.getUnattendedCount());
            locationStatsMap.get(task.getLocationName()).put("specialCount",
                                                             locationStatsMap.get(task.getLocationName()).get("specialCount") +
                                                             task.getSpecialCount());
        }

        // 将统计结果转换为PersonRollCallAbnormalRankVO列表
        return locationStatsMap.entrySet().stream()
                .map(entry -> {
                    Map<String, Long> stats = entry.getValue();
                    return PersonRollCallAbnormalRankVO.builder()
                            .locationName(entry.getKey())
                            .expectedCount(stats.get("expectedCount"))
                            .attendedCount(stats.get("attendedCount"))
                            .unattendedCount(stats.get("unattendedCount"))
                            .specialCount(stats.get("specialCount"))
                            .build();
                })
                .sorted(Comparator.comparingLong(PersonRollCallAbnormalRankVO::getUnattendedCount).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 实到人数趋势图
     */
    @GetMapping("/attended/trend")
    public List<PersonRollCallAttendedTrendVO> getAttendedTrend(PersonRollCallListParam param) {
        // 根据参数类型设置查询参数的时间范围
        var now = LocalDateTime.now();
        switch (param.getType()) {
            case "THIS_WEEK":
                param.setCallTimePeriod(new Period(DateUtils.beginOfWeek(now), DateUtils.endOfWeek(now)));
                break;
            case "THIS_MONTH":
                param.setCallTimePeriod(new Period(DateUtils.beginOfMonth(now), DateUtils.endOfMonth(now)));
                break;
            case "THIS_YEAR":
                param.setCallTimePeriod(new Period(DateUtils.beginOfYear(now), DateUtils.endOfYear(now)));
                break;
            case "TODAY":
            default:
                param.setCallTimePeriod(new Period(DateUtils.beginOfDay(now), DateUtils.endOfDay(now)));
        }

        // 获取指定时间范围内的所有点名任务记录
        List<PersonRollCallTaskVO> tasks = personRollCallTaskService.getList(param);

        // 使用Map来统计每个日期/月份的实到人数
        Map<String, Long> dateAttendedCountMap = new HashMap<>();

        for (PersonRollCallTaskVO task : tasks) {
            // 将日期格式化为 MM-dd 或 MM
            String formattedDate = formatTaskDate(task.getCallStartTime(), param.getType());

            // 如果map中没有该日期/月份的信息，则初始化其实到人数为0
            dateAttendedCountMap.putIfAbsent(formattedDate, 0L);

            // 更新该日期/月份的实到人数
            dateAttendedCountMap.put(
                    formattedDate,
                    dateAttendedCountMap.get(formattedDate) + task.getAttendedCount()
                                    );
        }

        // 生成完整的日期/月份列表
        List<String> completeDates = generateCompleteDates(param.getType());

        // 将完整的日期/月份列表与统计数据合并
        List<PersonRollCallAttendedTrendVO> trendList = completeDates.stream()
                .map(date -> {
                    long attendedCount = dateAttendedCountMap.getOrDefault(date, 0L);
                    return PersonRollCallAttendedTrendVO.builder()
                            .date(date)
                            .attendedCount(attendedCount)
                            .build();
                })
                .collect(Collectors.toList());

        // 按日期/月份排序
        return trendList.stream()
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .collect(Collectors.toList());
    }

    private String formatTaskDate(LocalDateTime callStartTime, String type) {
        LocalDate date = callStartTime.toLocalDate();
        switch (type) {
            case "TODAY":
            case "THIS_WEEK":
            case "THIS_MONTH":
                return date.format(DateTimeFormatter.ofPattern("MM-dd"));
            case "THIS_YEAR":
                return date.format(DateTimeFormatter.ofPattern("MM"));
            default:
                return date.format(DateTimeFormatter.ofPattern("MM-dd"));
        }
    }

    private List<String> generateCompleteDates(String type) {
        List<String> dates = new ArrayList<>();
        LocalDate today = LocalDate.now();

        switch (type) {
            case "TODAY":
                // 今天的日期
                dates.add(today.format(DateTimeFormatter.ofPattern("MM-dd")));
                break;
            case "THIS_WEEK":
                // 当前周的日期
                LocalDate weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                LocalDate weekEnd = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

                for (LocalDate date = weekStart; !date.isAfter(weekEnd); date = date.plusDays(1)) {
                    dates.add(date.format(DateTimeFormatter.ofPattern("MM-dd")));
                }
                break;
            case "THIS_MONTH":
                // 当前月的日期
                LocalDate monthStart = today.with(TemporalAdjusters.firstDayOfMonth());
                LocalDate monthEnd = today.with(TemporalAdjusters.lastDayOfMonth());

                for (LocalDate date = monthStart; !date.isAfter(monthEnd); date = date.plusDays(1)) {
                    dates.add(date.format(DateTimeFormatter.ofPattern("MM-dd")));
                }
                break;
            case "THIS_YEAR":
                // 当前年的月份
                for (int month = 1; month <= 12; month++) {
                    dates.add(String.format("%02d", month));
                }
                break;
            default:
                // 默认只返回今天的日期
                dates.add(today.format(DateTimeFormatter.ofPattern("MM-dd")));
        }

        return dates;
    }
}
