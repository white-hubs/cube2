package com.guhecloud.cube.biz.work.center.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.work.center.server.dao.WorkTaskMapper;
import com.guhecloud.cube.biz.work.center.server.entity.WorkTask;
import com.guhecloud.cube.biz.work.center.server.enumeration.WorkTaskStatusEnum;
import com.guhecloud.cube.biz.work.center.server.param.WorkTaskFlowCirculationParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkTaskModifyParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkTaskQueryParam;
import com.guhecloud.cube.biz.work.center.server.service.WorkPersonService;
import com.guhecloud.cube.biz.work.center.server.service.WorkTagService;
import com.guhecloud.cube.biz.work.center.server.service.WorkTaskService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkTaskServiceImpl extends ServiceImpl<WorkTaskMapper, WorkTask> implements WorkTaskService {

    private final WorkPersonService workPersonService;
    private final WorkTagService workTagService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull WorkTaskModifyParam param) {
        if (!CollectionUtils.isEmpty(param.getUserIds())) {
            WorkTask workTask = this.lambdaQuery().eq(WorkTask::getBusinessKey, param.getBusinessKey())
                    .last("limit 1").one();
            if (Objects.isNull(workTask)) {
                workTask = ObjectUtils.copying(param, WorkTask.class);
                workTask.setStatus(WorkTaskStatusEnum.TODO);
                this.baseMapper.insert(workTask);
            }
            this.workPersonService.saveBatch(workTask.getId(), workTask.getBusinessKey(), param.getUserIds());
            return workTask.getId();
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean workTaskDone(@NonNull WorkTaskFlowCirculationParam param) {
        List<WorkTask> workTasks = this.lambdaQuery().eq(WorkTask::getBusinessKey, param.getBusinessKey()).list();
        if (CollectionUtils.isEmpty(workTasks)) {
            return false;
        }
        Set<Long> workTaskIds = workTasks.stream().map(WorkTask::getId).collect(Collectors.toSet());
        LambdaUpdateWrapper<WorkTask> updateWrapper = Wrappers.<WorkTask>lambdaUpdate()
                .in(WorkTask::getId, workTaskIds)
                .set(WorkTask::getBusinessStatus, param.getNextBusinessStatus())
                .set(WorkTask::getBusinessStatusName, param.getNextBusinessStatusName())
                .set(WorkTask::getHandlerTime, param.getHandlerTime());
        if (!param.getGeneratedNextStatusTask()) {
            updateWrapper.set(WorkTask::getStatus, WorkTaskStatusEnum.DONE);
        }
        this.update(updateWrapper);
        if (CollectionUtils.isEmpty(param.getUserIds())) {
            param.setUserIds(new HashSet<>());
        }
        if (Objects.nonNull(param.getUserId())) {
            param.getUserIds().add(param.getUserId());
        }
        //当前用户已经处理完成
        if (!CollectionUtils.isEmpty(param.getUserIds())) {
            this.workPersonService.updateStatus(workTaskIds, param.getUserIds());
        }
        if (!CollectionUtils.isEmpty(param.getNextUserIds())) {
            param.getUserIds().addAll(param.getNextUserIds());
        }
        /**
         * 生成下一节点用户任务
         */
        if (!CollectionUtils.isEmpty(param.getNextUserIds())) {
            for (WorkTask workTask : workTasks) {
                this.workPersonService.saveBatch(workTask.getId(), workTask.getBusinessKey(), new ArrayList<>(param.getNextUserIds()));
            }
        }
        return true;
    }

    @Override
    public Page<WorkTask> workPage(WorkTaskQueryParam param) {
        if (Boolean.TRUE.equals(param.getHandover())) {
            Set<String> businessTypes = workTagService.listHandoverBusinessTypes();
            if (CollectionUtils.isEmpty(businessTypes)) {
                return new Page<>();
            }
            param.setBusinessTypes(businessTypes);
        }
        return this.baseMapper.selectWorkPage(param, SecurityContextHolder.getPrincipal().getId(), PagingUtils.convert(param));
    }

    @Override
    public Page<WorkTask> donePage(WorkTaskQueryParam param) {
        return this.baseMapper.selectDonePage(param, SecurityContextHolder.getPrincipal().getId(), PagingUtils.convert(param));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void workTaskDel(@NonNull String businessKey) {
        LambdaUpdateWrapper<WorkTask> updateWrapper = Wrappers.<WorkTask>lambdaUpdate()
                .set(WorkTask::getTaskDelState, Boolean.TRUE)
                .eq(WorkTask::getBusinessKey, businessKey);
        this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatchByBusinessIds(@NonNull Set<String> businessKeys) {
        if (CollectionUtils.isEmpty(businessKeys)) {
            return;
        }
        LambdaUpdateWrapper<WorkTask> updateWrapper = Wrappers.<WorkTask>lambdaUpdate()
                .set(WorkTask::getTaskDelState, Boolean.TRUE)
                .in(WorkTask::getBusinessKey, businessKeys);
        this.update(updateWrapper);
    }
}
