package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * 记录提交时，可以复制结果的对象
 */
@Data
public class ReplicableTargetVO {
    /**
     * 巡检记录id
     */
    private Long recordId;
    /**
     * 巡检对象类型：device设备 stall挡位 等
     */
    private String targetCategory;
    /**
     * 巡检对象id
     */
    private Long targetId;
    /**
     * 巡检对象名
     */
    private String targetName;
    /**
     * 巡检区域id
     */
    private String areaId;
    /**
     * 巡检区域名
     */
    private String areaName;
}
