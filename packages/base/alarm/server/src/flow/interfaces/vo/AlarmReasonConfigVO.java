package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.Data;

@Data
public class AlarmReasonConfigVO {

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 是否为初始化的
     */
    private Boolean initialized;

    /**
     * 原因
     */
    private String reason;

    /**
     * 原因备注
     */
    private String remark;
}
