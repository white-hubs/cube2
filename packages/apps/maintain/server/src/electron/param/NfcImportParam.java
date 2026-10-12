package com.guhecloud.cube.srv.gen.maintain.server.electron.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

@Data
public class NfcImportParam {
    /**
     * 设备编号
     */
    @NotEmpty(message = "NFC芯片编号不能为空")
    private String deviceCode;
    /**
     * NFC芯片编号
     */
    private String nfcCode;
    /**
     * 蓝牙编号
     */
    private String bluetoothCode;
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
}
