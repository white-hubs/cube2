package com.guhecloud.cube.biz.work.center.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.biz.work.center.server.coverts.HandoverRecordCovertToVO;
import com.guhecloud.cube.biz.work.center.server.coverts.WorkTaskCovertToVO;
import com.guhecloud.cube.biz.work.center.server.dao.HandoverRecordMapper;
import com.guhecloud.cube.biz.work.center.server.dao.WorkTagReferBusinessTypeMapper;
import com.guhecloud.cube.biz.work.center.server.dao.WorkTaskMapper;
import com.guhecloud.cube.biz.work.center.server.entity.HandoverRecord;
import com.guhecloud.cube.biz.work.center.server.entity.HandoverRecordExtend;
import com.guhecloud.cube.biz.work.center.server.entity.HandoverTaskRef;
import com.guhecloud.cube.biz.work.center.server.entity.WorkTagReferBusinessType;
import com.guhecloud.cube.biz.work.center.server.entity.WorkTask;
import com.guhecloud.cube.biz.work.center.server.enumeration.HandoverStatusEnum;
import com.guhecloud.cube.biz.work.center.server.param.HandTakeoverParam;
import com.guhecloud.cube.biz.work.center.server.param.HandoverRecordExtendParam;
import com.guhecloud.cube.biz.work.center.server.param.HandoverRecordParam;
import com.guhecloud.cube.biz.work.center.server.param.HandoverRecordQueryParam;
import com.guhecloud.cube.biz.work.center.server.rocketmq.message.WorkTaskTransferMessage;
import com.guhecloud.cube.biz.work.center.server.service.HandoverRecordExtendService;
import com.guhecloud.cube.biz.work.center.server.service.HandoverRecordService;
import com.guhecloud.cube.biz.work.center.server.service.HandoverTaskRefService;
import com.guhecloud.cube.biz.work.center.server.vo.HandoverRecordDetailVO;
import com.guhecloud.cube.biz.work.center.server.vo.HandoverRecordExtendVO;
import com.guhecloud.cube.biz.work.center.server.vo.HandoverRecordVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkTaskPageVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.guhecloud.platform.base.constants.DateFormat.COMPACT_DATETIME_FORMAT;
import static com.guhecloud.platform.base.util.DateUtils.PURE_DATE_FORMAT;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/10/29 16:00
 * @Description:
 */

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class HandoverRecordServiceImpl extends ServiceImpl<HandoverRecordMapper, HandoverRecord> implements HandoverRecordService {
    private static final String WORK_SIGN_FILE_SCENE = "WORK::HANDOVER";
    public static final String SHIFT_OVER_MESSAGE = "SHIFT_OVER_MESSAGE";
    private static final String TAKE_OVER_MESSAGE = "TAKE_OVER_MESSAGE";
    private final HandoverTaskRefService handoverTaskRefService;
    private final HandoverRecordExtendService handoverRecordExtendService;

    private final WorkTaskMapper workTaskMapper;

    private final WorkTagReferBusinessTypeMapper workTagReferBusinessTypeMapper;

    private final FileOperateClient fileOperateClient;
    private final FileRefClient fileRefClient;
    private final UserClient userClient;
    private final FileClient fileClient;
    private final MessageSendClient messageSendClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addHandoverRecord(HandoverRecordParam param) {
        Assert.isParameterError(Objects.equals(param.getTakeOverUser(), SecurityContextHolder.getPrincipal().getId()),
                                "不能交接给自己");
        if (CollectionUtils.isNotEmpty(param.getHandoverTaskList())) {
            Integer number = workTaskMapper.selectAllowedHandoverTaskList(param.getHandoverTaskList());
            number = Objects.isNull(number) ? 0 : number;
            Assert.isParameterError(number != param.getHandoverTaskList().size(), "存在不允许交班的任务");

        }
        var now = LocalDateTime.now();
        var handoverTime = DateUtils.format(now, PURE_DATE_FORMAT);
        var handoverRecord = new HandoverRecord();
        handoverRecord.setId(IdWorker.getId());
        handoverRecord.setRecordName(handoverTime + "工作交接");
        handoverRecord.setTaskNum(CollectionUtils.isEmpty(param.getHandoverTaskList()) ? 0 : param.getHandoverTaskList().size());
        handoverRecord.setShiftOverUser(SecurityContextHolder.getPrincipal().getId());
        handoverRecord.setShiftOverTime(now);
        handoverRecord.setTakeOverUser(param.getTakeOverUser());
        handoverRecord.setStatus(HandoverStatusEnum.WAIT_HANDOVER);
        handoverRecord.setShiftSignFileId(param.getShiftSignFileId());
        handoverRecord.setRemarks(param.getRemarks());
        handoverTaskRefService.addHandoverTaskRef(handoverRecord.getId(), param.getHandoverTaskList());
        //文件持久化
        if (Objects.nonNull(param.getShiftSignFileId())) {
            fileOperateClient.fileInduceSingle(WORK_SIGN_FILE_SCENE, null, param.getShiftSignFileId());
        }
        if (CollectionUtils.isNotEmpty(param.getFileIds())) {
            FileRefCreateParam fileParam = new FileRefCreateParam();
            fileParam.setFileIdList(param.getFileIds());
            fileParam.setBusinessKey(handoverRecord.getId());
            fileParam.setSceneCode(WORK_SIGN_FILE_SCENE);
            fileRefClient.refInduce(fileParam);
        }
        this.save(handoverRecord);
        saveHandoverExtend(handoverRecord.getId(), param.getExtend());
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userName", SecurityContextHolder.getPrincipal().getName());
        paramMap.put("taskName", handoverRecord.getRecordName());
        List<MessageNotifyTarget> notifyTargets = new ArrayList<>();
        MessageNotifyTarget notifyTarget = new MessageNotifyTarget();
        notifyTarget.setNotifyTargetType(MessageNotifyTargetEnum.USER);
        notifyTarget.setIds(Collections.singleton(param.getTakeOverUser()));
        notifyTargets.add(notifyTarget);
        MessageSceneSendRequest message = MessageSceneSendRequest.builder()
                .tenant(SessionContextHolder.getRequester().getTenant())
                .msgSceneCode(SHIFT_OVER_MESSAGE)
                .businessId(String.valueOf(handoverRecord.getId()))
                .tmpParams(paramMap)
                .messageNotifyTargets(notifyTargets)
                .build();
        try {
            MessageSendResponse messageSendResponse = messageSendClient.sceneSend(message);
            log.info("工作记录交接消息发送结果,{}", JsonUtils.serialize(messageSendResponse));
        } catch (Exception e) {
            log.error("工作记录交接发消息发送失败", e);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void takeOver(HandTakeoverParam param) {
        HandoverRecord handoverRecord = this.getById(param.getId());
        Assert.isConflict(Objects.isNull(handoverRecord), "交接记录不存在");
        if (!Objects.equals(handoverRecord.getTakeOverUser(), SecurityContextHolder.getPrincipal().getId())) {
            throw new ServiceException("您没有接班权限");
        }
        Assert.isConflict(!HandoverStatusEnum.WAIT_HANDOVER.equals(handoverRecord.getStatus()), "当前记录已接班或已撤销");
        List<HandoverTaskRef> handoverTaskRefList = handoverTaskRefService.lambdaQuery()
                .eq(HandoverTaskRef::getHandoverId, param.getId())
                .list();
        handoverRecord.setStatus(HandoverStatusEnum.HAND_OVERED);
        handoverRecord.setTakeOverTime(LocalDateTime.now());
        handoverRecord.setTakeSignFileId(param.getTakeSignFileId());
        if (Objects.nonNull(param.getTakeSignFileId())) {
            fileOperateClient.fileInduceSingle(WORK_SIGN_FILE_SCENE, null, param.getTakeSignFileId());
        }
        this.updateById(handoverRecord);
        if (CollectionUtils.isNotEmpty(handoverTaskRefList)) {
            var workTasks = workTaskMapper.selectList(Wrappers.<WorkTask>lambdaQuery()
                                                              .in(WorkTask::getId, handoverTaskRefList.stream()
                                                                      .map(HandoverTaskRef::getTaskId)
                                                                      .collect(Collectors.toList())));
            Assert.isNotFound(CollectionUtils.isEmpty(workTasks), "交接任务不存在");
            Set<String> businessTypes = workTasks.stream().map(WorkTask::getBusinessType).collect(Collectors.toSet());

            Map<String, String> workTagCodeMap = Optional
                    .ofNullable(workTagReferBusinessTypeMapper.selectList(Wrappers.<WorkTagReferBusinessType>lambdaQuery()
                                                                                  .in(WorkTagReferBusinessType::getBusinessType, businessTypes)))
                    .orElse(new ArrayList<>())
                    .stream()
                    .collect(Collectors.toMap(WorkTagReferBusinessType::getBusinessType, WorkTagReferBusinessType::getWorkTagCode, (k1, k2) -> k1));
            Map<String, Set<String>> workTagBusinessMap = new HashMap<>();
            for (WorkTask workTask : workTasks) {
                if (!workTagCodeMap.containsKey(workTask.getBusinessType())) {
                    continue;
                }
                workTagBusinessMap.computeIfAbsent(workTagCodeMap.get(workTask.getBusinessType()), v -> new HashSet<>())
                        .add(workTask.getBusinessKey());
            }
            for (var entry : workTagBusinessMap.entrySet()) {
                RocketMQContextHolder.send(WorkTaskTransferMessage.TOPIC + ":" + entry.getKey(),
                                           WorkTaskTransferMessage.builder()
                                                   .userId(handoverRecord.getShiftOverUser())
                                                   .targetId(handoverRecord.getTakeOverUser())
                                                   .tenantId(SessionContextHolder.getRequester().getTenant())
                                                   .businessKeyList(new ArrayList<>(entry.getValue()))
                                                   .build());
            }
        }

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userName", SecurityContextHolder.getPrincipal().getName());
        paramMap.put("takeTime", DateUtils.format(LocalDateTime.now(), COMPACT_DATETIME_FORMAT));
        paramMap.put("taskName", handoverRecord.getRecordName());
        List<MessageNotifyTarget> notifyTargets = new ArrayList<>();
        MessageNotifyTarget notifyTarget = new MessageNotifyTarget();
        notifyTarget.setNotifyTargetType(MessageNotifyTargetEnum.USER);
        notifyTarget.setIds(Collections.singleton(handoverRecord.getShiftOverUser()));
        notifyTargets.add(notifyTarget);
        MessageSceneSendRequest message = MessageSceneSendRequest.builder()
                .tenant(SessionContextHolder.getRequester().getTenant())
                .msgSceneCode(TAKE_OVER_MESSAGE)
                .businessId(String.valueOf(handoverRecord.getId()))
                .tmpParams(paramMap)
                .messageNotifyTargets(notifyTargets)
                .build();
        MessageSendClient sendClient = ApplicationContextHolder.getBean(MessageSendClient.class);
        try {
            MessageSendResponse messageSendResponse = sendClient.sceneSend(message);
            log.info("工作记录交接消息发送结果,{}", JsonUtils.serialize(messageSendResponse));
        } catch (Exception e) {
            log.error("工作记录交接发消息发送失败", e);
        }

    }

    @Override
    public Pagination<HandoverRecordVO> getHandoverRecord(HandoverRecordQueryParam param) {
        Period period = param.getPeriod();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (Objects.nonNull(period)) {
            startTime = period.getBegin();
            endTime = period.getEnd();
        }
        Set<Long> userIds = new HashSet<>();
        if (StringUtils.isNotBlank(param.getKeyword())) {
            userIds = Optional.ofNullable(userClient.listUsers(param.getKeyword()))
                    .orElse(new ArrayList<>())
                    .stream().map(UserDTO::getId).collect(Collectors.toSet());

        }
        if (Boolean.TRUE.equals(param.getPermission())) {
            userIds.add(SecurityContextHolder.getPrincipal().getId());
        }
        //已交班，且接班人是自己
        boolean isTakeOver = false;
        boolean isShiftOver = false;
        if (Objects.nonNull(param.getStatus())) {
            switch (param.getStatus()) {
                case HAND_OVERED:
                    isShiftOver = true;
                    break;
                case TAKE_OVERED:
                    param.setStatus(HandoverStatusEnum.HAND_OVERED);
                    isTakeOver = true;
                    break;
                case HAND_TAKE:
                    param.setStatus(HandoverStatusEnum.HAND_OVERED);
                    break;
                case CANCEL:
                    break;
            }
        }
        Set<Long> finalUserIds = userIds;
        Page<HandoverRecord> handoverRecordPage = this.lambdaQuery()
                .between(Objects.nonNull(startTime) && Objects.nonNull(endTime), HandoverRecord::getShiftOverTime, startTime, endTime)
                .eq(Objects.nonNull(param.getStatus()), HandoverRecord::getStatus, param.getStatus())
                .like(StringUtils.isNotBlank(param.getKeyword()), HandoverRecord::getRecordName, param.getKeyword())
                .eq(param.getShiftOverUser() != null, HandoverRecord::getShiftOverUser, param.getShiftOverUser())
                .eq(param.getTakeOverUser() != null, HandoverRecord::getTakeOverUser, param.getTakeOverUser())
                .and(CollectionUtils.isNotEmpty(finalUserIds), t ->
                        t.or().in(CollectionUtils.isNotEmpty(finalUserIds), HandoverRecord::getShiftOverUser, finalUserIds)
                                .or().in(CollectionUtils.isNotEmpty(finalUserIds), HandoverRecord::getTakeOverUser, finalUserIds)
                    )
                .eq(isTakeOver, HandoverRecord::getTakeOverUser, SecurityContextHolder.getPrincipal().getId())
                .eq(isShiftOver, HandoverRecord::getShiftOverUser, SecurityContextHolder.getPrincipal().getId())
                .orderByDesc(HandoverStatusEnum.HAND_OVERED.equals(param.getStatus()), HandoverRecord::getTakeOverTime)
                .orderByDesc(HandoverStatusEnum.WAIT_HANDOVER.equals(param.getStatus()), HandoverRecord::getShiftOverTime)
                .page(PagingUtils.convert(param));
        if (CollectionUtils.isEmpty(handoverRecordPage.getRecords())) {
            return new Pagination<>();
        }
        Set<Long> queryUserIds = new HashSet<>();
        handoverRecordPage.getRecords().forEach(handoverRecord -> {
            if (Objects.nonNull(handoverRecord.getShiftOverUser())) {
                queryUserIds.add(handoverRecord.getShiftOverUser());
            }
            if (Objects.nonNull(handoverRecord.getTakeOverUser())) {
                queryUserIds.add(handoverRecord.getTakeOverUser());
            }
        });
        Map<Long, String> userNameMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(queryUserIds)) {
            userNameMap = Optional.ofNullable(userClient.listUserByIds(queryUserIds))
                    .orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        }
        return HandoverRecordCovertToVO.toHandoverRecordVOPage(handoverRecordPage, userNameMap);
    }

    @Override
    public HandoverRecordDetailVO getHandoverRecordDetail(Long id) {
        HandoverRecord handoverRecord = this.getById(id);
        Assert.isNotFound(Objects.isNull(handoverRecord), "交接记录不存在");
        List<HandoverTaskRef> handoverTaskRefList = handoverTaskRefService.lambdaQuery()
                .eq(HandoverTaskRef::getHandoverId, id)
                .list();
        List<WorkTaskPageVO> workTaskVOs = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(handoverTaskRefList)) {
            List<WorkTask> workTasks = workTaskMapper.selectList(Wrappers.<WorkTask>lambdaQuery()
                                                                         .in(WorkTask::getId, handoverTaskRefList.stream()
                                                                                 .map(HandoverTaskRef::getTaskId)
                                                                                 .collect(Collectors.toList())));
            workTaskVOs = WorkTaskCovertToVO.toWorkTaskPageVOList(workTasks);
        }
        UserDTO user = userClient.getUser(handoverRecord.getShiftOverUser());
        HandoverRecordDetailVO result = HandoverRecordCovertToVO.toHandoverRecordDetailVO(handoverRecord);
        result.setShiftOverUserName(Objects.nonNull(user) ? user.getName() : null);
        result.setShiftOverTask(workTaskVOs);
        result.setFiles(fileClient.getByBusinessKey(handoverRecord.getId()));
        result.setExtend(buildHandoverExtendVO(id));
        //获取签名
        List<Long> signIdList = new ArrayList<>();
        if (Objects.nonNull(handoverRecord.getShiftSignFileId())) {
            signIdList.add(handoverRecord.getShiftSignFileId());
        }
        if (Objects.nonNull(handoverRecord.getTakeSignFileId())) {
            signIdList.add(handoverRecord.getTakeSignFileId());
        }
        if (CollectionUtils.isNotEmpty(signIdList)) {
            Map<Long, FileVO> signMap = Optional.ofNullable(fileClient.getByIds(signIdList))
                    .orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(FileVO::getId, Function.identity()));
            result.setShiftOverUserSign(signMap.get(handoverRecord.getShiftSignFileId()));
            result.setTakeOverUserSign(signMap.get(handoverRecord.getTakeSignFileId()));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(Long id) {
        HandoverRecord handoverRecord = this.getById(id);
        Assert.isConflict(Objects.isNull(handoverRecord), "交接记录不存在");
        Assert.isConflict(!Objects.equals(handoverRecord.getStatus(), HandoverStatusEnum.WAIT_HANDOVER), "当前记录不为待接班无法撤销");
        handoverRecord.setStatus(HandoverStatusEnum.CANCEL);
        return this.updateById(handoverRecord);
    }

    @Override
    public List<Integer> getTaskTenantList() {
        return this.workTaskMapper.selectHandoverRecordTenant();
    }

    @Override
    public List<HandoverRecordVO> getOverdueTask(Period period, Integer value) {
        List<HandoverRecordVO> overdueTask = this.workTaskMapper.getOverdueTask(period, value);
        if (CollectionUtils.isEmpty(overdueTask)) {
            return new ArrayList<>();
        }
        Set<Long> userIds = overdueTask.stream().map(HandoverRecordVO::getShiftOverUserId).collect(Collectors.toSet());
        UserQuery userQuery = new UserQuery();
        userQuery.setUserIds(userIds);
        Map<Long, String> userMap = userClient.postList(userQuery)
                .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        overdueTask.forEach(vo -> vo.setShiftOverUserName(userMap.get(vo.getShiftOverUserId())));
        return overdueTask;
    }

    private void saveHandoverExtend(Long handoverId, HandoverRecordExtendParam extendParam) {
        if (Objects.isNull(extendParam)) {
            return;
        }
        HandoverRecordExtend handoverRecordExtend = new HandoverRecordExtend();
        handoverRecordExtend.setId(IdWorker.getId());
        handoverRecordExtend.setHandoverId(handoverId);
        handoverRecordExtend.setAttInfo1(extendParam.getAttInfo1());
        handoverRecordExtend.setAttInfo2(extendParam.getAttInfo2());
        handoverRecordExtend.setAttInfo3(extendParam.getAttInfo3());
        handoverRecordExtend.setAttInfo4(extendParam.getAttInfo4());
        List<FileRefCreateParam> fileRefParams = new ArrayList<>();
        handoverRecordExtend.setFile1(bindExtendFileKey(extendParam.getFile1(), fileRefParams));
        handoverRecordExtend.setFile2(bindExtendFileKey(extendParam.getFile2(), fileRefParams));
        handoverRecordExtend.setFile3(bindExtendFileKey(extendParam.getFile3(), fileRefParams));
        handoverRecordExtend.setFile4(bindExtendFileKey(extendParam.getFile4(), fileRefParams));
        handoverRecordExtendService.save(handoverRecordExtend);
        if (CollectionUtils.isNotEmpty(fileRefParams)) {
            fileRefClient.refInduceBatch(fileRefParams);
        }
    }

    private HandoverRecordExtendVO buildHandoverExtendVO(Long handoverId) {
        HandoverRecordExtend extend = handoverRecordExtendService.lambdaQuery()
                .eq(HandoverRecordExtend::getHandoverId, handoverId)
                .one();
        if (Objects.isNull(extend)) {
            return null;
        }
        List<Long> businessKeys = new ArrayList<>();
        addBusinessKey(businessKeys, extend.getFile1());
        addBusinessKey(businessKeys, extend.getFile2());
        addBusinessKey(businessKeys, extend.getFile3());
        addBusinessKey(businessKeys, extend.getFile4());
        Map<Long, List<FileVO>> fileMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(businessKeys)) {
            fileMap = Optional.ofNullable(fileClient.getByBusinessKeys(
                            BusinessKeysParam.builder().businessKeys(businessKeys.stream().distinct().collect(Collectors.toList())).build()))
                    .orElse(new HashMap<>());
        }
        HandoverRecordExtendVO vo = new HandoverRecordExtendVO();
        vo.setAttInfo1(extend.getAttInfo1());
        vo.setAttInfo2(extend.getAttInfo2());
        vo.setAttInfo3(extend.getAttInfo3());
        vo.setAttInfo4(extend.getAttInfo4());
        vo.setFile1(toFileVOList(extend.getFile1(), fileMap));
        vo.setFile2(toFileVOList(extend.getFile2(), fileMap));
        vo.setFile3(toFileVOList(extend.getFile3(), fileMap));
        vo.setFile4(toFileVOList(extend.getFile4(), fileMap));
        return vo;
    }

    private Long bindExtendFileKey(List<Long> fileIds, List<FileRefCreateParam> fileRefParams) {
        if (CollectionUtils.isEmpty(fileIds)) {
            return null;
        }
        Long businessKey = IdWorker.getId();
        FileRefCreateParam fileParam = new FileRefCreateParam();
        fileParam.setFileIdList(fileIds.stream().distinct().collect(Collectors.toList()));
        fileParam.setBusinessKey(businessKey);
        fileParam.setSceneCode(WORK_SIGN_FILE_SCENE);
        fileRefParams.add(fileParam);
        return businessKey;
    }

    private void addBusinessKey(List<Long> businessKeys, Long businessKey) {
        if (Objects.nonNull(businessKey)) {
            businessKeys.add(businessKey);
        }
    }

    private List<FileVO> toFileVOList(Long businessKey, Map<Long, List<FileVO>> fileMap) {
        if (Objects.isNull(businessKey)) {
            return new ArrayList<>();
        }
        return Optional.ofNullable(fileMap.get(businessKey)).orElse(new ArrayList<>());
    }
}
