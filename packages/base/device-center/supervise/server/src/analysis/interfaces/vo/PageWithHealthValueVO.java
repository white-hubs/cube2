package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/8/8 14:17
 * @description
 */
@Data
public class PageWithHealthValueVO {
    private DeviceBaseArchivesInfoDTO baseArchivesInfo;
    private DeviceProductInfoDTO productInfo;
    private Integer healthValue;
}

