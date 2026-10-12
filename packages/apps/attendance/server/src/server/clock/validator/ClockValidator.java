package com.guhecloud.cube.srv.gen.attendance.management.server.clock.validator;

import com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.command.SubmitClockCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.ClassesMarkEnum;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClockValidator {

    /**
     * 验证休息班次不能打卡
     */
    public void verifyIsRest(SubmitClockCommand clockCommand) {
        Assert.isNotAcceptable(ClassesMarkEnum.All_DAY_REST.name().equals(clockCommand.getMark()), "当前为休息班次不能打卡");
    }

    /**
     * 验证上下班打卡时间范围
     */
    public void verifyDateRanges(SubmitClockCommand clockCommand) {
        LocalDateTime beforeStartRealTime = clockCommand.getBeforeStartRealTime();
        LocalDateTime afterStartRealTime = clockCommand.getAfterStartRealTime();
        LocalDateTime beforeEndRealTime = clockCommand.getBeforeEndRealTime();
        LocalDateTime afterEndRealTime = clockCommand.getAfterEndRealTime();
        LocalDateTime startWorkClockTime = clockCommand.getStartWorkClockTime();
        LocalDateTime endWorkClockTime = clockCommand.getEndWorkClockTime();
        if (null != startWorkClockTime) {
            Assert.isNotAcceptable((beforeStartRealTime != null &&
                                    DateUtils.compare(startWorkClockTime, beforeStartRealTime) < 0), "打卡失败，未到上班打卡时间范围");
            Assert.isNotAcceptable((afterStartRealTime != null && DateUtils.compare(startWorkClockTime, afterStartRealTime) >
                                                                  0), "打卡失败，已经超过上班打卡时间范围");
        } else if (null != endWorkClockTime) {
            Assert.isNotAcceptable((beforeEndRealTime != null &&
                                    DateUtils.compare(endWorkClockTime, beforeEndRealTime) < 0), "打卡失败，未到下班打卡时间范围");
            Assert.isNotAcceptable((afterEndRealTime != null &&
                                    DateUtils.compare(endWorkClockTime, afterEndRealTime) > 0), "打卡失败，已经超过下班打卡时间范围");
        }
    }

}
