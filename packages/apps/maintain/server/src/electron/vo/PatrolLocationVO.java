package com.guhecloud.cube.srv.gen.maintain.server.electron.vo;

import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PatrolLocationVO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 巡更设备id
     */
    private Long deviceId;
    /**
     * 巡更设备编号
     */
    private String deviceCode;
    /**
     * 巡更点名称
     */
    private String patrolLocationName;
    /**
     * 区域
     */
    private List<AreaDTO> area;
    private String areaId;
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
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 蓝牙编号
     */
    private String bluetoothCode;
}
