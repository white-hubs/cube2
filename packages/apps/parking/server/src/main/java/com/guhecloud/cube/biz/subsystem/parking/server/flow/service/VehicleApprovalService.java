package com.guhecloud.cube.biz.subsystem.parking.server.flow.service;

import instance.command.SubmitInitFormCommand;
import instance.event.ProcessCompletedMessage;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/10 10:17
 * @Description:
 */
public interface VehicleApprovalService {

    /**
     * 发起审批
     *
     * @param command 审批参数
     */
    String submitApproval(SubmitInitFormCommand command);

    /**
     * 流程审批结束业务操作
     *
     * @param message
     */

    void businessOperation(ProcessCompletedMessage message);

    /**
     * 获取指定用户下的车牌号
     *
     * @param id
     *
     * @return
     */
    List<String> getVechicleById(Long id);
}
