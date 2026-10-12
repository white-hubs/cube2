package com.guhecloud.cube.srv.gen.securitymanagement.server.lift.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 五方通话记录返回对象
 */
@Data
public class FiveCallLogVo {
    private Long id;
    /**
     * 来电人
     */
    private String caller;
    /**
     * 来电时间
     */
    private LocalDateTime callTime;
    /**
     * 电梯编号
     */
    private String liftNumber;
    /**
     * 所在区域id
     */
    private Long areaId;
    /**
     * 所在区域名称
     */
    private String areaName;
    /**
     * 区域序列号
     */
    private String sequence;
    /**
     * 通话内容
     */
    private String callContent;
    /**
     * 处理情况
     */
    private String handlingSituation;
}
