package com.guhecloud.cube.srv.gen.maintain.server.common.service;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;

/**
 * @author wql
 * @describe 待办任务转交
 * @date 2023/12/27 17:51
 */
public interface TodoTaskTransferService {
    void transferTodoTask(String receiveIds, InspectTaskPO taskPO);
}
