package com.guhecloud.cube.customization.xialing.server.liangcang.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/** 智慧仓库 warehouseInfo 响应。 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseDeviceResDTO {
    private Integer id;
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
}
