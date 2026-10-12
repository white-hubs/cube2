package com.guhecloud.cube.base.device.center.metadata.vo;

import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.Data;

import java.util.List;

@Data
public class DeviceGroupVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 分组名
     */
    private String name;

    /**
     * 描叙
     */
    private String remark;

    /**
     * 设备数量
     */
    private Integer deviceCount;

    /**
     * 组下设备
     */
    private List<DevicePageInfoVO> devices;

    public Integer getDeviceCount() {
        if (CollectionUtils.isEmpty(devices)) {
            return 0;
        }
        return devices.size();
    }
}
