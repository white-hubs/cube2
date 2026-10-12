package com.guhecloud.cube.base.device.center.metadata.std.param;

import lombok.Data;

import java.util.List;

@Data
public class DeviceFilterParam {

    /**
     * 设备名称/设备编码like
     */
    private String keywordLike;

    /**
     * 相当于原来的 model
     */
    private Long productId;

    /**
     * 品类ID
     */
    private String categoryCode;

    /**
     * 标签code
     */
    private List<String> tagCodes;

    /**
     * 设备ID匹配
     */
    private List<Long> matchedDeviceIds;

    /**
     * 区域ID过滤
     */
    private Long anyMatchedAreaId;

    /**
     * 部门ID过滤
     */
    private Long anyMatchedDepartmentId;

    /**
     * 是否包含设备扩展信息
     */
    private boolean withDeviceExpandInfo;

    /**
     * 是否包含设备标签信息
     */
    private boolean withDeviceTagInfo;
}
