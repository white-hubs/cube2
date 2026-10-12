package com.guhecloud.cube.biz.emergency.server.param;

import lombok.Data;

/**
 * 预案类型关联设备类型保存参数
 *
 * @author lanchun
 * @date 2022/9/2 10:45
 */
@Data
public class EmergencyDeviceGenreModifyParam {

    /**
     * 设备类型主键
     */
    private String deviceGenreId;

    /**
     * 设备类型名称
     */
    private String deviceGenreName;
}
