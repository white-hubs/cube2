package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces;

import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway.AttAskLeaveCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway.AttAwayBusinessCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway.AttCancelLeaveCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway.AttLeavePostCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.server.impl.WorkOvertimeRuleQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ApprovalBizEnum;
import instance.command.SubmitInitFormCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程申请入口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttApprovalController {

    private final AttAskLeaveCommandService attAskLeaveCommandService;
    private final AttCancelLeaveCommandService attCancelLeaveCommandService;
    private final FlowClient flowClient;
    private final AttLeavePostCommandService attLeavePostCommandService;
    private final WorkOvertimeRuleQueryService queryService;
    private final AttAwayBusinessCommandService attAwayBusinessCommandService;

    /**
     * 提交申请
     *
     * @param formCommand 申请单
     */
    @PostMapping("/att/approval/submit")
    public String submitApproval(@RequestBody SubmitInitFormCommand formCommand) {
        String processInstanceId = null;
        if (ApprovalBizEnum.LEAVE.name().equals(formCommand.getBusinessType())) {
            processInstanceId = attAskLeaveCommandService.submitAskLeaveApply(formCommand);
        } else if (ApprovalBizEnum.CANCEL_LEAVE.name().equals(formCommand.getBusinessType())) {
            processInstanceId = attCancelLeaveCommandService.submitCancelLeave(formCommand);
        } else if (ApprovalBizEnum.LEAVE_THE_POST.name().equals(formCommand.getBusinessType())) {
            processInstanceId = attLeavePostCommandService.submitLeavePostApply(formCommand);
        } else if (ApprovalBizEnum.CHANGE_CLASSES.name().equals(formCommand.getBusinessType())) {
            //processInstanceId = attChangeClassCommandService.submitChangeClass(formCommand);
        } else if (ApprovalBizEnum.OVERTIME.name().equals(formCommand.getBusinessType())) {
            processInstanceId = queryService.getWorkOvertimeDuration(formCommand);
        } else if (ApprovalBizEnum.AWAY_BUSINESS.name().equals(formCommand.getBusinessType())) {
            processInstanceId = attAwayBusinessCommandService.submitAwayBusinessApply(formCommand);
        } else {
            processInstanceId = flowClient.submitFirst(formCommand);
        }
        return processInstanceId;
    }

}
