package com.guhecloud.cube.base.device.center.metadata.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @date 2024/1/17 13:40
 * @description
 */
@Data
public class DeviceBaseInfoPageQry extends Paging {
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
}
