package com.guhecloud.cube.biz.emergency.server.time.core.plugin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.time.dto.EmergencyTimeInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import com.guhecloud.cube.biz.emergency.server.time.core.task.TimerTask;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExecuteExpirePlugin {

    public boolean reissueTask(TimerTask timerTask) {
        var now = DateUtils.now();
        if ("open".equals(timerTask.getOpenOrClose())) {
            return getOpenTriggered(timerTask, now);
        } else if ("close".equals(timerTask.getOpenOrClose())) {
            return getCloseTriggered(timerTask, now);
        }
        return false;
    }

    public boolean getOpenTriggered(TimerTask timerTask, LocalDateTime now) {
        //如果包含在执行时间内也需要执行一次(补发执行)
        if (EmergencyConditionTimeTypeEnum.MONTH == timerTask.getTimeType()) {
            var months = JsonUtils.deserialize(timerTask.getAllTimeConditions(), ArrayList.class);
            return !(CollectionUtils.isNotEmpty(months) && months.contains(now.getMonthValue()));
        } else if (EmergencyConditionTimeTypeEnum.WEEK == timerTask.getTimeType()) {
            var weeks = JsonUtils.deserialize(timerTask.getAllTimeConditions(), ArrayList.class);
            var dayOfWeek = now.getDayOfWeek().getValue();
            if (DayOfWeek.SUNDAY == now.getDayOfWeek()) {
                dayOfWeek = 1;
            } else {
                dayOfWeek = dayOfWeek + 1;
            }
            return !(CollectionUtils.isNotEmpty(weeks) && weeks.contains(dayOfWeek));
        } else if (EmergencyConditionTimeTypeEnum.TIME == timerTask.getTimeType()) {
            var emergencyTimeInfo = JsonUtils.deserialize(timerTask.getTimeConditions(), EmergencyTimeInfo.class);
            var nowTime = now.toLocalTime();
            if (emergencyTimeInfo != null) {
                return !((nowTime.isAfter(emergencyTimeInfo.getBeginTime()) || nowTime.equals(emergencyTimeInfo.getBeginTime()))
                         && nowTime.isBefore(emergencyTimeInfo.getEndTime()));
            }
            return false;
        } else {
            return EmergencyConditionTimeTypeEnum.ALL != timerTask.getTimeType();
        }
    }

    public boolean getCloseTriggered(TimerTask timerTask, LocalDateTime now) {
        //如果包含在执行时间内也需要执行一次(补发执行)
        if (EmergencyConditionTimeTypeEnum.MONTH == timerTask.getTimeType()) {
            var months = JsonUtils.deserialize(timerTask.getAllTimeConditions(), ArrayList.class);
            return !(CollectionUtils.isNotEmpty(months) && months.contains(now.getMonthValue()));
        } else if (EmergencyConditionTimeTypeEnum.WEEK == timerTask.getTimeType()) {
            var weeks = JsonUtils.deserialize(timerTask.getAllTimeConditions(), ArrayList.class);
            var dayOfWeek = now.getDayOfWeek().getValue();
            if (DayOfWeek.SUNDAY == now.getDayOfWeek()) {
                dayOfWeek = 1;
            } else {
                dayOfWeek = dayOfWeek + 1;
            }
            return !(CollectionUtils.isNotEmpty(weeks) && weeks.contains(dayOfWeek));
        } else if (EmergencyConditionTimeTypeEnum.TIME == timerTask.getTimeType()) {
            var emergencyTimeInfos = JsonUtils.deserialize(timerTask.getAllTimeConditions(), new TypeReference<List<EmergencyTimeInfo>>() {
            });
            var jobEmergencyTimeInfo = JsonUtils.deserialize(timerTask.getTimeConditions(), EmergencyTimeInfo.class);
            var nowTime = now.toLocalTime();
            return emergencyTimeInfos == null || jobEmergencyTimeInfo == null || jobEmergencyTimeInfo.getEndTime().isAfter(nowTime) ||
                   emergencyTimeInfos.stream().anyMatch(emergencyTimeInfo ->
                                                                !emergencyTimeInfo.getBeginTime().isAfter(nowTime) &&
                                                                !nowTime.isAfter(emergencyTimeInfo.getEndTime()));
        }
        return true;
    }
}
