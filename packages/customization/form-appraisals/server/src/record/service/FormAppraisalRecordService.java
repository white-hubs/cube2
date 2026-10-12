
package com.guhecloud.cube.srv.gen.appraisals.server.record.service;

import com.guhecloud.cube.srv.gen.appraisals.server.record.param.FormAppraisalRecordDetailAdjustCommand;
import com.guhecloud.cube.srv.gen.appraisals.server.record.param.FormAppraisalRecordScoreCommand;

/**
 * 考核记录服务接口
 * 提供考核记录的分数调整、明细调整、确认等操作
 */
public interface FormAppraisalRecordService {
    /**
     * 调整考核记录分数
     * 手动覆盖最终得分
     *
     * @param id      记录 ID
     * @param command 分数调整命令
     */
    void adjustScore(Long id, FormAppraisalRecordScoreCommand command);

    /**
     * 调整考核记录明细
     * 修改明细项的人工追加扣分并重新计算总分
     *
     * @param id      记录 ID
     * @param command 明细调整命令
     */
    void adjustDetails(Long id, FormAppraisalRecordDetailAdjustCommand command);

    /**
     * 确认考核记录
     * 将记录状态变更为已确认
     *
     * @param id 记录 ID
     */
    void confirm(Long id);

    /**
     * 重新计算生成的记录
     * 当规则数据变化时，允许重新计算生成状态的记录
     *
     * @param id 记录 ID
     */
    void recalculateGeneratedRecord(Long id);
}
