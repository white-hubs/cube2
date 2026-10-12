package com.guhecloud.cube.srv.gen.maintain.server.electron.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NfcDetailsVO {
    private Long id;
    /**
     * NFC芯片编号
     */
    private String nfcCode;
    /**
     * 经度
     */
    private String nfcLongitude;
    /**
     * 纬度
     */
    private String nfcDimension;
    /**
     * 说明
     */
    private String remarks;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 蓝牙编号
     */
    private String bluetoothCode;
    /**
     * 设备编号
     */
    private String deviceCode;
}
