
package com.guhecloud.cube.srv.gen.appraisals.server.rule.dto;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalCalcTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 考核规则项命令对象
 * 用于创建或更新考核规则的评分项
 */
@Data
public class ItemCommand {
    /**
     * 规则项 ID（更新时使用）
     */
    private Long id;
    /**
     * 规则项名称
     */
    @NotBlank(message = "项名称不能为空")
    private String itemName;
    /**
     * 字段标识（对应的数据字段 key）
     */
    private String fieldKey;
    /**
     * 计算方式（如扣分、加分等）
     */
    @NotNull(message = "计算方式不能为空")
    private AppraisalCalcTypeEnum calcType;
    /**
     * 排序号
     */
    private Integer sort;
    /**
     * 扣分区间配置列表
     */
    @NotNull(message = "扣分区间不能为空")
    private List<RangeConfigDTO> rangeConfig;
}
