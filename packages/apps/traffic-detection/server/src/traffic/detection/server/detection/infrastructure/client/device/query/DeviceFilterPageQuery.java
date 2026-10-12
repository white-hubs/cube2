package com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.client.device.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceFilterPageQuery extends Paging {

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
     * 设备ID匹配
     */
    private List<Long> matchedDeviceIds;

    /**
     * 过滤区域id
     */
    private List<Long> filterAreaIds;

    /**
     * 区域ID过滤
     */
    private String anyMatchedAreaId;

    /**
     * 任意区域ID过滤
     */
    private Long anyAreaId;

    /**
     * 部门ID过滤
     */
    private Long anyMatchedDepartmentId;

    /**
     * 在线状态
     */
    private Boolean deviceState;
}
