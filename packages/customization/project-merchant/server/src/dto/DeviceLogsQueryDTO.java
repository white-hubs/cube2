package com.guhecloud.cube.deliver.project.merchant.dto;


import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;

@Data
public class DeviceLogsQueryDTO extends BaseQueryDTO {

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 部门id
     */
    private String deptId;
}
