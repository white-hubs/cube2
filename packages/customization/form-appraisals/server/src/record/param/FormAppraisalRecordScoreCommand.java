
package com.guhecloud.cube.srv.gen.appraisals.server.record.param;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 考核记录分数调整命令
 * 用于手动覆盖考核记录的最终得分
 */
@Data
public class FormAppraisalRecordScoreCommand {
    /**
     * 覆盖后的最终总分
     */
    private BigDecimal overrideFinalTotalScore;
    /**
     * 覆盖后的最终抽查分
     */
    private BigDecimal overrideFinalSpotCheckScore;
    /**
     * 覆盖后的最终检查分
     */
    private BigDecimal overrideFinalInspectionScore;
    /**
     * 调整备注说明
     */
    private String adjustRemark;
}
