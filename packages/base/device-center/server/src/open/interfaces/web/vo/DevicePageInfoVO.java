package com.guhecloud.cube.base.device.center.open.interfaces.web.vo;

import com.guhecloud.cube.base.device.center.metadata.dto.AreaBaseInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.IdToName;
import lombok.Data;

/**
 * @author pengjian
 * @date 2024/1/16 20:21
 * @description
 */
@Data
public class DevicePageInfoVO {
    private Long id;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备编码
     */
    private String code;
    /**
     * 设备状态
     */
    private DeviceBaseArchivesInfoDTO.Status status;
    private IdToName department;
    private String thirdPartyId;
    private String lntAndLat;
    private String locationDes;

    /**
     * 设备产品ID
     */
    private Long iotProductId;
    /**
     * 区域信息
     */
    private AreaBaseInfoDTO area;
    private Long picture;
    private DeviceProductInfoDTO productInfo;

}
