package com.guhecloud.cube.base.device.center.metadata.std.param;

import com.guhecloud.cube.base.device.center.metadata.param.DeviceSortParam;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceFilterPageParam extends Paging {

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
     * 是否仅筛选带经纬度数据的设备
     */
    private Boolean hasLntAndLat;

    /**
     * 过滤TagCode
     */
    private List<String> filterTagCodes;

    /**
     * 所有匹配标签code
     */
    private List<String> allMatchedTagCodes;

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

    /**
     * 设备排序条件(目前单个,后续可能多个字段排序)
     */
    private DeviceSortParam deviceSort;
}
