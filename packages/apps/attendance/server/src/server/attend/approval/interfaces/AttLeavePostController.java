package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway.AttLeavePostQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param.AttLeavePostDurationParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 离岗
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttLeavePostController {

    private final AttLeavePostQueryService attLeavePostQueryService;

    /**
     * 离岗时间计算
     *
     * @param param 请假入参
     *
     * @return 请假时间
     */
    @GetMapping("/att/leavePost/duration")
    public String calcLeavePostDuration(AttLeavePostDurationParam param) {
        return attLeavePostQueryService.calcLeavePostDuration(param);
    }

}
