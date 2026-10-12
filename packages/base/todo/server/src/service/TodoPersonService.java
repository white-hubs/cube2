package com.guhecloud.cube.biz.todo.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.todo.server.entity.TodoPerson;

import java.util.List;
import java.util.Set;

public interface TodoPersonService extends IService<TodoPerson> {

    void saveBatch(Long todoTaskId, String businessKey, List<Long> userIds);

    void updateStatus(Set<Long> todoTaskIds, Set<Long> userIds);
}
