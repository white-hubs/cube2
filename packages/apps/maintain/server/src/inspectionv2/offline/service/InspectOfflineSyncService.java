package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.identity.client.QrcodeClient;
import com.guhecloud.cube.srv.gen.identity.client.vo.QrcodeVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.TaskService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectTaskTargetItemQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl.InspectQuery;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl.InspectTargetInfoQuery;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.TaskTargetRelationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.KeyValueDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.TaskTargetRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.TaskDetailResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.PerformInspectionVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.dao.InspectOfflineSyncChunkMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.dao.InspectOfflineSyncSessionMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.enums.InspectOfflineChunkTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.enums.InspectOfflineSyncSessionStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.dto.InspectOfflineTaskSnapshotDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.param.InspectOfflineCreateSyncSessionParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.po.InspectOfflineSyncChunkPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.po.InspectOfflineSyncSessionPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.result.InspectOfflineChunkManifestResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.result.InspectOfflineSyncSessionResult;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
/**
 * 日常巡检离线同步服务。
 * <p>
 * 负责创建同步会话、后台生成任务快照、写入 gzip 分片、返回 manifest 以及校验分片下载权限。
 */
public class InspectOfflineSyncService {
    private static final int SESSION_TTL_HOURS = 24;
    private static final int OBJECT_PAGE_SIZE = 500;
    private static final int QRCODE_QUERY_BATCH_SIZE = 500;

    private final InspectOfflineSyncSessionMapper sessionMapper;
    private final InspectOfflineSyncChunkMapper chunkMapper;
    private final InspectTaskMapper inspectTaskMapper;
    private final TaskService taskService;
    private final InspectQuery inspectQuery;
    private final TaskTargetRelationMapper taskTargetRelationMapper;
    private final InspectRecordMapper inspectRecordMapper;
    private final InspectTargetInfoQuery inspectTargetInfoQuery;
    private final InspectTaskTargetItemQueryService inspectTaskTargetItemQueryService;
    private final QrcodeClient qrcodeClient;

    @Resource(name = "InspectOfflineSyncExecutor")
    private ThreadPoolTaskExecutor inspectOfflineSyncExecutor;

    /**
     * 创建同步会话并触发后台快照构建。
     * <p>
     * 该方法只做轻量落库和任务调度，不在请求线程里同步组装大快照。
     */
    public InspectOfflineSyncSessionResult createSession(InspectOfflineCreateSyncSessionParam param) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Principal principal = SecurityContextHolder.getPrincipal();
        cleanupExpiredSessions(tenant, principal.getId());

        String sessionId = UUID.randomUUID().toString();
        String snapshotId = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(SESSION_TTL_HOURS);

        InspectOfflineSyncSessionPO session = new InspectOfflineSyncSessionPO();
        session.setId(IdWorker.getId());
        session.setSessionId(sessionId);
        session.setSnapshotId(snapshotId);
        session.setTenant(tenant);
        session.setUserId(principal.getId());
        session.setClientId(param.getClientId());
        session.setAppVersion(param.getAppVersion());
        session.setOfflineSchemaVersion(param.getOfflineSchemaVersion());
        session.setBaseCursor(param.getBaseCursor());
        session.setStatus(InspectOfflineSyncSessionStatusEnum.BUILDING);
        session.setProgress(0);
        session.setExpiresAt(expiresAt);
        session.setDeleted(false);
        sessionMapper.insert(session);

        try {
            inspectOfflineSyncExecutor.execute(() -> buildSession(sessionId, snapshotId, tenant, principal));
        } catch (TaskRejectedException e) {
            log.warn("离线巡检快照构建队列已满, sessionId={}", sessionId);
            updateSession(sessionId, InspectOfflineSyncSessionStatusEnum.FAILED, 100, null,
                    "SNAPSHOT_QUEUE_FULL", "离线同步任务较多，请稍后重试");
            session.setStatus(InspectOfflineSyncSessionStatusEnum.FAILED);
            session.setProgress(100);
            session.setErrorCode("SNAPSHOT_QUEUE_FULL");
            session.setErrorMessage("离线同步任务较多，请稍后重试");
        }
        return toResult(session, Collections.emptyList());
    }

    /**
     * 查询当前登录用户自己的同步会话状态和分片 manifest。
     */
    public InspectOfflineSyncSessionResult getSession(String sessionId) {
        cleanupExpiredSessions(SessionContextHolder.getRequester().getTenant(), SecurityContextHolder.getPrincipal().getId());
        InspectOfflineSyncSessionPO session = currentUserSession(sessionId);
        if (isExpired(session)) {
            expireSession(session);
            session.setStatus(InspectOfflineSyncSessionStatusEnum.EXPIRED);
            return toResult(session, Collections.emptyList());
        }
        List<InspectOfflineSyncChunkPO> chunks = chunkMapper.selectList(Wrappers.<InspectOfflineSyncChunkPO>lambdaQuery()
                .eq(InspectOfflineSyncChunkPO::getSessionId, sessionId)
                .orderByAsc(InspectOfflineSyncChunkPO::getPartNo));
        return toResult(session, chunks);
    }

    /**
     * 读取当前登录用户自己的同步分片。
     */
    public byte[] getChunk(String sessionId, String chunkId) {
        cleanupExpiredSessions(SessionContextHolder.getRequester().getTenant(), SecurityContextHolder.getPrincipal().getId());
        InspectOfflineSyncSessionPO session = currentUserSession(sessionId);
        if (isExpired(session)) {
            expireSession(session);
            throw new IllegalStateException("离线巡检同步会话已过期，请重新同步");
        }
        InspectOfflineSyncChunkPO chunk = chunkMapper.selectOne(Wrappers.<InspectOfflineSyncChunkPO>lambdaQuery()
                .eq(InspectOfflineSyncChunkPO::getSessionId, sessionId)
                .eq(InspectOfflineSyncChunkPO::getChunkId, chunkId)
                .last(" limit 1 "));
        Assert.isNotFound(chunk, "离线巡检分片不存在或已过期");
        Assert.isNotFound(chunk.getContentGzip(), "离线巡检分片内容不存在或已被清理");
        return chunk.getContentGzip();
    }

    /**
     * 后台构建同步会话。
     * <p>
     * 当前版本按任务生成一个 TASK 分片；构建期间更新进度，成功后将会话置为 READY。
     */
    private void buildSession(String sessionId, String snapshotId, Integer tenant, Principal principal) {
        try {
            SessionContextHolder.requestTenant(tenant);
            SecurityContextHolder.setPrincipal(principal);
            updateSession(sessionId, InspectOfflineSyncSessionStatusEnum.BUILDING, 5, null, null, null);

            List<InspectTaskPO> tasks = inspectTaskMapper.selectList(Wrappers.<InspectTaskPO>lambdaQuery()
                    .eq(InspectTaskPO::getTenant, tenant)
                    .eq(InspectTaskPO::getStatus, InspectTaskStatusEnum.EXECUTING)
                    .and(w -> w.like(InspectTaskPO::getReceiveIds, principal.getId())
                            .or()
                            .like(InspectTaskPO::getAssistIds, principal.getId()))
                    .orderByDesc(InspectTaskPO::getUpdateTime));

            int total = Math.max(tasks.size(), 1);
            int partNo = 1;
            for (InspectTaskPO task : tasks) {
                InspectOfflineTaskSnapshotDTO snapshot = buildTaskSnapshot(task);
                byte[] payload = gzip(JSON.toJSONString(snapshot).getBytes(StandardCharsets.UTF_8));
                String chunkId = UUID.randomUUID().toString();

                InspectOfflineSyncChunkPO chunk = new InspectOfflineSyncChunkPO();
                chunk.setId(IdWorker.getId());
                chunk.setChunkId(chunkId);
                chunk.setSessionId(sessionId);
                chunk.setSnapshotId(snapshotId);
                chunk.setTenant(tenant);
                chunk.setChunkType(InspectOfflineChunkTypeEnum.TASK);
                chunk.setTaskId(task.getId());
                chunk.setRevision(Optional.ofNullable(task.getRevision()).orElse(0L));
                chunk.setPartNo(partNo);
                chunk.setPartCount(tasks.size());
                chunk.setCompressedSize((long) payload.length);
                chunk.setSha256(sha256(payload));
                chunk.setStorageKey("db:" + chunkId);
                chunk.setContentGzip(payload);
                chunk.setExpiresAt(LocalDateTime.now().plusHours(SESSION_TTL_HOURS));
                chunk.setDeleted(false);
                chunkMapper.insert(chunk);

                updateSession(sessionId, InspectOfflineSyncSessionStatusEnum.BUILDING, 5 + (partNo * 90 / total), null, null, null);
                partNo++;
            }

            String cursor = LocalDateTime.now().toString();
            updateSession(sessionId, InspectOfflineSyncSessionStatusEnum.READY, 100, cursor, null, null);
        } catch (Exception e) {
            log.error("构建离线巡检快照失败, sessionId={}", sessionId, e);
            updateSession(sessionId, InspectOfflineSyncSessionStatusEnum.FAILED, 100, null, "SNAPSHOT_BUILD_FAILED", e.getMessage());
        }
    }

    /**
     * 组装单个任务的离线快照。
     * <p>
     * 首期复用在线 TaskService 语义以保证数据含义一致，后续性能深化时可替换为批量读模型。
     */
    private InspectOfflineTaskSnapshotDTO buildTaskSnapshot(InspectTaskPO task) {
        List<InspectionObjVO> objects = loadAllObjects(task.getId());
        fillObjectQrCodes(objects);
        List<PerformInspectionVO> inspections = new ArrayList<>();
        for (InspectionObjVO object : objects) {
            if (object.getTargetId() != null) {
                inspections.add(buildPerformInspection(task, object.getTargetId()));
            }
        }
        return InspectOfflineTaskSnapshotDTO.builder()
                .taskId(task.getId())
                .revision(Optional.ofNullable(task.getRevision()).orElse(0L))
                .snapshotTime(LocalDateTime.now())
                .detail(buildTaskDetail(task))
                .objectCount(taskService.getTaskObjCount(task.getId()))
                .objects(objects)
                .inspections(inspections)
                .build();
    }

    private TaskDetailResult buildTaskDetail(InspectTaskPO task) {
        TaskDetailResult result = new TaskDetailResult();
        result.setDeleted(task.getDeleted());
        result.setCreateBy(task.getCreateBy());
        result.setCreateTime(task.getCreateTime());
        result.setUpdateBy(task.getUpdateBy());
        result.setUpdateTime(task.getUpdateTime());
        result.setCreatorId(task.getCreatorId());
        result.setCreatorType(task.getCreatorType());
        result.setUpdaterId(task.getUpdaterId());
        result.setUpdaterType(task.getUpdaterType());
        result.setId(task.getId());
        result.setTenant(task.getTenant());
        result.setPlanId(task.getPlanId());
        result.setPlanName(task.getPlanName());
        result.setName(task.getName());
        result.setTypeId(task.getTypeId());
        result.setLevel(task.getLevel());
        result.setTypeName(task.getTypeName());
        result.setStatus(task.getStatus() == null ? null : task.getStatus().name());
        result.setPublisherId(task.getPublisherId());
        result.setPublisherName(task.getPublisherName());
        result.setLimitTimeJson(task.getLimitTimeJson());
        result.setLimitTotalMinute(task.getLimitTotalMinute());
        result.setReceiveType(task.getReceiveType());
        result.setReceiveDeptIds(task.getReceiveDeptIds());
        result.setReceivePostIds(task.getReceivePostIds());
        result.setReceiveRoleIds(task.getReceiveRoleIds());
        result.setReceiveIds(task.getReceiveIds());
        result.setAssistIds(task.getAssistIds());
        result.setValidEndTime(task.getValidEndTime());
        result.setBeginTime(task.getBeginTime());
        result.setEndTime(task.getEndTime());
        result.setTargetNum(task.getTargetNum());
        result.setRemark(task.getRemark());
        result.setMethod(task.getMethod());
        result.setReceiveNames(inspectQuery.extendUserNames(result.getTenant(), result.getReceiveIds()));
        result.setReceiveDeptNames(inspectQuery.extendDeptNames(result.getTenant(), result.getReceiveDeptIds()));
        result.setReceivePostNames(inspectQuery.extendPostNames(result.getTenant(), result.getReceivePostIds()));
        result.setReceiveRoleNames(inspectQuery.extendPostNames(result.getTenant(), result.getReceiveRoleIds()));
        result.setAssistNames(inspectQuery.extendUserNames(result.getTenant(), result.getAssistIds()));
        return result;
    }

    private PerformInspectionVO buildPerformInspection(InspectTaskPO task, Long objId) {
        TaskTargetRelationPO relation = taskTargetRelationMapper.selectOne(Wrappers.<TaskTargetRelationPO>lambdaQuery()
                .eq(TaskTargetRelationPO::getTaskId, task.getId())
                .eq(TaskTargetRelationPO::getTargetId, objId)
                .last(" limit 1 "));
        Assert.isNotFound(relation, "该任务没有关联巡检对象！");

        String targetCategory = relation.getTargetCategory().name();
        InspectRecordPO record = inspectRecordMapper.selectOne(Wrappers.<InspectRecordPO>lambdaQuery()
                .eq(InspectRecordPO::getTargetCategory, targetCategory)
                .eq(InspectRecordPO::getTargetId, objId)
                .eq(InspectRecordPO::getTaskId, task.getId())
                .last(" limit 1 "));
        Assert.isNotFound(record, "未找到指定的巡检记录");

        PerformInspectionVO inspection = PerformInspectionVO.builder()
                .taskName(task.getName())
                .taskId(task.getId())
                .targetId(objId)
                .recordId(record.getId())
                .targetName(relation.getTargetName())
                .targetCategory(targetCategory)
                .areaId(record.getAreaId())
                .areaName(record.getAreaName())
                .build();
        InspectTargetInfoDTO info = inspectTargetInfoQuery.getTargetInfoById(objId, targetCategory);
        if (info != null) {
            inspection.setAddress(info.getAddress());
            inspection.setTargetType(info.getTargetType());
            inspection.setTargetTypeName(info.getTargetTypeName());
            inspection.setAreaId(info.getAreaId());
            inspection.setAreaName(info.getAreaName());
        }
        inspection.setStandardVOS(inspectTaskTargetItemQueryService.getObjStandardList(task.getId(), objId));

        List<TaskTargetRelationPO> childTargets = taskTargetRelationMapper.selectList(Wrappers.<TaskTargetRelationPO>lambdaQuery()
                .select(TaskTargetRelationPO::getTargetId, TaskTargetRelationPO::getTargetName)
                .eq(TaskTargetRelationPO::getTaskId, relation.getTaskId())
                .eq(TaskTargetRelationPO::getTargetParentId, inspection.getTargetId())
                .groupBy(TaskTargetRelationPO::getTargetId));
        if (childTargets != null && !childTargets.isEmpty()) {
            inspection.setChildTargetNameList(childTargets.stream()
                    .map(target -> KeyValueDTO.builder().longKey(target.getTargetId()).value(target.getTargetName()).build())
                    .collect(Collectors.toList()));
        }
        return inspection;
    }

    /**
     * 分页读取任务下全部巡检对象。
     */
    private List<InspectionObjVO> loadAllObjects(Long taskId) {
        List<InspectionObjVO> all = new ArrayList<>();
        int pageNumber = 1;
        while (true) {
            TaskPageParam param = new TaskPageParam();
            param.setTaskId(taskId);
            param.setPageNumber(pageNumber);
            param.setPageSize(OBJECT_PAGE_SIZE);
            Pagination<InspectionObjVO> page = taskService.getInspectionObjInfo(param);
            List<InspectionObjVO> records = Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList());
            all.addAll(records);
            if (records.size() < OBJECT_PAGE_SIZE) {
                break;
            }
            pageNumber++;
        }
        return all;
    }

    /**
     * 批量查询对象关联的二维码并回填到离线快照对象。
     */
    private void fillObjectQrCodes(List<InspectionObjVO> objects) {
        if (objects == null || objects.isEmpty()) {
            return;
        }
        List<Long> targetIds = objects.stream()
                .map(InspectionObjVO::getTargetId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (targetIds.isEmpty()) {
            return;
        }

        Map<String, String> qrCodesByEntityId = new HashMap<>();
        for (int start = 0; start < targetIds.size(); start += QRCODE_QUERY_BATCH_SIZE) {
            int end = Math.min(start + QRCODE_QUERY_BATCH_SIZE, targetIds.size());
            List<QrcodeVO> qrcodes = qrcodeClient.getQrcodeCodesByEntityIds(targetIds.subList(start, end));
            for (QrcodeVO qrcode : Optional.ofNullable(qrcodes).orElse(Collections.emptyList())) {
                if (qrcode != null && qrcode.getEntityId() != null && qrcode.getCode() != null) {
                    qrCodesByEntityId.put(qrcode.getEntityId(), qrcode.getCode());
                }
            }
        }
        for (InspectionObjVO object : objects) {
            if (object.getTargetId() != null) {
                object.setQrCode(qrCodesByEntityId.get(String.valueOf(object.getTargetId())));
            }
        }
    }

    /**
     * 查询并校验同步会话属于当前登录租户和用户。
     */
    private InspectOfflineSyncSessionPO currentUserSession(String sessionId) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Long userId = SecurityContextHolder.getPrincipal().getId();
        InspectOfflineSyncSessionPO session = sessionMapper.selectOne(Wrappers.<InspectOfflineSyncSessionPO>lambdaQuery()
                .eq(InspectOfflineSyncSessionPO::getSessionId, sessionId)
                .eq(InspectOfflineSyncSessionPO::getTenant, tenant)
                .eq(InspectOfflineSyncSessionPO::getUserId, userId)
                .last(" limit 1 "));
        Assert.isNotFound(session, "离线巡检同步会话不存在或无权访问");
        return session;
    }

    /**
     * 清理当前用户已经过期的同步分片，并把对应 session 标记为 EXPIRED。
     * <p>
     * 只处理 expiresAt 早于当前时间的会话，不影响仍在构建或下载窗口内的正常会话。
     */
    private void cleanupExpiredSessions(Integer tenant, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        List<InspectOfflineSyncSessionPO> expiredSessions = sessionMapper.selectList(Wrappers.<InspectOfflineSyncSessionPO>lambdaQuery()
                .eq(InspectOfflineSyncSessionPO::getTenant, tenant)
                .eq(InspectOfflineSyncSessionPO::getUserId, userId)
                .lt(InspectOfflineSyncSessionPO::getExpiresAt, now)
                .ne(InspectOfflineSyncSessionPO::getStatus, InspectOfflineSyncSessionStatusEnum.EXPIRED));
        for (InspectOfflineSyncSessionPO session : expiredSessions) {
            expireSession(session);
        }
    }

    /**
     * 判断同步会话是否已经超过服务端缓存有效期。
     */
    private boolean isExpired(InspectOfflineSyncSessionPO session) {
        return session.getExpiresAt() != null && session.getExpiresAt().isBefore(LocalDateTime.now());
    }

    /**
     * 标记 session 过期并删除其分片正文和元数据。
     */
    private void expireSession(InspectOfflineSyncSessionPO session) {
        chunkMapper.delete(Wrappers.<InspectOfflineSyncChunkPO>lambdaQuery()
                .eq(InspectOfflineSyncChunkPO::getSessionId, session.getSessionId()));
        sessionMapper.update(null, Wrappers.<InspectOfflineSyncSessionPO>lambdaUpdate()
                .set(InspectOfflineSyncSessionPO::getStatus, InspectOfflineSyncSessionStatusEnum.EXPIRED)
                .set(InspectOfflineSyncSessionPO::getProgress, 100)
                .set(InspectOfflineSyncSessionPO::getErrorCode, "SESSION_EXPIRED")
                .set(InspectOfflineSyncSessionPO::getErrorMessage, "离线同步会话已过期，请重新同步")
                .eq(InspectOfflineSyncSessionPO::getSessionId, session.getSessionId()));
    }

    /**
     * 将会话和分片持久化对象转换为 App 侧同步会话响应。
     */
    private InspectOfflineSyncSessionResult toResult(InspectOfflineSyncSessionPO session, List<InspectOfflineSyncChunkPO> chunks) {
        List<InspectOfflineChunkManifestResult> manifest = new ArrayList<>();
        Optional.ofNullable(chunks).orElse(Collections.emptyList()).stream()
                .sorted(Comparator.comparing(InspectOfflineSyncChunkPO::getPartNo))
                .forEach(chunk -> manifest.add(InspectOfflineChunkManifestResult.builder()
                        .chunkId(chunk.getChunkId())
                        .type(chunk.getChunkType())
                        .taskId(chunk.getTaskId())
                        .revision(chunk.getRevision())
                        .partNo(chunk.getPartNo())
                        .partCount(chunk.getPartCount())
                        .compressedSize(chunk.getCompressedSize())
                        .sha256(chunk.getSha256())
                        .build()));
        return InspectOfflineSyncSessionResult.builder()
                .sessionId(session.getSessionId())
                .snapshotId(session.getSnapshotId())
                .status(session.getStatus())
                .cursor(session.getCursor())
                .expiresAt(session.getExpiresAt())
                .progress(session.getProgress())
                .pollIntervalSeconds(3)
                .deletedTaskIds(Collections.emptyList())
                .chunks(manifest)
                .inlineDelta(parseInlineDelta(session.getInlineDeltaJson()))
                .errorCode(session.getErrorCode())
                .errorMessage(session.getErrorMessage())
                .build();
    }

    /**
     * 解析内联小增量 JSON。
     */
    private Map<String, Object> parseInlineDelta(String inlineDeltaJson) {
        if (inlineDeltaJson == null || inlineDeltaJson.isBlank()) {
            return null;
        }
        return JSON.parseObject(inlineDeltaJson);
    }

    /**
     * 更新会话构建状态、进度、游标或错误信息。
     */
    private void updateSession(String sessionId, InspectOfflineSyncSessionStatusEnum status, Integer progress, String cursor,
                               String errorCode, String errorMessage) {
        sessionMapper.update(null, Wrappers.<InspectOfflineSyncSessionPO>lambdaUpdate()
                .set(InspectOfflineSyncSessionPO::getStatus, status)
                .set(InspectOfflineSyncSessionPO::getProgress, progress)
                .set(cursor != null, InspectOfflineSyncSessionPO::getCursor, cursor)
                .set(errorCode != null, InspectOfflineSyncSessionPO::getErrorCode, errorCode)
                .set(errorMessage != null, InspectOfflineSyncSessionPO::getErrorMessage, errorMessage)
                .eq(InspectOfflineSyncSessionPO::getSessionId, sessionId));
    }

    /**
     * 将 JSON 字节压缩成 gzip 分片内容。
     */
    private byte[] gzip(byte[] data) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(data);
        }
        return out.toByteArray();
    }

    /**
     * 计算分片内容摘要，供 App 下载后校验完整性。
     */
    private String sha256(byte[] data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data);
        StringBuilder sb = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
