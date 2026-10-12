package com.guhecloud.cube.customization.xialing.server.liangcang.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/** 货位卡 JSON（camelCase），与租户字典 variable 一致。 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiangcangLocationCardConfigDTO {

    private List<String> temperatureProductCodes = new ArrayList<>();
    private List<String> humidityProductCodes = new ArrayList<>();
    private List<String> gasProductCodes = new ArrayList<>();

    private String warehouseMaxTemperaturePropCode;
    private String warehouseMinTemperaturePropCode;
    private String warehouseAvgTemperaturePropCode;
    private String warehouseInternalTemperaturePropCode;
    private String warehouseExternalTemperaturePropCode;

    private String temperatureRealtimePropCode;
    private String humidityRealtimePropCode;
    private String gasRealtimePropCode;

    private String gasCoPpmPropCode;
    private String gasCo2PpmPropCode;
    private String gasNoPpmPropCode;
    private String gasO2PercentVolPropCode;
    private String gasPh3PpmPropCode;
    private String gasSo2f2PpmPropCode;
}
