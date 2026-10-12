package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo;

import lombok.Data;

/**
 * 机组房间
 */
@Data
public class MachinesRoomVO {
    private Long deviceId;
    private String deviceCode;
    private String deviceName;
    /**
     * 产品分类
     */
    private Long productType;
    private String productCategory;
    private String productTypeName;
    /**
     * 部门
     */
    private Long deptId;
    private String deptName;
    /**
     * 区域
     */
    private Long areaId;
    private String areaPathId;
    private String areaPathName;
}
