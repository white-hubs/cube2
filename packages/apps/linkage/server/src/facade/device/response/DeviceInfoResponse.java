package com.guhecloud.cube.biz.linkage.server.facade.device.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceInfoResponse {

    private Long id;

    /**
     * 设备编号
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 区域主键
     */
    private String areaId;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 类型主键
     */
    private Long genreId;

    /**
     * 类型名称
     */
    private String genreName;

    /**
     * 经纬度
     */
    private String lntAndLat;

    /**
     * 第三方设备主键
     */
    private String thirdpartyId;
}
