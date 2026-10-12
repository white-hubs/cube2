package com.guhecloud.cube.srv.gen.appraisals.server.record.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalContentTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalRecordStatusEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.record.dao.FormAppraisalRecordDetailMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.record.dao.FormAppraisalRecordMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.record.dto.FormAppraisalRecordDetailAdjustItemDTO;
import com.guhecloud.cube.srv.gen.appraisals.server.record.entity.FormAppraisalRecord;
import com.guhecloud.cube.srv.gen.appraisals.server.record.entity.FormAppraisalRecordDetail;
import com.guhecloud.cube.srv.gen.appraisals.server.record.param.FormAppraisalRecordDetailAdjustCommand;
import com.guhecloud.cube.srv.gen.appraisals.server.record.param.FormAppraisalRecordScoreCommand;
import com.guhecloud.cube.srv.gen.appraisals.server.record.service.AppraisalRecordGenerateService;
import com.guhecloud.cube.srv.gen.appraisals.server.record.service.FormAppraisalRecordService;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 考核记录服务实现类
 * 提供考核记录的分数调整、明细调整、确认等操作实现
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormAppraisalRecordServiceImpl implements FormAppraisalRecordService {
    private final FormAppraisalRecordMapper recordMapper;
    private final FormAppraisalRecordDetailMapper detailMapper;
    private final AppraisalRecordGenerateService generateService;

    /**
     * 调整考核记录分数
     * 手动覆盖最终得分并将状态标记为已调整
     *
     * @param id      记录 ID
     * @param command 分数调整命令
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adjustScore(Long id, FormAppraisalRecordScoreCommand command) {
        // 获取考核记录，不存在则抛出异常
        FormAppraisalRecord record = getRecord(id);
        // 设置手动覆盖的最终总分
        record.setOverrideFinalTotalScore(command.getOverrideFinalTotalScore());
        // 设置手动覆盖的最终抽查分
        record.setOverrideFinalSpotCheckScore(command.getOverrideFinalSpotCheckScore());
        // 设置手动覆盖的最终检查分
        record.setOverrideFinalInspectionScore(command.getOverrideFinalInspectionScore());
        // 设置调整备注
        record.setAdjustRemark(command.getAdjustRemark());
        // 将记录状态设置为已调整
        record.setStatus(AppraisalRecordStatusEnum.ADJUSTED);
        // 重新计算最终得分（根据覆盖值或明细扣分）
        refreshFinalScore(record);
        // 更新记录到数据库
        recordMapper.updateById(record);
    }

    /**
     * 调整考核记录明细
     * 修改明细项的人工追加扣分并重新计算总分
     *
     * @param id      记录 ID
     * @param command 明细调整命令
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adjustDetails(Long id, FormAppraisalRecordDetailAdjustCommand command) {
        // 获取考核记录，不存在则抛出异常
        FormAppraisalRecord record = getRecord(id);
        // 查询该记录下的所有明细项
        List<FormAppraisalRecordDetail> details = detailMapper.selectList(Wrappers.<FormAppraisalRecordDetail>lambdaQuery()
                                                                                  .eq(FormAppraisalRecordDetail::getRecordId, id));
        // 将明细项列表转换为 Map，key 为明细 ID，便于快速查找
        Map<Long, FormAppraisalRecordDetail> detailMap = details.stream()
                .collect(Collectors.toMap(FormAppraisalRecordDetail::getId, Function.identity()));
        // 遍历每个需要调整的明细项
        for (FormAppraisalRecordDetailAdjustItemDTO item : command.getDetails()) {
            // 从 Map 中获取对应的明细项
            FormAppraisalRecordDetail detail = detailMap.get(item.getDetailId());
            // 如果明细项不存在，跳过
            if (detail == null) {
                continue;
            }
            // 设置人工扣分值
            detail.setManualAdjustDeductScore(item.getManualAdjustDeductScore());
            // 设置调整备注
            detail.setRemark(item.getRemark());
            // 更新明细项到数据库
            detailMapper.updateById(detail);
        }
        // 将记录状态设置为已调整
        record.setStatus(AppraisalRecordStatusEnum.ADJUSTED);
        // 重新计算最终得分
        refreshFinalScore(record);
        // 更新记录到数据库
        recordMapper.updateById(record);
    }

    /**
     * 确认考核记录
     * 将记录状态变更为已确认
     *
     * @param id 记录 ID
     */
    @Override
    public void confirm(Long id) {
        // 获取考核记录，不存在则抛出异常
        FormAppraisalRecord record = getRecord(id);
        // 将记录状态设置为已确认
        record.setStatus(AppraisalRecordStatusEnum.CONFIRMED);
        // 更新记录到数据库
        recordMapper.updateById(record);
    }

    /**
     * 重新计算生成的记录
     * 调用生成服务重新生成数据
     *
     * @param id 记录 ID
     */
    @Override
    public void recalculateGeneratedRecord(Long id) {
        // 调用生成服务的重新生成方法
        generateService.regenerateIfAllowed(id);
    }

    /**
     * 获取考核记录，不存在则抛出异常
     *
     * @param id 记录 ID
     *
     * @return 考核记录实体
     */
    private FormAppraisalRecord getRecord(Long id) {
        // 根据 ID 查询考核记录
        FormAppraisalRecord record = recordMapper.selectById(id);
        Assert.isNotFound(record == null, "考核记录不存在");
        // 返回考核记录实体
        return record;
    }

    /**
     * 刷新最终得分
     * 根据明细项的最终扣分重新计算总分，如果有手动覆盖值则优先使用覆盖值
     *
     * @param record 考核记录实体
     */
    private void refreshFinalScore(FormAppraisalRecord record) {
        // 查询该记录下的所有明细项
        List<FormAppraisalRecordDetail> details = detailMapper.selectList(Wrappers.<FormAppraisalRecordDetail>lambdaQuery()
                                                                                  .eq(FormAppraisalRecordDetail::getRecordId, record.getId()));
        // 计算抽查类明细的总扣分：筛选出抽查类型，提取最终扣分，累加求和
        BigDecimal spotDeduct = details.stream()
                .filter(detail -> detail.getContentType() == AppraisalContentTypeEnum.SPOT_CHECK)
                .map(FormAppraisalRecordDetail::getFinalDeductScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 计算检查类明细的总扣分：筛选出检查类型，提取最终扣分，累加求和
        BigDecimal inspectionDeduct = details.stream()
                .filter(detail -> detail.getContentType() == AppraisalContentTypeEnum.INSPECTION)
                .map(FormAppraisalRecordDetail::getFinalDeductScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 计算抽查得分：原始抽查分减去抽查扣分，最低为 0
        BigDecimal calculatedSpot = record.getOriginSpotCheckScore().subtract(spotDeduct).max(BigDecimal.ZERO);
        // 计算检查得分：原始检查分减去检查扣分，最低为 0
        BigDecimal calculatedInspection = record.getOriginInspectionScore().subtract(inspectionDeduct).max(BigDecimal.ZERO);
        // 计算总分：抽查得分 + 检查得分
        BigDecimal calculatedTotal = calculatedSpot.add(calculatedInspection);
        // 设置最终抽查分：如果有手动覆盖值则使用覆盖值，否则使用计算值
        record.setFinalSpotCheckScore(
                record.getOverrideFinalSpotCheckScore() != null ? record.getOverrideFinalSpotCheckScore() : calculatedSpot);
        // 设置最终检查分：如果有手动覆盖值则使用覆盖值，否则使用计算值
        record.setFinalInspectionScore(
                record.getOverrideFinalInspectionScore() != null ? record.getOverrideFinalInspectionScore() : calculatedInspection);
        // 设置最终总分：如果有手动覆盖值则使用覆盖值，否则使用计算值
        record.setFinalTotalScore(record.getOverrideFinalTotalScore() != null ? record.getOverrideFinalTotalScore() : calculatedTotal);
    }
}
