package com.guhecloud.cube.biz.emergency.server.time.core.util;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.time.dto.EmergencyTimeInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleTimeInfo;
import com.guhecloud.cube.biz.emergency.server.time.domain.po.EmergencyRuleTimeJobPO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.job.jobtask.cron.CronExpression;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class RuleTimeParser {

    public static final List<Integer> ALL_WEEKS = new ArrayList<>(7);

    public static final List<Integer> ALL_MONTHS = new ArrayList<>(12);

    static {
        ALL_WEEKS.add(1);
        ALL_WEEKS.add(2);
        ALL_WEEKS.add(3);
        ALL_WEEKS.add(4);
        ALL_WEEKS.add(5);
        ALL_WEEKS.add(6);
        ALL_WEEKS.add(7);

        ALL_MONTHS.add(1);
        ALL_MONTHS.add(2);
        ALL_MONTHS.add(3);
        ALL_MONTHS.add(4);
        ALL_MONTHS.add(5);
        ALL_MONTHS.add(6);
        ALL_MONTHS.add(7);
        ALL_MONTHS.add(8);
        ALL_MONTHS.add(9);
        ALL_MONTHS.add(10);
        ALL_MONTHS.add(11);
        ALL_MONTHS.add(12);
    }

    public static List<EmergencyRuleTimeJobPO> buildMonthJob(Long ruleId, Long groupId, EmergencyRuleTimeInfo emergencyRuleTimeInfo) {
        var selectMonths = JsonUtils.deserialize(emergencyRuleTimeInfo.getTimeConditions(), new TypeReference<List<Integer>>() {});
        List<EmergencyRuleTimeJobPO> emergencyRuleTimeJobPOS = new ArrayList<>();
        if (selectMonths != null) {
            try {
                var startCronExpression = CronExpression.getIntervalYearMonthCron(selectMonths);
                var nextValidTimeAfter = new CronExpression(startCronExpression).getNextValidTimeAfter(DateUtils.now());
                var openConditions = JsonUtils.serialize(selectMonths);
                emergencyRuleTimeJobPOS.add(buildEmergencyRuleTimeJobPO(ruleId, groupId, emergencyRuleTimeInfo, openConditions, openConditions, "open", startCronExpression, nextValidTimeAfter));
                var closeMonths = ALL_MONTHS.stream().filter(month -> !selectMonths.contains(month)).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(closeMonths)) {
                    var closeConditions = JsonUtils.serialize(closeMonths);
                    var closeCronExpression = CronExpression.getIntervalYearMonthCron(closeMonths);
                    var closeNextValidTimeAfter = new CronExpression(closeCronExpression).getNextValidTimeAfter(DateUtils.now());
                    emergencyRuleTimeJobPOS.add(buildEmergencyRuleTimeJobPO(ruleId, groupId, emergencyRuleTimeInfo, closeConditions, closeConditions, "close", closeCronExpression, closeNextValidTimeAfter));
                }
            } catch (ParseException e) {
                log.error("月生成cron表达式失败,失败原因", e);
            }
        }
        return emergencyRuleTimeJobPOS;
    }

    public static List<EmergencyRuleTimeJobPO> buildWeekJob(Long ruleId, Long groupId, EmergencyRuleTimeInfo emergencyRuleTimeInfo) {
        var selectWeeks = JsonUtils.deserialize(emergencyRuleTimeInfo.getTimeConditions(), new TypeReference<List<Integer>>() {});
        List<EmergencyRuleTimeJobPO> emergencyRuleTimeJobPOS = new ArrayList<>();
        if (selectWeeks != null) {
            try {
                var startCronExpression = CronExpression.getIntervalWeeksCron(selectWeeks);
                var nextValidTimeAfter = new CronExpression(startCronExpression).getNextValidTimeAfter(DateUtils.now());
                var openConditions = JsonUtils.serialize(selectWeeks);
                emergencyRuleTimeJobPOS.add(buildEmergencyRuleTimeJobPO(ruleId, groupId, emergencyRuleTimeInfo, openConditions, openConditions, "open", startCronExpression, nextValidTimeAfter));
                var closeWeeks = ALL_WEEKS.stream().filter(week -> !selectWeeks.contains(week))
                        .collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(closeWeeks)) {
                    var closeConditions = JsonUtils.serialize(closeWeeks);
                    var closeCronExpression = CronExpression.getIntervalWeeksCron(closeWeeks);
                    var closeNextValidTimeAfter = new CronExpression(closeCronExpression).getNextValidTimeAfter(DateUtils.now());
                    emergencyRuleTimeJobPOS.add(buildEmergencyRuleTimeJobPO(ruleId, groupId, emergencyRuleTimeInfo, closeConditions, closeConditions, "close", closeCronExpression, closeNextValidTimeAfter));
                }
            } catch (ParseException e) {
                log.error("周生成cron表达式失败,失败原因", e);
            }
        }
        return emergencyRuleTimeJobPOS;
    }

    public static List<EmergencyRuleTimeJobPO> buildLocalTimeJob(Long ruleId, Long groupId, EmergencyRuleTimeInfo emergencyRuleTimeInfo) {
        var emergencyTimeInfos = JsonUtils.deserialize(emergencyRuleTimeInfo.getTimeConditions(), new TypeReference<List<EmergencyTimeInfo>>() {
        });
        List<EmergencyRuleTimeJobPO> emergencyRuleTimeJobPOS = new ArrayList<>();
        if (emergencyTimeInfos != null) {
            emergencyTimeInfos.forEach(emergencyTimeInfo -> {
                try {
                    var startCronExpression = CronExpression.getIntervalLocalTimeCron(emergencyTimeInfo.getBeginTime());
                    var nextValidTimeAfter = new CronExpression(startCronExpression).getNextValidTimeAfter(DateUtils.now());
                    emergencyRuleTimeJobPOS.add(buildEmergencyRuleTimeJobPO(ruleId, groupId, emergencyRuleTimeInfo, JsonUtils.serialize(emergencyTimeInfo), emergencyRuleTimeInfo.getTimeConditions(), "open", startCronExpression, nextValidTimeAfter));
                    var endCronExpression = CronExpression.getIntervalLocalTimeCron(emergencyTimeInfo.getEndTime());
                    var closeNextValidTimeAfter = new CronExpression(endCronExpression).getNextValidTimeAfter(DateUtils.now());
                    emergencyRuleTimeJobPOS.add(buildEmergencyRuleTimeJobPO(ruleId, groupId, emergencyRuleTimeInfo, JsonUtils.serialize(emergencyTimeInfo), emergencyRuleTimeInfo.getTimeConditions(), "close", endCronExpression, closeNextValidTimeAfter));
                } catch (ParseException e) {
                    log.error("时段生成cron表达式失败,失败原因", e);
                }
            });
        }
        return emergencyRuleTimeJobPOS;
    }

    public static EmergencyRuleTimeJobPO buildAllTimeJob(Long ruleId, Long groupId, EmergencyRuleTimeInfo emergencyRuleTimeInfo) {
        return buildEmergencyRuleTimeJobPO(ruleId, groupId, emergencyRuleTimeInfo, null, null, "open", null, null);
    }

    public static LocalDateTime getRileTimeJobEndTime(EmergencyRuleTimeJobPO emergencyRuleTimeJobPO) {
        try {
            //报警时段配置拆分成job任务
            if (EmergencyConditionTimeTypeEnum.MONTH == emergencyRuleTimeJobPO.getTimeType()) {
                var selectMonths = JsonUtils.deserialize(emergencyRuleTimeJobPO.getRuleTimeConditions(), new TypeReference<List<Integer>>() {});
                var closeMonths = ALL_MONTHS.stream().filter(month -> Objects.isNull(selectMonths) || !selectMonths.contains(month))
                        .collect(Collectors.toList());
                var closeCronExpression = CronExpression.getIntervalYearMonthCron(closeMonths);
                return new CronExpression(closeCronExpression).getNextValidTimeAfter(DateUtils.now());
            } else if (EmergencyConditionTimeTypeEnum.WEEK == emergencyRuleTimeJobPO.getTimeType()) {
                var selectWeeks = JsonUtils.deserialize(emergencyRuleTimeJobPO.getRuleTimeConditions(), new TypeReference<List<Integer>>() {});
                var closeWeeks = ALL_WEEKS.stream().filter(week -> Objects.isNull(selectWeeks) || !selectWeeks.contains(week))
                        .collect(Collectors.toList());
                var closeCronExpression = CronExpression.getIntervalWeeksCron(closeWeeks);
                return new CronExpression(closeCronExpression).getNextValidTimeAfter(DateUtils.now());
            } else if (EmergencyConditionTimeTypeEnum.TIME == emergencyRuleTimeJobPO.getTimeType()) {
                var deserialize = JsonUtils.deserialize(emergencyRuleTimeJobPO.getRuleTimeConditions(), new TypeReference<Map<String, Object>>() {});
                if (deserialize != null && deserialize.containsKey("endTime")) {
                    String endCronExpression = CronExpression.getIntervalLocalTimeCron(LocalTime.parse(deserialize.get("endTime")
                                                                                                               .toString()));
                    return new CronExpression(endCronExpression).getNextValidTimeAfter(DateUtils.now());
                }
                return null;
            }
        } catch (ParseException e) {
            log.error("时间转换失败,失败原因:", e);
        }
        return null;
    }

    public static EmergencyRuleTimeJobPO buildEmergencyRuleTimeJobPO(Long ruleId,
                                                                     Long groupId,
                                                                     EmergencyRuleTimeInfo emergencyRuleTimeInfo,
                                                                     String timeConditions,
                                                                     String ruleTimeConditions,
                                                                     String optType,
                                                                     String cronExpression,
                                                                     LocalDateTime nextValidTimeAfter
                                                                    ) {
        return EmergencyRuleTimeJobPO.builder()
                .id(IdWorker.getId())
                .emergencyRuleId(ruleId)
                .emergencyRuleConditionGroupId(groupId)
                .timeConditions(timeConditions)
                .ruleTimeConditions(ruleTimeConditions)
                .operateType(optType)
                .timeType(emergencyRuleTimeInfo.getTimeType())
                .emergencyRuleTimeId(emergencyRuleTimeInfo.getId())
                .cronExpression(cronExpression)
                .triggerNextTime(Objects.isNull(nextValidTimeAfter) ? 0L : DateUtils.toTimestamp(nextValidTimeAfter))
                .build();
    }
}
