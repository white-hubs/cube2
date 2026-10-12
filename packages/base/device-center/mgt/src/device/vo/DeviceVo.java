package com.guhecloud.cube.iot.mgt.device.vo;

import com.guhecloud.cube.iot.mgt.things.vo.ProductVo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeviceVo {
    /**
     * 主键id
     */
    private Long id;

    private Long deviceParentId;

    private String deviceParentName;

    /**
     * 设备KEY,唯一
     */
    private String deviceKey;

    /**
     * 编码,后续扩展
     */
    private String deviceNumber;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 节点类型: 1直连设备 2网关设备 3网关子设备
     */
    private Integer nodeType;

    private String nodeTypeName;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 状态:0未激活;1在线;2离线;3已禁用
     */
    private Integer status;

    private String statusName;

    /**
     * 设备类型:1真实设备; 0虚拟设备
     */
    private Integer deviceType;

    private String deviceTypeName;

    /**
     * 激活时间
     */
    private LocalDateTime activationTime;

    /**
     * 激活时间
     */

    private String activationTimeStr;

    /**
     * 最近上线时间
     */
    private LocalDateTime onlineTime;

    /**
     * 最近上线时间
     */
    private String onlineTimeStr;

    private LocalDateTime createTime;

    private String createTimeStr;

    /**
     * 预留透明字段(可下发至设备的配置)
     */
    private String config;

    private String rawDeviceId;

    private String authAlgorithm;

    private String salt;

    //设备描述 三方设备ID 物联配置

    private String remark;

    private String slaveDeviceIds;

    private String mainDeviceId;

    /**
     * 边缘设备标识
     */
    private String edgeDevIdentify;

    /**
     * 数据共享信息（分页/详情回显）
     */
    private DevDataShareEchoVo dataShare;

    /**
     * 所属产品
     */
    private ProductVo product;
}
