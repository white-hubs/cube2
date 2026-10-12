package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response;

import lombok.Data;

/**
 * 数据列表
 */
@Data
public class DaHuaDeviceChannelResponse {

    /**
     * 自增ID (主键)
     */
    private Long id;

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * 单元类型
     */
    private Integer unitType;

    /**
     * 单元序号
     */
    private Integer unitSeq;

    /**
     * 通道序号
     */
    private Integer channelSeq;

    /**
     * 通道编码
     */
    private String channelCode;

    /**
     * 通道标识码
     */
    private String channelSn;

    /**
     * 通道名称
     */
    private String channelName;

    /**
     * 通道类型
     */
    private String channelType;

    /**
     * 摄像头类型
     */
    private String cameraType;

    /**
     * 所属组织编码
     */
    private String ownerCode;

    /**
     * 经度（建议使用Double或BigDecimal存储坐标）
     */
    private String gpsX;

    /**
     * 纬度
     */
    private String gpsY;

    /**
     * Z轴高度（可能表示海拔）
     */
    private String gpsZ;

    /**
     * 光栅图ID
     */
    private Long mapId;

    /**
     * 域ID
     */
    private Long domainId;

    /**
     * 描述信息
     */
    private String memo;

    /**
     * 设备通道在线状态：0-离线 1-在线
     */
    private Integer isOnline;

    /**
     * 状态：0-关闭 1-开启
     */
    private Integer stat;

    /**
     * 是否休眠：0-未休眠 1-休眠
     */
    private Integer sleepStat;

    /**
     * 通道能力集（重复字段，需确认是否需要删除或合并）
     */
    private String capability;

    /**
     * 是否已接入：0-未接入 1-已接入
     */
    private Integer access;

    /**
     * 通道扩展信息（JSON格式）
     */
    private String chExt;

    /**
     * 是否为虚拟通道（建议使用Boolean类型）
     */
    private String isVirtual;

}
