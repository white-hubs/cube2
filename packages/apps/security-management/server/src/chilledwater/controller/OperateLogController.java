package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.enumeration.LogTypeEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.OperateLogParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.OperateLogService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.OperateLogVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OperateLogController {

    private final OperateLogService operateLogService;

    /**
     * @Description 日志信息
     * @Author lrj
     */
    @GetMapping("/operate-logs")
    public Pagination<OperateLogVo> getOperateLogs(OperateLogParam operateLogParam) {
        if (operateLogParam.getLogTypeEnum().equals(LogTypeEnum.HARDWARE_LOG)) {
            return operateLogService.getOperateHardwareLogs(operateLogParam);
        } else {
            return operateLogService.getOperateLogs(operateLogParam);
        }

    }
}
