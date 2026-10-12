package com.guhecloud.cube.biz.todo.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.todo.server.dao.TodoPersonMapper;
import com.guhecloud.cube.biz.todo.server.entity.TodoPerson;
import com.guhecloud.cube.biz.todo.server.enumeration.TodoTaskStatusEnum;
import com.guhecloud.cube.biz.todo.server.service.TodoPersonService;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TodoPersonServiceImpl extends ServiceImpl<TodoPersonMapper, TodoPerson> implements TodoPersonService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(@NonNull Long todoTaskId, @NonNull String businessKey, @NonNull List<Long> userIds) {
        List<TodoPerson> todoPeoples = new ArrayList<>();
        userIds.stream().forEach(userId -> {
            TodoPerson todoPerson = TodoPerson.builder()
                    .todoTaskId(todoTaskId)
                    .businessKey(businessKey)
                    .userId(userId)
                    .status(TodoTaskStatusEnum.TODO)
                    .build();
            todoPeoples.add(todoPerson);
        });
        this.saveBatch(todoPeoples);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(@NonNull Set<Long> todoTaskIds, Set<Long> userIds) {
        if (CollectionUtils.isEmpty(todoTaskIds)) {
            return;
        }
        LambdaUpdateWrapper<TodoPerson> updateWrapper = Wrappers.<TodoPerson>lambdaUpdate()
                .in(TodoPerson::getTodoTaskId, todoTaskIds)
                .in(!CollectionUtils.isEmpty(userIds), TodoPerson::getUserId, userIds)
                .set(TodoPerson::getStatus, TodoTaskStatusEnum.DONE);
        this.update(updateWrapper);
    }
}
