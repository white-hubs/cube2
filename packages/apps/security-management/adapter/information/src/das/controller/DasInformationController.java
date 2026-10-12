package com.guhecloud.cube.srv.gen.adapter.information.server.das.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.adapter.information.server.das.service.DasInformationService;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.ProgramScheduleParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.SendProgramToTerminalParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.ProgramVo;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.SendProgramToTerminalVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * ITC信息发布 TR-0700S1 v3.2.4
 */
@Slf4j
@RestController
@RequestMapping("/das")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DasInformationController {

    private final DasInformationService informationService;

    /**
     * 节目列表(节目库)
     */
    @PostMapping("/getProgramList")
    public List<ProgramVo> getProgramList(@RequestBody ProgramScheduleParam request) {
        return informationService.getProgramList(request);
    }

    /**
     * @Description 发送节目到指定终端
     */
    @PostMapping("/program/sendProgramToTerminal")
    public List<SendProgramToTerminalVo> sendProgramToTerminal(@RequestBody SendProgramToTerminalParam param) {
        try {
            return informationService.sendProgramToTerminal(param);
        } catch (Exception e) {
            return Collections.singletonList(SendProgramToTerminalVo.builder().id(String.valueOf(IdWorker.getId()))
                                                     .terminalId(param.getTerminals().get(0).getTerminalName())
                                                     .sendResult("发送失败").sendStatus(1).build());
        }
    }
}
