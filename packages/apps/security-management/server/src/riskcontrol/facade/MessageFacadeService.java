package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.bracing.api.AssemblyNoticeClient;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendParam;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.area.entity.RiskAreaEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.area.service.impl.RiskAreaService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskLevelEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskReviewResultEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskUnitTypeEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.service.impl.RiskUnitTypeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.entity.RiskIdentificationRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.entity.RiskObjectEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.service.impl.RiskIdentificationRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.service.impl.RiskObjectService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.measure.entity.RiskControlMeasureEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.plan.entity.RiskAssessmentPlanEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.plan.entity.RiskPlanNotifyUserEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.plan.service.impl.RiskPlanNotifyUserService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.review.entity.RiskReviewRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitIdentifierRelEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.service.impl.RiskUnitIdentifierRelService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.service.impl.RiskUnitService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("securityManagementRiskMessageFacadeService")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageFacadeService {
    private static final String APPLICATION_CODE = "RISK_GRADED_CONTROL";
    private static final String APPLICATION_NAME = "风险分级管控";
    private static final String WARNING_BUSINESS_TYPE = "RISK_WARNING";
    private static final String SCENE_IDENTIFICATION_SUBMIT = "RISK_IDENTIFICATION_SUBMIT";
    private static final String SCENE_IDENTIFICATION_APPROVED = "RISK_IDENTIFICATION_APPROVED";
    private static final String SCENE_IDENTIFICATION_REJECTED = "RISK_IDENTIFICATION_REJECTED";
    private static final String SCENE_ASSESSMENT_PLAN_DUE = "RISK_ASSESSMENT_PLAN_DUE";
    private static final String SCENE_CONTROL_MEASURE_TASK = "RISK_CONTROL_MEASURE_TASK";
    private static final String SCENE_LEVEL_UPGRADE_WARNING = "RISK_LEVEL_UPGRADE_WARNING";
    private static final String SCENE_UNIT_ASSIGNED = "RISK_UNIT_ASSIGNED";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final MessageSendClient messageSendClient;
    private final AssemblyNoticeClient assemblyNoticeClient;
    private final RiskUnitService unitService;
    private final RiskUnitTypeService unitTypeService;
    private final RiskAreaService areaService;
    private final RiskObjectService objectService;
    private final RiskIdentificationRecordService identificationRecordService;
    private final RiskUnitIdentifierRelService unitIdentifierRelService;
    private final RiskPlanNotifyUserService planNotifyUserService;
    private final OuaaFacadeService ouaaFacadeService;

    public void sendIdentificationSubmitted(Collection<String> userIds, Long unitId) {
        sendIdentificationSubmitted(unitId, Collections.emptyList());
    }

    public void sendReviewResult(Collection<String> userIds, Long unitId) {
        log.warn("Skip risk review message because review result is missing, unitId={}", unitId);
    }

    public void sendIdentificationSubmitted(Long unitId, Collection<RiskIdentificationRecordEntity> records) {
        try {
            RiskUnitEntity unit = unitService.getById(unitId);
            if (unit == null) {
                return;
            }
            Set<String> recipients = distinctUserIds(Collections.singletonList(unit.getResponsibleUserId()));
            if (recipients.isEmpty()) {
                log.warn("Skip risk identification submit message because unit reviewer is empty, unitId={}", unitId);
                return;
            }
            Set<String> identifierUserIds = distinctUserIds(records == null ? List.of()
                    : records.stream().map(RiskIdentificationRecordEntity::getIdentifierUserId).toList());
            if (identifierUserIds.isEmpty()) {
                String currentUserId = currentUserId();
                if (StringUtils.isNotBlank(currentUserId)) {
                    identifierUserIds.add(currentUserId);
                }
            }
            if (identifierUserIds.isEmpty()) {
                identifierUserIds.addAll(unitIdentifierUserIds(unitId));
            }

            Map<String, String> params = newParams();
            params.put("unitName", value(unit.getUnitName()));
            params.put("unitType", value(unitTypeName(unit.getUnitTypeId())));
            params.put("submitTime", formatDateTime(resolveSubmitTime(unit, records)));
            params.put("objectCount", String.valueOf(resolveObjectCount(unit, records)));
            params.put("identifierName", userNames(identifierUserIds));
            sceneSend(SCENE_IDENTIFICATION_SUBMIT, unitId, recipients, params, unit, tenant(unit));
        } catch (RuntimeException ex) {
            log.warn("Failed to send risk identification submit message, unitId={}", unitId, ex);
        }
    }

    public void sendReviewResult(RiskReviewResultEnum result,
                                 Collection<RiskObjectEntity> objects,
                                 Collection<RiskReviewRecordEntity> records) {
        if (result == null || CollectionUtils.isEmpty(objects) || CollectionUtils.isEmpty(records)) {
            return;
        }
        try {
            Map<Long, RiskObjectEntity> objectMap = objects.stream()
                    .filter(Objects::nonNull)
                    .filter(object -> object.getId() != null)
                    .collect(Collectors.toMap(RiskObjectEntity::getId, Function.identity(), (left, right) -> left));
            if (RiskReviewResultEnum.APPROVED == result) {
                sendApprovedMessages(objects, records);
            } else if (RiskReviewResultEnum.REJECTED == result) {
                records.stream()
                        .filter(Objects::nonNull)
                        .forEach(record -> sendRejectedMessage(objectMap.get(record.getObjectId()), record));
            }
        } catch (RuntimeException ex) {
            log.warn("Failed to send risk review message, result={}", result, ex);
        }
    }

    public void sendAssessmentPlanDue(RiskAssessmentPlanEntity plan) {
        if (plan == null || plan.getId() == null) {
            return;
        }
        try {
            RiskUnitEntity unit = unitService.getById(plan.getUnitId());
            if (unit == null) {
                return;
            }
            Set<String> recipients = planNotifyUserService.list(Wrappers.<RiskPlanNotifyUserEntity>lambdaQuery()
                            .eq(RiskPlanNotifyUserEntity::getPlanId, plan.getId()))
                    .stream()
                    .map(RiskPlanNotifyUserEntity::getUserId)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            if (recipients.isEmpty()) {
                recipients.addAll(distinctUserIds(Collections.singletonList(unit.getResponsibleUserId())));
            }
            if (recipients.isEmpty()) {
                log.warn("Skip risk assessment plan due message because notify users are empty, planId={}", plan.getId());
                return;
            }

            Map<String, String> params = newParams();
            params.put("unitName", value(unit.getUnitName()));
            params.put("unitType", value(unitTypeName(unit.getUnitTypeId())));
            params.put("planName", value(plan.getPlanName()));
            params.put("dueDate", formatDate(plan.getNextExecuteTime()));
            sceneSend(SCENE_ASSESSMENT_PLAN_DUE, plan.getId(), recipients, params, plan, tenant(unit));
        } catch (RuntimeException ex) {
            log.warn("Failed to send risk assessment plan due message, planId={}", plan.getId(), ex);
        }
    }

    public void sendControlMeasureTask(Long objectId, Collection<RiskControlMeasureEntity> measures) {
        if (objectId == null || CollectionUtils.isEmpty(measures)) {
            return;
        }
        try {
            RiskObjectEntity object = objectService.getById(objectId);
            if (object == null) {
                return;
            }
            RiskUnitEntity unit = unitService.getById(object.getUnitId());
            if (unit == null) {
                return;
            }
            Set<String> recipients = distinctUserIds(measures.stream()
                    .map(RiskControlMeasureEntity::getExecutorUserId)
                    .toList());
            if (recipients.isEmpty()) {
                log.warn("Skip risk control measure task message because executors are empty, objectId={}", objectId);
                return;
            }

            Map<String, String> params = newParams();
            params.put("unitName", value(unit.getUnitName()));
            params.put("unitType", value(unitTypeName(unit.getUnitTypeId())));
            params.put("controlLevel", joinValues(measures.stream().map(RiskControlMeasureEntity::getControlLevel).toList()));
            params.put("measureSummary", measureSummary(measures));
            sceneSend(SCENE_CONTROL_MEASURE_TASK, objectId, recipients, params, object, tenant(unit));
        } catch (RuntimeException ex) {
            log.warn("Failed to send risk control measure task message, objectId={}", objectId, ex);
        }
    }

    public void sendLevelUpgradeWarning(Long unitId, Long objectId, String oldLevel, String newLevel) {
        if (unitId == null || objectId == null) {
            return;
        }
        try {
            RiskUnitEntity unit = unitService.getById(unitId);
            RiskObjectEntity object = objectService.getById(objectId);
            if (unit == null || object == null) {
                return;
            }
            Map<String, String> params = newParams();
            params.put("unitName", value(unit.getUnitName()));
            params.put("objectName", value(object.getObjectName()));
            params.put("oldLevel", riskLevelName(oldLevel));
            params.put("newLevel", riskLevelName(newLevel));

            List<MessageSendConfig> configs = assemblyNoticeClient.getSendMsgConfig(MessageSendSceneQueryPram.builder()
                    .businessType(WARNING_BUSINESS_TYPE)
                    .sceneCodes(List.of(SCENE_LEVEL_UPGRADE_WARNING))
                    .build());
            if (CollectionUtils.isEmpty(configs)) {
                log.warn("Skip risk level upgrade warning message because bracing config is empty, unitId={}, objectId={}", unitId, objectId);
                return;
            }
            for (MessageSendConfig config : configs) {
                if (config == null || config.getNotifyId() == null) {
                    continue;
                }
                assemblyNoticeClient.sendMsg(MessageSendParam.builder()
                        .notifyId(config.getNotifyId())
                        .businessId(String.valueOf(objectId))
                        .applicationCode(APPLICATION_CODE)
                        .applicationName(APPLICATION_NAME)
                        .msgSceneCode(StringUtils.isNotBlank(config.getSceneCode())
                                ? config.getSceneCode()
                                : SCENE_LEVEL_UPGRADE_WARNING)
                        .tempParams(params)
                        .businessPrincipal(object)
                        .build());
            }
        } catch (RuntimeException ex) {
            log.warn("Failed to send risk level upgrade warning message, unitId={}, objectId={}", unitId, objectId, ex);
        }
    }

    public void sendUnitAssigned(Long unitId, Collection<String> identifierUserIds) {
        if (unitId == null) {
            return;
        }
        try {
            RiskUnitEntity unit = unitService.getById(unitId);
            if (unit == null) {
                return;
            }
            Set<String> recipients = distinctUserIds(identifierUserIds);
            if (recipients.isEmpty()) {
                recipients.addAll(unitIdentifierUserIds(unitId));
            }
            if (recipients.isEmpty()) {
                log.warn("Skip risk unit assigned message because identifiers are empty, unitId={}", unitId);
                return;
            }

            Map<String, String> params = newParams();
            params.put("unitName", value(unit.getUnitName()));
            params.put("unitType", value(unitTypeName(unit.getUnitTypeId())));
            params.put("assignTime", formatDateTime(LocalDateTime.now()));
            params.put("areaName", value(areaName(unit.getAreaId())));
            params.put("identifyCycle", unit.getIdentifyCycleDays() == null ? "" : unit.getIdentifyCycleDays() + "天");
            sceneSend(SCENE_UNIT_ASSIGNED, unitId, recipients, params, unit, tenant(unit));
        } catch (RuntimeException ex) {
            log.warn("Failed to send risk unit assigned message, unitId={}", unitId, ex);
        }
    }

    private void sendApprovedMessages(Collection<RiskObjectEntity> objects,
                                      Collection<RiskReviewRecordEntity> records) {
        Map<Long, List<RiskObjectEntity>> unitObjectMap = objects.stream()
                .filter(Objects::nonNull)
                .filter(object -> object.getUnitId() != null)
                .collect(Collectors.groupingBy(RiskObjectEntity::getUnitId));
        Map<Long, RiskReviewRecordEntity> recordMap = records.stream()
                .filter(Objects::nonNull)
                .filter(record -> record.getObjectId() != null)
                .collect(Collectors.toMap(RiskReviewRecordEntity::getObjectId, Function.identity(), (left, right) -> left));
        unitObjectMap.forEach((unitId, unitObjects) -> {
            RiskUnitEntity unit = unitService.getById(unitId);
            if (unit == null) {
                return;
            }
            List<RiskReviewRecordEntity> unitRecords = unitObjects.stream()
                    .map(object -> recordMap.get(object.getId()))
                    .filter(Objects::nonNull)
                    .toList();
            Set<String> recipients = identifierRecipients(unitId, unitObjects);
            if (recipients.isEmpty()) {
                log.warn("Skip risk review approved message because identifiers are empty, unitId={}", unitId);
                return;
            }
            RiskReviewRecordEntity review = firstRecord(unitRecords);
            Map<String, String> params = newParams();
            params.put("unitName", value(unit.getUnitName()));
            params.put("unitType", value(unitTypeName(unit.getUnitTypeId())));
            params.put("reviewerName", userName(review == null ? null : review.getReviewerUserId()));
            params.put("reviewTime", formatDateTime(review == null ? null : review.getReviewTime()));
            sceneSend(SCENE_IDENTIFICATION_APPROVED, unitId, recipients, params, unit, tenant(unit));
        });
    }

    private void sendRejectedMessage(RiskObjectEntity object, RiskReviewRecordEntity record) {
        if (object == null || record == null || object.getUnitId() == null) {
            return;
        }
        RiskUnitEntity unit = unitService.getById(object.getUnitId());
        if (unit == null) {
            return;
        }
        Set<String> recipients = identifierRecipients(object.getUnitId(), List.of(object));
        if (recipients.isEmpty()) {
            log.warn("Skip risk review rejected message because identifiers are empty, objectId={}", object.getId());
            return;
        }
        String reason = StringUtils.isNotBlank(record.getRejectReason()) ? record.getRejectReason() : record.getReviewComment();
        Map<String, String> params = newParams();
        params.put("unitName", value(unit.getUnitName()));
        params.put("unitType", value(unitTypeName(unit.getUnitTypeId())));
        params.put("objectName", value(object.getObjectName()));
        params.put("reason", value(reason));
        params.put("reasonSummary", abbreviate(reason, 30));
        params.put("reviewerName", userName(record.getReviewerUserId()));
        sceneSend(SCENE_IDENTIFICATION_REJECTED, object.getId(), recipients, params, object, tenant(unit));
    }

    private Set<String> identifierRecipients(Long unitId, Collection<RiskObjectEntity> objects) {
        if (CollectionUtils.isEmpty(objects)) {
            return unitIdentifierUserIds(unitId);
        }
        List<Long> objectIds = objects.stream()
                .map(RiskObjectEntity::getId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        List<String> roundNos = objects.stream()
                .map(RiskObjectEntity::getIdentifyRoundNo)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .toList();
        Set<String> recipients = new LinkedHashSet<>();
        if (CollectionUtils.isNotEmpty(objectIds) && CollectionUtils.isNotEmpty(roundNos)) {
            recipients.addAll(identificationRecordService.list(Wrappers.<RiskIdentificationRecordEntity>lambdaQuery()
                            .in(RiskIdentificationRecordEntity::getObjectId, objectIds)
                            .in(RiskIdentificationRecordEntity::getIdentifyRoundNo, roundNos))
                    .stream()
                    .map(RiskIdentificationRecordEntity::getIdentifierUserId)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
        }
        if (recipients.isEmpty()) {
            recipients.addAll(unitIdentifierUserIds(unitId));
        }
        return distinctUserIds(recipients);
    }

    private void sceneSend(String sceneCode,
                           Long businessId,
                           Collection<String> userIds,
                           Map<String, String> params,
                           Object principal,
                           Integer tenant) {
        Set<Long> notifyUserIds = toLongIds(userIds);
        if (notifyUserIds.isEmpty()) {
            return;
        }
        if (tenant == null) {
            log.warn("Skip risk message because tenant is empty, sceneCode={}, businessId={}", sceneCode, businessId);
            return;
        }
        messageSendClient.sceneSend(MessageSceneSendRequest.builder()
                .msgSceneCode(sceneCode)
                .applicationCode(APPLICATION_CODE)
                .applicationName(APPLICATION_NAME)
                .businessId(businessId == null ? null : String.valueOf(businessId))
                .tmpParams(params == null ? newParams() : params)
                .businessPrincipal(principal)
                .tenant(tenant)
                .messageNotifyTargets(List.of(MessageNotifyTarget.builder()
                        .notifyTargetType(MessageNotifyTargetEnum.USER)
                        .ids(notifyUserIds)
                        .build()))
                .build());
    }

    private Set<String> unitIdentifierUserIds(Long unitId) {
        if (unitId == null) {
            return new LinkedHashSet<>();
        }
        return unitIdentifierRelService.list(Wrappers.<RiskUnitIdentifierRelEntity>lambdaQuery()
                        .eq(RiskUnitIdentifierRelEntity::getUnitId, unitId))
                .stream()
                .map(RiskUnitIdentifierRelEntity::getUserId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private long resolveObjectCount(RiskUnitEntity unit, Collection<RiskIdentificationRecordEntity> records) {
        if (CollectionUtils.isNotEmpty(records)) {
            long count = records.stream()
                    .map(RiskIdentificationRecordEntity::getObjectId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .count();
            if (count > 0) {
                return count;
            }
        }
        return objectService.count(Wrappers.<RiskObjectEntity>lambdaQuery()
                .eq(RiskObjectEntity::getUnitId, unit.getId())
                .eq(StringUtils.isNotBlank(unit.getCurrentIdentifyRoundNo()),
                        RiskObjectEntity::getIdentifyRoundNo, unit.getCurrentIdentifyRoundNo()));
    }

    private LocalDateTime resolveSubmitTime(RiskUnitEntity unit, Collection<RiskIdentificationRecordEntity> records) {
        if (CollectionUtils.isNotEmpty(records)) {
            return records.stream()
                    .map(RiskIdentificationRecordEntity::getSubmitTime)
                    .filter(Objects::nonNull)
                    .max(LocalDateTime::compareTo)
                    .orElse(unit.getLastIdentifySubmitTime());
        }
        return unit.getLastIdentifySubmitTime();
    }

    private RiskReviewRecordEntity firstRecord(Collection<RiskReviewRecordEntity> records) {
        if (CollectionUtils.isEmpty(records)) {
            return null;
        }
        return records.stream()
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private String unitTypeName(Long unitTypeId) {
        if (unitTypeId == null) {
            return "";
        }
        RiskUnitTypeEntity unitType = unitTypeService.getById(unitTypeId);
        return unitType == null ? "" : unitType.getTypeName();
    }

    private String areaName(Long areaId) {
        if (areaId == null) {
            return "";
        }
        RiskAreaEntity area = areaService.getById(areaId);
        return area == null ? "" : area.getAreaName();
    }

    private String userName(String userId) {
        if (StringUtils.isBlank(userId)) {
            return "";
        }
        Map<String, String> names = ouaaFacadeService.batchUserNames(List.of(userId));
        String name = names.get(normalizeUserId(userId));
        return StringUtils.isNotBlank(name) ? name : value(userId);
    }

    private String userNames(Collection<String> userIds) {
        Set<String> normalizedIds = distinctUserIds(userIds);
        if (normalizedIds.isEmpty()) {
            return "";
        }
        Map<String, String> names = ouaaFacadeService.batchUserNames(normalizedIds);
        String joinedNames = normalizedIds.stream()
                .map(userId -> {
                    String name = names.get(normalizeUserId(userId));
                    return StringUtils.isNotBlank(name) ? name : userId;
                })
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("、"));
        return value(joinedNames);
    }

    private Set<String> distinctUserIds(Collection<String> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return new LinkedHashSet<>();
        }
        return userIds.stream()
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<Long> toLongIds(Collection<String> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return new LinkedHashSet<>();
        }
        Set<Long> ids = new LinkedHashSet<>();
        for (String userId : userIds) {
            Long id = parseLong(userId);
            if (id != null) {
                ids.add(id);
            }
        }
        return ids;
    }

    private Long parseLong(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return Long.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String currentUserId() {
        Long userId = SecurityContextHolder.getPrincipalProperty(Principal::getId);
        return userId == null ? null : String.valueOf(userId);
    }

    private Integer tenant(RiskUnitEntity unit) {
        Integer currentTenant = currentTenant();
        if (currentTenant != null) {
            return currentTenant;
        }
        return unit == null ? null : unit.getTenant();
    }

    private Integer currentTenant() {
        try {
            return SessionContextHolder.getRequester() == null ? null : SessionContextHolder.getRequester().getTenant();
        } catch (RuntimeException ex) {
            return null;
        }
    }

    private String normalizeUserId(String userId) {
        Long parsed = parseLong(userId);
        return parsed == null ? userId : String.valueOf(parsed);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? "" : dateTime.format(DATE_TIME_FORMATTER);
    }

    private String formatDate(LocalDateTime dateTime) {
        return dateTime == null ? "" : dateTime.format(DATE_FORMATTER);
    }

    private String joinValues(Collection<String> values) {
        if (CollectionUtils.isEmpty(values)) {
            return "";
        }
        return values.stream()
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .distinct()
                .collect(Collectors.joining("、"));
    }

    private String measureSummary(Collection<RiskControlMeasureEntity> measures) {
        if (CollectionUtils.isEmpty(measures)) {
            return "";
        }
        List<String> contents = measures.stream()
                .map(RiskControlMeasureEntity::getContent)
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .toList();
        if (contents.isEmpty()) {
            return "";
        }
        List<String> summary = new ArrayList<>(contents.stream().limit(3).toList());
        String joined = summary.stream()
                .map(content -> abbreviate(content, 20))
                .collect(Collectors.joining("；"));
        if (contents.size() > 3) {
            joined = joined + "等" + contents.size() + "项";
        }
        return joined;
    }

    private String abbreviate(String value, int maxLength) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        String trimmed = value.trim();
        return trimmed.length() <= maxLength ? trimmed : trimmed.substring(0, maxLength) + "...";
    }

    private String riskLevelName(String level) {
        if (StringUtils.isBlank(level)) {
            return "";
        }
        try {
            return switch (RiskLevelEnum.valueOf(level)) {
                case MAJOR -> "重大风险";
                case LARGE -> "较大风险";
                case GENERAL -> "一般风险";
                case LOW -> "低风险";
            };
        } catch (IllegalArgumentException ex) {
            return level;
        }
    }

    private Map<String, String> newParams() {
        return new HashMap<>();
    }

    private String value(String value) {
        return value == null ? "" : value;
    }
}
