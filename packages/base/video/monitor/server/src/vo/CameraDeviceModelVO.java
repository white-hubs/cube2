package com.guhecloud.cube.biz.vmonitor.server.vo;

import lombok.Data;

/**
 * 摄像机设备型号返回参数
 *
 * @author lanchun
 * @date 2022/11/8 10:15
 */
@Data
public class CameraDeviceModelVO {

    private Long id;

    /**
     * 设备型号主键
     */
    private Long deviceModelId;

    /**
     * 设备型号名称
     */
    private String deviceModelName;

    /**
     * 设备类型主键
     */
    private Long deviceGenreId;

    /**
     * 设备类型名称
     */
    private String deviceGenreName;

    /**
     * 设备品牌主键
     */
    private Long deviceBrandId;

    /**
     * 设备品牌名称
     */
    private String deviceBrandName;
}
