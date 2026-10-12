package com.guhecloud.cube.biz.vmonitor.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class CameraUserAuthDeviceQueryParam extends Paging {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 组织结构主键
     */
    private Long organizationId;

    /**
     * 设备主键
     */
    private Long deviceId;

    /**
     * 是否关联
     */
    private Boolean associated;
}
