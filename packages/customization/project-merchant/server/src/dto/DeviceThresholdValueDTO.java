package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.DeviceThresholdValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceThresholdValueDTO extends ConvertionDTO<DeviceThresholdValueDTO, DeviceThresholdValue> {

    public DeviceThresholdValueDTO() {
    }

    public DeviceThresholdValueDTO(DeviceThresholdValue entity) {
        super();
        map(entity);
    }

    public DeviceThresholdValueDTO(List<DeviceThresholdValue> list) {
        super();
        map(list);
    }

    /**
     * 主键
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 名称
     */
    private String name;

    /**
     * 业务平台设备IDS
     */
    private List<String> deviceIds;

    /**
     * 设备IDS
     */
    private String deviceIdStr;

    /**
     * 业务平台设备IDS
     */
    private List<DeviceDTO> deviceDTOS;

    /**
     * 设备属性值
     */
    //    private List<IotDevicePropertyDTO> deviceProperties;

    /**
     * 电压高限
     */
    private Integer dygxsz;

    /**
     * 电压低限
     */
    private Integer dydxsz;

    /**
     * 电流高限
     */
    private Integer dlgxsz;

    /**
     * 功率高限
     */
    private Integer glgxsz;

    /**
     * 漏电流高限
     */
    private Integer ldlgxsz;

    /**
     * 温度高限
     */
    private Integer wdgxsz;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}


