package com.guhecloud.cube.biz.todo.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.todo.server.dao.TodoTaskMapper;
import com.guhecloud.cube.biz.todo.server.entity.TodoTask;
import com.guhecloud.cube.biz.todo.server.enumeration.TodoTaskStatusEnum;
import com.guhecloud.cube.biz.todo.server.param.TodoTaskFlowCirculationParam;
import com.guhecloud.cube.biz.todo.server.param.TodoTaskModifyParam;
import com.guhecloud.cube.biz.todo.server.param.TodoTaskQueryParam;
import com.guhecloud.cube.biz.todo.server.service.TodoPersonService;
import com.guhecloud.cube.biz.todo.server.service.TodoTagService;
import com.guhecloud.cube.biz.todo.server.service.TodoTaskService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TodoTaskServiceImpl extends ServiceImpl<TodoTaskMapper, TodoTask> implements TodoTaskService {

    private final TodoPersonService todoPersonService;
    private final TodoTagService todoTagService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull TodoTaskModifyParam param) {
        if (!CollectionUtils.isEmpty(param.getUserIds())) {
            TodoTask todoTask = ObjectUtils.copying(param, TodoTask.class);
            todoTask.setStatus(TodoTaskStatusEnum.TODO);
            this.baseMapper.insert(todoTask);
            this.todoPersonService.saveBatch(todoTask.getId(), todoTask.getBusinessKey(), param.getUserIds());
            return todoTask.getId();
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean todoTaskDone(@NonNull TodoTaskFlowCirculationParam param) {
        List<TodoTask> todoTasks = this.lambdaQuery().eq(TodoTask::getBusinessKey, param.getBusinessKey())
                .eq(TodoTask::getBusinessStatus, param.getBusinessStatus())
                .eq(TodoTask::getStatus, TodoTaskStatusEnum.TODO).list();
        if (CollectionUtils.isEmpty(todoTasks)) {
            return false;
        }
        Set<Long> todoTaskIds = todoTasks.stream().map(todoTask -> todoTask.getId()).collect(Collectors.toSet());
        LambdaUpdateWrapper<TodoTask> updateWrapper = Wrappers.<TodoTask>lambdaUpdate()
                .in(TodoTask::getId, todoTaskIds)
                .set(TodoTask::getStatus, TodoTaskStatusEnum.DONE)
                .set(TodoTask::getNextBusinessStatus, param.getNextBusinessStatus())
                .set(TodoTask::getNextBusinessStatusName, param.getNextBusinessStatusName())
                .set(TodoTask::getHandlerTime, param.getHandlerTime());
        this.update(updateWrapper);
        if (CollectionUtils.isEmpty(param.getUserIds())) {
            param.setUserIds(new HashSet<>());
        }
        if (Objects.nonNull(param.getUserId())) {
            param.getUserIds().add(param.getUserId());
        }
        /**
         * 有用户处理时,才设置用户已处理,否则只改任务状态,不管人员是否处理
         */
        if (!CollectionUtils.isEmpty(param.getUserIds())) {
            this.todoPersonService.updateStatus(todoTaskIds, param.getUserIds());
        }
        //生成下一节点待办任务
        if (param.getGeneratedNextStatusTask() && !CollectionUtils.isEmpty(param.getNextUserIds())) {
            TodoTask todoTask = todoTasks.get(0);
            TodoTaskModifyParam todoTaskModifyParam = TodoTaskModifyParam.builder()
                    .taskName(todoTask.getTaskName())
                    .taskTime(param.getTaskTime())
                    .applicationCode(todoTask.getApplicationCode())
                    .applicationName(todoTask.getApplicationName())
                    .businessKey(todoTask.getBusinessKey())
                    .businessType(todoTask.getBusinessType())
                    .businessTypeName(todoTask.getBusinessTypeName())
                    .businessStatus(param.getNextBusinessStatus())
                    .businessStatusName(param.getNextBusinessStatusName())
                    .businessPrincipal(param.getBusinessPrincipal())
                    .userIds(param.getNextUserIds())
                    .build();
            this.save(todoTaskModifyParam);
        }
        return true;
    }

    @Override
    public Page<TodoTask> todoPage(TodoTaskQueryParam param) {
        if (Boolean.TRUE.equals(param.getHandover())) {
            Set<String> businessTypes = todoTagService.listHandoverBusinessTypes();
            if (CollectionUtils.isEmpty(businessTypes)) {
                return new Page<>();
            }
            param.setBusinessTypes(businessTypes);
        }
        return this.baseMapper.selectTodoPage(param, SecurityContextHolder.getPrincipal().getId(), PagingUtils.convert(param));
    }

    @Override
    public Page<TodoTask> donePage(TodoTaskQueryParam param) {
        return this.baseMapper.selectDonePage(param, SecurityContextHolder.getPrincipal().getId(), PagingUtils.convert(param));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void todoTaskDel(@NonNull String businessKey) {
        LambdaUpdateWrapper<TodoTask> updateWrapper = Wrappers.<TodoTask>lambdaUpdate()
                .set(TodoTask::getTaskDelState, Boolean.TRUE)
                .eq(TodoTask::getBusinessKey, businessKey);
        this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatchByBusinessIds(@NonNull Set<String> businessKeys) {
        if (CollectionUtils.isEmpty(businessKeys)) {
            return;
        }
        List<TodoTask> todoTasks = this.lambdaQuery().in(TodoTask::getBusinessKey, businessKeys)
                .eq(TodoTask::getStatus, TodoTaskStatusEnum.TODO).list();
        DatabaseContextHolder.softDelete(this, todoTasks);
    }
}
