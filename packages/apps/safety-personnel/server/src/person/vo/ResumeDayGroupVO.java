package com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 履职记录日期分组响应。
 */
@Data
public class ResumeDayGroupVO {

    /**
     * 分组日期。
     */
    private LocalDate date;

    /**
     * 巡查记录数量。
     */
    private Long patrolCount;

    /**
     * 会议记录数量。
     */
    private Long meetingCount;

    /**
     * 培训记录数量。
     */
    private Long trainingCount;

    /**
     * 审批记录数量。
     */
    private Long approvalCount;

    /**
     * 告警记录数量。
     */
    private Long alarmCount;

    /**
     * 其它任务数量。
     */
    private Long otherCount;

    /**
     * 当天履职明细列表。
     */
    private List<ResumeItemVO> records;

    public static ResumeDayGroupVO of(LocalDate date, List<ResumeItemVO> items) {
        Map<String, Long> countMap = items.stream().collect(Collectors.groupingBy(ResumeItemVO::getType, Collectors.counting()));
        ResumeDayGroupVO vo = new ResumeDayGroupVO();
        vo.setDate(date);
        vo.setPatrolCount(countMap.getOrDefault("patrol", 0L));
        vo.setMeetingCount(countMap.getOrDefault("meeting", 0L));
        vo.setTrainingCount(countMap.getOrDefault("training", 0L));
        vo.setApprovalCount(countMap.getOrDefault("approval", 0L));
        vo.setAlarmCount(countMap.getOrDefault("alarm", 0L));
        vo.setOtherCount(countMap.getOrDefault("other", 0L));
        vo.setRecords(items);
        return vo;
    }
}
