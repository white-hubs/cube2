package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * 能耗计费规则提交入参对象
 */
@Data
public class EnergyChargeRulesParam {
    private Long id;
    @NotEmpty(message = "规则名称不能为空")
    private String ruleName;
    /**
     * 用能类型code(InstrumentTypeEnum)
     */
    @NotEmpty(message = "用能类型不能为空")
    private String instrumentType;
    /**
     * 用能类型名称
     */
    private String instrumentTypeName;
    /**
     * 生效时间
     */
    @NotNull(message = "生效时间不能为空")
    private LocalDateTime effectiveTime;
    /**
     * 是否复费率(true为付费率，其他为单费率)
     */
    @NotNull(message = "费率类型不能为空")
    private Boolean complexFeeRate;
    /**
     * 阶梯用量类型(StairsEnergyType)
     */
    @NotEmpty(message = "阶梯类型不能为空")
    private String stairsEnergyType;
    /**
     * 阶梯用量内容(StairsEnergyContentsDTO)
     */
    @NotEmpty(message = "阶梯内容不能为空")
    private String stairsEnergyContents;
}
