
package com.guhecloud.cube.srv.gen.appraisals.server.record.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 考核记录明细调整项 DTO
 * 用于调整单个明细项的扣分和备注
 */
@Data
public class FormAppraisalRecordDetailAdjustItemDTO {
    /**
     * 明细 ID
     */
    @NotNull(message = "明细 ID 不能为空")
    private Long detailId;
    /**
     * 人工追加扣分值
     */
    @NotNull(message = "人工追加扣分不能为空")
    @DecimalMin(value = "0", message = "人工追加扣分不能小于 0")
    private BigDecimal manualAdjustDeductScore;
    /**
     * 调整备注
     */
    private String remark;
}
