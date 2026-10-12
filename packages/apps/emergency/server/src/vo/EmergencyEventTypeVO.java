package com.guhecloud.cube.biz.emergency.server.vo;

import lombok.Data;

@Data
public class EmergencyEventTypeVO {

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否初始化的
     */
    private Boolean initialized;

    /**
     * 备注说明
     */
    private String remark;
}
