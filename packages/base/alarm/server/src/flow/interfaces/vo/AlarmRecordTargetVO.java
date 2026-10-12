package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 报警发送人返回参数
 *
 * @author lanchun
 * @date 2022/6/8 16:48
 */
@Data
public class AlarmRecordTargetVO {

    private Long id;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;
}
