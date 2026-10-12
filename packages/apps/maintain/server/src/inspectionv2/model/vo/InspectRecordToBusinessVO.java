package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InspectRecordToBusinessVO {
    private Long id;

    /**
     * 巡检记录ID
     */
    private Long recordId;

    /**
     * 目标对象类别
     */
    private String targetCategory;

    /**
     * 目标对象ID
     */
    private Long targetId;

    /**
     * 目标对象名称
     */
    private String targetName;

    /**
     * 业务分类 工单（ticket）
     */
    private String businessCategory;

    /**
     * 业务ID
     */
    private String businessId;

    /**
     * 业务是否存在, 可用于是否能跳转到详情
     */
    private Boolean businessExist;

    /**
     * 业务对象名称
     */
    private String businessName;

    /**
     * 业务状态
     */
    private String businessStatus;

    /**
     * 业务时间
     */
    private LocalDateTime businessTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
