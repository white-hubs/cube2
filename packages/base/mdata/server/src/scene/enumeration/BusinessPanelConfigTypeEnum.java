package com.guhecloud.cube.biz.mdata.server.scene.enumeration;

import lombok.Getter;

/**
 * 设施场景配置类型
 *
 * @author lanchun
 * @date 2022/4/1 16:02
 */
@Getter
public enum BusinessPanelConfigTypeEnum {

    /**
     * 档位类型
     */
    STALL_TYPE,

    /**
     * 档位
     */
    STALL,

    /**
     * 设备类型
     */
    DEVICE_TYPE,

    /**
     * 设备
     */
    DEVICE,

    /**
     * 区域场景面板
     */
    AREA_SCENE;
}
