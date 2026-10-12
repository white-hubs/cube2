package com.guhecloud.cube.srv.gen.appraisals.server.record.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalPeriodTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.common.util.AppraisalDepartmentUtils;
import com.guhecloud.cube.srv.gen.appraisals.server.common.util.AppraisalPeriodUtils;
import com.guhecloud.cube.srv.gen.appraisals.server.record.dao.FormAppraisalPreviewRecordDetailMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.record.dao.FormAppraisalPreviewRecordMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.record.dao.FormAppraisalRecordDetailMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.record.dao.FormAppraisalRecordMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.record.dto.FormAppraisalDeductionReasonDTO;
import com.guhecloud.cube.srv.gen.appraisals.server.record.dto.FormAppraisalStatisticsAggregateDTO;
import com.guhecloud.cube.srv.gen.appraisals.server.record.entity.FormAppraisalPreviewRecord;
import com.guhecloud.cube.srv.gen.appraisals.server.record.entity.FormAppraisalPreviewRecordDetail;
import com.guhecloud.cube.srv.gen.appraisals.server.record.entity.FormAppraisalRecord;
import com.guhecloud.cube.srv.gen.appraisals.server.record.entity.FormAppraisalRecordDetail;
import com.guhecloud.cube.srv.gen.appraisals.server.record.param.FormAppraisalRecordPageParam;
import com.guhecloud.cube.srv.gen.appraisals.server.record.param.FormAppraisalRecordStatisticsParam;
import com.guhecloud.cube.srv.gen.appraisals.server.record.service.FormAppraisalRecordQueryService;
import com.guhecloud.cube.srv.gen.appraisals.server.record.vo.FormAppraisalDeductionReasonVO;
import com.guhecloud.cube.srv.gen.appraisals.server.record.vo.FormAppraisalRecordDetailItemVO;
import com.guhecloud.cube.srv.gen.appraisals.server.record.vo.FormAppraisalRecordDetailVO;
import com.guhecloud.cube.srv.gen.appraisals.server.record.vo.FormAppraisalRecordExportVO;
import com.guhecloud.cube.srv.gen.appraisals.server.record.vo.FormAppraisalRecordGroupVO;
import com.guhecloud.cube.srv.gen.appraisals.server.record.vo.FormAppraisalRecordPageVO;
import com.guhecloud.cube.srv.gen.appraisals.server.record.vo.FormAppraisalStatisticsPeriodVO;
import com.guhecloud.cube.srv.gen.appraisals.server.record.vo.FormAppraisalStatisticsSummaryVO;
import com.guhecloud.cube.srv.gen.appraisals.server.record.vo.FormAppraisalStatisticsVO;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dao.FormAppraisalRuleMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRule;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 考核记录查询服务实现类
 * 提供考核记录的查询操作实现
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormAppraisalRecordQueryServiceImpl implements FormAppraisalRecordQueryService {
    private final FormAppraisalRecordMapper recordMapper;
    private final FormAppraisalRecordDetailMapper detailMapper;
    private final FormAppraisalPreviewRecordMapper previewRecordMapper;
    private final FormAppraisalPreviewRecordDetailMapper previewDetailMapper;
    private final FormAppraisalRuleMapper ruleMapper;

    /**
     * 根据 ID 获取考核记录详情（包含明细项）
     *
     * @param id 记录 ID
     *
     * @return 考核记录详情 VO
     */
    @Override
    public FormAppraisalRecordDetailVO getById(Long id) {
        // 查询考核记录基本信息
        FormAppraisalRecord record = recordMapper.selectById(id);
        // 如果记录不存在，返回 null
        if (record == null) {
            return getPreviewByIdIfCurrent(id);
        }
        // 复制记录属性到详情 VO 对象
        FormAppraisalRecordDetailVO detailVO = toDetailVO(record);
        fillDepartmentInfo(detailVO, record);
        // 查询该记录下的所有明细项，按排序号升序排列
        List<FormAppraisalRecordDetail> details = detailMapper.selectList(Wrappers.<FormAppraisalRecordDetail>lambdaQuery()
                                                                                  .eq(FormAppraisalRecordDetail::getRecordId, id)
                                                                                  .orderByAsc(FormAppraisalRecordDetail::getSort));
        // 复制明细项属性到明细项 VO 列表并设置到详情 VO 中
        detailVO.setDetails(details.stream().map(this::toDetailItemVO).toList());
        // 返回完整的详情 VO
        return detailVO;
    }

    /**
     * 分页获取考核记录列表
     *
     * @param param 分页查询参数
     *
     * @return 考核记录分页数据
     */
    @Override
    public Pagination<FormAppraisalRecordGroupVO> getPage(FormAppraisalRecordPageParam param) {
        if (isCurrentPeriodParam(param)) {
            return getPreviewPage(param);
        }
        List<FormAppraisalRecord> records = recordMapper.selectList(buildQueryWrapper(param));
        List<FormAppraisalRecordGroupVO> groupRecords = buildGroupRecords(records);
        long total = groupRecords.size();
        long pages = total == 0 ? 0 : (total + param.getPageSize() - 1L) / param.getPageSize();
        return Pagination.<FormAppraisalRecordGroupVO>builder()
                .total(total)
                .pages(pages)
                .records(Paging.list(groupRecords, param.getPageNumber(), param.getPageSize()))
                .build();
    }

    @Override
    public FormAppraisalStatisticsVO statistics(FormAppraisalRecordStatisticsParam param) {
        Assert.isParameterError(param == null, "统计参数不能为空");
        Assert.isParameterError(param.getAssessmentYear() == null, "考核年份不能为空");
        Assert.isParameterError(param.getPeriodType() == null, "周期类型不能为空");

        List<FormAppraisalStatisticsAggregateDTO> aggregateRows = this.selectPeriodStatistics(param);
        Map<Integer, FormAppraisalStatisticsAggregateDTO> aggregateMap = new LinkedHashMap<>();
        for (FormAppraisalStatisticsAggregateDTO aggregateRow : aggregateRows) {
            if (aggregateRow != null && aggregateRow.getAssessmentPeriod() != null) {
                aggregateMap.put(aggregateRow.getAssessmentPeriod(), aggregateRow);
            }
        }
        FormAppraisalStatisticsAggregateDTO previewAggregate = this.selectCurrentPreviewStatistics(param);
        if (previewAggregate != null && previewAggregate.getAssessmentPeriod() != null) {
            aggregateMap.put(previewAggregate.getAssessmentPeriod(), previewAggregate);
        }

        List<Integer> periods = this.getPeriods(param.getPeriodType());
        List<FormAppraisalStatisticsPeriodVO> records = new ArrayList<>();
        BigDecimal totalOriginScore = BigDecimal.ZERO;
        BigDecimal totalFinalScore = BigDecimal.ZERO;
        BigDecimal totalDeductScore = BigDecimal.ZERO;
        for (Integer period : periods) {
            FormAppraisalStatisticsAggregateDTO aggregate = aggregateMap.get(period);
            BigDecimal originScore = aggregate == null ? BigDecimal.ZERO : this.safeScore(aggregate.getOriginTotalScore());
            BigDecimal finalScore = aggregate == null ? BigDecimal.ZERO : this.safeScore(aggregate.getFinalTotalScore());
            BigDecimal deductScore = aggregate == null ? BigDecimal.ZERO : this.safeScore(aggregate.getDeductScore());

            FormAppraisalStatisticsPeriodVO periodVO = new FormAppraisalStatisticsPeriodVO();
            periodVO.setAssessmentYear(param.getAssessmentYear());
            periodVO.setPeriodType(param.getPeriodType());
            periodVO.setAssessmentPeriod(period);
            periodVO.setPeriodLabel(this.buildPeriodLabel(param.getAssessmentYear(), param.getPeriodType(), period));
            AppraisalPeriodUtils.TargetPeriod targetPeriod = AppraisalPeriodUtils.targetPeriod(param.getPeriodType(), param.getAssessmentYear(), period);
            periodVO.setAssessmentBeginTime(aggregate != null && aggregate.getAssessmentBeginTime() != null
                                            ? aggregate.getAssessmentBeginTime()
                                            : targetPeriod.getBeginTime());
            periodVO.setAssessmentEndTime(aggregate != null && aggregate.getAssessmentEndTime() != null
                                          ? aggregate.getAssessmentEndTime()
                                          : targetPeriod.getEndTime());
            periodVO.setOriginTotalScore(originScore);
            periodVO.setFinalTotalScore(finalScore);
            periodVO.setDeductScore(deductScore);
            records.add(periodVO);

            totalOriginScore = totalOriginScore.add(originScore);
            totalFinalScore = totalFinalScore.add(finalScore);
            totalDeductScore = totalDeductScore.add(deductScore);
        }

        FormAppraisalStatisticsSummaryVO summary = new FormAppraisalStatisticsSummaryVO();
        summary.setOriginTotalScore(totalOriginScore);
        summary.setFinalTotalScore(totalFinalScore);
        summary.setDeductScore(totalDeductScore);

        FormAppraisalStatisticsVO vo = new FormAppraisalStatisticsVO();
        vo.setAssessmentYear(param.getAssessmentYear());
        vo.setPeriodType(param.getPeriodType());
        vo.setCurrentPreviewGeneratedAt(previewAggregate == null ? null : previewAggregate.getPreviewGeneratedAt());
        vo.setSummary(summary);
        vo.setRecords(records);
        return vo;
    }

    @Override
    public void export(FormAppraisalRecordPageParam param, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        try {
            String excelName = URLEncoder.encode("考核记录", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());

            EasyExcel.write(response.getOutputStream(), FormAppraisalRecordExportVO.class)
                    .sheet("考核记录")
                    .doWrite(buildExportRows(param));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private LambdaQueryWrapper<FormAppraisalRecord> buildQueryWrapper(FormAppraisalRecordPageParam param) {
        return Wrappers.<FormAppraisalRecord>lambdaQuery()
                .like(StringUtils.isNotBlank(param.getKeyword()), FormAppraisalRecord::getName, param.getKeyword())
                .like(StringUtils.isNotBlank(param.getInspectionItem()), FormAppraisalRecord::getInspectionItem, param.getInspectionItem())
                .eq(param.getPeriodType() != null, FormAppraisalRecord::getPeriodType, param.getPeriodType())
                .apply(
                        param.getResponsibleDepartmentId() != null,
                        "find_in_set({0}, responsible_department_ids)",
                        param.getResponsibleDepartmentId()
                      )
                .eq(param.getFirstLevelFormGroupId() !=
                    null, FormAppraisalRecord::getFirstLevelFormGroupId, param.getFirstLevelFormGroupId())
                .eq(param.getSecondLevelFormGroupId() !=
                    null, FormAppraisalRecord::getSecondLevelFormGroupId, param.getSecondLevelFormGroupId())
                .eq(param.getAssessmentYear() != null, FormAppraisalRecord::getAssessmentYear, param.getAssessmentYear())
                .eq(param.getAssessmentPeriod() != null, FormAppraisalRecord::getAssessmentPeriod, param.getAssessmentPeriod())
                .eq(param.getStatus() != null, FormAppraisalRecord::getStatus, param.getStatus())
                .ge(param.getGeneratedBeginTime() != null, FormAppraisalRecord::getGeneratedAt, param.getGeneratedBeginTime())
                .le(param.getGeneratedEndTime() != null, FormAppraisalRecord::getGeneratedAt, param.getGeneratedEndTime())
                .orderByDesc(FormAppraisalRecord::getGeneratedAt);
    }

    private Pagination<FormAppraisalRecordGroupVO> getPreviewPage(FormAppraisalRecordPageParam param) {
        if (param.getStatus() != null) {
            return emptyPage();
        }
        List<Long> activeRuleIds = selectActiveRuleIds(param.getPeriodType());
        if (activeRuleIds.isEmpty()) {
            return emptyPage();
        }
        List<FormAppraisalPreviewRecord> records = previewRecordMapper.selectList(buildPreviewQueryWrapper(param, activeRuleIds));
        List<FormAppraisalRecordGroupVO> groupRecords = buildPreviewGroupRecords(records);
        long total = groupRecords.size();
        long pages = total == 0 ? 0 : (total + param.getPageSize() - 1L) / param.getPageSize();
        return Pagination.<FormAppraisalRecordGroupVO>builder()
                .total(total)
                .pages(pages)
                .records(Paging.list(groupRecords, param.getPageNumber(), param.getPageSize()))
                .build();
    }

    private Pagination<FormAppraisalRecordGroupVO> emptyPage() {
        return Pagination.<FormAppraisalRecordGroupVO>builder()
                .total(0L)
                .pages(0L)
                .records(List.of())
                .build();
    }

    private LambdaQueryWrapper<FormAppraisalPreviewRecord> buildPreviewQueryWrapper(FormAppraisalRecordPageParam param,
                                                                                    List<Long> activeRuleIds
                                                                                   ) {
        return Wrappers.<FormAppraisalPreviewRecord>lambdaQuery()
                .like(StringUtils.isNotBlank(param.getKeyword()), FormAppraisalPreviewRecord::getName, param.getKeyword())
                .like(StringUtils.isNotBlank(param.getInspectionItem()), FormAppraisalPreviewRecord::getInspectionItem, param.getInspectionItem())
                .eq(FormAppraisalPreviewRecord::getPeriodType, param.getPeriodType())
                .in(FormAppraisalPreviewRecord::getRuleId, activeRuleIds)
                .apply(
                        param.getResponsibleDepartmentId() != null,
                        "find_in_set({0}, responsible_department_ids)",
                        param.getResponsibleDepartmentId()
                      )
                .eq(param.getFirstLevelFormGroupId() != null,
                    FormAppraisalPreviewRecord::getFirstLevelFormGroupId,
                    param.getFirstLevelFormGroupId())
                .eq(param.getSecondLevelFormGroupId() != null,
                    FormAppraisalPreviewRecord::getSecondLevelFormGroupId,
                    param.getSecondLevelFormGroupId())
                .eq(FormAppraisalPreviewRecord::getAssessmentYear, param.getAssessmentYear())
                .eq(FormAppraisalPreviewRecord::getAssessmentPeriod, param.getAssessmentPeriod())
                .ge(param.getGeneratedBeginTime() != null, FormAppraisalPreviewRecord::getGeneratedAt, param.getGeneratedBeginTime())
                .le(param.getGeneratedEndTime() != null, FormAppraisalPreviewRecord::getGeneratedAt, param.getGeneratedEndTime())
                .orderByDesc(FormAppraisalPreviewRecord::getGeneratedAt);
    }

    private List<FormAppraisalRecordExportVO> buildExportRows(FormAppraisalRecordPageParam param) {
        if (isCurrentPeriodParam(param)) {
            return buildPreviewExportRows(param);
        }
        List<FormAppraisalRecord> records = recordMapper.selectList(buildQueryWrapper(param));
        return records.stream().map(this::toExportVO).toList();
    }

    private List<FormAppraisalRecordExportVO> buildPreviewExportRows(FormAppraisalRecordPageParam param) {
        if (param.getStatus() != null) {
            return List.of();
        }
        List<Long> activeRuleIds = selectActiveRuleIds(param.getPeriodType());
        if (activeRuleIds.isEmpty()) {
            return List.of();
        }
        List<FormAppraisalPreviewRecord> records = previewRecordMapper.selectList(buildPreviewQueryWrapper(param, activeRuleIds));
        return records.stream().map(this::toExportVO).toList();
    }

    private FormAppraisalRecordExportVO toExportVO(FormAppraisalRecord record) {
        FormAppraisalRecordPageVO pageVO = toPageVO(record);
        fillDepartmentInfo(pageVO, record);

        FormAppraisalRecordExportVO vo = convertToExportVO(pageVO);

        if (pageVO.getPeriodType() != null) {
            vo.setPeriodTypeText(switch (pageVO.getPeriodType()) {
                case MONTHLY -> "月度";
                case QUARTERLY -> "季度";
                case HALF_YEAR -> "半年度";
            });
        }
        return vo;
    }

    private FormAppraisalRecordExportVO toExportVO(FormAppraisalPreviewRecord record) {
        FormAppraisalRecordPageVO pageVO = toPageVO(record);
        fillDepartmentInfo(pageVO, record);

        FormAppraisalRecordExportVO vo = convertToExportVO(pageVO);

        if (pageVO.getPeriodType() != null) {
            vo.setPeriodTypeText(switch (pageVO.getPeriodType()) {
                case MONTHLY -> "月度";
                case QUARTERLY -> "季度";
                case HALF_YEAR -> "半年度";
            });
        }
        return vo;
    }

    private void fillDepartmentInfo(FormAppraisalRecordPageVO vo, FormAppraisalRecord record) {
        vo.setResponsibleDepartmentIds(AppraisalDepartmentUtils.parseDepartmentIds(record.getResponsibleDepartmentIds()));
        vo.setResponsibleDepartmentNames(record.getResponsibleDepartmentNames());
    }

    private void fillDepartmentInfo(FormAppraisalRecordDetailVO vo, FormAppraisalRecord record) {
        vo.setResponsibleDepartmentIds(AppraisalDepartmentUtils.parseDepartmentIds(record.getResponsibleDepartmentIds()));
        vo.setResponsibleDepartmentNames(record.getResponsibleDepartmentNames());
    }

    private void fillDepartmentInfo(FormAppraisalRecordPageVO vo, FormAppraisalPreviewRecord record) {
        vo.setResponsibleDepartmentIds(AppraisalDepartmentUtils.parseDepartmentIds(record.getResponsibleDepartmentIds()));
        vo.setResponsibleDepartmentNames(record.getResponsibleDepartmentNames());
    }

    private void fillDepartmentInfo(FormAppraisalRecordDetailVO vo, FormAppraisalPreviewRecord record) {
        vo.setResponsibleDepartmentIds(AppraisalDepartmentUtils.parseDepartmentIds(record.getResponsibleDepartmentIds()));
        vo.setResponsibleDepartmentNames(record.getResponsibleDepartmentNames());
    }

    private FormAppraisalRecordPageVO toPageVO(FormAppraisalRecord record) {
        FormAppraisalRecordPageVO vo = new FormAppraisalRecordPageVO();
        vo.setId(record.getId());
        vo.setName(record.getName());
        vo.setInspectionItem(record.getInspectionItem());
        vo.setPeriodType(record.getPeriodType());
        vo.setAssessmentYear(record.getAssessmentYear());
        vo.setAssessmentPeriod(record.getAssessmentPeriod());
        vo.setAssessmentBeginTime(record.getAssessmentBeginTime());
        vo.setAssessmentEndTime(record.getAssessmentEndTime());
        vo.setFirstLevelFormGroupId(record.getFirstLevelFormGroupId());
        vo.setFirstLevelFormGroupName(record.getFirstLevelFormGroupName());
        vo.setSecondLevelFormGroupId(record.getSecondLevelFormGroupId());
        vo.setSecondLevelFormGroupName(record.getSecondLevelFormGroupName());
        vo.setOriginTotalScore(record.getOriginTotalScore());
        vo.setFinalTotalScore(record.getFinalTotalScore());
        vo.setOriginSpotCheckScore(record.getOriginSpotCheckScore());
        vo.setFinalSpotCheckScore(record.getFinalSpotCheckScore());
        vo.setOriginInspectionScore(record.getOriginInspectionScore());
        vo.setFinalInspectionScore(record.getFinalInspectionScore());
        vo.setStatus(record.getStatus());
        vo.setGeneratedAt(record.getGeneratedAt());
        return vo;
    }

    private FormAppraisalRecordPageVO toPageVO(FormAppraisalPreviewRecord record) {
        FormAppraisalRecordPageVO vo = new FormAppraisalRecordPageVO();
        vo.setId(record.getId());
        vo.setName(record.getName());
        vo.setInspectionItem(record.getInspectionItem());
        vo.setPeriodType(record.getPeriodType());
        vo.setAssessmentYear(record.getAssessmentYear());
        vo.setAssessmentPeriod(record.getAssessmentPeriod());
        vo.setAssessmentBeginTime(record.getAssessmentBeginTime());
        vo.setAssessmentEndTime(record.getAssessmentEndTime());
        vo.setFirstLevelFormGroupId(record.getFirstLevelFormGroupId());
        vo.setFirstLevelFormGroupName(record.getFirstLevelFormGroupName());
        vo.setSecondLevelFormGroupId(record.getSecondLevelFormGroupId());
        vo.setSecondLevelFormGroupName(record.getSecondLevelFormGroupName());
        vo.setOriginTotalScore(record.getOriginTotalScore());
        vo.setFinalTotalScore(record.getFinalTotalScore());
        vo.setOriginSpotCheckScore(record.getOriginSpotCheckScore());
        vo.setFinalSpotCheckScore(record.getFinalSpotCheckScore());
        vo.setOriginInspectionScore(record.getOriginInspectionScore());
        vo.setFinalInspectionScore(record.getFinalInspectionScore());
        vo.setGeneratedAt(record.getGeneratedAt());
        return vo;
    }

    private FormAppraisalRecordDetailVO toDetailVO(FormAppraisalRecord record) {
        FormAppraisalRecordDetailVO vo = new FormAppraisalRecordDetailVO();
        vo.setId(record.getId());
        vo.setRuleId(record.getRuleId());
        vo.setRuleVersion(record.getRuleVersion());
        vo.setName(record.getName());
        vo.setInspectionItem(record.getInspectionItem());
        vo.setPeriodType(record.getPeriodType());
        vo.setAssessmentYear(record.getAssessmentYear());
        vo.setAssessmentPeriod(record.getAssessmentPeriod());
        vo.setAssessmentBeginTime(record.getAssessmentBeginTime());
        vo.setAssessmentEndTime(record.getAssessmentEndTime());
        vo.setFirstLevelFormGroupId(record.getFirstLevelFormGroupId());
        vo.setFirstLevelFormGroupName(record.getFirstLevelFormGroupName());
        vo.setSecondLevelFormGroupId(record.getSecondLevelFormGroupId());
        vo.setSecondLevelFormGroupName(record.getSecondLevelFormGroupName());
        vo.setOriginTotalScore(record.getOriginTotalScore());
        vo.setOriginSpotCheckScore(record.getOriginSpotCheckScore());
        vo.setOriginInspectionScore(record.getOriginInspectionScore());
        vo.setOverrideFinalTotalScore(record.getOverrideFinalTotalScore());
        vo.setOverrideFinalSpotCheckScore(record.getOverrideFinalSpotCheckScore());
        vo.setOverrideFinalInspectionScore(record.getOverrideFinalInspectionScore());
        vo.setFinalTotalScore(record.getFinalTotalScore());
        vo.setFinalSpotCheckScore(record.getFinalSpotCheckScore());
        vo.setFinalInspectionScore(record.getFinalInspectionScore());
        vo.setStatus(record.getStatus());
        vo.setGeneratedAt(record.getGeneratedAt());
        vo.setAdjustRemark(record.getAdjustRemark());
        return vo;
    }

    private FormAppraisalRecordDetailVO toDetailVO(FormAppraisalPreviewRecord record) {
        FormAppraisalRecordDetailVO vo = new FormAppraisalRecordDetailVO();
        vo.setId(record.getId());
        vo.setRuleId(record.getRuleId());
        vo.setRuleVersion(record.getRuleVersion());
        vo.setName(record.getName());
        vo.setInspectionItem(record.getInspectionItem());
        vo.setPeriodType(record.getPeriodType());
        vo.setAssessmentYear(record.getAssessmentYear());
        vo.setAssessmentPeriod(record.getAssessmentPeriod());
        vo.setAssessmentBeginTime(record.getAssessmentBeginTime());
        vo.setAssessmentEndTime(record.getAssessmentEndTime());
        vo.setFirstLevelFormGroupId(record.getFirstLevelFormGroupId());
        vo.setFirstLevelFormGroupName(record.getFirstLevelFormGroupName());
        vo.setSecondLevelFormGroupId(record.getSecondLevelFormGroupId());
        vo.setSecondLevelFormGroupName(record.getSecondLevelFormGroupName());
        vo.setOriginTotalScore(record.getOriginTotalScore());
        vo.setOriginSpotCheckScore(record.getOriginSpotCheckScore());
        vo.setOriginInspectionScore(record.getOriginInspectionScore());
        vo.setFinalTotalScore(record.getFinalTotalScore());
        vo.setFinalSpotCheckScore(record.getFinalSpotCheckScore());
        vo.setFinalInspectionScore(record.getFinalInspectionScore());
        vo.setGeneratedAt(record.getGeneratedAt());
        return vo;
    }

    /**
     * 将分页 VO 转换为导出 VO
     *
     * @param pageVO 分页 VO 对象
     *
     * @return 导出 VO 对象
     */
    private FormAppraisalRecordExportVO convertToExportVO(FormAppraisalRecordPageVO pageVO) {
        FormAppraisalRecordExportVO vo = new FormAppraisalRecordExportVO();
        vo.setName(pageVO.getName());
        vo.setInspectionItem(pageVO.getInspectionItem());
        vo.setAssessmentYear(pageVO.getAssessmentYear());
        vo.setAssessmentBeginTime(pageVO.getAssessmentBeginTime());
        vo.setAssessmentEndTime(pageVO.getAssessmentEndTime());
        vo.setResponsibleDepartmentNames(pageVO.getResponsibleDepartmentNames());
        vo.setFirstLevelFormGroupName(pageVO.getFirstLevelFormGroupName());
        vo.setSecondLevelFormGroupName(pageVO.getSecondLevelFormGroupName());
        vo.setOriginTotalScore(pageVO.getOriginTotalScore());
        vo.setFinalTotalScore(pageVO.getFinalTotalScore());
        vo.setDeductScore(pageVO.getOriginTotalScore().subtract(pageVO.getFinalTotalScore()));
        vo.setGeneratedAt(pageVO.getGeneratedAt());
        return vo;
    }

    private FormAppraisalRecordDetailItemVO toDetailItemVO(FormAppraisalRecordDetail detail) {
        FormAppraisalRecordDetailItemVO vo = new FormAppraisalRecordDetailItemVO();
        vo.setId(detail.getId());
        vo.setContentType(detail.getContentType());
        vo.setSourceType(detail.getSourceType());
        vo.setSourceRefId(detail.getSourceRefId());
        vo.setSourceRefName(detail.getSourceRefName());
        vo.setContentDesc(detail.getContentDesc());
        vo.setStandardDesc(detail.getStandardDesc());
        vo.setItemName(detail.getItemName());
        vo.setFieldKey(detail.getFieldKey());
        vo.setFieldName(detail.getFieldName());
        vo.setCalcType(detail.getCalcType());
        vo.setRawValue(detail.getRawValue());
        vo.setMatchedRangeText(detail.getMatchedRangeText());
        vo.setDeductionReason(parseDeductionReason(detail));
        vo.setOriginDeductScore(detail.getOriginDeductScore());
        vo.setManualAdjustDeductScore(detail.getManualAdjustDeductScore());
        vo.setFinalDeductScore(detail.getFinalDeductScore());
        vo.setRemark(detail.getRemark());
        vo.setSort(detail.getSort());
        return vo;
    }

    private FormAppraisalRecordDetailItemVO toDetailItemVO(FormAppraisalPreviewRecordDetail detail) {
        FormAppraisalRecordDetailItemVO vo = new FormAppraisalRecordDetailItemVO();
        vo.setId(detail.getId());
        vo.setContentType(detail.getContentType());
        vo.setSourceType(detail.getSourceType());
        vo.setSourceRefId(detail.getSourceRefId());
        vo.setSourceRefName(detail.getSourceRefName());
        vo.setContentDesc(detail.getContentDesc());
        vo.setStandardDesc(detail.getStandardDesc());
        vo.setItemName(detail.getItemName());
        vo.setFieldKey(detail.getFieldKey());
        vo.setFieldName(detail.getFieldName());
        vo.setCalcType(detail.getCalcType());
        vo.setRawValue(detail.getRawValue());
        vo.setMatchedRangeText(detail.getMatchedRangeText());
        vo.setDeductionReason(parseDeductionReason(detail));
        vo.setOriginDeductScore(detail.getOriginDeductScore());
        vo.setManualAdjustDeductScore(detail.getManualAdjustDeductScore());
        vo.setFinalDeductScore(detail.getFinalDeductScore());
        vo.setRemark(detail.getRemark());
        vo.setSort(detail.getSort());
        return vo;
    }

    private FormAppraisalDeductionReasonVO parseDeductionReason(FormAppraisalRecordDetail detail) {
        String deductionReasonJson = detail.getDeductionReasonJson();
        if (deductionReasonJson == null || deductionReasonJson.isBlank()) {
            return null;
        }
        try {
            FormAppraisalDeductionReasonDTO dto = JSON.parseObject(deductionReasonJson, FormAppraisalDeductionReasonDTO.class);
            if (dto == null) {
                return null;
            }
            FormAppraisalDeductionReasonVO vo = new FormAppraisalDeductionReasonVO();
            vo.setSourceRefName(dto.getSourceRefName());
            vo.setFieldKey(dto.getFieldKey());
            vo.setFieldName(dto.getFieldName());
            vo.setFieldValue(dto.getFieldValue());
            vo.setMatchedRangeStartValue(dto.getMatchedRangeStartValue());
            vo.setMatchedRangeEndValue(dto.getMatchedRangeEndValue());
            vo.setDeductScore(dto.getDeductScore());
            return vo;
        } catch (Exception e) {
            log.warn("解析表单考核记录扣分原因失败, recordDetailId={}", detail.getId(), e);
            return null;
        }
    }

    private FormAppraisalDeductionReasonVO parseDeductionReason(FormAppraisalPreviewRecordDetail detail) {
        String deductionReasonJson = detail.getDeductionReasonJson();
        if (deductionReasonJson == null || deductionReasonJson.isBlank()) {
            return null;
        }
        try {
            FormAppraisalDeductionReasonDTO dto = JSON.parseObject(deductionReasonJson, FormAppraisalDeductionReasonDTO.class);
            if (dto == null) {
                return null;
            }
            FormAppraisalDeductionReasonVO vo = new FormAppraisalDeductionReasonVO();
            vo.setSourceRefName(dto.getSourceRefName());
            vo.setFieldKey(dto.getFieldKey());
            vo.setFieldName(dto.getFieldName());
            vo.setFieldValue(dto.getFieldValue());
            vo.setMatchedRangeStartValue(dto.getMatchedRangeStartValue());
            vo.setMatchedRangeEndValue(dto.getMatchedRangeEndValue());
            vo.setDeductScore(dto.getDeductScore());
            return vo;
        } catch (Exception e) {
            log.warn("解析表单考核预览记录扣分原因失败, previewRecordDetailId={}", detail.getId(), e);
            return null;
        }
    }

    private List<FormAppraisalRecordGroupVO> buildGroupRecords(List<FormAppraisalRecord> records) {
        Map<String, FormAppraisalRecordGroupVO> firstGroupMap = new LinkedHashMap<>();
        for (FormAppraisalRecord record : records) {
            FormAppraisalRecordGroupVO firstGroup = firstGroupMap.computeIfAbsent(
                    groupKey(record.getFirstLevelFormGroupId(), record.getFirstLevelFormGroupName()),
                    key -> buildGroupNode(record.getFirstLevelFormGroupId(), record.getFirstLevelFormGroupName(),
                                          record.getFirstLevelFormGroupId() == null ? null : 1)
                                                                                 );
            FormAppraisalRecordPageVO recordVO = toPageVO(record);
            fillDepartmentInfo(recordVO, record);
            if (Objects.isNull(record.getSecondLevelFormGroupId())) {
                firstGroup.getRecords().add(recordVO);
                continue;
            }
            FormAppraisalRecordGroupVO secondGroup = firstGroup.getChildren()
                    .stream()
                    .filter(group -> Objects.equals(group.getFormGroupId(), record.getSecondLevelFormGroupId()))
                    .findFirst()
                    .orElseGet(() -> {
                        FormAppraisalRecordGroupVO group = buildGroupNode(record.getSecondLevelFormGroupId(), record.getSecondLevelFormGroupName(), 2);
                        firstGroup.getChildren().add(group);
                        return group;
                    });
            secondGroup.getRecords().add(recordVO);
        }
        return new ArrayList<>(firstGroupMap.values());
    }

    private List<FormAppraisalRecordGroupVO> buildPreviewGroupRecords(List<FormAppraisalPreviewRecord> records) {
        Map<String, FormAppraisalRecordGroupVO> firstGroupMap = new LinkedHashMap<>();
        for (FormAppraisalPreviewRecord record : records) {
            FormAppraisalRecordGroupVO firstGroup = firstGroupMap.computeIfAbsent(
                    groupKey(record.getFirstLevelFormGroupId(), record.getFirstLevelFormGroupName()),
                    key -> buildGroupNode(record.getFirstLevelFormGroupId(), record.getFirstLevelFormGroupName(),
                                          record.getFirstLevelFormGroupId() == null ? null : 1)
                                                                                 );
            FormAppraisalRecordPageVO recordVO = toPageVO(record);
            fillDepartmentInfo(recordVO, record);
            if (Objects.isNull(record.getSecondLevelFormGroupId())) {
                firstGroup.getRecords().add(recordVO);
                continue;
            }
            FormAppraisalRecordGroupVO secondGroup = firstGroup.getChildren()
                    .stream()
                    .filter(group -> Objects.equals(group.getFormGroupId(), record.getSecondLevelFormGroupId()))
                    .findFirst()
                    .orElseGet(() -> {
                        FormAppraisalRecordGroupVO group = buildGroupNode(record.getSecondLevelFormGroupId(),
                                                                          record.getSecondLevelFormGroupName(),
                                                                          2);
                        firstGroup.getChildren().add(group);
                        return group;
                    });
            secondGroup.getRecords().add(recordVO);
        }
        return new ArrayList<>(firstGroupMap.values());
    }

    private FormAppraisalRecordGroupVO buildGroupNode(Long formGroupId, String formGroupName, Integer level) {
        FormAppraisalRecordGroupVO vo = new FormAppraisalRecordGroupVO();
        vo.setFormGroupId(formGroupId);
        vo.setFormGroupName(formGroupName);
        vo.setLevel(level);
        return vo;
    }

    private String groupKey(Long formGroupId, String formGroupName) {
        return formGroupId == null ? "null:" + formGroupName : String.valueOf(formGroupId);
    }

    private List<Integer> getPeriods(AppraisalPeriodTypeEnum periodType) {
        return switch (periodType) {
            case MONTHLY -> List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
            case QUARTERLY -> List.of(1, 2, 3, 4);
            case HALF_YEAR -> List.of(1, 2);
        };
    }

    private String buildPeriodLabel(Integer year, AppraisalPeriodTypeEnum periodType, Integer period) {
        return switch (periodType) {
            case MONTHLY -> year + "年" + period + "月";
            case QUARTERLY -> year + "年" + period + "季度";
            case HALF_YEAR -> year + "年" + (period == 1 ? "上半年" : "下半年");
        };
    }

    private BigDecimal safeScore(BigDecimal score) {
        return score == null ? BigDecimal.ZERO : score;
    }

    private FormAppraisalRecordDetailVO getPreviewByIdIfCurrent(Long id) {
        FormAppraisalPreviewRecord record = previewRecordMapper.selectById(id);
        if (record == null || !isCurrentPeriod(record.getPeriodType(), record.getAssessmentYear(), record.getAssessmentPeriod())) {
            return null;
        }
        FormAppraisalRecordDetailVO detailVO = toDetailVO(record);
        fillDepartmentInfo(detailVO, record);
        List<FormAppraisalPreviewRecordDetail> details = previewDetailMapper.selectList(
                Wrappers.<FormAppraisalPreviewRecordDetail>lambdaQuery()
                        .eq(FormAppraisalPreviewRecordDetail::getRecordId, id)
                        .orderByAsc(FormAppraisalPreviewRecordDetail::getSort)
                                                                                       );
        detailVO.setDetails(details.stream().map(this::toDetailItemVO).toList());
        return detailVO;
    }

    private boolean isCurrentPeriodParam(FormAppraisalRecordPageParam param) {
        return param != null &&
               param.getPeriodType() != null &&
               param.getAssessmentYear() != null &&
               param.getAssessmentPeriod() != null &&
               isCurrentPeriod(param.getPeriodType(), param.getAssessmentYear(), param.getAssessmentPeriod());
    }

    private boolean isCurrentPeriod(AppraisalPeriodTypeEnum periodType, Integer assessmentYear, Integer assessmentPeriod) {
        if (periodType == null || assessmentYear == null || assessmentPeriod == null) {
            return false;
        }
        AppraisalPeriodUtils.TargetPeriod currentPeriod = AppraisalPeriodUtils.currentPeriodUntil(periodType, LocalDateTime.now());
        return Objects.equals(assessmentYear, currentPeriod.getAssessmentYear()) &&
               Objects.equals(assessmentPeriod, currentPeriod.getAssessmentPeriod());
    }

    private List<Long> selectActiveRuleIds(AppraisalPeriodTypeEnum periodType) {
        return ruleMapper.selectList(Wrappers.<FormAppraisalRule>lambdaQuery()
                                             .select(FormAppraisalRule::getId)
                                             .eq(FormAppraisalRule::getPeriodType, periodType)
                                             .eq(FormAppraisalRule::getEnabled, true))
                .stream()
                .map(FormAppraisalRule::getId)
                .filter(Objects::nonNull)
                .toList();
    }

    private FormAppraisalStatisticsAggregateDTO selectCurrentPreviewStatistics(FormAppraisalRecordStatisticsParam param) {
        if (param.getStatus() != null) {
            return null;
        }
        AppraisalPeriodUtils.TargetPeriod currentPeriod = AppraisalPeriodUtils.currentPeriodUntil(param.getPeriodType(), LocalDateTime.now());
        if (!Objects.equals(param.getAssessmentYear(), currentPeriod.getAssessmentYear())) {
            return null;
        }
        List<Long> activeRuleIds = selectActiveRuleIds(param.getPeriodType());
        if (activeRuleIds.isEmpty()) {
            return null;
        }
        QueryWrapper<FormAppraisalPreviewRecord> queryWrapper = Wrappers.query();
        queryWrapper.select(
                        "assessment_period AS assessmentPeriod",
                        "IFNULL(SUM(origin_total_score), 0) AS originTotalScore",
                        "IFNULL(SUM(final_total_score), 0) AS finalTotalScore",
                        "IFNULL(SUM(origin_total_score - final_total_score), 0) AS deductScore",
                        "MIN(assessment_begin_time) AS assessmentBeginTime",
                        "MAX(assessment_end_time) AS assessmentEndTime",
                        "MAX(generated_at) AS previewGeneratedAt"
                           )
                .eq("assessment_year", currentPeriod.getAssessmentYear())
                .eq("assessment_period", currentPeriod.getAssessmentPeriod())
                .eq("period_type", param.getPeriodType())
                .in("rule_id", activeRuleIds)
                .like(StringUtils.isNotBlank(param.getKeyword()), "name", param.getKeyword())
                .like(StringUtils.isNotBlank(param.getInspectionItem()), "inspection_item", param.getInspectionItem())
                .apply(param.getResponsibleDepartmentId() != null,
                       "find_in_set({0}, responsible_department_ids)",
                       param.getResponsibleDepartmentId())
                .groupBy("assessment_period");
        List<Map<String, Object>> maps = previewRecordMapper.selectMaps(queryWrapper);
        if (maps == null || maps.isEmpty()) {
            return null;
        }
        Map<String, Object> map = maps.get(0);
        FormAppraisalStatisticsAggregateDTO dto = new FormAppraisalStatisticsAggregateDTO();
        dto.setAssessmentPeriod(this.toInteger(map.get("assessmentPeriod")));
        dto.setOriginTotalScore(this.toBigDecimal(map.get("originTotalScore")));
        dto.setFinalTotalScore(this.toBigDecimal(map.get("finalTotalScore")));
        dto.setDeductScore(this.toBigDecimal(map.get("deductScore")));
        dto.setAssessmentBeginTime(this.toLocalDateTime(map.get("assessmentBeginTime")));
        dto.setAssessmentEndTime(this.toLocalDateTime(map.get("assessmentEndTime")));
        dto.setPreviewGeneratedAt(this.toLocalDateTime(map.get("previewGeneratedAt")));
        return dto;
    }

    private List<FormAppraisalStatisticsAggregateDTO> selectPeriodStatistics(FormAppraisalRecordStatisticsParam param) {
        QueryWrapper<FormAppraisalRecord> queryWrapper = Wrappers.query();
        queryWrapper.select(
                        "assessment_period AS assessmentPeriod",
                        "IFNULL(SUM(origin_total_score), 0) AS originTotalScore",
                        "IFNULL(SUM(final_total_score), 0) AS finalTotalScore",
                        "IFNULL(SUM(origin_total_score - final_total_score), 0) AS deductScore"
                           )
                .eq("assessment_year", param.getAssessmentYear())
                .eq("period_type", param.getPeriodType())
                .like(StringUtils.isNotBlank(param.getKeyword()), "name", param.getKeyword())
                .like(StringUtils.isNotBlank(param.getInspectionItem()), "inspection_item", param.getInspectionItem())
                .apply(param.getResponsibleDepartmentId() != null,
                       "find_in_set({0}, responsible_department_ids)",
                       param.getResponsibleDepartmentId()
                      )
                .eq(param.getStatus() != null, "status", param.getStatus())
                .groupBy("assessment_period")
                .orderByAsc("assessment_period");
        List<Map<String, Object>> maps = recordMapper.selectMaps(queryWrapper);
        List<FormAppraisalStatisticsAggregateDTO> rows = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            FormAppraisalStatisticsAggregateDTO dto = new FormAppraisalStatisticsAggregateDTO();
            dto.setAssessmentPeriod(this.toInteger(map.get("assessmentPeriod")));
            dto.setOriginTotalScore(this.toBigDecimal(map.get("originTotalScore")));
            dto.setFinalTotalScore(this.toBigDecimal(map.get("finalTotalScore")));
            dto.setDeductScore(this.toBigDecimal(map.get("deductScore")));
            rows.add(dto);
        }
        return rows;
    }

    private Integer toInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(String.valueOf(value));
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        }
        return new BigDecimal(String.valueOf(value));
    }

    private LocalDateTime toLocalDateTime(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDateTime localDateTime) {
            return localDateTime;
        }
        if (value instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }
        return LocalDateTime.parse(String.valueOf(value).replace(" ", "T"));
    }
}
