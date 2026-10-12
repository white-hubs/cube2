package com.guhecloud.cube.srv.gen.adapter.information.server.hik.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.adapter.information.server.hik.service.HikInformationService;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.MaterialParam;
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
 * 海康信息发布
 */
@Slf4j
@RestController
@RequestMapping("/hik")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HikVisionInformationController {

    private final HikInformationService informationService;

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
     * 根据节目id集合批量获取节目对应的素材下载地址
     */
    @PostMapping("/getMaterialsByProgramIds")
    public List<ProgramVo> getMaterialsByProgramIds(@RequestBody MaterialParam request) {
        return informationService.getMaterialsByProgramId(request);
    }

    /**
     * @Description 发送节目到指定终端
     * 注：海康的节目插播仅支持临时插播，未提供让我们发送可定时播放节目的功能
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
