package com.guhecloud.cube.srv.gen.maintain.server.electron.param;

import lombok.Data;

import java.util.List;

@Data
public class PatrolLocationParam {
    /**
     * 主键
     */
    private Long id;
    /**
     * 巡更点名称
     */
    private String patrolLocationName;
    /**
     * nfc芯片编码
     */
    private String nfcCode;
    /**
     * 模型编码
     */
    private String bimCode;
    /**
     * 说明
     */
    private String remarks;
    /**
     * 经度
     */
    private String patrolLongitude;
    /**
     * 纬度
     */
    private String patrolDimension;
    /**
     * 区域id
     */
    private List<String> areaIds;
    /**
     * 蓝牙编号
     */
    private String bluetoothCode;
    /**
     * 巡更设备id
     */
    private Long deviceId;
}
