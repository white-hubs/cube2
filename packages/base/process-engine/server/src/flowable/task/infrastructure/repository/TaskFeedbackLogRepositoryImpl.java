package com.guhecloud.cube.base.flow.flowable.task.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command.TaskFeedbackLogCommand;
import com.guhecloud.cube.base.flow.flowable.task.domain.repository.TaskFeedbackLogRepository;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper.TaskFeedbackLogMapper;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.TaskFeedbackLogPO;
import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.TaskFeedbackLogDTO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/11 11:35
 * @Description: 任务反馈记录服务层
 */

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskFeedbackLogRepositoryImpl extends ServiceImpl<TaskFeedbackLogMapper, TaskFeedbackLogPO> implements TaskFeedbackLogRepository {

    private final FileRefClient fileRefClient;
    private final FileClient fileClient;
    private final UserClient userClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean taskFeedbackSubmit(TaskFeedbackLogCommand taskFeedbackLogCommand) {
        TaskFeedbackLogPO feedbackLogPO = TaskFeedbackLogPO.builder()
                .id(IdWorker.getId())
                .taskId(taskFeedbackLogCommand.getTaskId())
                .processId(taskFeedbackLogCommand.getProcessId())
                .taskNodeKey(taskFeedbackLogCommand.getTaskNodeKey())
                .feedbackInfo(taskFeedbackLogCommand.getFeedbackInfo())
                .feedbackUser(SecurityContextHolder.getPrincipal().getId())
                .build();
        this.save(feedbackLogPO);
        if (CollectionUtils.isNotEmpty(taskFeedbackLogCommand.getFileIdList())) {
            List<FileRefCreateParam> refCreateParamList = taskFeedbackLogCommand.getFileIdList().stream().map(fileId -> {
                FileRefCreateParam refCreateParam = new FileRefCreateParam();
                refCreateParam.setBusinessKey(feedbackLogPO.getId());
                refCreateParam.setFileId(fileId);
                return refCreateParam;
            }).collect(Collectors.toList());
            fileRefClient.saveBatch(refCreateParamList);
        }
        return true;
    }

    @Override
    public List<TaskFeedbackLogDTO> getTaskFeedbackLog(String processId, String taskNodeKey) {

        List<TaskFeedbackLogPO> taskFeedbackLogPOS = this.lambdaQuery()
                .eq(TaskFeedbackLogPO::getProcessId, processId)
                .eq(StringUtils.isNotBlank(taskNodeKey), TaskFeedbackLogPO::getTaskNodeKey, taskNodeKey)
                .orderByAsc(TaskFeedbackLogPO::getCreateTime)
                .list();
        if (CollectionUtils.isEmpty(taskFeedbackLogPOS)) {
            return Collections.emptyList();
        }
        List<Long> feedbackIdList = taskFeedbackLogPOS.stream().map(TaskFeedbackLogPO::getId).collect(Collectors.toList());
        BusinessKeysParam businessKeysParam = new BusinessKeysParam();
        businessKeysParam.setBusinessKeys(feedbackIdList);
        Map<Long, List<FileVO>> fileBusinessKeyMap = fileClient.getByBusinessKeys(businessKeysParam);
        List<Long> feedbackMobileList = taskFeedbackLogPOS.stream().map(TaskFeedbackLogPO::getFeedbackUser).collect(Collectors.toList());
        Map<Long, String> userMap = userClient.listUserByIds(feedbackMobileList)
                .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        return taskFeedbackLogPOS
                .stream()
                .map(t -> {
                    TaskFeedbackLogDTO taskFeedbackLogDTO = new TaskFeedbackLogDTO();
                    taskFeedbackLogDTO.setFeedbackInfo(t.getFeedbackInfo());
                    taskFeedbackLogDTO.setTaskId(t.getTaskId());
                    taskFeedbackLogDTO.setProcessId(t.getProcessId());
                    taskFeedbackLogDTO.setTaskNodeKey(t.getTaskNodeKey());
                    taskFeedbackLogDTO.setFileVOList(fileBusinessKeyMap.get(t.getId()));
                    taskFeedbackLogDTO.setFeedbackUser(userMap.get(t.getFeedbackUser()));
                    taskFeedbackLogDTO.setFeedbackUserId(t.getFeedbackUser());
                    taskFeedbackLogDTO.setFeedbackTime(t.getCreateTime());
                    return taskFeedbackLogDTO;
                }).collect(Collectors.toList());

    }
}
