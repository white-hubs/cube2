package com.guhecloud.cube.srv.gen.safety.management.dto.vo;

import lombok.Data;

@Data
public class AlarmTypeVO {

    private Long id;

    /**
     * 报警分类编号
     */
    private String alarmTypeCode;

    /**
     * 报警分类名称
     */
    private String alarmTypeName;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 备注说明
     */
    private String remark;

}
