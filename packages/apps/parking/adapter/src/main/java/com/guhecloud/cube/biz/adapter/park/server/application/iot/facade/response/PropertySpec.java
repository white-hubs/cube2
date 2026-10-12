package com.guhecloud.cube.biz.adapter.park.server.application.iot.facade.response;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * @author mioopp
 * @create 2021-11-19 14:07
 */
@Data
public class PropertySpec {

    private String propCode;

    private String propName;

    private String dataType;

    private String dataUnitCode;

    private Integer scale;

    private JSONObject dataConfig;

    private String isEnabled;

    private String remark;

    private String isBasic;

    private String isTelemetry;

    private String extendConfig;

    private String accessMode;
}
