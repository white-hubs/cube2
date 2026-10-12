package com.guhecloud.cube.iot.mgt.north.vo;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ProductPropVo {
    private String propCode;
    private String propName;
    private String dataType;
    private Integer precision;
    private String unitName;
    private Integer dataWrite;
    private JSONArray dataConstraint;//直接用JSON吧
    @JSONField(serialize = false)//屏蔽输出
    @JsonIgnore
    private String dataConstraintStr;
    private Boolean isTelemetry;
    private String remark;
}
