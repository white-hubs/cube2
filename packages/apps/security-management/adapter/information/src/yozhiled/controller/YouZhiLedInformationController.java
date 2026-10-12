package com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.adapter.information.server.huifeng.qo.InformationParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.ProgramScheduleParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.SendProgramToTerminalParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.PLayRecordVo;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.ProgramScheduleVo;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.ProgramVo;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.SendProgramToTerminalVo;
import com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.service.YouZhiLedInformationService;
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
 * 优质光电信息发布
 */
@Slf4j
@RestController
@RequestMapping("/youZhiLed")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class YouZhiLedInformationController {

    private final YouZhiLedInformationService informationService;

    /**
     * 播放记录
     *
     * @param informationParam
     *
     * @return
     */
    @PostMapping("/getPayLogs")
    public List<PLayRecordVo> getPayLogs(@RequestBody InformationParam informationParam) {
        return null;
    }

    /**
     * 节目单列表
     *
     * @param request
     *
     * @return
     */
    @PostMapping("/getProgramScheduleList")
    public List<ProgramScheduleVo> getProgramScheduleList(@RequestBody ProgramScheduleParam request) {
        return informationService.getProgramInfoList(request);
    }

    /**
     * 节目列表
     *
     * @param request
     *
     * @return
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
