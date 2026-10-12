package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway.AttAskLeaveCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway.AttAskLeaveQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param.AttCalcLeaveDurationParam;
import instance.command.SubmitInitFormCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 请假申请
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAskLeaveController {

    private final AttAskLeaveQueryService attAskLeaveQueryService;
    private final AttAskLeaveCommandService attAskLeaveCommandService;

    /**
     * 提交请假申请
     *
     * @param formCommand 入参
     */
    @PostMapping("/att/askLeave/submit")
    public void submitAskLeaveApply(SubmitInitFormCommand formCommand) {
        attAskLeaveCommandService.submitAskLeaveApply(formCommand);
    }

    /**
     * 计算请假时长
     *
     * @param param 入参
     *
     * @return 时长
     */
    @GetMapping("/att/askLeave/calcDuration")
    public String calcAskLeaveDuration(@Valid AttCalcLeaveDurationParam param) {
        return attAskLeaveQueryService.calcAskLeaveDuration(param);
    }
}
