package com.guhecloud.cube.base.device.center.metadata.std.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 设备类别分组统计在线离线状态数量离线比例
 */
@Data
public class DeviceOfflineCountVO {
    //标签名称
    private String tagName;
    //标签主键
    private String tagCode;
    //标签设备总数
    private Integer num;
    //标签设备离线数量
    private Integer offLineNum;
    //标签设备在线数量
    private Integer onLineNum;
    //标签离线占比
    private BigDecimal proportion;
}
