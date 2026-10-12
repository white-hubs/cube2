package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectRecordCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordListSubmitCommand;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectTaskRecordSubmitCommand;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.dao.InspectOfflineSubmitOperationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.enums.InspectOfflineSubmitStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.param.InspectOfflineSubmitStatusParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.po.InspectOfflineSubmitOperationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.result.InspectOfflineSubmitBatchResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.result.InspectOfflineSubmitItemResult;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
/**
 * 离线巡检提交幂等服务。
 * <p>
 * 对带 clientOperationId 的提交进行去重、payload hash 校验、record revision 校验和状态查询。
 */
public class InspectOfflineSubmitOperationService {
    private final InspectOfflineSubmitOperationMapper operationMapper;
    private final InspectRecordCommandService inspectRecordCommandService;
    private final InspectTaskMapper inspectTaskMapper;

    /**
     * 执行一批离线巡检提交。
     * <p>
     * 每个 command 独立返回结果，避免同批中单个对象失败影响其他对象的状态表达。
     */
    public InspectOfflineSubmitBatchResult submit(InspectRecordListSubmitCommand batchCommand) {
        Assert.isParameterError(batchCommand == null || batchCommand.getCommandList() == null || batchCommand.getCommandList().isEmpty(), "提交内容不能为空！");
        List<InspectOfflineSubmitItemResult> results = new ArrayList<>();
        for (InspectTaskRecordSubmitCommand command : batchCommand.getCommandList()) {
            results.add(submitOne(batchCommand, command));
        }
        return InspectOfflineSubmitBatchResult.builder()
                .clientBatchId(batchCommand.getClientBatchId())
                .results(results)
                .build();
    }

    /**
     * 查询离线提交操作的最终状态。
     * <p>
     * App 在提交超时或响应丢失后使用该接口确认服务端是否已经处理成功。
     */
    public List<InspectOfflineSubmitItemResult> status(InspectOfflineSubmitStatusParam param) {
        Assert.isParameterError(param == null || param.getClientOperationIds() == null || param.getClientOperationIds().isEmpty(), "操作ID不能为空");
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Long userId = SecurityContextHolder.getPrincipal().getId();
        List<InspectOfflineSubmitOperationPO> operations = operationMapper.selectList(Wrappers.<InspectOfflineSubmitOperationPO>lambdaQuery()
                .eq(InspectOfflineSubmitOperationPO::getTenant, tenant)
                .eq(InspectOfflineSubmitOperationPO::getUserId, userId)
                .in(InspectOfflineSubmitOperationPO::getClientOperationId, param.getClientOperationIds()));
        List<InspectOfflineSubmitItemResult> results = new ArrayList<>();
        for (InspectOfflineSubmitOperationPO operation : operations) {
            results.add(toResult(operation, operation.getStatus()));
        }
        return results;
    }

    /**
     * 执行单个巡检对象的离线提交。
     * <p>
     * 如果同一 clientOperationId 已处理过，会根据 payload hash 返回幂等成功或冲突。
     */
    private InspectOfflineSubmitItemResult submitOne(InspectRecordListSubmitCommand batchCommand, InspectTaskRecordSubmitCommand command) {
        String clientOperationId = command.getClientOperationId();
        if (clientOperationId == null || clientOperationId.isBlank()) {
            return failed(command, "CLIENT_OPERATION_ID_REQUIRED", "离线提交必须传 clientOperationId");
        }

        String requestJson = JSON.toJSONString(command);
        String payloadHash = sha256(requestJson);
        InspectOfflineSubmitOperationPO existing = getExisting(clientOperationId);
        if (existing != null) {
            if (!payloadHash.equals(existing.getPayloadHash())) {
                return conflict(existing, "OPERATION_PAYLOAD_CONFLICT", "相同 clientOperationId 对应的提交内容不一致");
            }
            if (InspectOfflineSubmitStatusEnum.SUCCESS.equals(existing.getStatus())) {
                return toResult(existing, InspectOfflineSubmitStatusEnum.ALREADY_APPLIED);
            }
            return toResult(existing, existing.getStatus());
        }

        InspectOfflineSubmitOperationPO operation = createOperation(batchCommand, command, payloadHash, requestJson);
        try {
            InspectRecordPO record = validateBeforeSubmit(command);
            operation.setTaskId(record.getTaskId());
            operationMapper.updateById(operation);

            inspectRecordCommandService.submitTaskRecordBatch(Collections.singletonList(command));
            incrementRevision(record);

            InspectRecordPO latest = selectRecord(command.getRecordId());
            operation.setStatus(InspectOfflineSubmitStatusEnum.SUCCESS);
            operation.setServerRecordId(command.getRecordId());
            operation.setServerRecordRevision(normalRevision(latest.getRevision()));
            operation.setErrorCode(null);
            operation.setErrorMessage(null);
            operationMapper.updateById(operation);
            return toResult(operation, InspectOfflineSubmitStatusEnum.SUCCESS);
        } catch (Exception e) {
            log.warn("离线巡检提交失败, clientOperationId={}, recordId={}", clientOperationId, command.getRecordId(), e);
            operation.setStatus(InspectOfflineSubmitStatusEnum.FAILED);
            operation.setErrorCode("SUBMIT_FAILED");
            operation.setErrorMessage(e.getMessage());
            operationMapper.updateById(operation);
            return toResult(operation, InspectOfflineSubmitStatusEnum.FAILED);
        }
    }

    /**
     * 提交前做最终事务校验。
     * <p>
     * check 接口只能做前置提示；离线恢复后真正提交时仍需重新校验任务、记录、权限和 revision。
     */
    private InspectRecordPO validateBeforeSubmit(InspectTaskRecordSubmitCommand command) {
        InspectRecordPO record = selectRecord(command.getRecordId());
        if (record == null) {
            throw new IllegalStateException("记录不存在");
        }
        if (!InspectRecordStatusEnum.EXECUTING.equals(record.getStatus())) {
            throw new IllegalStateException("记录已被巡检或当前状态不允许提交");
        }
        if (command.getRecordRevision() != null && !command.getRecordRevision().equals(normalRevision(record.getRevision()))) {
            throw new IllegalStateException("记录版本已变化");
        }
        InspectTaskPO task = inspectTaskMapper.selectById(record.getTaskId());
        if (task == null) {
            throw new IllegalStateException("任务不存在");
        }
        if (!InspectTaskStatusEnum.EXECUTING.equals(task.getStatus())) {
            throw new IllegalStateException("任务状态不允许提交");
        }
        Long userId = SecurityContextHolder.getPrincipal().getId();
        String uid = String.valueOf(userId);
        boolean receive = task.getReceiveIds() != null && task.getReceiveIds().contains(uid);
        boolean assist = task.getAssistIds() != null && task.getAssistIds().contains(uid);
        if (!receive && !assist) {
            throw new IllegalStateException("当前用户无权提交该任务");
        }
        return record;
    }

    /**
     * 创建离线操作记录。
     * <p>
     * 先落库是为了后续重复请求或响应丢失查询能找到同一个 clientOperationId 的处理轨迹。
     */
    private InspectOfflineSubmitOperationPO createOperation(InspectRecordListSubmitCommand batchCommand, InspectTaskRecordSubmitCommand command,
                                                            String payloadHash, String requestJson) {
        InspectOfflineSubmitOperationPO operation = new InspectOfflineSubmitOperationPO();
        operation.setId(IdWorker.getId());
        operation.setTenant(SessionContextHolder.getRequester().getTenant());
        operation.setUserId(SecurityContextHolder.getPrincipal().getId());
        operation.setClientId(batchCommand.getClientId());
        operation.setClientBatchId(batchCommand.getClientBatchId());
        operation.setClientOperationId(command.getClientOperationId());
        operation.setRecordId(command.getRecordId());
        operation.setRecordRevision(command.getRecordRevision());
        operation.setPayloadHash(payloadHash);
        operation.setRequestJson(requestJson);
        operation.setStatus(InspectOfflineSubmitStatusEnum.FAILED);
        operation.setDeleted(false);
        operationMapper.insert(operation);
        return operation;
    }

    /**
     * 查询当前租户、当前用户下已经存在的客户端操作。
     */
    private InspectOfflineSubmitOperationPO getExisting(String clientOperationId) {
        return operationMapper.selectOne(Wrappers.<InspectOfflineSubmitOperationPO>lambdaQuery()
                .eq(InspectOfflineSubmitOperationPO::getTenant, SessionContextHolder.getRequester().getTenant())
                .eq(InspectOfflineSubmitOperationPO::getUserId, SecurityContextHolder.getPrincipal().getId())
                .eq(InspectOfflineSubmitOperationPO::getClientOperationId, clientOperationId)
                .last(" limit 1 "));
    }

    /**
     * 查询巡检记录。
     */
    private InspectRecordPO selectRecord(Long recordId) {
        if (recordId == null) {
            return null;
        }
        return inspectRecordCommandService.getById(recordId);
    }

    /**
     * 提交成功后递增记录和任务 revision。
     * <p>
     * App 后续增量同步可通过 revision 识别本地缓存已经过期。
     */
    private void incrementRevision(InspectRecordPO record) {
        inspectRecordCommandService.update(Wrappers.<InspectRecordPO>lambdaUpdate()
                .eq(InspectRecordPO::getId, record.getId())
                .setSql("revision = COALESCE(revision, 0) + 1"));
        if (record.getTaskId() != null) {
            inspectTaskMapper.update(null, Wrappers.<InspectTaskPO>lambdaUpdate()
                    .eq(InspectTaskPO::getId, record.getTaskId())
                    .setSql("revision = COALESCE(revision, 0) + 1"));
        }
    }

    /**
     * 构造未入库或参数级失败结果。
     */
    private InspectOfflineSubmitItemResult failed(InspectTaskRecordSubmitCommand command, String code, String message) {
        return InspectOfflineSubmitItemResult.builder()
                .clientOperationId(command.getClientOperationId())
                .recordId(command.getRecordId())
                .status(InspectOfflineSubmitStatusEnum.FAILED)
                .errorCode(code)
                .errorMessage(message)
                .build();
    }

    /**
     * 标记并返回幂等冲突结果。
     */
    private InspectOfflineSubmitItemResult conflict(InspectOfflineSubmitOperationPO operation, String code, String message) {
        operation.setStatus(InspectOfflineSubmitStatusEnum.CONFLICT);
        operation.setErrorCode(code);
        operation.setErrorMessage(message);
        operationMapper.updateById(operation);
        return toResult(operation, InspectOfflineSubmitStatusEnum.CONFLICT);
    }

    /**
     * 将离线操作记录转换为接口返回项。
     */
    private InspectOfflineSubmitItemResult toResult(InspectOfflineSubmitOperationPO operation, InspectOfflineSubmitStatusEnum status) {
        String taskStatus = null;
        if (operation.getTaskId() != null) {
            InspectTaskPO task = inspectTaskMapper.selectById(operation.getTaskId());
            taskStatus = task == null || task.getStatus() == null ? null : task.getStatus().name();
        }
        return InspectOfflineSubmitItemResult.builder()
                .clientOperationId(operation.getClientOperationId())
                .recordId(operation.getRecordId())
                .taskId(operation.getTaskId())
                .status(status)
                .serverRecordId(operation.getServerRecordId())
                .serverRecordRevision(operation.getServerRecordRevision())
                .taskStatus(taskStatus)
                .errorCode(operation.getErrorCode())
                .errorMessage(operation.getErrorMessage())
                .build();
    }

    /**
     * 将历史空 revision 归一为 0，兼容新增字段前的数据。
     */
    private Long normalRevision(Long revision) {
        return Optional.ofNullable(revision).orElse(0L);
    }

    /**
     * 计算提交内容摘要，用于判定同一 clientOperationId 是否提交了相同 payload。
     */
    private String sha256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("计算提交摘要失败", e);
        }
    }
}
