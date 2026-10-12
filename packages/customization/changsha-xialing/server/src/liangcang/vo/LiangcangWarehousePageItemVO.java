package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/** warehousePage 单条 + 本地绑定状态。 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiangcangWarehousePageItemVO {
    private Long id;
    private Integer parentId;
    private String name;
    private String categoryName;
    private Double totalVolume;
    private String qualityGrade;
    private Double warehouseHeight;
    private Boolean enabledScan;
    private String scanCorn;
    private Double materialVolume;
    private Double materialWeight;
    private Double usageRate;
    private String cameraSetting;
    private String setting;
    private List<WarehouseDeviceInfoVO> deviceList;
    private String status;
}
