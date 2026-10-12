package com.guhecloud.cube.srv.gen.securitymanagement.server.common.param;

import lombok.Data;

import java.util.List;

/**
 * 设备过滤参数
 *
 * @author lanchun
 * @date 2024/4/30 10:02
 */
@Data
public class DeviceFilterPageParam {

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
