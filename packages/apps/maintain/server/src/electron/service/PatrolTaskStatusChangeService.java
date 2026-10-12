package com.guhecloud.cube.srv.gen.maintain.server.electron.service;

import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTask;

/**
 * @author wql
 * @describe 巡更任务状态变更
 * @date 2023/6/2 9:02
 */
public interface PatrolTaskStatusChangeService {
    /**
     * @Author wql
     * @Description 变更为待接收状态
     * @Date 9:05 2023/6/2
     **/
    PatrolTask changeToWaitAccept(Long taskId);

    /**
     * 变更为执行中状态
     *
     * @param taskId
     *
     * @return
     */
    PatrolTask changeToInExecution(Long taskId);

    /**
     * 变更为已结束状态
     *
     * @param taskId
     *
     * @return
     */
    PatrolTask changeToFinish(Long taskId);

}
