package com.guhecloud.cube.base.device.center.open.interfaces.web.vo;

import com.guhecloud.cube.base.device.center.metadata.dto.AreaBaseInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.IdToName;
import lombok.Data;

import java.util.Set;

/**
 * @author pengjian
 * @date 2024/1/18 9:24
 * @description
 */
@Data
public class DeviceBaseArchivesInfoVO {
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

    /**
     * 区域信息
     * key: 区域id
     * value: 区域名称
     */
    private AreaBaseInfoDTO area;

    /**
     * 设备管理员
     */
    private Set<IdToName> manager;

    /**
     * 三方设备id
     */
    private String thirdPartyId;
    /**
     * 三方配置
     */
    private String thirdpartyCfg;
    /**
     * 设备图片
     */
    private Long picture;
}
