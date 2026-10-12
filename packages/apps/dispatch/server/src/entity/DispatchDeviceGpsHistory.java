package com.guhecloud.cube.biz.dispatch.server.entity;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class DispatchDeviceGpsHistory extends BasicTenantEntity<Long> {

    private Long id;

    /**
     * 名称，可以是人员名称，也可以是设备名称
     */
    private String deviceId;

    /**
     * 编号
     */
    private String deviceCode;

    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 方向
     */
    private Integer direction;

    /**
     * 经度
     */
    private Double lng;

    /**
     * 纬度
     */
    private Double lat;

    /**
     * 位置信息
     */
    private String address;

    /**
     * 高度
     */
    private Float height;

    /**
     * 速度
     */
    private Float speed;

}
