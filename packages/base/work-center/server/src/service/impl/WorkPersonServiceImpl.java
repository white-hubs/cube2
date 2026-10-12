package com.guhecloud.cube.biz.work.center.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.work.center.server.dao.WorkPersonMapper;
import com.guhecloud.cube.biz.work.center.server.entity.WorkPerson;
import com.guhecloud.cube.biz.work.center.server.enumeration.WorkTaskStatusEnum;
import com.guhecloud.cube.biz.work.center.server.service.WorkPersonService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkPersonServiceImpl extends ServiceImpl<WorkPersonMapper, WorkPerson> implements WorkPersonService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(@NonNull Long workTaskId, @NonNull String businessKey, @NonNull List<Long> userIds) {
        List<WorkPerson> workPeople = this.lambdaQuery().eq(WorkPerson::getWorkTaskId, workTaskId)
                .eq(WorkPerson::getBusinessKey, businessKey)
                .in(WorkPerson::getUserId, userIds)
                .list();
        if (!CollectionUtils.isEmpty(workPeople)) {
            DatabaseContextHolder.softDelete(this, workPeople);
        }
        List<WorkPerson> workPeoples = new ArrayList<>();
        userIds.forEach(userId -> {
            WorkPerson workPerson = WorkPerson.builder()
                    .workTaskId(workTaskId)
                    .businessKey(businessKey)
                    .userId(userId)
                    .status(WorkTaskStatusEnum.TODO)
                    .build();
            workPeoples.add(workPerson);
        });
        this.saveBatch(workPeoples);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(@NonNull Set<Long> workTaskIds, Set<Long> userIds) {
        if (CollectionUtils.isEmpty(workTaskIds)) {
            return;
        }
        LambdaUpdateWrapper<WorkPerson> updateWrapper = Wrappers.<WorkPerson>lambdaUpdate()
                .in(WorkPerson::getWorkTaskId, workTaskIds)
                .in(!CollectionUtils.isEmpty(userIds), WorkPerson::getUserId, userIds)
                .set(WorkPerson::getStatus, WorkTaskStatusEnum.DONE);
        this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByWorkTaskIds(Collection<Long> workTaskIds) {
        List<WorkPerson> users = this.list(Wrappers.<WorkPerson>lambdaQuery()
                                                   .in(WorkPerson::getWorkTaskId, workTaskIds));
        if (!CollectionUtils.isEmpty(users)) {
            DatabaseContextHolder.softDelete(this, users);
        }
    }
}
