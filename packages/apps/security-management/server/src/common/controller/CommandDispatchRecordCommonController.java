package com.guhecloud.cube.srv.gen.securitymanagement.server.common.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.common.query.CommandRecordPageQuery;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.CommandDispatchRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.CommandRecordPageVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommandDispatchRecordCommonController {

    private final CommandDispatchRecordService commandDispatchRecordService;

    /**
     * @Description 根据应用 获取指令下发记录
     * @Author xiaofeng
     */
    @GetMapping("/command/dispatch/record/getCommandDispatchRecordPage")
    public Pagination<CommandRecordPageVo> getCommandDispatchRecordPage(CommandRecordPageQuery param) {
        return commandDispatchRecordService.getCommandDispatchRecordPage(param);
    }

    /**
     * @Description 根据应用 获取指令下发记录
     * @Author xiaofeng
     */
    @GetMapping("/command/dispatch/record/getCommandDispatchRecordInfo")
    public CommandRecordPageVo getCommandDispatchRecordInfo(String id) {
        return commandDispatchRecordService.getCommandDispatchRecordInfo(Long.parseLong(id));
    }
}
