package com.guhecloud.kaihe.scada.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.kaihe.scada.server.dto.VideoCamDTO;
import com.guhecloud.kaihe.scada.server.dto.VideoServerDTO;
import com.guhecloud.kaihe.scada.server.entity.VideoCam;
import com.guhecloud.kaihe.scada.server.entity.VideoServer;
import com.guhecloud.kaihe.scada.server.infrastructure.web.response.UnifiedResponseBody;
import com.guhecloud.kaihe.scada.server.service.VideoCamService;
import com.guhecloud.kaihe.scada.server.service.VideoServerService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/Video")
@RestController
@UnifiedResponseBody
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VideoServerController {

    private final VideoServerService videoServerService;
    private final VideoCamService videoCamService;

    /**
     * 加载流媒体
     * @return
     */
    @PostMapping("/loadServerName")
    public List<VideoServerDTO> loadServerName(@RequestParam(value = "projectId", defaultValue = "") Integer projectId) {
//        projectId = SessionContextHolder.getRequester().getTenant();
        QueryWrapper<VideoServer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("projectId", projectId);
        List<VideoServer> list = videoServerService.list(queryWrapper);
        return list.stream().map(videoServer -> {
            VideoServerDTO videoServerDTO = new VideoServerDTO();
            videoServerDTO.setId(videoServer.getId().toString() + "#" + videoServer.getIp() + "#" + videoServer.getPort() + "#" + videoServer.getSession());
            videoServerDTO.setName(videoServer.getServerName());
            return videoServerDTO;
        }).collect(Collectors.toList());
    }

    /**
     * 根据流媒体加载摄像机
     * @param serverId
     * @return
     */
    @GetMapping("/loadCamByServer")
    public List<VideoCamDTO> loadCamByServer(Integer serverId) {
        List<VideoCam> list = videoCamService.list(Wrappers.<VideoCam>lambdaQuery().eq(VideoCam::getServerId,serverId));
        return list.stream().map(videoCam -> {
            VideoCamDTO videoCamDTO = new VideoCamDTO();
            videoCamDTO.setID(videoCam.getCamToken()+"#"+videoCam.getStrName());
            videoCamDTO.setName(videoCam.getStrName());
            return videoCamDTO;
        }).collect(Collectors.toList());
    }

}
