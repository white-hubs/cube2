package com.guhecloud.cube.srv.gen.securitymanagement.server.information.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.DownloadMaterialsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.ProgramPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.service.ProgramService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.DownloadMaterialVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.ProgramPageVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.subsystem.requester.sdk.param.information.SendProgramToTerminalParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 节目库
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProgramController {
    private final ProgramService programService;

    /**
     * @Description 节目库列表分页
     */
    @GetMapping("/program/getProgramPage")
    public Pagination<ProgramPageVo> getProgramSchedulePage(ProgramPageParam param) {
        return programService.getProgramPage(param);
    }

    /**
     * @Description 根据节目id集合批量获取节目对应的素材下载地址(目前仅支持厂商返回URL方式, 暂未支持返回文件流)
     */
    @PostMapping("/program/getMaterialsByProgramIds")
    public List<DownloadMaterialVo> getMaterialsByProgramIds(@RequestBody DownloadMaterialsParam param) {
        return programService.getMaterialsByProgramIds(param);
    }

    /**
     * @Description 发送节目到指定终端
     */
    @PostMapping("/program/sendProgramToTerminal")
    public void sendProgramToTerminal(@RequestBody SendProgramToTerminalParam param) throws JsonProcessingException {
        programService.sendProgramToTerminal(param);
    }
}
