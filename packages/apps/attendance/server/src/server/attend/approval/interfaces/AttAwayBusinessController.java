package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway.AttAwayBusinessQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param.AttAwayBusinessDurationParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 出差
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAwayBusinessController {

    private final AttAwayBusinessQueryService attAwayBusinessQueryService;

    /**
     * 出差时间计算
     *
     * @param param 请假入参
     *
     * @return 请假时间
     */
    @GetMapping("/att/awayBusiness/duration")
    public String calcLeavePostDuration(AttAwayBusinessDurationParam param) {
        return attAwayBusinessQueryService.calcAwayBusinessDuration(param);
    }

}
