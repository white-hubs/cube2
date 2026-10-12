package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskIdentificationObjectParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskIdentificationPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskIdentificationSubmitParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskObjectPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskIdentificationRecordVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskObjectVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.entity.RiskAssessmentRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.service.impl.RiskAssessmentRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskIdentificationOperationTypeEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskObjectStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskUnitIdentifyStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model.RiskPageAssembler;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskControlCodeUtils;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskControlCodeUtils.CodeType;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskDataPermissionContext;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskControlRefUtils;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade.MessageFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade.OuaaFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.convert.RiskIdentificationConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.entity.RiskIdentificationRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.entity.RiskObjectEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.service.impl.RiskIdentificationRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.service.impl.RiskObjectService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.entity.RiskControlMeasureEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.entity.RiskMeasureAttachmentEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.entity.RiskMeasureExecutionRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.service.impl.RiskControlMeasureService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.service.impl.RiskMeasureAttachmentService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.service.impl.RiskMeasureExecutionRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.review.entity.RiskReviewRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.review.service.impl.RiskReviewRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.convert.RiskUnitConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitIdentifierRelEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.service.impl.RiskUnitIdentifierRelService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.service.impl.RiskUnitService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("securityManagementRiskIdentificationWorkflowService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskIdentificationWorkflowService {
    private final RiskUnitService unitService;
    private final RiskObjectService objectService;
    private final RiskIdentificationRecordService identificationRecordService;
    private final RiskAssessmentRecordService assessmentRecordService;
    private final RiskControlMeasureService controlMeasureService;
    private final RiskMeasureExecutionRecordService executionRecordService;
    private final RiskMeasureAttachmentService attachmentService;
    private final RiskReviewRecordService reviewRecordService;
    private final MessageFacadeService messageFacadeService;
    private final OuaaFacadeService ouaaFacadeService;
    private final RiskUnitIdentifierRelService unitIdentifierRelService;

    @Transactional(rollbackFor = Exception.class)
    public List<RiskObjectVO> submit(Long unitId, RiskIdentificationSubmitParam param) {
        RiskUnitEntity unit = unitService.getById(unitId);
        Assert.isNotFound(unit == null, "风险单元不存在");
        List<RiskIdentificationObjectParam> objectParams = param.getObjects() == null ? List.of() : param.getObjects();
        long effectiveCount = objectParams.stream().filter(object -> !Boolean.TRUE.equals(object.getDeleted())).count();
        Assert.isParameterError(effectiveCount == 0, "至少提交一个风险对象");
        Assert.isParameterError(effectiveCount > 5, "风险对象最多 5 个");

        String identifyStatus = unit.getIdentifyStatus();
        boolean identifying = identifyStatus == null || Objects.equals(identifyStatus, RiskUnitIdentifyStatusEnum.IDENTIFYING.name());
        boolean pendingReview = Objects.equals(identifyStatus, RiskUnitIdentifyStatusEnum.PENDING_REVIEW.name());
        Assert.isNotAcceptable(Objects.equals(identifyStatus, RiskUnitIdentifyStatusEnum.REVIEWED.name()),
                "已审核单元需等待重新辨识周期开始后再提交");
        Assert.isNotAcceptable(!identifying && !pendingReview, "当前风险单元状态不允许提交辨识结果");
        String submitUserId = resolveSubmitUserId(objectParams);
        if (StringUtils.isNotBlank(submitUserId)) {
            Assert.isNotAcceptable(!unitIdentifierUserIds(unitId).contains(submitUserId),
                    "当前用户不是该风险单元辨识人员，不能提交辨识");
            objectParams.forEach(object -> object.setUserId(submitUserId));
        }

        String roundNo = unit.getCurrentIdentifyRoundNo();
        if (roundNo == null || roundNo.isBlank()) {
            roundNo = "RID-" + IdWorker.getId();
            unit.setCurrentIdentifyRoundNo(roundNo);
        }

        List<Long> objectIds = objectParams.stream().map(RiskIdentificationObjectParam::getObjectId).filter(Objects::nonNull).toList();
        Map<Long, RiskObjectEntity> existingMap = CollectionUtils.isEmpty(objectIds) ? Map.of()
                : objectService.listByIds(objectIds).stream().collect(Collectors.toMap(RiskObjectEntity::getId, Function.identity()));

        List<RiskObjectEntity> saveObjects = new ArrayList<>();
        List<RiskObjectEntity> updateObjects = new ArrayList<>();
        List<RiskIdentificationRecordEntity> records = new ArrayList<>();
        List<RiskObjectEntity> deleteObjects = new ArrayList<>();
        Set<String> submittedObjectCodes = new HashSet<>();

        for (RiskIdentificationObjectParam objectParam : objectParams) {
            if (objectParam.getObjectId() == null) {
                Assert.isNotAcceptable(!identifying, "审核中单元仅允许退回对象重新提交");
                RiskObjectEntity object = RiskIdentificationConvert.toEntity(unitId, roundNo, objectParam);
                Long objectId = IdWorker.getId();
                object.setId(objectId);
                String objectCode = RiskControlCodeUtils.resolve(objectParam.getCode(), CodeType.RISK_OBJECT, objectId);
                Assert.isConflict(!submittedObjectCodes.add(objectCode), "风险对象编号重复");
                assertObjectCodeAvailable(objectCode, null);
                object.setObjectCode(objectCode);
                saveObjects.add(object);
                records.add(toRecord(object, RiskIdentificationOperationTypeEnum.CREATE, objectParam));
            } else {
                RiskObjectEntity object = existingMap.get(objectParam.getObjectId());
                Assert.isNotFound(object == null, "风险对象不存在");
                Assert.isNotAcceptable(!Objects.equals(object.getUnitId(), unitId), "风险对象不属于当前风险单元");
                if (Boolean.TRUE.equals(objectParam.getDeleted())) {
                    Assert.isNotAcceptable(pendingReview && !Objects.equals(object.getStatus(), RiskObjectStatusEnum.DRAFT.name()),
                            "审核中单元仅允许删除退回对象");
                    deleteObjects.add(object);
                    records.add(toRecord(object, RiskIdentificationOperationTypeEnum.DELETE, objectParam));
                } else {
                    Assert.isNotAcceptable(pendingReview && !Objects.equals(object.getStatus(), RiskObjectStatusEnum.DRAFT.name()),
                            "审核中单元仅允许退回对象重新提交");
                    if (objectParam.getCode() != null && !objectParam.getCode().isBlank()) {
                        String objectCode = objectParam.getCode().trim();
                        Assert.isConflict(!submittedObjectCodes.add(objectCode), "风险对象编号重复");
                        assertObjectCodeAvailable(objectCode, object.getId());
                        object.setObjectCode(objectCode);
                    }
                    RiskIdentificationConvert.updateEntity(object, objectParam);
                    object.setStatus(RiskObjectStatusEnum.PENDING_REVIEW.name());
                    object.setIdentifyRoundNo(roundNo);
                    updateObjects.add(object);
                    records.add(toRecord(object, RiskIdentificationOperationTypeEnum.UPDATE, objectParam));
                }
            }
        }

        if (CollectionUtils.isNotEmpty(saveObjects)) {
            objectService.saveBatch(saveObjects);
        }
        if (CollectionUtils.isNotEmpty(updateObjects)) {
            objectService.updateBatchById(updateObjects);
        }
        if (CollectionUtils.isNotEmpty(deleteObjects)) {
            softDeleteRiskObjectCascade(deleteObjects);
        }
        identificationRecordService.saveBatch(records);

        unit.setIdentifyStatus(RiskUnitIdentifyStatusEnum.PENDING_REVIEW.name());
        unit.setLastIdentifySubmitTime(LocalDateTime.now());
        unitService.updateById(unit);
        messageFacadeService.sendIdentificationSubmitted(unitId, records);

        List<RiskObjectVO> objects = objectService.list(Wrappers.<RiskObjectEntity>lambdaQuery()
                        .eq(RiskObjectEntity::getUnitId, unitId)
                        .eq(RiskObjectEntity::getIdentifyRoundNo, roundNo))
                .stream().map(RiskIdentificationConvert::toVO).toList();
        enrichObjects(objects);
        return objects;
    }

    public Pagination<RiskIdentificationRecordVO> identificationPage(RiskIdentificationPageParam param) {
        List<Long> areaUnitIds = unitIdsByArea(param.getAreaId());
        if (param.getAreaId() != null && CollectionUtils.isEmpty(areaUnitIds)) {
            return Pagination.empty();
        }
        List<Long> keywordObjectIds = objectIdsByKeyword(param.getKeyword());
        if (StringUtils.isNotBlank(param.getKeyword()) && CollectionUtils.isEmpty(keywordObjectIds)) {
            return Pagination.empty();
        }
        IPage<RiskIdentificationRecordEntity> page = identificationRecordService.page(PagingUtils.convert(param),
                identificationQuery(param, areaUnitIds, keywordObjectIds));
        Pagination<RiskIdentificationRecordVO> pagination = RiskPageAssembler.toPagination(page, RiskIdentificationConvert::toVO);
        RiskDataPermissionContext.withoutDataPermission(() -> enrichIdentificationRecords(pagination.getRecords()));
        return pagination;
    }

    private LambdaQueryWrapper<RiskIdentificationRecordEntity> identificationQuery(RiskIdentificationPageParam param,
                                                                                  List<Long> areaUnitIds,
                                                                                  List<Long> keywordObjectIds) {
        return Wrappers.<RiskIdentificationRecordEntity>lambdaQuery()
                .eq(param.getUnitId() != null, RiskIdentificationRecordEntity::getUnitId, param.getUnitId())
                .in(param.getAreaId() != null && CollectionUtils.isNotEmpty(areaUnitIds),
                        RiskIdentificationRecordEntity::getUnitId, areaUnitIds)
                .eq(param.getObjectId() != null, RiskIdentificationRecordEntity::getObjectId, param.getObjectId())
                .in(StringUtils.isNotBlank(param.getKeyword()) && CollectionUtils.isNotEmpty(keywordObjectIds),
                        RiskIdentificationRecordEntity::getObjectId, keywordObjectIds)
                .eq(StringUtils.isNotBlank(param.getIdentifierUserId()),
                        RiskIdentificationRecordEntity::getIdentifierUserId, param.getIdentifierUserId())
                .ge(param.getStartTime() != null, RiskIdentificationRecordEntity::getSubmitTime, param.getStartTime())
                .le(param.getEndTime() != null, RiskIdentificationRecordEntity::getSubmitTime, param.getEndTime())
                .orderByDesc(RiskIdentificationRecordEntity::getSubmitTime);
    }

    private List<Long> unitIdsByArea(Long areaId) {
        if (areaId == null) {
            return List.of();
        }
        return RiskDataPermissionContext.withoutDataPermission(() -> unitService.list(Wrappers.<RiskUnitEntity>lambdaQuery()
                        .eq(RiskUnitEntity::getAreaId, areaId)))
                .stream()
                .map(RiskUnitEntity::getId)
                .toList();
    }

    private List<Long> objectIdsByKeyword(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return List.of();
        }
        return objectService.list(Wrappers.<RiskObjectEntity>lambdaQuery()
                        .like(RiskObjectEntity::getObjectName, keyword))
                .stream()
                .map(RiskObjectEntity::getId)
                .toList();
    }

    public RiskIdentificationRecordVO identificationDetail(Long id) {
        RiskIdentificationRecordEntity entity = identificationRecordService.getById(id);
        Assert.isNotFound(entity == null, "辨识记录不存在");
        RiskIdentificationRecordVO vo = RiskIdentificationConvert.toVO(entity);
        RiskDataPermissionContext.withoutDataPermission(() -> enrichIdentificationRecords(List.of(vo)));
        return vo;
    }

    public Pagination<RiskObjectVO> objectPage(RiskObjectPageParam param) {
        IPage<RiskObjectEntity> page = objectService.page(PagingUtils.convert(param),
                Wrappers.<RiskObjectEntity>lambdaQuery()
                        .eq(param.getUnitId() != null, RiskObjectEntity::getUnitId, param.getUnitId())
                        .eq(param.getObjectId() != null, RiskObjectEntity::getId, param.getObjectId())
                        .eq(param.getReviewResult() != null && param.getReviewResult().equals("REJECTED"), RiskObjectEntity::getStatus,
                                RiskObjectStatusEnum.DRAFT.name())
                        .orderByDesc(RiskObjectEntity::getId));
        Pagination<RiskObjectVO> pagination = RiskPageAssembler.toPagination(page, RiskIdentificationConvert::toVO);
        enrichObjects(pagination.getRecords());
        return pagination;
    }

    public RiskObjectVO objectDetail(Long objectId) {
        RiskObjectEntity object = objectService.getById(objectId);
        Assert.isNotFound(object == null, "风险对象不存在");
        return enrichObject(RiskIdentificationConvert.toVO(object));
    }

    private RiskObjectVO enrichObject(RiskObjectVO object) {
        enrichObjects(List.of(object));
        return object;
    }

    private void enrichObjects(List<RiskObjectVO> objects) {
        if (CollectionUtils.isEmpty(objects)) {
            return;
        }
        List<Long> unitIds = objects.stream().map(RiskObjectVO::getUnitId).filter(Objects::nonNull).distinct().toList();
        Map<Long, RiskUnitEntity> unitMap = CollectionUtils.isEmpty(unitIds) ? Map.of()
                : unitService.listByIds(unitIds).stream().collect(Collectors.toMap(
                RiskUnitEntity::getId, Function.identity(), (left, right) -> left));
        Set<String> userIds = new LinkedHashSet<>();
        objects.forEach(object -> RiskControlRefUtils.addId(userIds, object.getResponsibleUserId()));
        Map<String, String> userNames = ouaaFacadeService.batchUserNames(userIds);
        objects.forEach(object -> {
            RiskUnitEntity unit = unitMap.get(object.getUnitId());
            if (unit != null) {
                object.setUnit(RiskUnitConvert.toOption(unit));
            }
            object.setResponsibleUser(RiskControlRefUtils.toUser(object.getResponsibleUserId(), userNames));
        });
    }

    private RiskIdentificationRecordVO enrichIdentificationRecord(RiskIdentificationRecordVO record) {
        enrichIdentificationRecords(List.of(record));
        return record;
    }

    private void enrichIdentificationRecords(List<RiskIdentificationRecordVO> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        List<Long> objectIds = records.stream().map(RiskIdentificationRecordVO::getObjectId).filter(Objects::nonNull).distinct().toList();
        Map<Long, RiskObjectEntity> objectMap = CollectionUtils.isEmpty(objectIds) ? Map.of()
                : objectService.listByIds(objectIds).stream().collect(Collectors.toMap(
                RiskObjectEntity::getId, Function.identity(), (left, right) -> left));
        List<Long> unitIds = records.stream().map(RiskIdentificationRecordVO::getUnitId).filter(Objects::nonNull).distinct().toList();
        Map<Long, RiskUnitEntity> unitMap = CollectionUtils.isEmpty(unitIds) ? Map.of()
                : unitService.listByIds(unitIds).stream().collect(Collectors.toMap(
                RiskUnitEntity::getId, Function.identity(), (left, right) -> left));
        fillIdentificationReviewResults(records, objectIds);
        Set<String> userIds = new LinkedHashSet<>();
        records.forEach(record -> {
            RiskControlRefUtils.addId(userIds, record.getIdentifierUserId());
            RiskControlRefUtils.addId(userIds, record.getReviewerUserId());
        });
        Map<String, String> userNames = ouaaFacadeService.batchUserNames(userIds);
        records.forEach(record -> {
            RiskObjectEntity object = objectMap.get(record.getObjectId());
            if (object != null) {
                record.setObjectCode(object.getObjectCode());
                record.setObjectName(object.getObjectName());
            }
            RiskUnitEntity unit = unitMap.get(record.getUnitId());
            if (unit != null) {
                record.setUnit(RiskUnitConvert.toOption(unit));
            }
            record.setIdentifierUser(RiskControlRefUtils.toUser(record.getIdentifierUserId(), userNames));
            record.setReviewerUser(RiskControlRefUtils.toUser(record.getReviewerUserId(), userNames));
        });
    }

    private void fillIdentificationReviewResults(List<RiskIdentificationRecordVO> records, List<Long> objectIds) {
        if (CollectionUtils.isEmpty(objectIds)) {
            return;
        }
        List<String> roundNos = records.stream()
                .map(RiskIdentificationRecordVO::getIdentifyRoundNo)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .toList();
        if (CollectionUtils.isEmpty(roundNos)) {
            return;
        }
        Map<Long, RiskIdentificationRecordEntity> currentRecordMap = records.stream()
                .filter(record -> record.getId() != null)
                .collect(Collectors.toMap(
                        RiskIdentificationRecordVO::getId,
                        record -> {
                            RiskIdentificationRecordEntity entity = new RiskIdentificationRecordEntity();
                            entity.setId(record.getId());
                            entity.setObjectId(record.getObjectId());
                            entity.setIdentifyRoundNo(record.getIdentifyRoundNo());
                            entity.setSubmitTime(record.getSubmitTime());
                            return entity;
                        },
                        (left, right) -> left));
        Map<String, List<RiskIdentificationRecordEntity>> identificationRecordMap = identificationRecordService.list(
                        Wrappers.<RiskIdentificationRecordEntity>lambdaQuery()
                                .in(RiskIdentificationRecordEntity::getObjectId, objectIds)
                                .in(RiskIdentificationRecordEntity::getIdentifyRoundNo, roundNos)
                                .orderByAsc(RiskIdentificationRecordEntity::getSubmitTime)
                                .orderByAsc(RiskIdentificationRecordEntity::getId))
                .stream()
                .filter(record -> record.getObjectId() != null && StringUtils.isNotBlank(record.getIdentifyRoundNo()))
                .collect(Collectors.groupingBy(record -> reviewIdentityKey(record.getObjectId(), record.getIdentifyRoundNo())));
        Map<String, List<RiskReviewRecordEntity>> reviewRecordMap = reviewRecordService.list(
                        Wrappers.<RiskReviewRecordEntity>lambdaQuery()
                                .in(RiskReviewRecordEntity::getObjectId, objectIds)
                                .in(RiskReviewRecordEntity::getIdentifyRoundNo, roundNos)
                                .orderByAsc(RiskReviewRecordEntity::getReviewTime)
                                .orderByAsc(RiskReviewRecordEntity::getId))
                .stream()
                .filter(record -> record.getObjectId() != null && StringUtils.isNotBlank(record.getIdentifyRoundNo()))
                .collect(Collectors.groupingBy(record -> reviewIdentityKey(record.getObjectId(), record.getIdentifyRoundNo())));
        records.forEach(record -> {
            RiskIdentificationRecordEntity currentRecord = currentRecordMap.get(record.getId());
            if (currentRecord == null) {
                return;
            }
            List<RiskIdentificationRecordEntity> identificationRecords = identificationRecordMap.get(
                    reviewIdentityKey(record.getObjectId(), record.getIdentifyRoundNo()));
            RiskIdentificationRecordEntity nextRecord = nextIdentificationRecord(currentRecord, identificationRecords);
            RiskReviewRecordEntity reviewRecord = matchingReviewRecord(currentRecord, nextRecord,
                    reviewRecordMap.get(reviewIdentityKey(record.getObjectId(), record.getIdentifyRoundNo())));
            if (reviewRecord != null) {
                fillReviewResult(record, reviewRecord);
            }
        });
    }

    private RiskIdentificationRecordEntity nextIdentificationRecord(RiskIdentificationRecordEntity currentRecord,
                                                                    List<RiskIdentificationRecordEntity> records) {
        if (CollectionUtils.isEmpty(records)) {
            return null;
        }
        for (RiskIdentificationRecordEntity record : records) {
            if (record.getId() == null || Objects.equals(record.getId(), currentRecord.getId())) {
                continue;
            }
            if (isAfterIdentificationRecord(record, currentRecord)) {
                return record;
            }
        }
        return null;
    }

    private boolean isAfterIdentificationRecord(RiskIdentificationRecordEntity candidate,
                                                RiskIdentificationRecordEntity currentRecord) {
        LocalDateTime candidateSubmitTime = candidate.getSubmitTime();
        LocalDateTime currentSubmitTime = currentRecord.getSubmitTime();
        if (candidateSubmitTime == null || currentSubmitTime == null) {
            return idValue(candidate.getId()) > idValue(currentRecord.getId());
        }
        int result = candidateSubmitTime.compareTo(currentSubmitTime);
        return result > 0 || (result == 0 && idValue(candidate.getId()) > idValue(currentRecord.getId()));
    }

    private RiskReviewRecordEntity matchingReviewRecord(RiskIdentificationRecordEntity currentRecord,
                                                        RiskIdentificationRecordEntity nextRecord,
                                                        List<RiskReviewRecordEntity> reviewRecords) {
        if (CollectionUtils.isEmpty(reviewRecords)) {
            return null;
        }
        return reviewRecords.stream()
                .filter(review -> isReviewAfterSubmit(review, currentRecord))
                .filter(review -> nextRecord == null || isReviewBeforeNextSubmit(review, nextRecord))
                .findFirst()
                .orElse(null);
    }

    private boolean isReviewAfterSubmit(RiskReviewRecordEntity reviewRecord,
                                        RiskIdentificationRecordEntity identificationRecord) {
        LocalDateTime reviewTime = reviewRecord.getReviewTime();
        LocalDateTime submitTime = identificationRecord.getSubmitTime();
        if (reviewTime == null || submitTime == null) {
            return idValue(reviewRecord.getId()) > idValue(identificationRecord.getId());
        }
        return !reviewTime.isBefore(submitTime);
    }

    private boolean isReviewBeforeNextSubmit(RiskReviewRecordEntity reviewRecord,
                                             RiskIdentificationRecordEntity nextRecord) {
        LocalDateTime reviewTime = reviewRecord.getReviewTime();
        LocalDateTime nextSubmitTime = nextRecord.getSubmitTime();
        if (reviewTime == null || nextSubmitTime == null) {
            return idValue(reviewRecord.getId()) < idValue(nextRecord.getId());
        }
        return reviewTime.isBefore(nextSubmitTime);
    }

    private void fillReviewResult(RiskIdentificationRecordVO target, RiskReviewRecordEntity source) {
        target.setReviewMode(source.getReviewMode());
        target.setReviewBatchNo(source.getReviewBatchNo());
        target.setReviewResult(source.getReviewResult());
        target.setRejectReason(source.getRejectReason());
        target.setReviewComment(source.getReviewComment());
        target.setReviewerUserId(source.getReviewerUserId());
        target.setReviewTime(source.getReviewTime());
    }

    private String reviewIdentityKey(Long objectId, String identifyRoundNo) {
        return objectId + "::" + identifyRoundNo;
    }

    private long idValue(Long id) {
        return id == null ? 0L : id;
    }

    private void assertObjectCodeAvailable(String code, Long excludeId) {
        Assert.isConflict(objectService.count(Wrappers.<RiskObjectEntity>lambdaQuery()
                .eq(RiskObjectEntity::getObjectCode, code)
                .ne(excludeId != null, RiskObjectEntity::getId, excludeId)) > 0, "风险对象编号已存在");
    }

    private RiskIdentificationRecordEntity toRecord(RiskObjectEntity object,
                                                    RiskIdentificationOperationTypeEnum operationType,
                                                    RiskIdentificationObjectParam objectParam) {
        RiskIdentificationRecordEntity record = new RiskIdentificationRecordEntity();
        record.setId(IdWorker.getId());
        record.setObjectId(object.getId());
        record.setUnitId(object.getUnitId());
        record.setIdentifyRoundNo(object.getIdentifyRoundNo());
        record.setOperationType(operationType.name());
        record.setIdentifierUserId(objectParam.getUserId());
        record.setSubmitTime(LocalDateTime.now());
        record.setSnapshotJson(JsonUtils.serialize(objectParam));
        return record;
    }

    private String resolveSubmitUserId(List<RiskIdentificationObjectParam> objectParams) {
        Long currentUserId = SecurityContextHolder.getPrincipalProperty(Principal::getId);
        if (currentUserId != null) {
            return String.valueOf(currentUserId);
        }
        if (CollectionUtils.isEmpty(objectParams)) {
            return null;
        }
        return objectParams.stream()
                .map(RiskIdentificationObjectParam::getUserId)
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .findFirst()
                .orElse(null);
    }

    private Set<String> unitIdentifierUserIds(Long unitId) {
        if (unitId == null) {
            return Set.of();
        }
        return unitIdentifierRelService.list(Wrappers.<RiskUnitIdentifierRelEntity>lambdaQuery()
                        .eq(RiskUnitIdentifierRelEntity::getUnitId, unitId))
                .stream()
                .map(RiskUnitIdentifierRelEntity::getUserId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
    }

    private void softDeleteRiskObjectCascade(List<RiskObjectEntity> deleteObjects) {
        List<Long> objectIds = deleteObjects.stream().map(RiskObjectEntity::getId).toList();
        List<RiskAssessmentRecordEntity> assessments = assessmentRecordService.list(Wrappers.<RiskAssessmentRecordEntity>lambdaQuery()
                .in(RiskAssessmentRecordEntity::getObjectId, objectIds));
        if (CollectionUtils.isNotEmpty(assessments)) {
            DatabaseContextHolder.softDelete(assessmentRecordService, assessments);
        }
        List<RiskControlMeasureEntity> measures = controlMeasureService.list(Wrappers.<RiskControlMeasureEntity>lambdaQuery()
                .in(RiskControlMeasureEntity::getObjectId, objectIds));
        if (CollectionUtils.isNotEmpty(measures)) {
            List<Long> measureIds = measures.stream().map(RiskControlMeasureEntity::getId).toList();
            List<RiskMeasureExecutionRecordEntity> executions = executionRecordService.list(Wrappers.<RiskMeasureExecutionRecordEntity>lambdaQuery()
                    .in(RiskMeasureExecutionRecordEntity::getMeasureId, measureIds));
            if (CollectionUtils.isNotEmpty(executions)) {
                Set<Long> executionIds = executions.stream().map(RiskMeasureExecutionRecordEntity::getId).collect(Collectors.toSet());
                List<RiskMeasureAttachmentEntity> attachments = attachmentService.list(Wrappers.<RiskMeasureAttachmentEntity>lambdaQuery()
                        .in(RiskMeasureAttachmentEntity::getExecutionRecordId, executionIds));
                if (CollectionUtils.isNotEmpty(attachments)) {
                    DatabaseContextHolder.softDelete(attachmentService, attachments);
                }
                DatabaseContextHolder.softDelete(executionRecordService, executions);
            }
            DatabaseContextHolder.softDelete(controlMeasureService, measures);
        }
        DatabaseContextHolder.softDelete(objectService, deleteObjects);
    }
}
