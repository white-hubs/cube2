
package com.guhecloud.cube.srv.gen.appraisals.server.record.param;

import com.guhecloud.cube.srv.gen.appraisals.server.record.dto.FormAppraisalRecordDetailAdjustItemDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 考核记录明细调整命令
 * 用于批量调整多个明细项的扣分
 */
@Data
public class FormAppraisalRecordDetailAdjustCommand {
    /**
     * 考核记录 ID
     */
    @NotNull(message = "记录 ID 不能为空")
    private Long recordId;
    /**
     * 明细调整项列表
     */
    @Valid
    @NotNull(message = "调整明细不能为空")
    private List<FormAppraisalRecordDetailAdjustItemDTO> details;
}
