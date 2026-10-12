package com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 履职记录明细响应。
 */
@Data
public class ResumeItemVO {

    /**
     * 来源记录ID。
     */
    private String id;

    /**
     * 履职类型，见 {@link com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.ResumeRecordTypeEnum}。
     */
    private String type;

    /**
     * 履职类型名称。
     */
    private String typeName;

    /**
     * 记录名称。
     */
    private String name;

    /**
     * 记录发生时间。
     */
    private LocalDateTime recordTime;

    /**
     * 工作中心业务类型。
     */
    private String businessType;

    /**
     * 工作中心业务类型名称。
     */
    private String businessTypeName;

    /**
     * 业务主键。
     */
    private String businessKey;

    /**
     * 前端跳转地址。
     */
    private String url;
}
