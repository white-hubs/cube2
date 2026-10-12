package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class ProductPropDTO {
    private String propCode;
    private String propName;
    private String dataType;
    private Integer precision;
    private String unitName;
    private Integer dataWrite;
    private JSONArray dataConstraint;//直接用JSON吧
    @JSONField(serialize = false)
    private String dataConstraintStr;
    private Boolean isTelemetry;
    private String remark;
}
