package com.guhecloud.cube.srv.gen.personnel.management.server.common.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.commandgateway.command.SubmitFlowFormDataCommand;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.po.FlowFormDataPO;
import instance.command.SubmitInitFormCommand;

/**
 * @author bq
 * @date 2023/02/22
 * @description
 */
public interface FlowFormDataCommandService extends IService<FlowFormDataPO> {

    /**
     * 保存
     *
     * @param command 提交数据
     *
     * @return
     */
    void save(SubmitFlowFormDataCommand command);

    /**
     * 定时任务更新转正业务数据
     */
    void jobUpdateRegularWorkerData();

    /**
     * 定时任务更新轮岗业务数据
     */
    void jobUpdateRotationPostData();

    /**
     * 定时任务更新离职业务数据
     */
    void jobUpdateLeaveOfficeData();

    /**
     * 人事管理发起审批
     *
     * @param submitInitFormCommand
     *
     * @return
     */
    String submitApproval(SubmitInitFormCommand submitInitFormCommand);
}
