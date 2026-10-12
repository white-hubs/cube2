package com.guhecloud.cube.srv.gen.securitymanagement.server.lift.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.param.FiveCallLogParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.service.FiveCallLogService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.vo.FiveCallLogVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 五方通话记录控制层
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FiveCallLogController {
    private final FiveCallLogService fiveCallLogService;

    /**
     * 五方通话记录分页列表
     *
     * @param param 条件入参
     *
     * @return 分页列表
     */
    @GetMapping("/lift-fiveCallLog-page")
    public Pagination<FiveCallLogVo> fiveCallLogPage(FiveCallLogParam param) {
        return fiveCallLogService.fiveCallLogPage(param);
    }

}
