package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskObjectStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskReviewModeEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskReviewResultEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskUnitIdentifyStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model.RiskUnitControlStatusResolver;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskIdentificationObjectParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskReviewCreateParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskReviewTaskPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskObjectVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskReviewRecordVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskDataPermissionContext;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskControlRefUtils;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade.MessageFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade.OuaaFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.convert.RiskIdentificationConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.entity.RiskIdentificationRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.entity.RiskObjectEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.service.impl.RiskIdentificationRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.service.impl.RiskObjectService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.review.convert.RiskReviewConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.convert.RiskUnitConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.service.impl.RiskUnitService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.review.entity.RiskReviewRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.review.service.impl.RiskReviewRecordService;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("securityManagementRiskReviewWorkflowService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskReviewWorkflowService {
    private final RiskObjectService objectService;
    private final RiskUnitService unitService;
    private final RiskIdentificationRecordService identificationRecordService;
    private final RiskReviewRecordService reviewRecordService;
    private final MessageFacadeService messageFacadeService;
    private final OuaaFacadeService ouaaFacadeService;

    public Pagination<RiskObjectVO> reviewTaskPage(RiskReviewTaskPageParam param) {
        String status = normalizeReviewTaskStatus(param.getStatus());
        List<Long> permittedUnitIds = permittedReviewUnitIds(param);
        if (CollectionUtils.isEmpty(permittedUnitIds)) {
            return Pagination.empty();
        }
        List<RiskObjectVO> records = new ArrayList<>(RiskDataPermissionContext.withoutDataPermission(() ->
                identificationReviewRows(param, status, permittedUnitIds)));
        records.sort(this::compareReviewTaskRow);
        return paginate(records, param);
    }

    private List<RiskObjectVO> identificationReviewRows(RiskReviewTaskPageParam param,
                                                        String status,
                                                        List<Long> permittedUnitIds) {
        List<RiskIdentificationRecordEntity> identificationRecords = identificationRecordService.list(
                identificationReviewQuery(param, permittedUnitIds));
        if (CollectionUtils.isEmpty(identificationRecords)) {
            return List.of();
        }
        List<RiskObjectVO> records = identificationRecords.stream()
                .map(this::toReviewTaskRow)
                .toList();
        fillIdentificationRecordReviews(records, identificationRecords);
        enrichReviewTaskRefs(records);
        return records.stream()
                .filter(record -> matchesReviewStatus(record, status))
                .filter(record -> matchesReviewKeyword(record, param.getKeyword()))
                .toList();
    }

    private LambdaQueryWrapper<RiskIdentificationRecordEntity> identificationReviewQuery(RiskReviewTaskPageParam param,
                                                                                        List<Long> permittedUnitIds) {
        return Wrappers.<RiskIdentificationRecordEntity>lambdaQuery()
                .eq(param.getUnitId() != null, RiskIdentificationRecordEntity::getUnitId, param.getUnitId())
                .in(RiskIdentificationRecordEntity::getUnitId, permittedUnitIds)
                .eq(StringUtils.isNotBlank(param.getIdentifierUserId()),
                        RiskIdentificationRecordEntity::getIdentifierUserId, param.getIdentifierUserId())
                .ge(param.getStartTime() != null, RiskIdentificationRecordEntity::getSubmitTime, param.getStartTime())
                .le(param.getEndTime() != null, RiskIdentificationRecordEntity::getSubmitTime, param.getEndTime())
                .orderByDesc(RiskIdentificationRecordEntity::getSubmitTime)
                .orderByDesc(RiskIdentificationRecordEntity::getId);
    }

    private RiskObjectVO toReviewTaskRow(RiskIdentificationRecordEntity entity) {
        RiskObjectVO vo = new RiskObjectVO();
        vo.setId(entity.getId());
        vo.setObjectId(entity.getObjectId());
        vo.setUnitId(entity.getUnitId());
        vo.setIdentifyRoundNo(entity.getIdentifyRoundNo());
        vo.setIdentifierUserId(entity.getIdentifierUserId());
        vo.setSubmitTime(entity.getSubmitTime());
        vo.setStatus(RiskObjectStatusEnum.PENDING_REVIEW.name());
        fillSnapshotFields(vo, parseIdentificationSnapshot(entity.getSnapshotJson()));
        return vo;
    }

    private boolean matchesReviewStatus(RiskObjectVO record, String status) {
        if (StringUtils.isBlank(status)) {
            return true;
        }
        if (RiskObjectStatusEnum.PENDING_REVIEW.name().equals(status)) {
            return StringUtils.isBlank(record.getReviewResult());
        }
        return Objects.equals(record.getReviewResult(), status);
    }

    private boolean matchesReviewKeyword(RiskObjectVO record, String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return true;
        }
        String value = keyword.trim();
        return contains(record.getObjectName(), value)
                || contains(record.getName(), value)
                || contains(record.getHazardDesc(), value)
                || contains(record.getAccidentTypes(), value);
    }

    private boolean contains(String source, String keyword) {
        return StringUtils.isNotBlank(source) && source.contains(keyword);
    }

    private void fillIdentificationRecordReviews(List<RiskObjectVO> rows,
                                                 List<RiskIdentificationRecordEntity> identificationRecords) {
        List<Long> objectIds = rows.stream().map(RiskObjectVO::getObjectId).filter(Objects::nonNull).distinct().toList();
        List<String> roundNos = rows.stream().map(RiskObjectVO::getIdentifyRoundNo).filter(StringUtils::isNotBlank).distinct().toList();
        if (CollectionUtils.isEmpty(objectIds) || CollectionUtils.isEmpty(roundNos)) {
            return;
        }
        Map<Long, RiskIdentificationRecordEntity> identificationRecordMap = identificationRecords.stream()
                .filter(record -> record.getId() != null)
                .collect(Collectors.toMap(RiskIdentificationRecordEntity::getId, Function.identity(), (left, right) -> left));
        Map<String, List<RiskIdentificationRecordEntity>> identificationGroupMap = identificationRecords.stream()
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
        rows.forEach(row -> {
            RiskIdentificationRecordEntity currentRecord = identificationRecordMap.get(row.getId());
            if (currentRecord == null) {
                return;
            }
            RiskIdentificationRecordEntity nextRecord = nextIdentificationRecord(currentRecord,
                    identificationGroupMap.get(reviewIdentityKey(row.getObjectId(), row.getIdentifyRoundNo())));
            RiskReviewRecordEntity reviewRecord = matchingReviewRecord(currentRecord, nextRecord,
                    reviewRecordMap.get(reviewIdentityKey(row.getObjectId(), row.getIdentifyRoundNo())));
            if (reviewRecord != null) {
                fillReviewResult(row, reviewRecord);
                row.setStatus(reviewRecord.getReviewResult());
            }
        });
    }

    private RiskIdentificationRecordEntity nextIdentificationRecord(RiskIdentificationRecordEntity currentRecord,
                                                                    List<RiskIdentificationRecordEntity> records) {
        if (CollectionUtils.isEmpty(records)) {
            return null;
        }
        return records.stream()
                .filter(record -> record.getId() != null && !Objects.equals(record.getId(), currentRecord.getId()))
                .filter(record -> isAfterIdentificationRecord(record, currentRecord))
                .min(this::compareIdentificationRecord)
                .orElse(null);
    }

    private int compareIdentificationRecord(RiskIdentificationRecordEntity left, RiskIdentificationRecordEntity right) {
        LocalDateTime leftSubmitTime = left.getSubmitTime();
        LocalDateTime rightSubmitTime = right.getSubmitTime();
        if (leftSubmitTime == null && rightSubmitTime == null) {
            return Long.compare(idValue(left.getId()), idValue(right.getId()));
        }
        if (leftSubmitTime == null) {
            return -1;
        }
        if (rightSubmitTime == null) {
            return 1;
        }
        int result = leftSubmitTime.compareTo(rightSubmitTime);
        return result == 0 ? Long.compare(idValue(left.getId()), idValue(right.getId())) : result;
    }

    private boolean isAfterIdentificationRecord(RiskIdentificationRecordEntity candidate,
                                                RiskIdentificationRecordEntity currentRecord) {
        return compareIdentificationRecord(candidate, currentRecord) > 0;
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

    private String normalizeReviewTaskStatus(String status) {
        if (StringUtils.isBlank(status)) {
            return null;
        }
        return switch (status.trim().toUpperCase(Locale.ROOT)) {
            case "ALL", "全部", "全部状态" -> null;
            case "PENDING_REVIEW", "待审核", "审核中" -> RiskObjectStatusEnum.PENDING_REVIEW.name();
            case "APPROVED", "通过", "已通过" -> RiskReviewResultEnum.APPROVED.name();
            case "REJECTED", "退回", "已退回", "不通过" -> RiskReviewResultEnum.REJECTED.name();
            default -> status.trim().toUpperCase(Locale.ROOT);
        };
    }

    private Pagination<RiskObjectVO> paginate(List<RiskObjectVO> records, Paging param) {
        Integer pageNumber = param.getPageNumber() == null ? 1 : param.getPageNumber();
        Integer pageSize = param.getPageSize() == null ? 10 : param.getPageSize();
        long total = records.size();
        long pages = total == 0 ? 0 : (total + pageSize - 1) / pageSize;
        return new Pagination<>(total, pages, Paging.list(records, pageNumber, pageSize));
    }

    private int compareReviewTaskRow(RiskObjectVO left, RiskObjectVO right) {
        LocalDateTime leftTime = reviewTaskSortTime(left);
        LocalDateTime rightTime = reviewTaskSortTime(right);
        if (leftTime == null && rightTime == null) {
            return Long.compare(idValue(right.getId()), idValue(left.getId()));
        }
        if (leftTime == null) {
            return 1;
        }
        if (rightTime == null) {
            return -1;
        }
        int result = rightTime.compareTo(leftTime);
        return result == 0 ? Long.compare(idValue(right.getId()), idValue(left.getId())) : result;
    }

    private LocalDateTime reviewTaskSortTime(RiskObjectVO row) {
        return row.getReviewTime() == null ? row.getSubmitTime() : row.getReviewTime();
    }

    private long idValue(Long id) {
        return id == null ? 0L : id;
    }

    public RiskObjectVO reviewTaskDetail(Long objectId) {
        RiskObjectEntity object = objectService.getById(objectId);
        Assert.isNotFound(object == null, "审核任务不存在");
        assertReviewAccess(object);
        RiskObjectVO detail = RiskIdentificationConvert.toVO(object);
        if (!Objects.equals(object.getStatus(), RiskObjectStatusEnum.PENDING_REVIEW.name())) {
            RiskReviewRecordEntity record = latestReviewRecord(objectId);
            if (record != null) {
                fillReviewResult(detail, record);
                detail.setStatus(record.getReviewResult());
            }
        }
        enrichObject(detail);
        return detail;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<RiskReviewRecordVO> createReviews(RiskReviewCreateParam param) {
        List<Long> objectIds = param.getObjectIds();
        if (CollectionUtils.isEmpty(objectIds) && param.getObjectId() != null) {
            objectIds = List.of(param.getObjectId());
        }
        Assert.isParameterError(CollectionUtils.isEmpty(objectIds), "审核对象不能为空");
        RiskReviewResultEnum result = parseReviewResult(param.getReviewResult());
        String rejectReason = trimToNull(param.getRejectReason());
        String reviewComment = trimToNull(param.getReviewComment());
        Assert.isParameterError(result == RiskReviewResultEnum.REJECTED && StringUtils.isBlank(rejectReason), "退回原因不能为空");

        List<RiskObjectEntity> objects = objectService.listByIds(objectIds).stream().toList();
        Assert.isNotFound(objects.size() != objectIds.size(), "部分审核对象不存在");
        objects.forEach(object -> Assert.isNotAcceptable(!Objects.equals(object.getStatus(), RiskObjectStatusEnum.PENDING_REVIEW.name()),
                "仅待审核对象可以审核"));

        String batchNo = objectIds.size() > 1 ? "RB-" + IdWorker.getId() : null;
        RiskReviewModeEnum mode = objectIds.size() > 1 ? RiskReviewModeEnum.BATCH : RiskReviewModeEnum.SINGLE;
        LocalDateTime now = LocalDateTime.now();
        String reviewerUserId = currentReviewerUserId(param.getUserId());
        Assert.isParameterError(StringUtils.isBlank(reviewerUserId), "审核人不能为空");
        assertReviewerResponsible(objects, reviewerUserId);
        List<RiskReviewRecordEntity> records = objects.stream().map(object -> {
            RiskReviewRecordEntity record = new RiskReviewRecordEntity();
            record.setId(IdWorker.getId());
            record.setObjectId(object.getId());
            record.setIdentifyRoundNo(object.getIdentifyRoundNo());
            record.setReviewMode(mode.name());
            record.setReviewBatchNo(batchNo);
            record.setReviewResult(result.name());
            record.setRejectReason(rejectReason);
            record.setReviewComment(reviewComment);
            record.setReviewerUserId(reviewerUserId);
            record.setReviewTime(now);
            return record;
        }).toList();
        reviewRecordService.saveBatch(records);

        objects.forEach(object -> object.setStatus(result == RiskReviewResultEnum.APPROVED
                ? RiskObjectStatusEnum.INHERENT_EVAL.name()
                : RiskObjectStatusEnum.DRAFT.name()));
        objectService.updateBatchById(objects);
        refreshUnitReviewStatus(objects);
        messageFacadeService.sendReviewResult(result, objects, records);
        List<RiskReviewRecordVO> vos = records.stream().map(RiskReviewConvert::toVO).toList();
        enrichReviewRecords(vos);
        return vos;
    }

    private String trimToNull(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value.trim();
    }

    private String currentReviewerUserId(String paramUserId) {
        Long principalId = SecurityContextHolder.getPrincipalProperty(Principal::getId);
        if (principalId != null) {
            return String.valueOf(principalId);
        }
        return StringUtils.isNotBlank(paramUserId) ? paramUserId.trim() : null;
    }

    private void assertReviewAccess(RiskObjectEntity object) {
        RiskUnitEntity unit = unitService.getById(object.getUnitId());
        Assert.isNotAcceptable(unit == null, "无权限查看该审核任务");
    }

    private void assertReviewerResponsible(List<RiskObjectEntity> objects, String reviewerUserId) {
        List<Long> unitIds = objects.stream()
                .map(RiskObjectEntity::getUnitId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        Map<Long, RiskUnitEntity> unitMap = unitService.listByIds(unitIds).stream()
                .collect(Collectors.toMap(RiskUnitEntity::getId, Function.identity(), (left, right) -> left));
        objects.forEach(object -> {
            RiskUnitEntity unit = unitMap.get(object.getUnitId());
            Assert.isNotAcceptable(unit == null || !Objects.equals(unit.getResponsibleUserId(), reviewerUserId),
                    "当前用户不是风险单元负责人，不能审核");
        });
    }

    private List<Long> permittedReviewUnitIds(RiskReviewTaskPageParam param) {
        return unitService.list(Wrappers.<RiskUnitEntity>lambdaQuery()
                        .eq(param.getUnitId() != null, RiskUnitEntity::getId, param.getUnitId()))
                .stream()
                .map(RiskUnitEntity::getId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private RiskReviewRecordEntity latestReviewRecord(Long objectId) {
        return reviewRecordService.getOne(Wrappers.<RiskReviewRecordEntity>lambdaQuery()
                .eq(RiskReviewRecordEntity::getObjectId, objectId)
                .orderByDesc(RiskReviewRecordEntity::getReviewTime)
                .orderByDesc(RiskReviewRecordEntity::getId)
                .last("LIMIT 1"));
    }

    private void fillReviewResult(RiskObjectVO target, RiskReviewRecordEntity source) {
        target.setReviewMode(source.getReviewMode());
        target.setReviewBatchNo(source.getReviewBatchNo());
        target.setReviewResult(source.getReviewResult());
        target.setRejectReason(source.getRejectReason());
        target.setReviewComment(source.getReviewComment());
        target.setReviewerUserId(source.getReviewerUserId());
        target.setReviewTime(source.getReviewTime());
    }

    private void fillSnapshotFields(RiskObjectVO row, RiskIdentificationObjectParam snapshot) {
        if (snapshot == null) {
            return;
        }
        String objectCode = snapshot.getCode();
        String objectName = snapshot.getName();
        if (StringUtils.isNotBlank(objectCode)) {
            row.setObjectCode(objectCode);
            row.setCode(objectCode);
        }
        if (StringUtils.isNotBlank(objectName)) {
            row.setObjectName(objectName);
            row.setName(objectName);
        }
        if (snapshot.getAssetId() != null) {
            row.setAssetId(snapshot.getAssetId());
        }
        if (StringUtils.isNotBlank(snapshot.getResponsibleUserId())) {
            row.setResponsibleUserId(snapshot.getResponsibleUserId());
        }
        if (StringUtils.isNotBlank(snapshot.getHazardDesc())) {
            row.setHazardDesc(snapshot.getHazardDesc());
        }
        if (StringUtils.isNotBlank(snapshot.getAccidentTypes())) {
            row.setAccidentTypes(snapshot.getAccidentTypes());
        }
        if (StringUtils.isNotBlank(snapshot.getAccidentCause())) {
            row.setAccidentCause(snapshot.getAccidentCause());
        }
        if (StringUtils.isNotBlank(snapshot.getTriggerFactor())) {
            row.setTriggerFactor(snapshot.getTriggerFactor());
        }
        if (StringUtils.isNotBlank(snapshot.getEmergencyMeasure())) {
            row.setEmergencyMeasure(snapshot.getEmergencyMeasure());
        }
    }

    private RiskIdentificationObjectParam parseIdentificationSnapshot(String snapshotJson) {
        try {
            return JsonUtils.deserialize(snapshotJson, RiskIdentificationObjectParam.class);
        } catch (RuntimeException e) {
            return null;
        }
    }

    private String reviewIdentityKey(Long objectId, String identifyRoundNo) {
        return objectId + "::" + identifyRoundNo;
    }

    private void enrichReviewTaskRefs(List<RiskObjectVO> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        List<Long> unitIds = records.stream().map(RiskObjectVO::getUnitId).filter(Objects::nonNull).distinct().toList();
        Map<Long, RiskUnitEntity> unitMap = CollectionUtils.isEmpty(unitIds) ? Map.of()
                : unitService.listByIds(unitIds).stream().collect(Collectors.toMap(
                RiskUnitEntity::getId, Function.identity(), (left, right) -> left));
        Set<String> userIds = new LinkedHashSet<>();
        records.forEach(record -> {
            RiskControlRefUtils.addId(userIds, record.getResponsibleUserId());
            RiskControlRefUtils.addId(userIds, record.getIdentifierUserId());
            RiskControlRefUtils.addId(userIds, record.getReviewerUserId());
        });
        Map<String, String> userNames = ouaaFacadeService.batchUserNames(userIds);
        records.forEach(record -> {
            RiskUnitEntity unit = unitMap.get(record.getUnitId());
            if (unit != null) {
                record.setUnit(RiskUnitConvert.toOption(unit));
            }
            record.setResponsibleUser(RiskControlRefUtils.toUser(record.getResponsibleUserId(), userNames));
            record.setIdentifierUser(RiskControlRefUtils.toUser(record.getIdentifierUserId(), userNames));
            record.setReviewerUser(RiskControlRefUtils.toUser(record.getReviewerUserId(), userNames));
        });
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
        enrichLatestIdentificationInfo(objects);
        Set<String> userIds = new LinkedHashSet<>();
        objects.forEach(object -> {
            RiskControlRefUtils.addId(userIds, object.getResponsibleUserId());
            RiskControlRefUtils.addId(userIds, object.getIdentifierUserId());
            RiskControlRefUtils.addId(userIds, object.getReviewerUserId());
        });
        Map<String, String> userNames = ouaaFacadeService.batchUserNames(userIds);
        objects.forEach(object -> {
            RiskUnitEntity unit = unitMap.get(object.getUnitId());
            if (unit != null) {
                object.setUnit(RiskUnitConvert.toOption(unit));
            }
            object.setResponsibleUser(RiskControlRefUtils.toUser(object.getResponsibleUserId(), userNames));
            object.setIdentifierUser(RiskControlRefUtils.toUser(object.getIdentifierUserId(), userNames));
            object.setReviewerUser(RiskControlRefUtils.toUser(object.getReviewerUserId(), userNames));
        });
    }

    private void enrichLatestIdentificationInfo(List<RiskObjectVO> objects) {
        List<Long> objectIds = objects.stream().map(RiskObjectVO::getObjectId).filter(Objects::nonNull).distinct().toList();
        if (CollectionUtils.isEmpty(objectIds)) {
            return;
        }
        Map<Long, RiskIdentificationRecordEntity> identificationRecordMap = identificationRecordService.list(
                        Wrappers.<RiskIdentificationRecordEntity>lambdaQuery()
                                .in(RiskIdentificationRecordEntity::getObjectId, objectIds)
                                .orderByDesc(RiskIdentificationRecordEntity::getSubmitTime)
                                .orderByDesc(RiskIdentificationRecordEntity::getId))
                .stream()
                .filter(record -> record.getObjectId() != null)
                .collect(Collectors.toMap(
                        RiskIdentificationRecordEntity::getObjectId,
                        Function.identity(),
                        (left, right) -> left));
        objects.forEach(object -> {
            RiskIdentificationRecordEntity record = identificationRecordMap.get(object.getObjectId());
            if (record == null) {
                return;
            }
            if (StringUtils.isNotBlank(record.getIdentifierUserId())) {
                object.setIdentifierUserId(record.getIdentifierUserId().trim());
            }
            object.setSubmitTime(record.getSubmitTime());
        });
    }

    private void enrichReviewRecords(List<RiskReviewRecordVO> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        Set<String> userIds = new LinkedHashSet<>();
        records.forEach(record -> RiskControlRefUtils.addId(userIds, record.getReviewerUserId()));
        Map<String, String> userNames = ouaaFacadeService.batchUserNames(userIds);
        records.forEach(record -> record.setReviewerUser(
                RiskControlRefUtils.toUser(record.getReviewerUserId(), userNames)));
    }

    private RiskReviewResultEnum parseReviewResult(String reviewResult) {
        try {
            return RiskReviewResultEnum.valueOf(reviewResult);
        } catch (IllegalArgumentException | NullPointerException e) {
            Assert.isParameterError(true, "审核结论非法");
            return null;
        }
    }

    private void refreshUnitReviewStatus(List<RiskObjectEntity> reviewedObjects) {
        List<Long> unitIds = reviewedObjects.stream().map(RiskObjectEntity::getUnitId).distinct().toList();
        List<RiskUnitEntity> units = unitService.listByIds(unitIds).stream().toList();
        Map<Long, RiskUnitEntity> unitMap = units.stream().collect(Collectors.toMap(RiskUnitEntity::getId, Function.identity()));
        List<RiskObjectEntity> roundObjects = objectService.list(Wrappers.<RiskObjectEntity>lambdaQuery()
                .in(RiskObjectEntity::getUnitId, unitIds));
        Map<Long, List<RiskObjectEntity>> objectMap = roundObjects.stream().collect(Collectors.groupingBy(RiskObjectEntity::getUnitId));
        LocalDateTime now = LocalDateTime.now();
        units.forEach(unit -> {
            List<RiskObjectEntity> currentRoundObjects = objectMap.getOrDefault(unit.getId(), List.of()).stream()
                    .filter(object -> Objects.equals(object.getIdentifyRoundNo(), unitMap.get(unit.getId()).getCurrentIdentifyRoundNo()))
                    .toList();
            boolean unfinished = currentRoundObjects.stream()
                    .anyMatch(object -> Objects.equals(object.getStatus(), RiskObjectStatusEnum.DRAFT.name())
                            || Objects.equals(object.getStatus(), RiskObjectStatusEnum.PENDING_REVIEW.name()));
            if (unfinished) {
                unit.setIdentifyStatus(RiskUnitIdentifyStatusEnum.PENDING_REVIEW.name());
            } else if (CollectionUtils.isNotEmpty(currentRoundObjects)) {
                unit.setIdentifyStatus(RiskUnitIdentifyStatusEnum.REVIEWED.name());
                unit.setLastIdentifyReviewTime(now);
                if (unit.getIdentifyCycleDays() != null) {
                    unit.setNextIdentifyTime(now.plusDays(unit.getIdentifyCycleDays()));
                }
            }
            unit.setControlStatus(RiskUnitControlStatusResolver.resolve(currentRoundObjects));
        });
        unitService.updateBatchById(units);
    }
}
