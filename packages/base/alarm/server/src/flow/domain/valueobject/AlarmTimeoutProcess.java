package com.guhecloud.cube.base.alarm.server.flow.domain.valueobject;

import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.DateUnitEnum;
import com.guhecloud.cube.base.alarm.server.util.DateUnitUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 报警超时
 *
 * @author lanchun
 * @date 2023/2/28 11:47
 */
@Getter
@AllArgsConstructor
public class AlarmTimeoutProcess {

    /**
     * 是否配备超时
     */
    private Boolean timeoutEquipped;

    /**
     * 超时处理时间
     */
    private LocalDateTime timeoutProcessTime;

    /**
     * 超时处理时间
     */
    private Integer timeoutProcessTimes;

    /**
     * 超时处理时间单位
     */
    private String timeoutProcessTimesUnit;

    public AlarmTimeoutProcess(Integer timeoutProcessTimes, String timeoutProcessTimesUnit) {
        this.timeoutProcessTimes = timeoutProcessTimes;
        this.timeoutProcessTimesUnit = timeoutProcessTimesUnit;
        if (Objects.nonNull(timeoutProcessTimes) && 0 < timeoutProcessTimes) {
            this.timeoutEquipped = true;
            this.timeoutProcessTime = DateUnitUtils.add(DateUtils.now(), DateUnitEnum.valueOf(timeoutProcessTimesUnit), timeoutProcessTimes);
        }
    }
}
