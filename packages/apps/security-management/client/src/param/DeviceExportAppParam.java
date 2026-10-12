package com.guhecloud.cube.srv.gen.securitymanagement.param;

import lombok.Data;

import java.util.List;

@Data
public class DeviceExportAppParam {

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 设备名称/设备编码like
     */
    private String keyword;

    /**
     * 相当于原来的 model
     */
    private Long productId;

    /**
     * 品类ID
     */
    private String categoryCode;

    /**
     * 匹配标签code
     */
    private List<String> tagCodes;

    /**
     * 设备ID匹配
     */
    private List<Long> matchedDeviceIds;

    /**
     * 区域ID过滤
     */
    private String anyMatchedAreaId;

    /**
     * 部门ID过滤
     */
    private Long anyMatchedDepartmentId;

    /**
     * 在线状态
     */
    private Boolean deviceState;

    /**
     * 主键id集合
     */
    private List<Long> ids;
}
