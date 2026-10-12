package com.guhecloud.cube.srv.gen.securitymanagement.server.information.param;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InformationPlanParam extends Paging {
    /**
     * 计划主键id
     */
    @JSONField(serializeFeatures = JSONWriter.Feature.WriteLongAsString)
    private Long id;
    /**
     * 计划名称
     */
    private String planName;
    /**
     * 是否启用 0：禁用 1：启用
     */
    private Integer isEnable;
    /**
     * 当前时间
     */
    private LocalDateTime nowDate;
    //应用
    private String applicationCode;
    //查询入参
    private String keyword;
}
