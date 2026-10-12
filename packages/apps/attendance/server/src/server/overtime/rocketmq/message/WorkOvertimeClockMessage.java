package com.guhecloud.cube.srv.gen.attendance.management.server.overtime.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 打卡消息：此消息在打下班卡后发出；
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkOvertimeClockMessage {
    /**
     * 消息主题
     */
    public static final String TOPIC = "work-clock-off";
    //班次ID
    private Long classId;
    //下班时间
    private LocalDateTime workEndTime;
    //下班打卡时间
    private LocalDateTime workEndClockTime;
    //用户ID
    private Long userId;
    //加班类型
    private String workOvertimeType;

    /**
     * 时间戳
     */
    @Builder.Default
    private final Long timestamp = System.currentTimeMillis();
}
