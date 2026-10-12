package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Getter;

@Getter
public class InspectRecordToBusinessCommandModel {

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
     * 业务对象名称
     */
    private String businessName;

}
