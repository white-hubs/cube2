package com.guhecloud.cube.biz.todo.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.todo.server.entity.TodoTask;
import com.guhecloud.cube.biz.todo.server.param.TodoTaskFlowCirculationParam;
import com.guhecloud.cube.biz.todo.server.param.TodoTaskModifyParam;
import com.guhecloud.cube.biz.todo.server.param.TodoTaskQueryParam;

import java.util.Set;

public interface TodoTaskService extends IService<TodoTask> {

    /**
     * 待办任务新增
     *
     * @param param
     *
     * @return
     */
    Long save(TodoTaskModifyParam param);

    /**
     * 待办任务完成
     *
     * @param param
     */
    Boolean todoTaskDone(TodoTaskFlowCirculationParam param);

    /**
     * 待办列表
     *
     * @param param
     *
     * @return
     */
    Page<TodoTask> todoPage(TodoTaskQueryParam param);

    /**
     * 已办列表
     *
     * @param param
     *
     * @return
     */
    Page<TodoTask> donePage(TodoTaskQueryParam param);

    /**
     * 待办任务删除
     *
     * @param businessKey
     */
    void todoTaskDel(String businessKey);

    /**
     * 待办任务批量删除
     *
     * @param businessKeys
     */
    void removeBatchByBusinessIds(Set<String> businessKeys);
}
