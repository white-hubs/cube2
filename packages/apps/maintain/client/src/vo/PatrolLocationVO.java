package com.guhecloud.cube.srv.gen.maintain.vo;

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
    /**
     * 标签ID
     */
    private List<Long> tagIds;
    /**
     * 标签名称
     */
    private String tagNames;
    private String name;
    private String typeId;
    private String typeName;
    private List<Long> departmentIds;
    private String departmentNames;
    private String areaName;
}
