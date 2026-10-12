package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import lombok.Data;

import java.util.List;

@Data
public class RiskObjectMeasureSaveParam {
    /**
     * 风险对象配置的管控措施列表。
     */
    private List<RiskControlMeasureParam> measures;

    /**
     * 是否提交进入执行；false 表示仅保存草稿并停留在管控配置状态。
     */
    private Boolean submitted;
}
