package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business;

public interface InspectTaskParentStatusSyncService {

    /**
     * 子任务完成后，同步检查并更新主任务状态。
     *
     * @param taskId 子任务 id
     */
    void syncParentTaskStatus(Long taskId);
}
