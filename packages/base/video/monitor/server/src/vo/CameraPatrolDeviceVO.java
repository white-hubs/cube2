package com.guhecloud.cube.biz.vmonitor.server.vo;

import lombok.Data;

import java.util.List;

/**
 * 巡逻监控返回参数
 *
 * @author lanchun
 * @date 2021/12/6 19:50
 */
@Data
public class CameraPatrolDeviceVO {

    private Long id;

    /**
     * 巡逻线路主键
     */
    private Long cameraPatrolLineId;

    /**
     * 巡逻监控设备主键
     */
    private Long deviceId;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 区域
     */
    private String area;

    /**
     * 区域主键集合
     */
    private List<Long> areaIds;

    /**
     * 所属区域
     */
    private String areaNames;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 流媒体信息
     */
    private StreamMediaVO streamMedia;

    /**
     * 第三方设备标识
     */
    private String thirdPartyId;
}