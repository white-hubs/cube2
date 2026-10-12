package com.guhecloud.cube.srv.gen.visitor.management.server.flow.service;

import instance.command.SubmitInitFormCommand;
import instance.event.NodePassMessage;
import instance.event.ProcessBackMessage;
import instance.event.ProcessCompletedMessage;
import instance.event.ProcessStartedMessage;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/13 9:26
 * @Description: 访客邀约
 */
public interface VisitorInviteService {

    /**
     * 访客邀约申请提交
     *
     * @param submitInitFormCommand
     *
     * @return
     */
    String visitorInviteSubmit(SubmitInitFormCommand submitInitFormCommand);

    /**
     * 访客邀约事件发起
     *
     * @param message
     */
    void visitorInviteEventStart(ProcessStartedMessage message);

    /**
     * 访客邀约事件审批通过
     *
     * @param message
     */
    void visitorInviteEventNodePass(NodePassMessage message);

    /**
     * 访客邀约事件退回
     *
     * @param backMessage
     */
    void visitorInviteEventNodeBack(ProcessBackMessage backMessage);

    /**
     * 访客邀约审批事件结束
     *
     * @param completedMessage
     */
    void visitorInviteEventNodeComplete(ProcessCompletedMessage completedMessage);

}
