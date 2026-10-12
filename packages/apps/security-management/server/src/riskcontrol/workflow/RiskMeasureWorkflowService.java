package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskControlMeasurePageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskControlMeasureParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskMeasureExecutionRecordParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskObjectMeasureSaveParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskControlMeasureVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskMeasureExecutionRecordVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskMeasureExecutionStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskObjectStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model.RiskPageAssembler;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model.RiskUnitControlStatusResolver;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskDataPermissionContext;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskExcelExportUtils;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskControlRefUtils;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade.FileFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade.MessageFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade.OuaaFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.entity.RiskObjectEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.service.impl.RiskObjectService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.convert.RiskMeasureConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.entity.RiskControlMeasureEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.entity.RiskMeasureAttachmentEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.entity.RiskMeasureExecutionRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.service.impl.RiskControlMeasureService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.service.impl.RiskMeasureAttachmentService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.service.impl.RiskMeasureExecutionRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.convert.RiskUnitConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.service.impl.RiskUnitService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service("securityManagementRiskMeasureWorkflowService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskMeasureWorkflowService {
    private static final Set<RiskObjectStatusEnum> MEASURE_CONFIGURABLE_STATUSES = Set.of(
            RiskObjectStatusEnum.CONTROL_SET,
            RiskObjectStatusEnum.EXECUTING,
            RiskObjectStatusEnum.ACTIVE
    );

    private final RiskObjectService objectService;
    private final RiskUnitService unitService;
    private final RiskControlMeasureService measureService;
    private final RiskMeasureExecutionRecordService executionRecordService;
    private final RiskMeasureAttachmentService attachmentService;
    private final FileFacadeService fileFacadeService;
    private final OuaaFacadeService ouaaFacadeService;
    private final MessageFacadeService messageFacadeService;

    public Pagination<RiskControlMeasureVO> measurePage(RiskControlMeasurePageParam param) {
        return measurePageInternal(param);
    }

    public Pagination<RiskControlMeasureVO> measureExecutionPage(RiskControlMeasurePageParam param) {
        return measurePageInternal(param);
    }

    private Pagination<RiskControlMeasureVO> measurePageInternal(RiskControlMeasurePageParam param) {
        Long currentUserId = "current".equals(param.getExecutor()) ? SecurityContextHolder.getPrincipalProperty(Principal::getId) : null;
        boolean hasObjectScopeFilter = hasObjectScopeFilter(param);
        List<Long> scopedObjectIds = scopedObjectIds(param, hasObjectScopeFilter);
        List<Long> keywordObjectIds = objectIdsByKeyword(param.getKeyword());
        if (hasObjectScopeFilter && CollectionUtils.isEmpty(scopedObjectIds)) {
            return Pagination.empty();
        }
        IPage<RiskControlMeasureEntity> page = measureService.page(PagingUtils.convert(param),
                measureQuery(param, currentUserId, scopedObjectIds, hasObjectScopeFilter, keywordObjectIds));
        Pagination<RiskControlMeasureVO> pagination = RiskPageAssembler.toPagination(page, RiskMeasureConvert::toVO);
        enrichMeasures(pagination.getRecords());
        return pagination;
    }

    public void exportMeasures(RiskControlMeasurePageParam param, HttpServletResponse response) {
        exportMeasuresInternal(param, response);
    }

    public void exportMeasureExecutions(RiskControlMeasurePageParam param, HttpServletResponse response) {
        exportMeasuresInternal(param, response);
    }

    private void exportMeasuresInternal(RiskControlMeasurePageParam param, HttpServletResponse response) {
        Long currentUserId = "current".equals(param.getExecutor()) ? SecurityContextHolder.getPrincipalProperty(Principal::getId) : null;
        boolean hasObjectScopeFilter = hasObjectScopeFilter(param);
        List<Long> scopedObjectIds = scopedObjectIds(param, hasObjectScopeFilter);
        List<Long> keywordObjectIds = objectIdsByKeyword(param.getKeyword());
        List<RiskControlMeasureVO> measures = hasObjectScopeFilter && CollectionUtils.isEmpty(scopedObjectIds) ? List.of()
                : measureService.list(measureQuery(param, currentUserId, scopedObjectIds, hasObjectScopeFilter, keywordObjectIds))
                .stream().map(RiskMeasureConvert::toVO).toList();
        enrichMeasures(measures);
        List<Map<String, Object>> rows = measures.stream().map(measure -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("措施ID", measure.getMeasureId());
            row.put("单元名称", measure.getUnit() == null ? "" : measure.getUnit().getName());
            row.put("风险对象", measure.getObjectName());
            row.put("管控类型", measure.getControlType());
            row.put("管控层级", measure.getControlLevel());
            row.put("执行人", measure.getExecutorUser() == null ? measure.getExecutorUserId() : measure.getExecutorUser().getUserName());
            row.put("管控措施内容", measure.getContent());
            row.put("执行状态", measure.getExecutionStatus());
            return row;
        }).toList();
        RiskExcelExportUtils.write(response, "风险管控措施", rows);
    }

    private LambdaQueryWrapper<RiskControlMeasureEntity> measureQuery(RiskControlMeasurePageParam param,
                                                                      Long currentUserId,
                                                                      List<Long> scopedObjectIds,
                                                                      boolean hasObjectScopeFilter,
                                                                      List<Long> keywordObjectIds) {
        List<String> statuses = splitValues(param.getStatus());
        return Wrappers.<RiskControlMeasureEntity>lambdaQuery()
                .eq(param.getObjectId() != null, RiskControlMeasureEntity::getObjectId, param.getObjectId())
                .in(hasObjectScopeFilter && CollectionUtils.isNotEmpty(scopedObjectIds),
                        RiskControlMeasureEntity::getObjectId, scopedObjectIds)
                .and(StringUtils.isNotBlank(param.getKeyword()), wrapper -> {
                    wrapper.like(RiskControlMeasureEntity::getContent, param.getKeyword())
                            .or()
                            .like(RiskControlMeasureEntity::getExecutorUserId, param.getKeyword());
                    if (CollectionUtils.isNotEmpty(keywordObjectIds)) {
                        wrapper.or().in(RiskControlMeasureEntity::getObjectId, keywordObjectIds);
                    }
                })
                .eq(StringUtils.isNotBlank(param.getControlType()), RiskControlMeasureEntity::getControlType, param.getControlType())
                .in(CollectionUtils.isNotEmpty(statuses), RiskControlMeasureEntity::getExecutionStatus, statuses)
                .eq(currentUserId != null, RiskControlMeasureEntity::getExecutorUserId, String.valueOf(currentUserId))
                .orderByDesc(RiskControlMeasureEntity::getId);
    }

    private List<Long> objectIdsByKeyword(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return List.of();
        }
        return objectService.list(Wrappers.<RiskObjectEntity>lambdaQuery()
                        .like(RiskObjectEntity::getObjectName, keyword.trim())
                        .or()
                        .like(RiskObjectEntity::getHazardDesc, keyword.trim()))
                .stream()
                .map(RiskObjectEntity::getId)
                .toList();
    }

    private List<String> splitValues(String value) {
        if (StringUtils.isBlank(value)) {
            return List.of();
        }
        return Arrays.stream(value.split("[,，]"))
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .toList();
    }

    private boolean hasObjectScopeFilter(RiskControlMeasurePageParam param) {
        return param.getUnitId() != null || StringUtils.isNotBlank(param.getRiskLevel());
    }

    private List<Long> scopedObjectIds(RiskControlMeasurePageParam param, boolean hasObjectScopeFilter) {
        if (!hasObjectScopeFilter) {
            return List.of();
        }
        return objectService.list(Wrappers.<RiskObjectEntity>lambdaQuery()
                        .eq(param.getUnitId() != null, RiskObjectEntity::getUnitId, param.getUnitId())
                        .eq(StringUtils.isNotBlank(param.getRiskLevel()),
                                RiskObjectEntity::getControlRiskLevel, param.getRiskLevel()))
                .stream()
                .map(RiskObjectEntity::getId)
                .toList();
    }

    public List<RiskControlMeasureVO> objectMeasures(Long objectId) {
        List<RiskControlMeasureVO> measures = measureService.list(Wrappers.<RiskControlMeasureEntity>lambdaQuery()
                        .eq(RiskControlMeasureEntity::getObjectId, objectId)
                        .orderByDesc(RiskControlMeasureEntity::getId))
                .stream().map(RiskMeasureConvert::toVO).toList();
        enrichMeasures(measures);
        return measures;
    }

    public RiskControlMeasureVO measureDetail(Long measureId) {
        RiskControlMeasureEntity measure = measureService.getById(measureId);
        Assert.isNotFound(measure == null, "管控措施不存在");
        RiskControlMeasureVO vo = RiskMeasureConvert.toVO(measure);
        List<RiskMeasureExecutionRecordEntity> records = executionRecordService.list(Wrappers.<RiskMeasureExecutionRecordEntity>lambdaQuery()
                .eq(RiskMeasureExecutionRecordEntity::getMeasureId, measureId)
                .orderByDesc(RiskMeasureExecutionRecordEntity::getExecutionTime));
        List<Long> recordIds = records.stream().map(RiskMeasureExecutionRecordEntity::getId).toList();
        Map<Long, List<String>> fileMap = CollectionUtils.isEmpty(recordIds) ? Map.of()
                : attachmentService.list(Wrappers.<RiskMeasureAttachmentEntity>lambdaQuery()
                .in(RiskMeasureAttachmentEntity::getExecutionRecordId, recordIds))
                .stream().collect(Collectors.groupingBy(RiskMeasureAttachmentEntity::getExecutionRecordId,
                        Collectors.mapping(RiskMeasureAttachmentEntity::getFileId, Collectors.toList())));
        vo.setExecutionRecords(records.stream().map(record -> {
            RiskMeasureExecutionRecordVO recordVO = RiskMeasureConvert.toVO(record);
            recordVO.setFileIds(fileMap.getOrDefault(record.getId(), List.of()));
            return recordVO;
        }).toList());
        enrichMeasures(List.of(vo));
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<RiskControlMeasureVO> saveObjectMeasures(Long objectId, RiskObjectMeasureSaveParam param) {
        RiskObjectEntity object = objectService.getById(objectId);
        Assert.isNotFound(object == null, "风险对象不存在");
        RiskObjectStatusEnum objectStatus = RiskObjectStatusEnum.valueOf(object.getStatus());
        Assert.isNotAcceptable(!MEASURE_CONFIGURABLE_STATUSES.contains(objectStatus), "仅管控配置、措施执行中或管控中状态对象可以配置措施");
        Assert.isParameterError(CollectionUtils.isEmpty(param.getMeasures()), "至少配置一条管控措施");
        validateMeasureParams(param.getMeasures());
        List<RiskControlMeasureEntity> existing = measureService.list(Wrappers.<RiskControlMeasureEntity>lambdaQuery()
                .eq(RiskControlMeasureEntity::getObjectId, objectId));
        Map<Long, RiskControlMeasureEntity> existingMap = existing.stream()
                .collect(Collectors.toMap(RiskControlMeasureEntity::getId, measure -> measure, (left, right) -> left));
        Set<Long> submittedIds = new LinkedHashSet<>();
        List<RiskControlMeasureEntity> createMeasures = new ArrayList<>();
        List<RiskControlMeasureEntity> updateMeasures = new ArrayList<>();
        List<RiskControlMeasureEntity> notifyMeasures = new ArrayList<>();
        for (RiskControlMeasureParam measureParam : param.getMeasures()) {
            Long measureId = measureParam.getId();
            if (measureId == null) {
                RiskControlMeasureEntity measure = RiskMeasureConvert.toMeasureEntity(objectId, measureParam);
                measure.setId(IdWorker.getId());
                createMeasures.add(measure);
                notifyMeasures.add(measure);
                continue;
            }
            Assert.isParameterError(!submittedIds.add(measureId), "管控措施 ID 不能重复");
            RiskControlMeasureEntity measure = existingMap.get(measureId);
            Assert.isNotFound(measure == null, "管控措施不存在或不属于当前风险对象");
            boolean changed = !Objects.equals(measure.getControlType(), measureParam.getControlType())
                    || !Objects.equals(measure.getControlLevel(), measureParam.getControlLevel())
                    || !Objects.equals(measure.getExecutorUserId(), measureParam.getExecutorUserId())
                    || !Objects.equals(measure.getContent(), measureParam.getContent());
            measure.setControlType(measureParam.getControlType());
            measure.setControlLevel(measureParam.getControlLevel());
            measure.setExecutorUserId(measureParam.getExecutorUserId());
            measure.setContent(measureParam.getContent());
            updateMeasures.add(measure);
            if (changed) {
                notifyMeasures.add(measure);
            }
        }
        List<RiskControlMeasureEntity> removedMeasures = existing.stream()
                .filter(measure -> !submittedIds.contains(measure.getId()))
                .toList();
        if (CollectionUtils.isNotEmpty(removedMeasures)) {
            DatabaseContextHolder.softDelete(measureService, removedMeasures);
        }
        if (CollectionUtils.isNotEmpty(updateMeasures)) {
            measureService.updateBatchById(updateMeasures);
        }
        if (CollectionUtils.isNotEmpty(createMeasures)) {
            measureService.saveBatch(createMeasures);
        }
        if (param.getSubmitted() == null || Boolean.TRUE.equals(param.getSubmitted())) {
            object.setStatus(RiskObjectStatusEnum.EXECUTING.name());
            objectService.updateById(object);
            refreshUnitControlStatus(List.of(object.getUnitId()));
            messageFacadeService.sendControlMeasureTask(objectId, notifyMeasures);
        }
        List<RiskControlMeasureEntity> measures = measureService.list(Wrappers.<RiskControlMeasureEntity>lambdaQuery()
                .eq(RiskControlMeasureEntity::getObjectId, objectId)
                .orderByDesc(RiskControlMeasureEntity::getId));
        List<RiskControlMeasureVO> vos = measures.stream().map(RiskMeasureConvert::toVO).toList();
        enrichMeasures(vos);
        return vos;
    }

    private void validateMeasureParams(List<RiskControlMeasureParam> measures) {
        for (int i = 0; i < measures.size(); i++) {
            RiskControlMeasureParam measure = measures.get(i);
            String prefix = "第" + (i + 1) + "条管控措施";
            Assert.isParameterError(measure == null, prefix + "不能为空");
            Assert.isParameterError(StringUtils.isBlank(measure.getControlType()), prefix + "管控类型不能为空");
            Assert.isParameterError(StringUtils.isBlank(measure.getControlLevel()), prefix + "管控层级不能为空");
            Assert.isParameterError(StringUtils.isBlank(measure.getExecutorUserId()), prefix + "执行人不能为空");
            Assert.isParameterError(StringUtils.isBlank(measure.getContent()), prefix + "内容不能为空");
            measure.setControlType(measure.getControlType().trim());
            measure.setControlLevel(measure.getControlLevel().trim());
            measure.setExecutorUserId(measure.getExecutorUserId().trim());
            measure.setContent(measure.getContent().trim());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskMeasureExecutionRecordVO createExecutionRecord(Long measureId, RiskMeasureExecutionRecordParam param) {
        List<RiskMeasureExecutionRecordVO> records = createExecutionRecords(List.of(toExecutionParam(measureId, param)));
        return records.get(0);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<RiskMeasureExecutionRecordVO> createExecutionRecords(List<RiskMeasureExecutionRecordParam> params) {
        Assert.isParameterError(CollectionUtils.isEmpty(params), "执行反馈不能为空");
        List<Long> measureIds = params.stream().map(RiskMeasureExecutionRecordParam::getMeasureId).filter(Objects::nonNull).distinct().toList();
        Assert.isParameterError(measureIds.size() != params.size(), "管控措施 ID 不能为空且不能重复");
        List<RiskControlMeasureEntity> measures = measureService.listByIds(measureIds).stream().toList();
        Assert.isNotFound(measures.size() != measureIds.size(), "部分管控措施不存在");
        Map<Long, RiskControlMeasureEntity> measureMap = measures.stream().collect(Collectors.toMap(RiskControlMeasureEntity::getId, measure -> measure));
        List<Long> objectIds = measures.stream().map(RiskControlMeasureEntity::getObjectId).distinct().toList();
        Map<Long, RiskObjectEntity> objectMap = objectService.listByIds(objectIds).stream()
                .collect(Collectors.toMap(RiskObjectEntity::getId, object -> object));
        measures.forEach(measure -> {
            RiskObjectEntity object = objectMap.get(measure.getObjectId());
            Assert.isNotFound(object == null, "风险对象不存在");
            Assert.isNotAcceptable(!Objects.equals(object.getStatus(), RiskObjectStatusEnum.EXECUTING.name()),
                    "仅措施执行中对象可以提交执行反馈");
        });
        params.forEach(param -> {
            List<String> fileIds = fileFacadeService.normalizeAndValidate(param.getFileIds());
            Assert.isParameterError(fileIds.size() > 9, "执行照片最多 9 张");
            param.setFileIds(fileIds);
        });
        Long currentUserId = SecurityContextHolder.getPrincipalProperty(Principal::getId);
        String currentExecutorUserId = currentUserId == null ? null : String.valueOf(currentUserId);
        if (StringUtils.isNotBlank(currentExecutorUserId)) {
            measures.forEach(measure -> Assert.isNotAcceptable(!Objects.equals(measure.getExecutorUserId(), currentExecutorUserId),
                    "当前用户不是措施执行人，不能提交执行反馈"));
        }
        params.forEach(param -> {
            Assert.isParameterError(StringUtils.isBlank(param.getExecutionDesc()), "执行说明不能为空");
            param.setExecutionDesc(param.getExecutionDesc().trim());
            RiskControlMeasureEntity measure = measureMap.get(param.getMeasureId());
            param.setExecutorUserId(StringUtils.isNotBlank(currentExecutorUserId)
                    ? currentExecutorUserId : measure.getExecutorUserId());
        });
        List<RiskMeasureExecutionRecordEntity> records = params.stream()
                .map(param -> RiskMeasureConvert.toExecutionRecordEntity(param.getMeasureId(), param))
                .peek(record -> record.setId(IdWorker.getId()))
                .toList();
        executionRecordService.saveBatch(records);
        List<RiskMeasureAttachmentEntity> attachments = new ArrayList<>();
        for (int i = 0; i < params.size(); i++) {
            RiskMeasureExecutionRecordParam param = params.get(i);
            RiskMeasureExecutionRecordEntity record = records.get(i);
            if (CollectionUtils.isNotEmpty(param.getFileIds())) {
                attachments.addAll(param.getFileIds().stream()
                        .map(fileId -> RiskMeasureConvert.toAttachmentEntity(record.getId(), fileId))
                        .peek(attachment -> attachment.setId(IdWorker.getId()))
                        .toList());
            }
            RiskControlMeasureEntity measure = measureMap.get(param.getMeasureId());
            measure.setExecutionStatus(resolveExecutionStatus(param.getExecutionResult()).name());
        }
        if (CollectionUtils.isNotEmpty(attachments)) {
            attachmentService.saveBatch(attachments);
        }
        measureService.updateBatchById(measures);
        refreshObjectStatusAfterExecution(measures);
        for (int i = 0; i < params.size(); i++) {
            RiskMeasureExecutionRecordParam param = params.get(i);
            if (CollectionUtils.isNotEmpty(param.getFileIds())) {
                fileFacadeService.induce(FileFacadeService.SCENE_RISK_MEASURE_EXECUTION_ATTACHMENT,
                        records.get(i).getId(), param.getFileIds());
            }
        }
        List<RiskMeasureExecutionRecordVO> vos = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            RiskMeasureExecutionRecordVO vo = RiskMeasureConvert.toVO(records.get(i));
            vo.setFileIds(params.get(i).getFileIds());
            vos.add(vo);
        }
        enrichExecutionRecords(vos);
        return vos;
    }

    private void enrichMeasures(List<RiskControlMeasureVO> measures) {
        if (CollectionUtils.isEmpty(measures)) {
            return;
        }
        List<Long> objectIds = measures.stream().map(RiskControlMeasureVO::getObjectId).filter(Objects::nonNull).distinct().toList();
        Map<Long, RiskObjectEntity> objectMap = RiskDataPermissionContext.withoutDataPermission(() ->
                CollectionUtils.isEmpty(objectIds) ? Map.<Long, RiskObjectEntity>of()
                        : objectService.listByIds(objectIds).stream().collect(Collectors.toMap(
                                RiskObjectEntity::getId, object -> object, (left, right) -> left)));
        List<Long> unitIds = objectMap.values().stream().map(RiskObjectEntity::getUnitId).filter(Objects::nonNull).distinct().toList();
        Map<Long, RiskUnitEntity> unitMap = RiskDataPermissionContext.withoutDataPermission(() ->
                CollectionUtils.isEmpty(unitIds) ? Map.<Long, RiskUnitEntity>of()
                        : unitService.listByIds(unitIds).stream().collect(Collectors.toMap(
                                RiskUnitEntity::getId, unit -> unit, (left, right) -> left)));
        Set<String> userIds = new LinkedHashSet<>();
        measures.forEach(measure -> {
            RiskControlRefUtils.addId(userIds, measure.getExecutorUserId());
            if (CollectionUtils.isNotEmpty(measure.getExecutionRecords())) {
                measure.getExecutionRecords()
                        .forEach(record -> RiskControlRefUtils.addId(userIds, record.getExecutorUserId()));
            }
        });
        Map<String, String> userNames = ouaaFacadeService.batchUserNames(userIds);
        measures.forEach(measure -> {
            RiskObjectEntity object = objectMap.get(measure.getObjectId());
            if (object != null) {
                measure.setObjectCode(object.getObjectCode());
                measure.setObjectName(object.getObjectName());
                measure.setUnitId(object.getUnitId());
                measure.setInherentRiskLevel(object.getInherentRiskLevel());
                measure.setControlRiskLevel(object.getControlRiskLevel());
                RiskUnitEntity unit = unitMap.get(object.getUnitId());
                if (unit != null) {
                    measure.setUnit(RiskUnitConvert.toOption(unit));
                }
            }
            measure.setExecutorUser(RiskControlRefUtils.toUser(measure.getExecutorUserId(), userNames));
            if (CollectionUtils.isNotEmpty(measure.getExecutionRecords())) {
                measure.getExecutionRecords().forEach(record -> record.setExecutorUser(
                        RiskControlRefUtils.toUser(record.getExecutorUserId(), userNames)));
                enrichExecutionFiles(measure.getExecutionRecords());
            }
        });
    }

    private void enrichExecutionRecords(List<RiskMeasureExecutionRecordVO> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        Set<String> userIds = new LinkedHashSet<>();
        records.forEach(record -> RiskControlRefUtils.addId(userIds, record.getExecutorUserId()));
        Map<String, String> userNames = ouaaFacadeService.batchUserNames(userIds);
        records.forEach(record -> record.setExecutorUser(RiskControlRefUtils.toUser(record.getExecutorUserId(), userNames)));
        enrichExecutionFiles(records);
    }

    private void enrichExecutionFiles(List<RiskMeasureExecutionRecordVO> records) {
        Set<String> fileIds = new LinkedHashSet<>();
        records.forEach(record -> {
            if (record.getFileIds() != null) {
                record.getFileIds().forEach(fileId -> addFileId(fileIds, fileId));
            }
        });
        Map<String, FileVO> fileMap = fileFacadeService.batchFiles(fileIds);
        records.forEach(record -> {
            if (record.getFileIds() != null) {
                record.setFiles(record.getFileIds().stream()
                        .map(this::normalizeFileId)
                        .map(fileMap::get)
                        .filter(file -> file != null)
                        .toList());
            }
        });
    }

    private void addFileId(Set<String> fileIds, String fileId) {
        if (fileId != null && !fileId.isBlank()) {
            fileIds.add(normalizeFileId(fileId));
        }
    }

    private String normalizeFileId(String fileId) {
        if (fileId == null || fileId.isBlank()) {
            return null;
        }
        try {
            return String.valueOf(Long.valueOf(fileId.trim()));
        } catch (NumberFormatException ex) {
            return fileId.trim();
        }
    }

    private RiskMeasureExecutionStatusEnum resolveExecutionStatus(String executionResult) {
        Assert.isParameterError(executionResult == null || executionResult.isBlank(), "措施执行结果不能为空");
        String normalized = executionResult.trim().toUpperCase(Locale.ROOT);
        return switch (normalized) {
            case "COMPLETED", "DONE", "EXECUTED", "FINISHED", "SUCCESS", "Y", "YES", "已完成", "已执行" ->
                    RiskMeasureExecutionStatusEnum.COMPLETED;
            case "EXECUTING", "IN_PROGRESS", "PARTIAL", "PARTLY", "PARTIAL_COMPLETED", "部分完成", "部分执行", "执行中" ->
                    RiskMeasureExecutionStatusEnum.EXECUTING;
            case "PENDING", "NOT_EXECUTED", "TODO", "N", "NO", "未完成", "未执行" ->
                    RiskMeasureExecutionStatusEnum.PENDING;
            default -> {
                try {
                    yield RiskMeasureExecutionStatusEnum.valueOf(normalized);
                } catch (IllegalArgumentException e) {
                    Assert.isParameterError(true, "措施执行结果非法");
                    yield RiskMeasureExecutionStatusEnum.PENDING;
                }
            }
        };
    }

    private RiskMeasureExecutionRecordParam toExecutionParam(Long measureId, RiskMeasureExecutionRecordParam param) {
        param.setMeasureId(measureId);
        return param;
    }

    private void refreshObjectStatusAfterExecution(List<RiskControlMeasureEntity> changedMeasures) {
        List<Long> objectIds = changedMeasures.stream().map(RiskControlMeasureEntity::getObjectId).distinct().toList();
        List<RiskControlMeasureEntity> allMeasures = measureService.list(Wrappers.<RiskControlMeasureEntity>lambdaQuery()
                .in(RiskControlMeasureEntity::getObjectId, objectIds));
        Map<Long, List<RiskControlMeasureEntity>> measureMap = allMeasures.stream().collect(Collectors.groupingBy(RiskControlMeasureEntity::getObjectId));
        List<RiskObjectEntity> objects = objectService.listByIds(objectIds).stream().toList();
        List<RiskObjectEntity> activeObjects = objects.stream().filter(object -> {
            List<RiskControlMeasureEntity> objectMeasures = measureMap.getOrDefault(object.getId(), List.of());
            return CollectionUtils.isNotEmpty(objectMeasures) && objectMeasures.stream()
                    .allMatch(measure -> Objects.equals(measure.getExecutionStatus(), RiskMeasureExecutionStatusEnum.COMPLETED.name()));
        }).peek(object -> object.setStatus(RiskObjectStatusEnum.ACTIVE.name())).toList();
        if (CollectionUtils.isNotEmpty(activeObjects)) {
            objectService.updateBatchById(activeObjects);
        }
        List<Long> unitIds = objects.stream()
                .map(RiskObjectEntity::getUnitId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        refreshUnitControlStatus(unitIds);
    }

    private void refreshUnitControlStatus(List<Long> unitIds) {
        List<Long> ids = unitIds.stream().filter(Objects::nonNull).distinct().toList();
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        List<RiskUnitEntity> units = unitService.listByIds(ids).stream().toList();
        List<RiskObjectEntity> objects = objectService.list(Wrappers.<RiskObjectEntity>lambdaQuery()
                .in(RiskObjectEntity::getUnitId, ids));
        Map<Long, List<RiskObjectEntity>> objectMap = objects.stream()
                .collect(Collectors.groupingBy(RiskObjectEntity::getUnitId));
        units.forEach(unit -> unit.setControlStatus(
                RiskUnitControlStatusResolver.resolve(objectMap.getOrDefault(unit.getId(), List.of()))));
        unitService.updateBatchById(units);
    }
}
