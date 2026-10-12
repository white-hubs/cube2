package com.guhecloud.cube.srv.gen.device.supervise.server.account.interfaces.vo;

import com.guhecloud.cube.srv.gen.device.supervise.server.account.enumeration.DeviceStatusEnum;
import lombok.Data;

@Data
public class DeviceAccountVO {

    private Long id;

    /**
     * 收藏
     */
    private Boolean collected = false;

    /**
     * 状态
     */
    private DeviceStatusEnum status;

    /**
     * 状态名
     */
    private String statusName;

    /**
     * 维护成本
     */
    private double maintainCost;

    private String configJson;

    public void setStatus(DeviceStatusEnum status) {
        this.status = status;
        this.statusName = status.getDescription();
    }

}
