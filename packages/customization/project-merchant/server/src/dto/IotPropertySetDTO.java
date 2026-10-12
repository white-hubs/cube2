package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class IotPropertySetDTO {

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 业务平台设备IDS
     */
    private List<String> deviceIds;

    /**
     * 设备属性值
     */
    //    private List<IotDevicePropertyDTO> deviceProperties;

    /**
     * 全部设备标识，true：全部电气设备,deviceIds就不传
     */
    private Boolean allDevice;

}


