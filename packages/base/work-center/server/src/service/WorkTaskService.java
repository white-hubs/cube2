package com.guhecloud.cube.biz.work.center.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.work.center.server.entity.WorkTask;
import com.guhecloud.cube.biz.work.center.server.param.WorkTaskFlowCirculationParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkTaskModifyParam;
import com.guhecloud.cube.biz.work.center.server.param.WorkTaskQueryParam;

import java.util.Set;

public interface WorkTaskService extends IService<WorkTask> {

    /**
     * 工作任务新增
     *
     * @param param
     *
     * @return
     */
    Long save(WorkTaskModifyParam param);

    /**
     * 工作任务完成
     *
     * @param param
     */
    Boolean workTaskDone(WorkTaskFlowCirculationParam param);

    /**
     * 工作任务列表
     *
     * @param param
     *
     * @return
     */
    Page<WorkTask> workPage(WorkTaskQueryParam param);

    /**
     * 已办工作任务列表
     *
     * @param param
     *
     * @return
     */
    Page<WorkTask> donePage(WorkTaskQueryParam param);

    /**
     * 工作任务删除
     *
     * @param businessKey
     */
    void workTaskDel(String businessKey);

    /**
     * 工作任务批量删除
     *
     * @param businessKeys
     */
    void removeBatchByBusinessIds(Set<String> businessKeys);
}
