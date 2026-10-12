package com.guhecloud.cube.deliver.project.merchant.commons.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AcreageDTO {   // 面积类型

    /**
     * 建筑面积
     */
    private float buildingArea;

    /**
     * 占地面积
     */
    private float coverArea;

    /**
     * 租赁面积
     */
    private float leaseArea;

    /**
     * 租赁实际面积
     */
    private float leaseRealArea;

    /**
     * 租赁公摊面积
     */
    private float leasePublicArea;

    /**
     * 消防面积
     */
    private float fireArea;

    /**
     * 清洁面积
     */
    private float cleanArea;

    /**
     * 车位面积
     */
    private float parkingArea;


}
