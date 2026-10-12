package com.guhecloud.cube.biz.dispatch.server.param;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 调度设备-状态
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchDeviceStatusParam {

    //设备编号
    private String code;
    //经度
    private Double lng;
    //纬度
    private Double lat;
    //方向
    private Integer direction;
    //速度
    private Float speed;
    //上报时间
    private LocalDateTime uploadTime;
    //在线状态( 1 在线 0 离线)
    private Boolean onlineStatus;
    //视频状态( 1 打开 0 关闭)
    private Boolean videoStatus;

}