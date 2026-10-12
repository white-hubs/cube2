package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;

public interface InspectTaskStatusChangeService {

    InspectTaskPO changeForTaskAccept(Long taskId);

    /**
     * 任务生成状态改变
     */
    InspectTaskPO changeForTaskGenerate(Long taskId);

    /**
     * 任务过期状态改变
     */
    InspectTaskPO changeForTaskExpire(Long taskId);

    /**
     * 任务处理完成
     */
    InspectTaskPO changeForTaskComplete(Long taskId);

    /**
     * 任务终止
     */
    InspectTaskPO changeForTaskSTOP(Long taskId);
}
