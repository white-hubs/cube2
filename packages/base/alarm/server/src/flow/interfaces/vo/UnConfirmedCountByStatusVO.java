package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnConfirmedCountByStatusVO {
    /**
     * 待接收
     */
    private Integer alarmUnConfirmed;
    /**
     * 处理中
     */
    private Integer alarmProgressConfirmed;
    /**
     * 已挂起
     */
    private Integer alarmSuspend;
    /**
     * 已处理
     */
    private Integer alarmHandler;
}
