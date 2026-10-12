package com.guhecloud.cube.srv.gen.device.supervise.server.account.interfaces.vo;

import com.guhecloud.cube.srv.gen.device.supervise.server.account.enumeration.DeviceStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeviceAccountPageVO {

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 收藏
     */
    private Boolean collected;

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

    public void setStatus(DeviceStatusEnum status) {
        this.status = status;
        this.statusName = status.getDescription();
    }
}
