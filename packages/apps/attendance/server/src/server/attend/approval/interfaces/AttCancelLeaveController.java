package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway.AttCancelLeaveCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway.AttAskLeaveQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param.AttCalcLeaveDurationParam;
import instance.command.SubmitInitFormCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 销假
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttCancelLeaveController {

    private final AttAskLeaveQueryService attAskLeaveQueryService;
    private final AttCancelLeaveCommandService attCancelLeaveCommandService;

    /**
     * 计销假时长（同请假时间长）
     *
     * @param param 入参
     *
     * @return 时长
     */
    @PostMapping("/att/cancelLeave/calcDuration")
    public String cancelLeaveDuration(@RequestBody @Valid AttCalcLeaveDurationParam param) {

        return attAskLeaveQueryService.calcAskLeaveDuration(param);
    }

    /**
     * 发起销假审批
     *
     * @param formCommand 表单对象
     */
    @PostMapping("/att/cancelLeave/submit")
    public void submitCancelLeave(SubmitInitFormCommand formCommand) {
        attCancelLeaveCommandService.submitCancelLeave(formCommand);
    }
}
