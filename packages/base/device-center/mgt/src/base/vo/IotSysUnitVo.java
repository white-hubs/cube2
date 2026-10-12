package com.guhecloud.cube.iot.mgt.base.vo;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class IotSysUnitVo {
    /**
     * 单位主键
     */
    @JSONField(serializeFeatures = JSONWriter.Feature.WriteLongAsString)
    private Long id;

    /**
     * 单位code
     */
    private String unitCode;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 单位类型
     */
    private Integer unitType;

    /**
     * 状态0正常 1停用
     */
    private Integer unitStatus;

    /**
     * 备注
     */
    private String remark;

}
